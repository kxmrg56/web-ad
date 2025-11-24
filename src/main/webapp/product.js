// 所有商品数据（从各个分类汇总）
const allProducts = [
    // 手机数码
    { id: 1, name: 'iPhone 15 Pro', description: 'A17 Pro芯片，钛金属设计', price: 7999, originalPrice: 8999, stock: 50, category: 'electronics' },
    { id: 2, name: '华为 Mate 60 Pro', description: '麒麟9000S，卫星通话', price: 6999, originalPrice: 7999, stock: 30, category: 'electronics' },
    { id: 3, name: '小米 14 Ultra', description: '骁龙8 Gen 3，徕卡镜头', price: 5999, originalPrice: 6499, stock: 45, category: 'electronics' },

    // 电脑办公
    { id: 4, name: 'MacBook Pro 16寸', description: 'M3 Pro芯片，32GB内存', price: 18999, originalPrice: 20999, stock: 20, category: 'computer' },
    { id: 5, name: '联想 ThinkPad X1', description: '13代i7，商务办公', price: 8999, originalPrice: 9999, stock: 35, category: 'computer' },

    // 家用电器
    { id: 6, name: '索尼 85寸电视', description: '4K HDR，智能安卓', price: 12999, originalPrice: 14999, stock: 15, category: 'appliances' },
    { id: 7, name: '海尔 冰箱', description: '对开门，变频节能', price: 5999, originalPrice: 6999, stock: 25, category: 'appliances' },

    // 生活家具
    { id: 8, name: '真皮沙发组合', description: '头层牛皮，舒适坐感', price: 8999, originalPrice: 11999, stock: 15, category: 'furniture' },

    // 运动专区
    { id: 9, name: '专业跑步机', description: '静音电机，多功能', price: 2999, originalPrice: 3999, stock: 20, category: 'sports' },

    // 美妆护理
    { id: 10, name: '护肤精华套装', description: '补水保湿，改善肤质', price: 599, originalPrice: 799, stock: 100, category: 'beauty' },

    // 居家厨具
    { id: 11, name: '不粘锅套装', description: '德国工艺，健康涂层', price: 399, originalPrice: 599, stock: 80, category: 'kitchen' },

    // 日用文创
    { id: 12, name: '创意文具礼盒', description: '精美设计，实用收纳', price: 199, originalPrice: 299, stock: 150, category: 'daily' },

    // 服装精选
    { id: 13, name: '男士休闲西装', description: '修身剪裁，商务休闲', price: 899, originalPrice: 1299, stock: 60, category: 'clothing' },
    { id: 14, name: '女士连衣裙', description: '时尚设计，舒适面料', price: 599, originalPrice: 899, stock: 75, category: 'clothing' }
];

// 分页配置
const productsPerPage = 12;
let currentPage = 1;

// 随机打乱商品顺序
function shuffleProducts() {
    for (let i = allProducts.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [allProducts[i], allProducts[j]] = [allProducts[j], allProducts[i]];
    }
}

// 页面加载时初始化
document.addEventListener('DOMContentLoaded', function() {
    // 随机打乱商品顺序
    shuffleProducts();

    renderProducts();
    updatePagination();
    updateCartCount();
});

// 渲染商品列表
function renderProducts() {
    const productsGrid = document.getElementById('products-grid');
    const totalProductsElement = document.getElementById('total-products');

    if (!productsGrid) {
        console.error('找不到 products-grid 元素');
        return;
    }

    // 计算分页
    const startIndex = (currentPage - 1) * productsPerPage;
    const endIndex = startIndex + productsPerPage;
    const productsToShow = allProducts.slice(startIndex, endIndex);

    productsGrid.innerHTML = '';

    if (totalProductsElement) {
        totalProductsElement.textContent = allProducts.length;
    }

    if (productsToShow.length === 0) {
        productsGrid.innerHTML = '<div class="no-products">暂无商品</div>';
        return;
    }

    productsToShow.forEach((product, index) => {
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

// 更新分页
function updatePagination() {
    const pagination = document.getElementById('pagination');
    if (!pagination) return;

    const totalPages = Math.ceil(allProducts.length / productsPerPage);

    let paginationHTML = '';

    // 上一页
    if (currentPage > 1) {
        paginationHTML += `<a href="#" class="page-btn" onclick="changePage(${currentPage - 1})"><i class="fas fa-chevron-left"></i></a>`;
    }

    // 页码
    for (let i = 1; i <= totalPages; i++) {
        if (i === currentPage) {
            paginationHTML += `<a href="#" class="page-btn active">${i}</a>`;
        } else {
            paginationHTML += `<a href="#" class="page-btn" onclick="changePage(${i})">${i}</a>`;
        }
    }

    // 下一页
    if (currentPage < totalPages) {
        paginationHTML += `<a href="#" class="page-btn" onclick="changePage(${currentPage + 1})"><i class="fas fa-chevron-right"></i></a>`;
    }

    pagination.innerHTML = paginationHTML;
}

// 切换页码
function changePage(page) {
    currentPage = page;
    renderProducts();
    updatePagination();
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

// 添加到购物车
function addToCart(productId, productIndex) {
    const product = allProducts.find(p => p.id === productId);
    if (!product) return;

    // 通过索引找到对应的数量输入框
    const quantityInputs = document.querySelectorAll('.quantity-input');
    const quantityInput = quantityInputs[productIndex];
    const quantity = parseInt(quantityInput?.value) || 1;

    if (quantity > product.stock) {
        alert('库存不足！');
        return;
    }

    let cart = JSON.parse(localStorage.getItem('shoppingCart') || '[]');
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

    localStorage.setItem('shoppingCart', JSON.stringify(cart));
    updateCartCount();
    alert(`已添加 ${quantity} 件 ${product.name} 到购物车！`);
}

// 更新购物车数量
function updateCartCount() {
    const cart = JSON.parse(localStorage.getItem('shoppingCart') || '[]');
    const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);

    document.querySelectorAll('.cart-count').forEach(element => {
        element.textContent = totalItems;
    });
}