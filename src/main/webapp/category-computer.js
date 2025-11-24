// 电脑办公分类商品数据
const categoryProducts = [
    {
        id: 101,
        name: 'MacBook Pro 16寸',
        description: 'M3 Pro芯片，32GB内存，1TB SSD， Liquid 视网膜 XDR 显示屏',
        price: 18999,
        originalPrice: 20999,
        stock: 15
    },
    {
        id: 102,
        name: '联想 ThinkPad X1 Carbon',
        description: '13代英特尔酷睿i7，16GB内存，512GB SSD，商务轻薄本',
        price: 8999,
        originalPrice: 9999,
        stock: 25
    },
    {
        id: 103,
        name: '戴尔 XPS 13 Plus',
        description: '12代英特尔酷睿i7，16GB内存，1TB SSD，3.5K OLED触控屏',
        price: 10999,
        originalPrice: 12999,
        stock: 20
    },
    {
        id: 104,
        name: '华为 MateBook X Pro',
        description: '13代英特尔酷睿i7，16GB内存，1TB SSD，3.1K原色全面屏',
        price: 9499,
        originalPrice: 10999,
        stock: 18
    },
    {
        id: 105,
        name: '华硕 ROG 枪神7',
        description: '13代英特尔酷睿i9，RTX 4060，16GB内存，1TB SSD，电竞游戏本',
        price: 12999,
        originalPrice: 14999,
        stock: 12
    },
    {
        id: 106,
        name: '苹果 iMac 24寸',
        description: 'M3芯片，8核CPU，8核GPU，8GB内存，256GB SSD，4.5K视网膜屏',
        price: 9999,
        originalPrice: 11999,
        stock: 30
    },
    {
        id: 107,
        name: '微软 Surface Laptop 5',
        description: '12代英特尔酷睿i5，8GB内存，512GB SSD，触控PixelSense显示屏',
        price: 7999,
        originalPrice: 8999,
        stock: 22
    },
    {
        id: 108,
        name: '惠普 暗影精灵9',
        description: '13代英特尔酷睿i7，RTX 4050，16GB内存，512GB SSD，电竞游戏本',
        price: 8499,
        originalPrice: 9999,
        stock: 16
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