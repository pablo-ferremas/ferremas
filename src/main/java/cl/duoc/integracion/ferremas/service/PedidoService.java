package cl.duoc.integracion.ferremas.service;

import org.springframework.stereotype.Service;

import cl.duoc.integracion.ferremas.entity.Pedido;
import cl.duoc.integracion.ferremas.entity.ItemPedido;
import cl.duoc.integracion.ferremas.entity.Usuario;
import cl.duoc.integracion.ferremas.entity.Direccion;
import cl.duoc.integracion.ferremas.entity.Producto;
import cl.duoc.integracion.ferremas.entity.EstadoPedido;
import cl.duoc.integracion.ferremas.entity.TipoEnvio;
import cl.duoc.integracion.ferremas.repository.PedidoRepository;
import cl.duoc.integracion.ferremas.repository.UsuarioRepository;
import cl.duoc.integracion.ferremas.repository.DireccionRepository;
import cl.duoc.integracion.ferremas.repository.ProductoRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DireccionRepository direccionRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository, 
                        UsuarioRepository usuarioRepository,
                        DireccionRepository direccionRepository,
                        ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.direccionRepository = direccionRepository;
        this.productoRepository = productoRepository;
    }

    /**
     * Obtiene todos los pedidos
     */
    public List<Pedido> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll();
    }

    /**
     * Busca un pedido por ID
     */
    public Pedido buscarPorId(Long id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        return pedido.orElse(null);
    }

    /**
     * Busca pedido por número de orden
     */
    public Pedido buscarPorNumeroOrden(String numeroOrden) {
        Optional<Pedido> pedido = pedidoRepository.findByNumeroOrden(numeroOrden);
        return pedido.orElse(null);
    }

    /**
     * Crea un nuevo pedido
     */
    public Pedido crearPedido(Pedido pedido) {
        // Validar que exista el usuario
        if (pedido.getUsuario() != null && pedido.getUsuario().getId() != null) {
            Optional<Usuario> usuario = usuarioRepository.findById(pedido.getUsuario().getId());
            if (usuario.isPresent()) {
                pedido.setUsuario(usuario.get());
            } else {
                throw new RuntimeException("Usuario no encontrado con ID: " + pedido.getUsuario().getId());
            }
        } else {
            throw new RuntimeException("Debe especificar un usuario para el pedido");
        }

        // Validar que exista la dirección de entrega
        if (pedido.getDireccionEntrega() != null && pedido.getDireccionEntrega().getId() != null) {
            Optional<Direccion> direccion = direccionRepository.findById(pedido.getDireccionEntrega().getId());
            if (direccion.isPresent()) {
                pedido.setDireccionEntrega(direccion.get());
            } else {
                throw new RuntimeException("Dirección no encontrada con ID: " + pedido.getDireccionEntrega().getId());
            }
        } else if (pedido.getTipoEnvio() == TipoEnvio.DOMICILIO) {
            throw new RuntimeException("Debe especificar una dirección de entrega para envío a domicilio");
        }

        // Establecer costo de envío según el tipo
        if (pedido.getTipoEnvio() == TipoEnvio.RETIRO_TIENDA) {
            pedido.setCostoEnvio(BigDecimal.ZERO);
        } else if (pedido.getTipoEnvio() == TipoEnvio.DOMICILIO) {
            pedido.setCostoEnvio(new BigDecimal("3000"));
        }

        // Generar número de orden único
        if (pedido.getNumeroOrden() == null || pedido.getNumeroOrden().isEmpty()) {
            pedido.setNumeroOrden(generarNumeroOrden());
        }

        // Establecer estado por defecto
        if (pedido.getEstado() == null) {
            pedido.setEstado(EstadoPedido.PENDIENTE);
        }

        // Establecer fecha de creación si no está definida
        if (pedido.getFechaCreacion() == null) {
            pedido.setFechaCreacion(LocalDateTime.now());
        }

        // IMPORTANTE: Establecer la referencia del pedido en cada item
        if (pedido.getItems() != null && !pedido.getItems().isEmpty()) {
            for (ItemPedido item : pedido.getItems()) {
                // Establecer la referencia al pedido padre
                item.setPedido(pedido);
                
                // Si el frontend envía producto con ID, necesitamos extraer el ID y validar
                if (item.getProductoId() == null) {
                    // Esto podría venir en el JSON como "producto": {"id": 12}
                    // pero necesitamos manejar esto en el controller o crear un DTO
                    throw new RuntimeException("Debe especificar un productoId para cada item");
                }
                
                // Validar que el producto existe
                // Convertir Long a Integer para compatibilidad con ProductoRepository
                Optional<Producto> producto = productoRepository.findById(item.getProductoId().intValue());
                if (producto.isPresent()) {
                    Producto prod = producto.get();
                    
                    // Si no se especificó nombre del producto, usar el del producto
                    if (item.getNombreProducto() == null || item.getNombreProducto().isEmpty()) {
                        item.setNombreProducto(prod.getNombre());
                    }
                    // Agregar información adicional del producto
                    if (item.getDescripcionProducto() == null) {
                        item.setDescripcionProducto(prod.getDescripcion());
                    }
                    if (item.getImagenProducto() == null) {
                        item.setImagenProducto(prod.getImagen());
                    }
                    if (item.getSkuProducto() == null) {
                        item.setSkuProducto(prod.getCodigo_producto());
                    }
                } else {
                    throw new RuntimeException("Producto no encontrado con ID: " + item.getProductoId());
                }
                
                // Calcular el subtotal del item si no está calculado
                if (item.getSubtotalItem() == null && item.getPrecioUnitario() != null && item.getCantidad() != null) {
                    BigDecimal subtotal = item.getPrecioUnitario().multiply(new BigDecimal(item.getCantidad()));
                    item.setSubtotalItem(subtotal);
                }
            }
        }

        // Calcular totales después de procesar los items
        calcularTotales(pedido);

        return pedidoRepository.save(pedido);
    }

    /**
     * Actualiza un pedido existente
     */
    public Pedido actualizarPedido(Long id, Pedido pedidoActualizado) {
        Optional<Pedido> pedidoExistente = pedidoRepository.findById(id);
        
        if (pedidoExistente.isPresent()) {
            Pedido pedido = pedidoExistente.get();
            
            // Solo permitir actualizar ciertos campos según el estado
            if (pedido.getEstado() == EstadoPedido.PENDIENTE) {
                // En estado pendiente se pueden actualizar más campos
                if (pedidoActualizado.getTipoEnvio() != null) {
                    pedido.setTipoEnvio(pedidoActualizado.getTipoEnvio());
                    // Recalcular costo de envío
                    if (pedido.getTipoEnvio() == TipoEnvio.RETIRO_TIENDA) {
                        pedido.setCostoEnvio(BigDecimal.ZERO);
                    } else {
                        pedido.setCostoEnvio(new BigDecimal("3000"));
                    }
                }
                
                if (pedidoActualizado.getDireccionEntrega() != null) {
                    pedido.setDireccionEntrega(pedidoActualizado.getDireccionEntrega());
                }
            }
            
            // Siempre se puede actualizar el estado
            if (pedidoActualizado.getEstado() != null) {
                pedido.setEstado(pedidoActualizado.getEstado());
            }

            // Recalcular totales
            calcularTotales(pedido);

            return pedidoRepository.save(pedido);
        }
        
        return null;
    }

    /**
     * Actualiza solo el estado de un pedido
     */
    public Pedido actualizarEstado(Long id, EstadoPedido nuevoEstado) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            pedido.setEstado(nuevoEstado);
            return pedidoRepository.save(pedido);
        }
        
        return null;
    }

    /**
     * Elimina un pedido (solo si está en estado PENDIENTE)
     */
    public boolean eliminarPedido(Long id) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            if (pedido.getEstado() == EstadoPedido.PENDIENTE) {
                pedidoRepository.deleteById(id);
                return true;
            } else {
                throw new RuntimeException("Solo se pueden eliminar pedidos en estado PENDIENTE");
            }
        }
        
        return false;
    }

    /**
     * Busca pedidos por usuario
     */
    public List<Pedido> buscarPorUsuarioId(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId);
    }

    /**
     * Busca pedidos por estado
     */
    public List<Pedido> buscarPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstadoOrderByFechaCreacionDesc(estado);
    }

    /**
     * Busca pedidos por usuario y estado
     */
    public List<Pedido> buscarPorUsuarioYEstado(Long usuarioId, EstadoPedido estado) {
        return pedidoRepository.findByUsuarioIdAndEstadoOrderByFechaCreacionDesc(usuarioId, estado);
    }

    /**
     * Busca pedidos por tipo de envío
     */
    public List<Pedido> buscarPorTipoEnvio(TipoEnvio tipoEnvio) {
        return pedidoRepository.findByTipoEnvioOrderByFechaCreacionDesc(tipoEnvio);
    }

    /**
     * Busca pedidos recientes (últimos N días)
     */
    public List<Pedido> buscarPedidosRecientes(int dias) {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(dias);
        return pedidoRepository.findPedidosRecientes(fechaLimite);
    }

    /**
     * Cuenta pedidos por usuario
     */
    public long contarPorUsuarioId(Long usuarioId) {
        return pedidoRepository.countByUsuarioId(usuarioId);
    }

    /**
     * Cuenta pedidos por estado
     */
    public long contarPorEstado(EstadoPedido estado) {
        return pedidoRepository.countByEstado(estado);
    }

    /**
     * Calcula los totales del pedido basado en sus items
     */
    private void calcularTotales(Pedido pedido) {
        BigDecimal subtotal = BigDecimal.ZERO;
        
        if (pedido.getItems() != null && !pedido.getItems().isEmpty()) {
            for (ItemPedido item : pedido.getItems()) {
                if (item.getSubtotalItem() != null) {
                    subtotal = subtotal.add(item.getSubtotalItem());
                }
            }
        }
        
        pedido.setSubtotal(subtotal);
        pedido.setTotal(subtotal.add(pedido.getCostoEnvio() != null ? pedido.getCostoEnvio() : BigDecimal.ZERO));
    }

    /**
     * Genera un número de orden único
     */
    private String generarNumeroOrden() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "ORD-" + timestamp;
    }
}
