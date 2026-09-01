
public class Producto {
    private final String uuid;
    private String nombre;
    private LineaProduccion linea_produccion;
    private Float costo_unitario;
    private Float minutos_utilizados;
    private Boolean esDefectuoso;
    private Float peso_materia_prima;
    
    public Producto(String uuid, String nombre, String linea_produccion, Float costo_unitario, Float minutos_utilizados, Boolean esDefectuoso, Float peso_materia_prima){
        this.uuid = uuid;
        this.nombre = nombre;
        this.linea_produccion = LineaProduccion.EN_PROCESO;
        this.costo_unitario = costo_unitario;
        this.minutos_utilizados = minutos_utilizados;
        this.esDefectuoso = false;
        this.peso_materia_prima = peso_materia_prima;
    }

    // Getters

    public String getUuid(){
        return uuid;
    }
    public String getNombre(){
        return nombre;
    }
    public LineaProduccion getLineaProduccion(){
        return linea_produccion;
    }
    public Float getCostoUnitario(){
        return costo_unitario;
    }
    public Float getMinutosUtilizados(){
        return minutos_utilizados;
    }
    public Boolean getEsDefectuoso(){
        return esDefectuoso;
    }
    public Float get_peso_materia_prima(){
        return peso_materia_prima;
    }

    // Setters
    public void setNombre(String newName){
        this.nombre = newName;
    }
    public void setLineaProduccion(LineaProduccion newLineaProduccion){
        this.linea_produccion = newLineaProduccion;
    }
    public void setCostoUnitario(Float newCostoUnitario){
        this.costo_unitario = newCostoUnitario;
    }
    public void setMinutosUtilizados(Float newMinutosUtilizados){
        this.minutos_utilizados = newMinutosUtilizados;
    }
    public void setEsDefectuoso(Boolean newEsDefectuoso){
        this.esDefectuoso = newEsDefectuoso;
    }
    public void setPesoMateriaPrima(Float newPesoMateriaPrima){
        this.peso_materia_prima = newPesoMateriaPrima;
    }


}
