public class Main {
    public static void main(String [] args) {
        Produccion produccion1 = new Produccion();
        
        System.out.println("--- Inicio de Operaciones ---");
        
        // 1. Generar automáticamente un registro de prueba
        produccion1.generarRegistroPrueba();
        
        // Registrar otros productos para tener variedad en el reporte
        for(int i = 0; i < 80; i++) {
            produccion1.registrarProducto("2b", "freno de disco", LineaProduccion.EN_PROCESO, 800f, 60f, i < 2, 50f, 50); // Superará la meta
        }
        for(int i = 0; i < 30; i++) {
            produccion1.registrarProducto("3c", "puerta", LineaProduccion.TERMINANDO, 1500f, 90f, i < 5, 80f, 100); // Bajo cumplimiento
        }
        
        // 2. Modificar cantidades producidas cuando se reporten unidades adicionales
        produccion1.reportarUnidadesAdicionales("3c", "puerta", LineaProduccion.TERMINANDO, 1500f, 90f, 80f, 100, 10, 2);
        
        // 3. Aplicar ajustes porcentuales sobre determinados registros
        // Incrementamos el costo del "motor v8" un 10%
        produccion1.aplicarAjustePorcentualCosto("motor v8", 10.0);
        
        System.out.println("\n--- Consultas de Desempeño ---");
        java.util.Map.Entry<String, Double> mayor = produccion1.productoMayorDesempeno.get();
        if (mayor != null) {
            System.out.println("Producto de mayor desempeño: " + mayor.getKey() + " (" + String.format("%.2f", mayor.getValue()) + "%)");
        }
        
        java.util.Map.Entry<String, Double> menor = produccion1.productoMenorDesempeno.get();
        if (menor != null) {
            System.out.println("Producto de menor desempeño: " + menor.getKey() + " (" + String.format("%.2f", menor.getValue()) + "%)");
        }
        
        System.out.println("\nLíneas con bajo cumplimiento (< 80%):");
        produccion1.lineasConBajoCumplimiento.get().forEach((linea, porcentaje) -> System.out.println(" - " + linea + " (" + String.format("%.2f", porcentaje) + "%)"));
        
        // 4. Ejecutar un proceso de cierre del turno
        produccion1.cierreDeTurno();
    }
}
