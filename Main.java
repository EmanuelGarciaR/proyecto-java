public class Main {
    public static void main(String [] args) {
        Produccion produccion1 = new Produccion();
        
        System.out.println("--- Registrando Productos ---");
        // Producto inválido (debería ignorarse)
        produccion1.registrarProducto("1a", "bombillo", LineaProduccion.ENSAMBLANDO, 1000f, 30f, false, 10f, 150);
        produccion1.registrarProducto("2a", "silla", LineaProduccion.TERMINANDO, 500f, 20f, false, 5f, 50);
        
        // Productos válidos
        // Simulamos 100 motores v8, 15 defectuosos, meta 120
        for(int i = 0; i < 100; i++) {
            produccion1.registrarProducto("1b", "motor v8", LineaProduccion.ENSAMBLANDO, 5000f, 120f, i < 15, 150f, 120);
        }
        
        // Simulamos 50 frenos de disco, 2 defectuosos, meta 50
        for(int i = 0; i < 50; i++) {
            produccion1.registrarProducto("2b", "freno de disco", LineaProduccion.EN_PROCESO, 800f, 60f, i < 2, 50f, 50);
        }
        
        System.out.println("\n--- Resultados de Análisis ---");
        System.out.println("Cantidad total producida: " + produccion1.cantidadProducida.get());
        System.out.println("Cantidad total defectuosa: " + produccion1.cantidadDefectuosa.get());
        
        System.out.println("\n1. Productos con altos niveles de defectos (>10%):");
        produccion1.productosConAltosDefectos.get().forEach((nombre, porcentaje) -> 
            System.out.println(" - " + nombre + " (" + String.format("%.2f", porcentaje) + "% defectuoso)")
        );
        
        System.out.println("\n2. Cumplimiento de metas:");
        produccion1.cumplimientoDeMetas.get().forEach((nombre, porcentaje) -> 
            System.out.println(" - " + nombre + ": " + String.format("%.2f", porcentaje) + "%")
        );
        
        System.out.println("\n3. Costo total de fabricación: $" + String.format("%.2f", produccion1.costoTotalFabricacion.get()));
        System.out.println("4. Pérdidas económicas (por defectos): $" + String.format("%.2f", produccion1.perdidasEconomicas.get()));
        
        System.out.println("\n5. Desempeño por línea de producción (Total Producido):");
        produccion1.produccionPorLinea.get().forEach((linea, cantidad) -> 
            System.out.println(" - " + linea + ": " + cantidad + " unidades")
        );
    }
}
