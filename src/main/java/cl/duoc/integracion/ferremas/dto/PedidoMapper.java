package cl.duoc.integracion.ferremas.dto;

import cl.duoc.integracion.ferremas.entity.Pedido;
import cl.duoc.integracion.ferremas.entity.ItemPedido;
import cl.duoc.integracion.ferremas.entity.Usuario;
import cl.duoc.integracion.ferremas.entity.Direccion;

import java.util.List;
import java.util.stream.Collectors;

public class PedidoMapper {

    public static PedidoResponse toResponse(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        PedidoResponse response = new PedidoResponse();
        response.setId(pedido.getId());
        response.setNumeroOrden(pedido.getNumeroOrden());
        response.setEstado(pedido.getEstado());
        response.setTipoEnvio(pedido.getTipoEnvio());
        response.setSubtotal(pedido.getSubtotal());
        response.setCostoEnvio(pedido.getCostoEnvio());
        response.setTotal(pedido.getTotal());
        response.setFechaCreacion(pedido.getFechaCreacion());

        // Mapear usuario básico
        response.setUsuario(toUsuarioBasico(pedido.getUsuario()));

        // Mapear dirección básica
        response.setDireccionEntrega(toDireccionBasica(pedido.getDireccionEntrega()));

        // Mapear items
        if (pedido.getItems() != null) {
            List<ItemPedidoResponse> items = pedido.getItems().stream()
                .map(PedidoMapper::toItemResponse)
                .collect(Collectors.toList());
            response.setItems(items);
        }

        return response;
    }

    public static UsuarioBasicoResponse toUsuarioBasico(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioBasicoResponse response = new UsuarioBasicoResponse();
        response.setId(usuario.getId());
        response.setNombre(usuario.getNombre());
        response.setEmail(usuario.getEmail());
        response.setTelefono(usuario.getTelefono());
        return response;
    }

    public static DireccionBasicaResponse toDireccionBasica(Direccion direccion) {
        if (direccion == null) {
            return null;
        }

        DireccionBasicaResponse response = new DireccionBasicaResponse();
        response.setId(direccion.getId());
        response.setCalle(direccion.getCalle());
        response.setNumero(direccion.getNumero());
        response.setComplemento(""); // No existe este campo en la entidad actual
        
        // Obtener nombres sin cargar las relaciones completas
        if (direccion.getComuna() != null) {
            response.setNombreComuna(direccion.getComuna().getNombre());
        }
        
        if (direccion.getRegion() != null) {
            response.setNombreRegion(direccion.getRegion().getNombre());
        }
        
        return response;
    }

    public static ItemPedidoResponse toItemResponse(ItemPedido item) {
        if (item == null) {
            return null;
        }

        ItemPedidoResponse response = new ItemPedidoResponse();
        response.setId(item.getId());
        response.setProductoId(item.getProductoId());
        response.setNombreProducto(item.getNombreProducto());
        response.setDescripcionProducto(item.getDescripcionProducto());
        response.setImagenProducto(item.getImagenProducto());
        response.setSkuProducto(item.getSkuProducto());
        response.setPrecioUnitario(item.getPrecioUnitario());
        response.setCantidad(item.getCantidad());
        response.setSubtotalItem(item.getSubtotalItem());
        return response;
    }
}
