// 运动专区分类商品数据
const categoryProducts = [
    {
        id: 401,
        name: '专业跑步机',
        description: '静音电机，多功能显示，可折叠设计，家用健身器材',
        price: 2999,
        originalPrice: 3999,
        stock: 20
    }
];

// 页面加载时渲染商品
document.addEventListener('DOMContentLoaded', function() {
    renderProducts();
    updateCartCount();
});

// 渲染商品列表
function renderProducts() {
    // 更新商品数量显示
    const productCountElement = document.getElementById('product-count-number');
    if (productCountElement) {
        productCountElement.textContent = categoryProducts.length;
    }

    const productsGrid = document.getElementById('products-grid');
    if (!productsGrid) return;

    productsGrid.innerHTML = '';

    categoryProducts.forEach((product, index) => {
        const productCard = document.createElement('div');
        productCard.className = 'product-card';
        productCard.innerHTML = `
            <div class="product-image">商品图片</div>
            <div class="product-info">
                <h3 class="product-name">${product.name}</h3>
                <p class="product-description">${product.description}</p>
                <div class="product-price">
                    <span class="current-price">¥${product.price}</span>
                    <span class="original-price">¥${product.originalPrice}</span>
                </div>
                <div class="product-stock">库存: ${product.stock}</div>
                <div class="product-actions">
                    <input type="number" class="quantity-input" value="1" min="1" max="${product.stock}">
                    <button class="btn btn-primary" onclick="addToCart(${product.id}, ${index})">
                        <i class="fas fa-cart-plus"></i> 加入购物车
                    </button>
                </div>
            </div>
        `;
        productsGrid.appendChild(productCard);
    });
}

// 添加到购物车功能
function addToCart(productId, productIndex) {
    const product = categoryProducts.find(p => p.id === productId);
    if (!product) return;

    const quantityInputs = document.querySelectorAll('.quantity-input');
    const quantityInput = quantityInputs[productIndex];
    const quantity = parseInt(quantityInput?.value) || 1;

    if (quantity > product.stock) {
        alert('库存不足！');
        return;
    }

    // 获取现有购物车数据
    let cart = JSON.parse(localStorage.getItem('shoppingCart') || '[]');

    // 检查商品是否已在购物车
    const existingItem = cart.find(item => item.id === productId);
    if (existingItem) {
        existingItem.quantity += quantity;
    } else {
        cart.push({
            id: product.id,
            name: product.name,
            price: product.price,
            quantity: quantity,
            image: '商品图片'
        });
    }

    // 保存到localStorage
    localStorage.setItem('shoppingCart', JSON.stringify(cart));

    // 更新购物车数量显示
    updateCartCount();

    alert(`已添加 ${quantity} 件 ${product.name} 到购物车！`);
}

// 更新购物车数量显示
function updateCartCount() {
    const cart = JSON.parse(localStorage.getItem('shoppingCart') || '[]');
    const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);

    const cartCountElements = document.querySelectorAll('.cart-count');
    cartCountElements.forEach(element => {
        element.textContent = totalItems;
    });
}

// 搜索功能
document.addEventListener('DOMContentLoaded', function() {
    const searchForm = document.querySelector('.search-form');
    if (searchForm) {
        searchForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const searchInput = this.querySelector('.search-input');
            const searchTerm = searchInput.value.trim();

            if (searchTerm) {
                const filteredProducts = categoryProducts.filter(product =>
                    product.name.includes(searchTerm) ||
                    product.description.includes(searchTerm)
                );

                if (filteredProducts.length > 0) {
                    // 临时显示筛选结果
                    const originalProducts = [...categoryProducts];
                    categoryProducts.length = 0;
                    categoryProducts.push(...filteredProducts);
                    renderProducts();

                    // 3秒后恢复显示所有商品
                    setTimeout(() => {
                        categoryProducts.length = 0;
                        categoryProducts.push(...originalProducts);
                        renderProducts();
                        searchInput.value = '';
                    }, 3000);
                } else {
                    alert('未找到相关商品！');
                }
            }
        });
    }
});