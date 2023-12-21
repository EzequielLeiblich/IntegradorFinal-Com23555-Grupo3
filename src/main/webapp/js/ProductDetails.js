document.addEventListener("DOMContentLoaded", function () {
    const queryParams = new URLSearchParams(window.location.search);

    const productDetailId = {
        id: queryParams.get("id")
    };

    const productDetailsContainer = document.getElementById("productDetails");
    const btnEliminarElement = document.getElementById("btnEliminar");
    const btnModificarElement = document.getElementById("btnModificar");
    const btnGuardarElement = document.getElementById("btnGuardar");
    const btnContainerElement = document.getElementById("btnContainer");

    let objetoProducto = {
        id: 0,
        nombre: "",
        categoria: "",
        condicion: "",
        detalle: "",
        cantidad: 0,
        precio: 0,
        imagen: ""
    };

    function loadProduct() {

        fetch(`/app/products?action=getById&id=${productDetailId.id}`)
                .then(response => response.json())
                .then(data => {
                    productDetailsContainer.innerHTML += `
                        <div class="col-md-6 text-center">
                            <div class="clearfix">
                                <img src="data:image/jpeg;base64,${data.imagenBase64}" class="my-4" style="width: 75%" alt="imagen de producto"/>
                            </div>
                        </div>
                        <div class="card-body col-md-6">
                            <ul class="list-group list-group-flush">
                                <li class="list-group-item">
                                    <h2 class="card-title">${data.nombre}</h2>
                                </li>
                                <li class="list-group-item">Categoria: ${data.categoria}</li> 
                                <li class="list-group-item">Condicion: ${data.condicion}</li> 
                                <li class="list-group-item">Sinopsis: ${data.detalle}</li> 
                                <li class="list-group-item">Cantidad: ${data.cantidad}</li> 
                                <li class="list-group-item">
                                    <h5>Precio: ${data.precio}</h5>  
                                </li> 
                            </ul>
                        </div>
                    `;
                    objetoProducto.id = data.id;
                    objetoProducto.nombre = data.nombre;
                    objetoProducto.categoria = data.categoria;
                    objetoProducto.condicion = data.condicion;
                    objetoProducto.detalle = data.detalle;
                    objetoProducto.cantidad = data.cantidad;
                    objetoProducto.precio = data.precio;
                    objetoProducto.imagen = data.imagen;

                });
    }
    
     btnEliminarElement.addEventListener('click',function(){
        fetch(`/app/products?action=delete&id=${productDetailId.id}`,{
            method:"DELETE"
        })
                .then(response => response.json())
                .then(data=>{
                    if(data.success){
                        console.log("estoy dentro del if");
                        window.location.href = `/app/index.html`;
                    }
                });
    });
    
    btnModificarElement.addEventListener('click', function(){
        btnModificarElement.classList.add("d-none");
        btnEliminarElement.classList.add("d-none");
        btnGuardarElement.classList.remove("d-none");
        
        productDetailsContainer.innerHTML = `
            <div class="col-md-6 text-center">
                <div class="clearfix">
                    <img src="data:image/jpeg;base64,${objetoProducto.imagen}" class="my-4" style="width: 75%" alt="imagen de producto">
                </div>
            </div>
            <div class="card-body col-md-6">
                <form  class="mb-4" id = "updateProductForm" enctype="multipart/form-data">
                    <div class="card-body">
                        <div class="row">
                            <div class="form-floating my-3">
                                <input type="text" class="form-control" name="nombre" id="nombre" placeholder="nombre" value="${objetoProducto.nombre}" required/>
                                <label for="nombre">Nombre</label>
                            </div>

                            <div class="form-floating my-3">
                                <input type="text" class="form-control" name="categoria" id="categoria" placeholder="categoria" value="${objetoProducto.categoria}" required/>
                                <label for="categoria">Categoria</label>
                            </div>

                            <div class="form-floating my-3">
                                <input type="text" class="form-control" name="condicion" id="condicion" placeholder="condicion" value="${objetoProducto.condicion}" required/>
                                <label for="condicion">Condicion</label>
                            </div>

                            <div class="form-floating my-3">
                                <input type="number" class="form-control" name="cantidad" id="cantidad" placeholder="cantidad" value="${objetoProducto.cantidad}" required/>
                                <label  for="cantidad">Cantidad de Productos</label>
                            </div>

                            <div class="form-floating">
                                <textarea class="form-control" placeholder="Escriba el detalle aqui" name="detalle" id="detalle">${objetoProducto.detalle}</textarea>
                                <label for="detalle">Detalles</label>
                            </div>

                            <div class="form-floating my-3">
                                <input type="number" class="form-control" name="precio" id="precio" placeholder="precio" value="${objetoProducto.precio}" required/>
                                <label  for="precio">Precio</label>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        `
    });
    
    btnGuardarElement.addEventListener('click' , function(e){
        e.preventDefault();
        const formulario = new FormData();
        
        formulario.append("action", "update");
        formulario.append("id", productDetailId.id);
        formulario.append("nombre", document.getElementById("nombre").value);
        formulario.append("categoria", document.getElementById("categoria").value);
        formulario.append("condicion", document.getElementById("condicion").value);
        formulario.append("cantidad", document.getElementById("cantidad").value);
        formulario.append("detalle", document.getElementById("detalle").value);
        formulario.append("precio", document.getElementById("precio").value);
        formulario.append("imagen", objetoProducto.imagen);
        
        fetch(`/app/products`,{
           method:"POST",
           body: formulario
        })
                .then(response => {
                    if(!response.ok){
                        throw new Error(`Error en la solicitud: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data =>{
                    if(data.success == "true"){
                        window.location.href = `/app/index.html`;
                    }
                    else{
                        console.error("La solicitud fue exitosa, pero la respuesta indica un error: "+data.message);
                    }
                });
    });

    loadProduct();
});

