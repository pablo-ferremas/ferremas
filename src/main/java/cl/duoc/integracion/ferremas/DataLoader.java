package cl.duoc.integracion.ferremas;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import cl.duoc.integracion.ferremas.entity.Categoria;
import cl.duoc.integracion.ferremas.entity.Marca;
import cl.duoc.integracion.ferremas.entity.Precio;
import cl.duoc.integracion.ferremas.entity.Producto;
import cl.duoc.integracion.ferremas.entity.Usuario;
import cl.duoc.integracion.ferremas.repository.CategoriaRepository;
import cl.duoc.integracion.ferremas.repository.MarcaRepository;
import cl.duoc.integracion.ferremas.repository.PrecioRepository;
import cl.duoc.integracion.ferremas.repository.ProductoRepository;
import cl.duoc.integracion.ferremas.repository.UsuarioRepository;
import net.datafaker.Faker;


@Component
@Profile("test")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PrecioRepository precioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        try {
            System.out.println("🚀 Iniciando carga de datos de prueba para Ferremas...");

            // Generar categorías de herramientas con Faker
            System.out.println("📂 Generando categorías...");
            java.util.Set<String> categoriasSet = new java.util.HashSet<>();
            while (categoriasSet.size() < 10) {
                categoriasSet.add(faker.commerce().department());
            }
            for (String nombreCategoria : categoriasSet) {
                Categoria categoria = new Categoria();
                categoria.setNombre(nombreCategoria);
                categoriaRepository.save(categoria);
            }

            // Generar marcas de herramientas con Faker
            System.out.println("🏷️ Generando marcas...");
            java.util.Set<String> marcasSet = new java.util.HashSet<>();
            while (marcasSet.size() < 12) {
                marcasSet.add(faker.company().name());
            }
            for (String nombreMarca : marcasSet) {
                Marca marca = new Marca();
                marca.setNombre(nombreMarca);
                marcaRepository.save(marca);
            }

            // Generar usuarios
            System.out.println("👥 Generando usuarios...");
            for (int i = 0; i < 25; i++) {
                Usuario usuario = new Usuario();
                usuario.setNombre(faker.name().fullName());
                usuario.setEmail(faker.internet().emailAddress());
                usuario.setPassword(passwordEncoder.encode("password123"));
                
                // Asignar roles aleatorios
                String[] roles = {"CLIENTE", "ADMIN", "VENDEDOR"};
                usuario.setRol(roles[random.nextInt(roles.length)]);
                
                usuarioRepository.save(usuario);
            }

            List<Categoria> categorias = categoriaRepository.findAll();
            List<Marca> marcas = marcaRepository.findAll();

            // Generar productos
            System.out.println("🔧 Generando productos...");
            int totalProductos = 50;
            for (int i = 0; i < totalProductos; i++) {
                Producto producto = new Producto();
                producto.setCodigo_producto("PROD-" + String.format("%04d", i + 1));
                // Nombre generado con Faker
                String nombreHerramienta = faker.commerce().productName() + " " + faker.commerce().material();
                producto.setNombre(nombreHerramienta);
                producto.setDescripcion(faker.lorem().paragraph(2));
                producto.setImagen("https://via.placeholder.com/300x200?text=" + nombreHerramienta.replace(" ", "+"));
                producto.setDescuento(random.nextInt(31)); // 0-30% descuento
                producto.setDestacado(random.nextBoolean());
                producto.setOculto(random.nextBoolean());
                producto.setStock(random.nextInt(100) + 5); // 5-104 unidades
                producto.setCategoria(categorias.get(random.nextInt(categorias.size())));
                producto.setMarca(marcas.get(random.nextInt(marcas.size())));
                
                Producto productoGuardado = productoRepository.save(producto);

                // Generar precios para cada producto
                for (int j = 0; j < random.nextInt(3) + 1; j++) { // 1-3 precios por producto
                    Precio precio = new Precio();
                    precio.setPrecio(faker.number().numberBetween(3000, 200000));
                    precio.setProducto(productoGuardado);
                    precioRepository.save(precio);
                }
            }

            // Generar algunos productos adicionales con nombres generados por Faker
            System.out.println("🛠️ Generando productos adicionales...");
            for (int i = 0; i < 20; i++) {
                Producto producto = new Producto();
                producto.setCodigo_producto("PROD-" + String.format("%04d", totalProductos + i + 1));
                producto.setNombre(faker.commerce().productName() + " " + faker.commerce().material());
                producto.setDescripcion(faker.lorem().paragraph(2));
                producto.setImagen("https://via.placeholder.com/300x200?text=Herramienta+" + (i + 1));
                producto.setDescuento(random.nextInt(31)); // 0-30% descuento
                producto.setDestacado(random.nextBoolean());
                producto.setOculto(random.nextBoolean());
                producto.setStock(random.nextInt(100) + 5); // 5-104 unidades
                producto.setCategoria(categorias.get(random.nextInt(categorias.size())));
                producto.setMarca(marcas.get(random.nextInt(marcas.size())));
                
                Producto productoGuardado = productoRepository.save(producto);

                // Generar precios para cada producto
                for (int j = 0; j < random.nextInt(2) + 1; j++) { // 1-2 precios por producto
                    Precio precio = new Precio();
                    precio.setPrecio(faker.number().numberBetween(3000, 200000));
                    precio.setProducto(productoGuardado);
                    precioRepository.save(precio);
                }
            }

            System.out.println("✅ Carga de datos completada exitosamente!");
            System.out.println("📊 Resumen de datos generados:");
            System.out.println("   - Categorías: " + categoriaRepository.count());
            System.out.println("   - Marcas: " + marcaRepository.count());
            System.out.println("   - Usuarios: " + usuarioRepository.count());
            System.out.println("   - Productos: " + productoRepository.count());
            System.out.println("   - Precios: " + precioRepository.count());

        } catch (Exception e) {
            System.err.println("❌ Error durante la carga de datos de prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
