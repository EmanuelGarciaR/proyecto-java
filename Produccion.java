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


    // Obtener producto de mayor desempeño
    Supplier<Map.Entry<String, Double>> productoMayorDesempeno = () -> cumplimientoDeMetas.get().entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .orElse(null);

    // Obtener producto de menor desempeño
    Supplier<Map.Entry<String, Double>> productoMenorDesempeno = () -> cumplimientoDeMetas.get().entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .orElse(null);

    // Identificar líneas con bajo cumplimiento (menos del 80%)
    Supplier<Map<LineaProduccion, Double>> lineasConBajoCumplimiento = () -> productos.stream()
            .collect(Collectors.groupingBy(Producto::getLineaProduccion))
            .entrySet().stream()
            .filter(entry -> {
                long totalProducido = entry.getValue().size();
                long totalMetas = entry.getValue().stream()
                                    .map(Producto::getNombre)
                                    .distinct()
                                    .mapToLong(nombre -> entry.getValue().stream().filter(p -> p.getNombre().equals(nombre)).findFirst().get().getMetaProduccion())
                                    .sum();
                return totalMetas > 0 && ((double) totalProducido / totalMetas) < 0.80;
            })
            .map(Map.Entry::getKey)
            .collect(Collectors.toMap(
                k -> k,
                k -> {
                    long totalProducido = productos.stream().filter(p -> p.getLineaProduccion() == k).count();
                    long totalMetas = productos.stream().filter(p -> p.getLineaProduccion() == k).mapToLong(Producto::getMetaProduccion).sum();
                    return totalMetas > 0 ? ((double) totalProducido / totalMetas) * 100 : 0.0;
                }
            ));

    // Productos que superaron la meta (> 100%)
    Supplier<List<String>> productosSuperaronMeta = () -> cumplimientoDeMetas.get().entrySet().stream()
            .filter(entry -> entry.getValue() > 100.0)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

    // Producto con mayor pérdida económica
    Supplier<String> productoMayorPerdida = () -> productos.stream()
            .filter(esDefectuoso)
            .collect(Collectors.groupingBy(Producto::getNombre, Collectors.summingDouble(Producto::getCostoUnitario)))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("Ninguno");

    // Línea con mayor cantidad producida
    Supplier<LineaProduccion> lineaMayorCantidad = () -> produccionPorLinea.get().entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);


    // Generar un registro de producción de prueba
    public void generarRegistroPrueba() {
        System.out.println("-> Generando registro de producción de prueba...");
        for(int i = 0; i < 50; i++) {
            registrarProducto("test1", "motor v8", LineaProduccion.ENSAMBLANDO, 5000f, 120f, i < 5, 150f, 60);
        }
    }

    // Modificar cantidades producidas cuando se reporten unidades adicionales
    // (Como cada unidad es un objeto, añadimos unidades adicionales a la lista)
    public void reportarUnidadesAdicionales(String codigo, String nombre, LineaProduccion linea, Float costo_unitario, Float minutos_utilizados, Float peso, Integer meta, int adicionalesBuenas, int adicionalesDefectuosas) {
        System.out.println("-> Reportando " + (adicionalesBuenas + adicionalesDefectuosas) + " unidades adicionales de " + nombre);
        for (int i = 0; i < adicionalesBuenas; i++) {
            registrarProducto(codigo, nombre, linea, costo_unitario, minutos_utilizados, false, peso, meta);
        }
        for (int i = 0; i < adicionalesDefectuosas; i++) {
            registrarProducto(codigo, nombre, linea, costo_unitario, minutos_utilizados, true, peso, meta);
        }
    }

    // Aplicar ajustes porcentuales sobre determinados registros (ej. incrementar costo)
    public void aplicarAjustePorcentualCosto(String nombreProducto, double porcentaje) {
        System.out.println("-> Aplicando ajuste del " + porcentaje + "% al costo de " + nombreProducto);
        productos.stream()
            .filter(p -> p.getNombre().equals(nombreProducto))
            .forEach(p -> p.setCostoUnitario((float) (p.getCostoUnitario() * (1 + (porcentaje / 100)))));
    }

    // Ejecutar un proceso de cierre del turno
    public void cierreDeTurno() {
        System.out.println("\n========== REPORTE DE CIERRE DE TURNO ==========");
        
        System.out.println("\nLíneas existentes:");
        produccionPorLinea.get().keySet().forEach(linea -> System.out.println(" - " + linea));
        
        System.out.println("\nProducción total por línea:");
        produccionPorLinea.get().forEach((linea, cantidad) -> System.out.println(" - " + linea + ": " + cantidad + " unid."));
        
        System.out.println("\nProductos críticos (altos defectos):");
        productosConAltosDefectos.get().forEach((nombre, porc) -> System.out.println(" - " + nombre + " (" + String.format("%.2f", porc) + "%)"));
        
        System.out.println("\nProductos que superaron la meta:");
        productosSuperaronMeta.get().forEach(nombre -> System.out.println(" - " + nombre));
        
        System.out.println("\nUnidades defectuosas totales: " + cantidadDefectuosa.get());
        
        System.out.println("Costo total de producción: $" + String.format("%.2f", costoTotalFabricacion.get()));
        
        System.out.println("Pérdidas económicas: $" + String.format("%.2f", perdidasEconomicas.get()));
        
        System.out.println("Producto con mayor pérdida: " + productoMayorPerdida.get());
        
        System.out.println("Línea con mayor cantidad producida: " + lineaMayorCantidad.get());
        
        System.out.println("================================================");
    }
            
    public ArrayList<Producto> getProductos() {
        return productos;
    }
}
