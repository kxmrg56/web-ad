// 美妆护理分类商品数据 - 改为从数据库加载
let categoryProducts = [];

// 页面加载时从数据库加载商品
document.addEventListener('DOMContentLoaded', async function() {
    // 先确保购物车管理器已加载
    if (!window.shoppingCart) {
        console.error('购物车管理器未加载，请确保 shopping-cart-manager.js 已引入');
        return;
    }

    // 初始化购物车
    await window.initShoppingCart();

    // 加载商品数据
    await loadProductsFromDatabase();
    renderProducts();

    // 搜索功能
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
                    }, 60*1000);
                } else {
                    alert('未找到相关商品！');
                }
            }
        });
    }
});

// 从数据库加载商品数据
async function loadProductsFromDatabase() {
    try {
        const response = await fetch('/shopping_web_war_exploded/api/products?category=beauty');

        if (!response.ok) {
            throw new Error(`加载失败: ${response.status}`);
        }

        const data = await response.json();

        // 将API返回的数据转换为原来的格式
        categoryProducts = data.map(item => ({
            id: item.id,
            name: item.name,
            description: item.description,
            price: item.price,
            originalPrice: item.price * 1.15,
            stock: item.stock || 10,
            imageUrl: item.imageUrl || '商品图片'
        }));

    } catch (error) {
        console.error('加载商品失败:', error);
        // 如果API失败，使用原来的硬编码数据
        categoryProducts = [
            {
                id: 501,
                name: '护肤精华套装',
                description: '补水保湿，改善肤质，提亮肤色，全套护肤解决方案',
                price: 599,
                originalPrice: 799,
                stock: 100
            }
        ];
        alert('网络连接失败，显示本地商品数据');
    }
}

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
    <div class="product-image">
        <img src="${product.imageUrl}" 
             alt="${product.name}" 
             class="product-img"
             onerror="this.src='https://via.placeholder.com/300x200?text=${encodeURIComponent(product.name)}';">
    </div>
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
            <button class="btn btn-primary" onclick="addToCartHandler(${product.id}, ${index}, '${product.name.replace(/'/g, "\\'")}')">
                <i class="fas fa-cart-plus"></i> 加入购物车
            </button>
        </div>
    </div>
`;
        productsGrid.appendChild(productCard);
    });
}

// 添加购物车处理函数
async function addToCartHandler(productId, productIndex, productName) {
    const product = categoryProducts.find(p => p.id === productId);
    if (!product) return;

    const quantityInputs = document.querySelectorAll('.quantity-input');
    const quantityInput = quantityInputs[productIndex];
    const quantity = parseInt(quantityInput?.value) || 1;

    if (quantity > product.stock) {
        alert('库存不足！');
        return;
    }

    // 调用全局购物车函数
    await window.addToCartGlobal(productId, quantity, productName);
}