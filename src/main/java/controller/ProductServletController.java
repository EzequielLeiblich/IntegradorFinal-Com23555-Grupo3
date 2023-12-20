package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;
import model.Product;
import org.apache.commons.io.IOUtils;

@WebServlet("/products")
@MultipartConfig(
        location = "/media/temp",
        fileSizeThreshold = 1024 * 1024, //Tamaño umbral 1MB
        maxFileSize = 1024 * 1024 * 5, //Tamaño maximo de archivo en bytes 5MB
        maxRequestSize = 1024 * 1024 * 10 // Tamaño maximo de request en bytes 10MB
)
public class ProductServletController extends HttpServlet{
    
    List<Product> productList = new ArrayList();
    ObjectMapper mapper = new ObjectMapper();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{
        req.setCharacterEncoding("UTF-8");
        String route = req.getParameter("action");
        System.out.println("parametro: "+ route);
        switch (route){
            case "getAll"->{
                res.setContentType("application/json; charset=UTF-8");
                productList = ProductDAO.seleccionar();
                
                for(Product product : productList){
                    byte[] imagenBytes = product.getImagen();
                    String imagenBase64 = Base64.getEncoder().encodeToString(imagenBytes);
                    product.setImagenBase64(imagenBase64);
                }
                
                mapper.writeValue(res.getWriter(), productList);
            }
            
            case "getDetails"->{
                String productId = req.getParameter("id");
                
                Product productDetails = ProductDAO.seleccionarPorId(Integer.parseInt(productId));
                
                res.setContentType("application/json");
                mapper.writeValue(res.getWriter(),productDetails);
            }
            
            case "getById"->{
                
                int id = Integer.parseInt(req.getParameter("id"));
                
                res.setContentType("application/json");
                Product productDetails = ProductDAO.seleccionarPorId(id);
                
                byte[] imagenBytes = productDetails.getImagen();
                String imagenBase64 = Base64.getEncoder().encodeToString(imagenBytes);
                productDetails.setImagenBase64(imagenBase64);
                
                mapper.writeValue(res.getWriter(), productDetails);
            }
            
            default->{
                    System.out.println("parametro no valido.");
            }
            
            
                
        }   
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{
        String route = req.getParameter("action");
        
        switch(route){
            case "add" ->{
                String nombre = req.getParameter("nombre");
                String categoria = req.getParameter("categoria");
                String condicion = req.getParameter("condicion");
                int cantidad = Integer.parseInt(req.getParameter("cantidad"));
                String detalle = req.getParameter("detalle");
                double precio = Double.parseDouble(req.getParameter("precio"));
                
                Part filePart = req.getPart("imagen");
                byte [] imagenBytes = IOUtils.toByteArray(filePart.getInputStream());
                
                Product newProduct = new Product(nombre,categoria,condicion,precio,detalle, cantidad, imagenBytes);
                
                ProductDAO.insertar(newProduct);
                
                res.setContentType("application/json");
                Map <String, String> response = new HashMap();
                response.put("message", "Producto guardado exitosamente!!!");
                mapper.writeValue(res.getWriter(), response);
            }
            
            case "update"->{
                
                try{
                    int id= Integer.parseInt(req.getParameter("id"));
                    String nombre = req.getParameter("nombre");
                    String categoria = req.getParameter("categoria");
                    String condicion = req.getParameter("condicion");
                    int cantidad = Integer.parseInt(req.getParameter("cantidad"));
                    String detalle = req.getParameter("detalle");
                    double precio = Double.parseDouble(req.getParameter("precio"));

                    Part filePart = req.getPart("imagen");
                    byte[] imageBytes = IOUtils.toByteArray(filePart.getInputStream());

                    Product product = new Product(id,nombre, categoria, condicion, precio, detalle, cantidad, imageBytes);

                    ProductDAO.actualizar(product);

                    res.setContentType("application/json");
                    res.setCharacterEncoding("UTF-8");

                    Map <String, String> response = new HashMap<>();
                    response.put("success", "true");
                    mapper.writeValue(res.getWriter(), response);
                }catch(Exception e){
                    res.setContentType("application/json");
                    res.setCharacterEncoding("UTF-8");
                    
                    Map <String, String> responseFail = new HashMap<>();
                    responseFail.put("success", "false");
                    responseFail.put("message", e.getMessage());
                    mapper.writeValue(res.getWriter(), responseFail);
                }
                
            }
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException{
        String route = req.getParameter("action");
        System.out.println("route = " + route);
        
        switch(route){
            case "delete"->{
                try{
                    int id = Integer.parseInt(req.getParameter("id"));
                    System.out.println("id:" + id);
                    
                    int result = ProductDAO.eliminar(id);
                    res.setContentType("application/json");
                    res.setCharacterEncoding("UTF-8");
                    res.getWriter().write("{\"success\": true}");
                }catch(Exception e){
                    res.setContentType("application/json");
                    res.setCharacterEncoding("UTF-8");
                    res.getWriter().write("{\"success\": false, \"message\": \"Error al borrar el registro.\"}");
                }
            }
            
            default->{
                System.out.println("error en parametro.");
            }
        }
    }
}
