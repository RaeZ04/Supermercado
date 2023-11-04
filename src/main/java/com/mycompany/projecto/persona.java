package com.mycompany.projecto;

import java.util.ArrayList;
import java.util.List;

public class persona {
    private String name;
    private String dni;
    private List<Compra> compras;
    private double gastoTotal;

    public persona(String name, String dni) {
        this.name = name;
        this.dni = dni;
        this.compras = new ArrayList<>();
        this.gastoTotal = 0.0;
    }

    public String getName() {
        return name;
    }

    public String getDNI() {
        return dni;
    }

    public void registrarCompra(Producto producto) {
        compras.add(new Compra(producto));
        gastoTotal += producto.getPrecio();
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public double getGastoTotal() {
        return gastoTotal;
    }
}
