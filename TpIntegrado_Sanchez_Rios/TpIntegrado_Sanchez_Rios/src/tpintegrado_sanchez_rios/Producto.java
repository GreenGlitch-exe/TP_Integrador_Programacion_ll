package tpintegrado_sanchez_rios;

public abstract class Producto implements Vendible {

    private static int contadorId = 1; // contador para IDs automáticos
    private int id;
    private String tipo;
    private String descripcion;
    private double precio;
    private int stock;

    public Producto(String tipo, String descripcion, double precio, int stock) {
        this.id = contadorId++; // asigna automáticamente el ID
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    @Override
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public void actualizarStock(int delta) throws ExcepcionStock {
        if (this.stock + delta < 0) throw new ExcepcionStock("Stock insuficiente para " + descripcion);
        this.stock += delta;
    }

    public void modificarDatos(String nuevaDesc, double nuevoPrecio, int nuevoStock) {
        if (nuevaDesc != null && !nuevaDesc.isEmpty()) this.descripcion = nuevaDesc;
        if (nuevoPrecio > 0) this.precio = nuevoPrecio;
        if (nuevoStock >= 0) this.stock = nuevoStock;
        System.out.println("✅ Producto actualizado correctamente.");
    }

    public boolean validarDatos() {
        return (descripcion != null && !descripcion.isEmpty() && precio > 0 && stock >= 0);
    }

    @Override
    public String toString() {
        return String.format("ID:%d - %s - %s - $%.2f - stock:%d", id, tipo, descripcion, precio, stock);
    }

    // Método para sincronizar el contador con la BD
    public static void sincronizarContador(int ultimoId) {
        contadorId = ultimoId + 1;
    }
}
