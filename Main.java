public class Main {
    public static void main(String [] args) {
        Produccion produccion1 = new Produccion();
        produccion1.registrarProducto("1a", "bombillo", LineaProduccion.ENSAMBLANDO, 1000f, 5f, false, 10f);
    }
}