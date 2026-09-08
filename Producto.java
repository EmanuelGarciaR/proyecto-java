public class Producto {
    private String codigo;
    private String nombre;
    private LineaProduccion linea_produccion;
    private Float costo_unitario;
    private Float minutos_utilizados;
    private Boolean esDefectuoso;
    private Float peso_materia_prima;
    private Integer meta_produccion;
    
    public Producto(String codigo, String nombre, LineaProduccion linea_produccion, Float costo_unitario, Float minutos_utilizados, Boolean esDefectuoso, Float peso_materia_prima, Integer meta_produccion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.linea_produccion = linea_produccion;
        this.costo_unitario = costo_unitario;
        this.minutos_utilizados = minutos_utilizados;
        this.esDefectuoso = esDefectuoso;
        this.peso_materia_prima = peso_materia_prima;
        this.meta_produccion = meta_produccion;
    }

    // Getters

    public String getCodigo(){
        return codigo;
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
    public Float getPesoMateriaPrima(){
        return peso_materia_prima;
    }
    public Integer getMetaProduccion(){
        return meta_produccion;
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
    public void setMetaProduccion(Integer newMetaProduccion){
        this.meta_produccion = newMetaProduccion;
    }
}
