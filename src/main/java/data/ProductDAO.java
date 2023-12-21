package data;

import static data.Conexion.*;
import java.sql.*;
import java.util.*;
import model.Product;

public class ProductDAO {
    
    private static final String SQL_SELECT = "SELECT * FROM productos";
    
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM productos WHERE idproductos = ?";
    
    private static final String SQL_INSERT = "INSERT INTO productos(nombre, categoria, condicion, precio, cantidad, imagen, detalle) VALUES(?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_UPDATE = "UPDATE productos SET nombre = ?, categoria = ?, condicion = ?, precio= ?, cantidad=?, detalle=? WHERE idproductos = ?";
    
    private static final String SQL_DELETE = "DELETE FROM productos WHERE idproductos = ?";
    
    public static List<Product> seleccionar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Product producto = null;
        List<Product> productos = new ArrayList();
        
        try {
            conn = getConexion();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int idproducto = rs.getInt(1);
                String nombre = rs.getString("nombre");
                String categoria = rs.getString("categoria");
                String condicion = rs.getString("condicion");
                double precio = rs.getDouble("precio");
                String detalle = rs.getString("detalle");
                int cantidad = rs.getInt("cantidad");
                
                Blob blob = rs.getBlob("imagen");
                byte[] imagenBytes = blob.getBytes(1, (int)blob.length());
                
                producto = new Product(idproducto, nombre, categoria, condicion, precio, detalle, cantidad, imagenBytes);
                
                productos.add(producto);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                close(rs);
                close(stmt);
                close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            } 
        }
        
        return productos;
    }
    
    public static int insertar(Product producto){
        Connection conn = null;
        PreparedStatement stmt = null;
        int registros = 0;
        try {
            conn = getConexion();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getCategoria());
            stmt.setString(3, producto.getCondicion());
            stmt.setDouble(4, producto.getPrecio());
            stmt.setInt(5, producto.getCantidad());
            
            Blob imagenBlob = conn.createBlob();
            imagenBlob.setBytes(1, producto.getImagen());
            stmt.setBlob(6, imagenBlob);
            
            stmt.setString(7, producto.getDetalle());
            
            registros = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally {
            try {
                close(stmt);
                close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return registros;
    }
    
    public static Product seleccionarPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Product producto = null;
        
        try {
            conn = getConexion();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                int idproductos = rs.getInt("idproductos");
                String nombre = rs.getString("nombre");
                String categoria = rs.getString("categoria");
                String condicion = rs.getString("condicion");
                String detalle = rs.getString("detalle");
                double precio = rs.getDouble("precio");
                int cantidad = rs.getInt("cantidad");

                Blob blob = rs.getBlob("imagen");
                byte[] imagenBytes = blob.getBytes(1, (int)blob.length());

                producto = new Product(idproductos, nombre, categoria, condicion, precio, detalle, cantidad, imagenBytes);
            
            
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                close(rs);
                close(stmt);
                close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return producto;
    }
    
    public static int eliminar(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int registros = 0;
        try {
            conn = getConexion();
            
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, id);
            
            registros = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally{
            try {
                close(stmt);
                close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return registros;
    }
    
    public static int actualizar(Product producto) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int registros = 0;
        try {
            conn = getConexion();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getCategoria());
            stmt.setString(3, producto.getCondicion());
            stmt.setDouble(4, producto.getPrecio());
            stmt.setInt(5, producto.getCantidad());
            stmt.setString(6, producto.getDetalle());
            stmt.setInt(7, producto.getIdproductos());
            
            registros = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally{
            try {
                close(stmt);
                close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return registros;
    }
    
}