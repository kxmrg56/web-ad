// 家用电器分类商品数据
const categoryProducts = [
    {
        id: 201,
        name: '索尼 85英寸4K电视',
        description: 'XR认知芯片，全阵列背光，杜比视界，智能安卓系统',
        price: 12999,
        originalPrice: 14999,
        stock: 8
    },
    {
        id: 202,
        name: '海尔 对开门冰箱',
        description: '变频风冷无霜，智能温控，大容量存储，节能静音',
        price: 5999,
        originalPrice: 6999,
        stock: 15
    },
    {
        id: 203,
        name: '美的 变频空调',
        description: '新一级能效，智能控温，自清洁，静音运行',
        price: 3299,
        originalPrice: 3999,
        stock: 20
    },
    {
        id: 204,
        name: '西门子 滚筒洗衣机',
        description: '10公斤大容量，智能除菌，多种洗涤程序，节能省水',
        price: 4599,
        originalPrice: 5299,
        stock: 12
    },
    {
        id: 205,
        name: '戴森 无叶风扇',
        description: '空气净化，智能温控，远程控制，安全设计',
        price: 3999,
        originalPrice: 4599,
        stock: 18
    },
    {
        id: 206,
        name: '小米 扫地机器人',
        description: 'LDS激光导航，智能路径规划，大吸力，自动回充',
        price: 1999,
        originalPrice: 2499,
        stock: 25
    },
    {
        id: 207,
        name: '格力 立式空调',
        description: '3匹变频，快速制冷制热，智能WiFi控制，静音设计',
        price: 6999,
        originalPrice: 7999,
        stock: 10
    },
    {
        id: 208,
        name: '松下 微波炉',
        description: '变频加热，智能菜单，蒸汽功能，大容量内胆',
        price: 899,
        originalPrice: 1199,
        stock: 30
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