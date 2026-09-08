import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collectors;

public class Produccion {
    private ArrayList<Producto> productos;
    
    // Predicado reutilizable sugerido para filtrar productos defectuosos
    private final Predicate<Producto> esDefectuoso = Producto::getEsDefectuoso;

    // Lista de productos válidos dentro del contexto automotriz
    private static final List<String> PRODUCTOS_VALIDOS = Arrays.asList(
        "motor", "freno", "puerta", "chasis", "llanta", "piston", "valvula", "amortiguador", "espejo", "bateria"
    );

    public Produccion() {
        this.productos = new ArrayList<>();
    }
        
    public void registrarProducto(String codigo, String nombre, LineaProduccion linea_produccion, Float costo_unitario, Float minutos_utilizados, Boolean esDefectuosoFlag, Float peso_materia_prima, Integer meta_produccion) {
        if (!esProductoValido(nombre)) {
            System.out.println("Error: El producto '" + nombre + "' no es válido en el contexto de piezas automotrices y será ignorado.");
            return; 
        }
        
        Producto productoNuevo = new Producto(codigo, nombre, linea_produccion, costo_unitario, minutos_utilizados, esDefectuosoFlag, peso_materia_prima, meta_produccion);        
        this.productos.add(productoNuevo);
    }
    
    private boolean esProductoValido(String nombre) {
        if (nombre == null) return false;
        String nombreLower = nombre.trim().toLowerCase();
        return PRODUCTOS_VALIDOS.stream().anyMatch(valido -> nombreLower.contains(valido));
    }

    // Cantidad producida: cuenta el tamaño de la lista
    Supplier<Long> cantidadProducida = () -> productos.stream().count();

    // Cantidad defectuosa: filtra con el Predicate y cuenta
    Supplier<Long> cantidadDefectuosa = () -> productos.stream()
            .filter(esDefectuoso)
            .count();

    // Minutos utilizados
    Supplier<Float> minutosUtilizados = () -> (float) productos.stream()
            .mapToDouble(Producto::getMinutosUtilizados)
            .sum();

    // Kilogramos de materia prima consumida
    Supplier<Float> kilogramosMateriaPrimaConsumida = () -> (float) productos.stream()
            .mapToDouble(Producto::getPesoMateriaPrima)
            .sum();

    // 1. Identificar productos con niveles altos de defectos
    Supplier<Map<String, Double>> productosConAltosDefectos = () -> productos.stream()
            .collect(Collectors.groupingBy(Producto::getNombre))
            .entrySet().stream()
            .filter(entry -> {
                long total = entry.getValue().size();
                long defectuosos = entry.getValue().stream().filter(esDefectuoso).count();
                return total > 0 && ((double) defectuosos / total) > 0.10;
            })
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> {
                    long total = entry.getValue().size();
                    long defectuosos = entry.getValue().stream().filter(esDefectuoso).count();
                    return ((double) defectuosos / total) * 100;
                }
            ));

    // 2. Calcular el cumplimiento de metas por producto (%)
    Supplier<Map<String, Double>> cumplimientoDeMetas = () -> productos.stream()
            .collect(Collectors.groupingBy(Producto::getNombre))
            .entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> {
                    long totalProducido = entry.getValue().size();
                    int meta = entry.getValue().get(0).getMetaProduccion();
                    return meta > 0 ? ((double) totalProducido / meta) * 100 : 0.0;
                }
            ));

    // 3. Conocer el costo total de fabricación
    Supplier<Double> costoTotalFabricacion = () -> productos.stream()
            .mapToDouble(Producto::getCostoUnitario)
            .sum();

    // 4. Determinar pérdidas económicas asociadas a productos defectuosos
    Supplier<Double> perdidasEconomicas = () -> productos.stream()
            .filter(esDefectuoso)
            .mapToDouble(Producto::getCostoUnitario)
            .sum();

    // 5. Analizar el desempeño general de cada línea de producción (Cantidad Producida Total por Línea)
    Supplier<Map<LineaProduccion, Long>> produccionPorLinea = () -> productos.stream()
            .collect(Collectors.groupingBy(
                Producto::getLineaProduccion,
                Collectors.counting()
            ));
            
    public ArrayList<Producto> getProductos() {
        return productos;
    }
}
