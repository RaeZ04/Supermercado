package com.mycompany.projecto;

public class Compra {
    private Producto producto;
    private int cantidad;

    public Compra(Producto producto) {
        this.producto = producto;
        this.cantidad = 1; 
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void incrementarCantidad() {
        cantidad++;
    }
}
