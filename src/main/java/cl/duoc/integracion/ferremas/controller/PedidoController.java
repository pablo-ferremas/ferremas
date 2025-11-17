package cl.duoc.integracion.ferremas.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.duoc.integracion.ferremas.entity.Pedido;
import cl.duoc.integracion.ferremas.entity.ItemPedido;
import cl.duoc.integracion.ferremas.entity.Usuario;
import cl.duoc.integracion.ferremas.entity.Direccion;
import cl.duoc.integracion.ferremas.entity.EstadoPedido;
import cl.duoc.integracion.ferremas.entity.TipoEnvio;
import cl.duoc.integracion.ferremas.dto.CrearPedidoRequest;
import cl.duoc.integracion.ferremas.dto.PedidoResponse;
import cl.duoc.integracion.ferremas.dto.PedidoMapper;
import cl.duoc.integracion.ferremas.service.PedidoService;

@RestController
@RequestMapping("/api/v1/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    /**
     * Obtiene todos los pedidos
     */
    @GetMapping
    public ResponseEntity<List<PedidoResponse>> obtenerTodosLosPedidos() {
        List<Pedido> pedidos = pedidoService.obtenerTodosLosPedidos();
        List<PedidoResponse> response = pedidos.stream()
            .map(PedidoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene un pedido por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obtenerPedidoPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPorId(id);
        if (pedido != null) {
            PedidoResponse response = PedidoMapper.toResponse(pedido);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene un pedido por número de orden
     */
    @GetMapping("/numero/{numeroOrden}")
    public ResponseEntity<PedidoResponse> obtenerPedidoPorNumeroOrden(@PathVariable String numeroOrden) {
        Pedido pedido = pedidoService.buscarPorNumeroOrden(numeroOrden);
        if (pedido != null) {
            PedidoResponse response = PedidoMapper.toResponse(pedido);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea un nuevo pedido
     */
    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody CrearPedidoRequest request) {
        try {
            Pedido pedido = convertirDtoAPedido(request);
            Pedido nuevoPedido = pedidoService.crearPedido(pedido);
            
            // Convertir a DTO de respuesta para evitar referencias circulares
            PedidoResponse response = PedidoMapper.toResponse(nuevoPedido);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Convierte el DTO de request a entidad Pedido
     */
    private Pedido convertirDtoAPedido(CrearPedidoRequest request) {
        Pedido pedido = new Pedido();
        
        // Configurar usuario
        if (request.getUsuario() != null && request.getUsuario().getId() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(request.getUsuario().getId());
            pedido.setUsuario(usuario);
        }
        
        // Configurar dirección de entrega
        if (request.getDireccionEntrega() != null && request.getDireccionEntrega().getId() != null) {
            Direccion direccion = new Direccion();
            direccion.setId(request.getDireccionEntrega().getId());
            pedido.setDireccionEntrega(direccion);
        }
        
        // Configurar tipo de envío
        pedido.setTipoEnvio(request.getTipoEnvio());
        
        // Configurar número de orden si viene
        pedido.setNumeroOrden(request.getNumeroOrden());
        
        // Configurar items
        if (request.getItems() != null) {
            List<ItemPedido> items = request.getItems().stream()
                .map(this::convertirDtoAItemPedido)
                .collect(Collectors.toList());
            pedido.setItems(items);
        }
        
        return pedido;
    }
    
    /**
     * Convierte el DTO de item a entidad ItemPedido
     */
    private ItemPedido convertirDtoAItemPedido(CrearPedidoRequest.ItemPedidoRequest itemRequest) {
        ItemPedido item = new ItemPedido();
        
        // Configurar ID del producto
        if (itemRequest.getProducto() != null && itemRequest.getProducto().getId() != null) {
            item.setProductoId(itemRequest.getProducto().getId().longValue());
        }
        
        item.setCantidad(itemRequest.getCantidad());
        item.setPrecioUnitario(itemRequest.getPrecioUnitario());
        item.setNombreProducto(itemRequest.getNombreProducto());
        item.setDescripcionProducto(itemRequest.getDescripcionProducto());
        item.setImagenProducto(itemRequest.getImagenProducto());
        item.setSkuProducto(itemRequest.getSkuProducto());
        
        return item;
    }

    /**
     * Actualiza un pedido existente
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        try {
            Pedido pedidoActualizado = pedidoService.actualizarPedido(id, pedido);
            if (pedidoActualizado != null) {
                PedidoResponse response = PedidoMapper.toResponse(pedidoActualizado);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Actualiza solo el estado de un pedido
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoPedido(@PathVariable Long id, @RequestParam EstadoPedido estado) {
        try {
            Pedido pedidoActualizado = pedidoService.actualizarEstado(id, estado);
            if (pedidoActualizado != null) {
                PedidoResponse response = PedidoMapper.toResponse(pedidoActualizado);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Elimina un pedido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPedido(@PathVariable Long id) {
        try {
            boolean eliminado = pedidoService.eliminarPedido(id);
            if (eliminado) {
                return ResponseEntity.ok().body("Pedido eliminado correctamente");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Busca pedidos por usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PedidoResponse>> obtenerPedidosPorUsuario(@PathVariable Long usuarioId) {
        List<Pedido> pedidos = pedidoService.buscarPorUsuarioId(usuarioId);
        List<PedidoResponse> response = pedidos.stream()
            .map(PedidoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Busca pedidos por estado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PedidoResponse>> obtenerPedidosPorEstado(@PathVariable EstadoPedido estado) {
        List<Pedido> pedidos = pedidoService.buscarPorEstado(estado);
        List<PedidoResponse> response = pedidos.stream()
            .map(PedidoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Busca pedidos por usuario y estado
     */
    @GetMapping("/usuario/{usuarioId}/estado/{estado}")
    public ResponseEntity<List<PedidoResponse>> obtenerPedidosPorUsuarioYEstado(
            @PathVariable Long usuarioId, 
            @PathVariable EstadoPedido estado) {
        List<Pedido> pedidos = pedidoService.buscarPorUsuarioYEstado(usuarioId, estado);
        List<PedidoResponse> response = pedidos.stream()
            .map(PedidoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Busca pedidos por tipo de envío
     */
    @GetMapping("/tipo-envio/{tipoEnvio}")
    public ResponseEntity<List<PedidoResponse>> obtenerPedidosPorTipoEnvio(@PathVariable TipoEnvio tipoEnvio) {
        List<Pedido> pedidos = pedidoService.buscarPorTipoEnvio(tipoEnvio);
        List<PedidoResponse> response = pedidos.stream()
            .map(PedidoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Busca pedidos recientes
     */
    @GetMapping("/recientes")
    public ResponseEntity<List<PedidoResponse>> obtenerPedidosRecientes(@RequestParam(defaultValue = "30") int dias) {
        List<Pedido> pedidos = pedidoService.buscarPedidosRecientes(dias);
        List<PedidoResponse> response = pedidos.stream()
            .map(PedidoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Cuenta pedidos por usuario
     */
    @GetMapping("/contar/usuario/{usuarioId}")
    public ResponseEntity<Long> contarPedidosPorUsuario(@PathVariable Long usuarioId) {
        long count = pedidoService.contarPorUsuarioId(usuarioId);
        return ResponseEntity.ok(count);
    }

    /**
     * Cuenta pedidos por estado
     */
    @GetMapping("/contar/estado/{estado}")
    public ResponseEntity<Long> contarPedidosPorEstado(@PathVariable EstadoPedido estado) {
        long count = pedidoService.contarPorEstado(estado);
        return ResponseEntity.ok(count);
    }
}
