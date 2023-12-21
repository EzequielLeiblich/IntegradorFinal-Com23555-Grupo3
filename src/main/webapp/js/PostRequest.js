document.addEventListener("DOMContentLoaded", function(){
    
    const addProductForm = document.getElementById("addProductForm");
    const parrafoAlerta = document.createElement("P");
    const productoElement = document.getElementById("nombre");
    const categoriaElement = document.getElementById("categoria");
    const condicionElement = document.getElementById("condicion");
    const cantidadElement = document.getElementById("cantidad");
    const detalleElement = document.getElementById("detalle");
    const precioElement = document.getElementById("precio");
    
    const imageElement = document.getElementById("imagen");
    const imagenPreview = document.getElementById("imagenPreview");
    const imagenContainer = document.getElementById("imagenContainer");
    
    addProductForm.addEventListener("submit", function(e){
        e.preventDefault();
        
        const datos = new FormData();
        
        datos.append("action","add");
        datos.append("nombre",productoElement.value);
        datos.append("categoria",categoriaElement.value);
        datos.append("condicion",condicionElement.value);
        datos.append("cantidad",cantidadElement.value);
        datos.append("detalle",detalleElement.value);
        datos.append("precio",precioElement.value);
        datos.append("imagen",imageElement.files[0]);
        
        fetch("/app/products", {
            method: "POST",
            body: datos
        })
                .then(response => response.json())
                .then(data=> {
                    parrafoAlerta.textContent = data.message;
                    addProductForm.appendChild(parrafoAlerta);
                    
                    setTimeout(()=>{
                        parrafoAlerta.remove();
                        productoElement.value = "";
                        categoriaElement.value = "";
                        condicionElement.value = "";
                        cantidadElement.value = "";
                        detalleElement.value = "";
                        precioElement.value = "";
                        imageElement.value = "";
                        imagenContainer.classList.add("d-none");
                    },3000)
                });
    });
    
    imageElement.addEventListener("change", function(){
        const selectedImage = imageElement.files[0];
        
        if(selectedImage){
            const reader = new FileReader();
            reader.onload = function(e){
                imagenPreview.src = e.target.result;
                imagenContainer.classList.remove("d-none");
            };
            
            reader.readAsDataURL(selectedImage);
        }else{
            imagenPreview.src= "";
        }
    });
    
});

