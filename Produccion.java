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

    // #Cantidad producida
    Supplier<Long> cantidadProducida = () -> productos.stream().count();


    //Cantidad defectuosa
    Supplier<Long> cantidadDefectuosa = () -> productos.stream()
    .filter(producto -> producto.getEsDefectuoso())
    .count();

    //Minutos utilzados
    Supplier<Float> minutosUtilizados = () -> productos.stream()
    .map(Producto::getMinutosUtilizados)
    .reduce(0f, (acumulador, minutoActual)-> acumulador + minutoActual);

    //Kilogramos de materia prima consumida
    Supplier<Float> kilogramosMateriaPrimaConsumida = () -> productos.stream()
    .map(Producto::getPesoMateriaPrima)
    .reduce(0f, (acumulador, pesoActual) -> acumulador + pesoaActual);
}
