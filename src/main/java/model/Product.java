package model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private int idproductos;
    private String nombre;
    private String categoria;
    private String condicion;
    private double precio;
    private String detalle;
    private int cantidad;
    private byte[] imagen;
    private String imagenBase64;

    public Product(String nombre, String categoria, String condicion, double precio, String detalle, int cantidad, byte[] imagen) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.condicion = condicion;
        this.precio = precio;
        this.detalle = detalle;
        this.cantidad = cantidad;
        this.imagen = imagen;
    }

    public Product(int idproductos, String nombre, String categoria, String condicion, double precio, String detalle, int cantidad, byte[] imagen) {
        this.idproductos = idproductos;
        this.nombre = nombre;
        this.categoria = categoria;
        this.condicion = condicion;
        this.precio = precio;
        this.detalle = detalle;
        this.cantidad = cantidad;
        this.imagen = imagen;
    }
    
}