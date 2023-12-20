document.addEventListener("DOMContentLoaded", function() {
    const productCards = document.getElementById("productsCards");
    const products =[];
    
    function loadProductList(){
        fetch("/app/products?action=getAll")
            .then(response=> response.json())
            .then(data =>{
                data.forEach(product =>{
                    products.push(product);
                    productCards.innerHTML += `
                        <div class="col-md-3 mb-4 ident" data-product-id="${product.idproductos}">
                            <div class="card h-100 animate-hover-card">
                                <img src="data:image/jpeg;base64,${product.imagenBase64}" class="card-img-top h-75" alt="Imagen del Producto">
                                <div class="card-body">
                                    <h5 class="card-tittle">${product.nombre}</h5>
                                    <p class="card-text">${product.detalle}</p>
                                </div>
                            </div>
                        </div>
                    `
                });
            });
    }
    
    function filterProdructs(palabra){
        const productosFiltrados = products.filter( product=>{
           return product.nombre.toLowerCase().includes(palabra.toLowerCase()); 
        });
        
        productCards.innerHTML = "";
        
        productosFiltrados.forEach(product =>{
            const card = document.createElement("div");
            card.className = "col-md-3 mb-4 ident";
            card.setAttribute("data-product-id", product.idproductos);
            card.innerHTML = `
                <div class="card h-100 animate-hover-card">
                    <img src="data:image/jpeg;base64,${product.imagenBase64}" class="card-img-top h-75" alt="imagen de producto">
                    <div class="card-body">
                        <h5 class="card-title">${product.nombre}</h5>
                        <p class="card-text">${product.detalle}</p>
                    </div>
                </div>
            `;
                productsCards.appendChild(card);
        });
    }
    
    const searchForm = document.querySelector("form[role='search']");
    searchForm.addEventListener("submit", function(e){
        e.preventDefault();
        const searchTerm = searchForm.querySelector("input[type='search']").value;
        filterProdructs(searchTerm);
    });
    
    productCards.addEventListener("click", function(e){
        const clickedCard = e.target.closest(".ident");
        if(!clickedCard){
            return;
        }
        
        const productId = clickedCard.dataset.productId;
        
        fetch(`/app/products?action=getDetails&id=${productId}`)
                .then(response => response.json())
                .then (productDetails => {
                    const queryParams = new URLSearchParams({
                        id : productDetails.idproductos
                    });
                    
                    window.location.href = `/app/pages/productDetails.html?${queryParams.toString()}`;
                })
                .catch(error => console.error("Error en la solicitud GET:", error));
    });
    
    loadProductList();
});