import java.util.ArrayList;
import java.util.function.*;

    public class Produccion {
        private ArrayList<Producto>productos;

    public Produccion(){
        this.productos = new ArrayList<>();
    }
        
    public void registrarProducto(String uuid, String nombre, LineaProduccion linea_produccion, Float costo_unitario, Float minutos_utilizados, Boolean esDefectuoso, Float peso_materia_prima){
        Producto productoNuevo = new Producto(uuid, nombre, linea_produccion, costo_unitario, minutos_utilizados, esDefectuoso, peso_materia_prima);        
        this.productos.add(productoNuevo);
    }

    Supplier<Long> cantidadProducida = () -> productos.stream().count();
}
