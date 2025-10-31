package tpintegrado_sanchez_rios;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class Factura {

    private int id;
    private String fecha;
    private Cliente cliente;
    private Map<Producto, Integer> items = new LinkedHashMap<>();
    private double total;

    public Factura(Cliente cliente) {
        this.cliente = cliente;
        this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.id = 0;
        this.total = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Map<Producto, Integer> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    public void addItem(Producto p, int cantidad) throws ExcepcionStock {
        if (p.getStock() < cantidad) {
            throw new ExcepcionStock("Stock insuficiente para " + p.getDescripcion());
        }
        items.put(p, items.getOrDefault(p, 0) + cantidad);
    }

    public void calcularTotal() {
        double suma = 0;
        for (Map.Entry<Producto, Integer> e : items.entrySet()) {
            suma += e.getKey().getPrecio() * e.getValue();
        }
        this.total = suma;
    }

    public void finalizar() {
        calcularTotal();

    }

    public void mostrarFactura() {
        System.out.println("Factura id: " + id + " | Fecha: " + fecha);
        System.out.println("Cliente: " + (cliente != null ? cliente.getNombre() + " (DNI: " + cliente.getDni() + ")" : "Anonimo"));
        for (Map.Entry<Producto, Integer> e : items.entrySet()) {
            System.out.println(e.getKey().getDescripcion() + " x" + e.getValue() + " | $ " + e.getKey().getPrecio());
        }
        System.out.printf("TOTAL: $%.2f\n", total);
    }

    @Override
    public String toString() {
        return "Factura{" + "id=" + id + ", fecha=" + fecha + ", total=" + total + ", cliente="
                + (cliente != null ? cliente.getNombre() + " (DNI: " + cliente.getDni() + ")" : "Anonimo") + "}";
    }
}
