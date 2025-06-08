// Wait for the DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Product sorting functionality
    const sortSelect = document.getElementById('sortOptions');
    if (sortSelect) {
        sortSelect.addEventListener('change', function() {
            const selectedOption = this.value;
            const productCards = document.querySelectorAll('.product-card');
            const productContainer = document.querySelector('.product-container');
            
            // Convert NodeList to Array for sorting
            const productsArray = Array.from(productCards);
            
            // Sort products based on selected option
            switch(selectedOption) {
                case 'name_asc':
                    productsArray.sort((a, b) => {
                        return a.querySelector('.card-title').textContent.localeCompare(b.querySelector('.card-title').textContent);
                    });
                    break;
                case 'name_desc':
                    productsArray.sort((a, b) => {
                        return b.querySelector('.card-title').textContent.localeCompare(a.querySelector('.card-title').textContent);
                    });
                    break;
                case 'price_asc':
                    productsArray.sort((a, b) => {
                        const priceA = parseFloat(a.querySelector('.card-text.fw-bold').textContent.replace('$', ''));
                        const priceB = parseFloat(b.querySelector('.card-text.fw-bold').textContent.replace('$', ''));
                        return priceA - priceB;
                    });
                    break;
                case 'price_desc':
                    productsArray.sort((a, b) => {
                        const priceA = parseFloat(a.querySelector('.card-text.fw-bold').textContent.replace('$', ''));
                        const priceB = parseFloat(b.querySelector('.card-text.fw-bold').textContent.replace('$', ''));
                        return priceB - priceA;
                    });
                    break;
            }
            
            // Clear and re-append sorted products
            if (productContainer) {
                productContainer.innerHTML = '';
                productsArray.forEach(product => {
                    productContainer.appendChild(product);
                });
            }
        });
    }
    
    // Quantity input validation for product details page
    const quantityInput = document.getElementById('quantity');
    if (quantityInput) {
        quantityInput.addEventListener('change', function() {
            if (this.value < 1) {
                this.value = 1;
            }
            if (this.value > 10) {
                this.value = 10;
            }
        });
    }
    
    // Image zoom effect on product details page
    const productImage = document.querySelector('.product-details-img');
    if (productImage) {
        productImage.addEventListener('mouseover', function() {
            this.style.cursor = 'zoom-in';
        });
        
        productImage.addEventListener('click', function() {
            const modal = document.createElement('div');
            modal.classList.add('modal', 'fade', 'show');
            modal.style.display = 'block';
            modal.style.backgroundColor = 'rgba(0,0,0,0.5)';
            modal.style.position = 'fixed';
            modal.style.top = '0';
            modal.style.left = '0';
            modal.style.width = '100%';
            modal.style.height = '100%';
            modal.style.zIndex = '1050';
            
            const modalDialog = document.createElement('div');
            modalDialog.classList.add('modal-dialog', 'modal-xl', 'modal-dialog-centered');
            
            const modalContent = document.createElement('div');
            modalContent.classList.add('modal-content');
            modalContent.style.backgroundColor = 'transparent';
            modalContent.style.border = 'none';
            
            const modalBody = document.createElement('div');
            modalBody.classList.add('modal-body', 'text-center');
            
            const zoomedImage = document.createElement('img');
            zoomedImage.src = this.src;
            zoomedImage.classList.add('img-fluid');
            zoomedImage.style.maxHeight = '90vh';
            
            modalBody.appendChild(zoomedImage);
            modalContent.appendChild(modalBody);
            modalDialog.appendChild(modalContent);
            modal.appendChild(modalDialog);
            
            document.body.appendChild(modal);
            
            modal.addEventListener('click', function() {
                this.remove();
            });
        });
    }
});
