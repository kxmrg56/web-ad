
// 所有商品数据 - 从数据库加载
let allProducts = [];
const productsPerPage = 12;
let currentPage = 1;

// 所有分类列表
const categories = ['electronics', 'computer', 'appliances', 'furniture',
    'sports', 'beauty', 'kitchen', 'daily', 'clothing'];

// 页面加载时从数据库加载所有商品
document.addEventListener('DOMContentLoaded', async function() {
    // 先确保购物车管理器已加载
    if (!window.shoppingCart) {
        console.error('购物车管理器未加载，请确保 shopping-cart-manager.js 已引入');
        return;
    }

    // 初始化购物车
    await window.initShoppingCart();

    // 加载商品数据
    await loadAllProductsByCategories();
    renderProducts();
    updatePagination();
    initSearch();
});

// 分别加载所有分类的商品
async function loadAllProductsByCategories() {
    console.log('开始加载所有分类的商品...');
    allProducts = [];

    // 使用 Promise.all 并发加载所有分类
    const promises = categories.map(category =>
        loadProductsByCategory(category)
    );

    try {
        // 等待所有分类加载完成
        const results = await Promise.all(promises);

        // 合并所有商品
        results.forEach(products => {
            if (products && products.length > 0) {
                allProducts.push(...products);
            }
        });

        console.log(`成功加载 ${allProducts.length} 个商品，来自 ${categories.length} 个分类`);

        if (allProducts.length === 0) {
            console.warn('所有分类都没有商品数据！');
            showNoDataMessage();
        }

    } catch (error) {
        console.error('加载商品失败:', error);
        showError(error.message);
    }
}

// 加载单个分类的商品
async function loadProductsByCategory(category) {
    try {
        const response = await fetch(`/shopping_web_war_exploded/api/products?category=${category}`);

        if (!response.ok) {
            console.warn(`${category} 分类加载失败: ${response.status}`);
            return [];
        }

        const data = await response.json();

        if (!data || data.length === 0) {
            console.log(`${category} 分类没有商品`);
            return [];
        }

        // 处理商品数据
        const categoryProducts = data.map(item => ({
            id: item.id,
            name: item.name,
            description: item.description || '暂无描述',
            price: parseFloat(item.price) || 0,
            originalPrice: parseFloat(item.price) * 1.15,
            stock: item.stock || 0,
            category: category,
            categoryName: getCategoryName(category),
            imageUrl: getImageUrl(item) // 处理图片URL
        }));

        console.log(`${category} 分类: ${categoryProducts.length} 个商品`);
        return categoryProducts;

    } catch (error) {
        console.error(`加载 ${category} 分类失败:`, error);
        return [];
    }
}

// 处理图片URL（支持多种字段名）
function getImageUrl(item) {
    // 尝试不同的字段名
    return item.image_url || item.imageUrl || item.image ||
        `https://via.placeholder.com/300x200/cccccc/000000?text=${encodeURIComponent(item.name.substring(0, 12))}`;
}

// 获取分类中文名
function getCategoryName(category) {
    const categoryMap = {
        'electronics': '手机数码',
        'computer': '电脑办公',
        'appliances': '家用电器',
        'furniture': '生活家具',
        'sports': '运动专区',
        'beauty': '美妆护理',
        'kitchen': '居家厨具',
        'daily': '日用文创',
        'clothing': '服装精选'
    };
    return categoryMap[category] || category;
}

// 显示没有数据的提示
function showNoDataMessage() {
    const productsGrid = document.getElementById('products-grid');
    if (productsGrid) {
        productsGrid.innerHTML = `
            <div style="grid-column: 1/-1; text-align: center; padding: 80px 20px;">
                <div style="font-size: 64px; color: #e0e0e0; margin-bottom: 20px;">
                    <i class="fas fa-shopping-cart"></i>
                </div>
                <h3 style="color: #666; margin-bottom: 15px; font-size: 20px;">商品库空空如也</h3>
                <p style="color: #999; margin-bottom: 25px; line-height: 1.6;">
                    数据库中没有商品数据<br>
                    请先在后台管理系统中添加商品
                </p>
                <div style="display: flex; justify-content: center; gap: 15px;">
                    <button onclick="loadAllProductsByCategories()" 
                            style="padding: 10px 25px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer;">
                        <i class="fas fa-sync-alt"></i> 重新加载
                    </button>
                    <button onclick="window.location.href='index.html'" 
                            style="padding: 10px 25px; background: #f8f9fa; color: #666; border: 1px solid #ddd; border-radius: 4px; cursor: pointer;">
                        <i class="fas fa-home"></i> 返回首页
                    </button>
                </div>
            </div>
        `;
    }
}

// 显示错误信息
function showError(message) {
    const productsGrid = document.getElementById('products-grid');
    if (productsGrid) {
        productsGrid.innerHTML = `
            <div style="grid-column: 1/-1; text-align: center; padding: 80px 20px;">
                <div style="font-size: 64px; color: #ff6b6b; margin-bottom: 20px;">
                    <i class="fas fa-exclamation-triangle"></i>
                </div>
                <h3 style="color: #ff6b6b; margin-bottom: 15px; font-size: 20px;">数据加载失败</h3>
                <p style="color: #999; margin-bottom: 15px;">${message}</p>
                <p style="color: #aaa; font-size: 14px; margin-bottom: 25px;">
                    请检查：<br>
                    1. 数据库是否正常运行<br>
                    2. 后台API服务是否启动<br>
                    3. 网络连接是否正常
                </p>
                <div style="display: flex; justify-content: center; gap: 15px;">
                    <button onclick="loadAllProductsByCategories()" 
                            style="padding: 10px 25px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer;">
                        <i class="fas fa-redo"></i> 重试
                    </button>
                    <button onclick="window.location.reload()" 
                            style="padding: 10px 25px; background: #6c757d; color: white; border: none; border-radius: 4px; cursor: pointer;">
                        <i class="fas fa-sync-alt"></i> 刷新页面
                    </button>
                </div>
            </div>
        `;
    }
}

// 初始化搜索功能
function initSearch() {
    const searchForm = document.querySelector('.search-form');
    if (searchForm) {
        searchForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const searchInput = this.querySelector('.search-input');
            const searchTerm = searchInput.value.trim();

            if (searchTerm) {
                performSearch(searchTerm);
                searchInput.value = '';
            }
        });
    }
}

// 执行搜索
function performSearch(searchTerm) {
    if (allProducts.length === 0) {
        alert('暂无商品数据，无法搜索');
        return;
    }

    const filteredProducts = allProducts.filter(product =>
        product.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        product.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
        product.categoryName.includes(searchTerm)
    );

    if (filteredProducts.length > 0) {
        const tempProducts = [...allProducts];
        const tempTotal = allProducts.length;

        allProducts = filteredProducts;
        currentPage = 1;
        renderProducts();
        updatePagination();

        // 显示搜索结果提示
        showSearchResult(searchTerm, filteredProducts.length);

        // 60秒后恢复
        setTimeout(() => {
            allProducts = tempProducts;
            currentPage = 1;
            renderProducts();
            updatePagination();
            hideSearchResult();
        }, 60*1000);
    } else {
        alert(`未找到包含"${searchTerm}"的商品`);
    }
}

// 显示搜索结果提示
function showSearchResult(keyword, count) {
    let message = document.getElementById('search-result-message');
    if (!message) {
        message = document.createElement('div');
        message.id = 'search-result-message';
        message.style.cssText = `
            position: fixed;
            top: 100px;
            left: 50%;
            transform: translateX(-50%);
            background: rgba(0, 123, 255, 0.9);
            color: white;
            padding: 10px 20px;
            border-radius: 20px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.2);
            z-index: 999;
            font-size: 14px;
        `;
        document.body.appendChild(message);
    }
    message.innerHTML = `<i class="fas fa-search"></i> 搜索 "${keyword}" 找到 ${count} 个商品 (5秒后恢复)`;
    message.style.display = 'block';
}

// 隐藏搜索结果提示
function hideSearchResult() {
    const message = document.getElementById('search-result-message');
    if (message) {
        message.style.display = 'none';
    }
}

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

    // 更新商品总数
    if (totalProductsElement) {
        totalProductsElement.textContent = allProducts.length;
    }

    // 如果没有商品
    if (allProducts.length === 0) {
        showNoDataMessage();
        return;
    }

    // 渲染商品卡片
    productsToShow.forEach((product, index) => {
        const globalIndex = startIndex + index;
        const productCard = document.createElement('div');
        productCard.className = 'product-card';
        productCard.innerHTML = `
            <div class="product-image">
                <img src="${product.imageUrl}" 
                     alt="${product.name}" 
                     class="product-img"
                     onerror="this.onerror=null; this.src='https://via.placeholder.com/300x200/cccccc/000000?text=${encodeURIComponent(product.name.substring(0, 12))}';"
                     loading="lazy">
            </div>
            <div class="product-info">
                <div class="product-category-badge">
                    ${product.categoryName}
                </div>
                <h3 class="product-name">${product.name}</h3>
                <p class="product-description">${product.description}</p>
                <div class="product-price">
                    <span class="current-price">¥${product.price.toFixed(2)}</span>
                    <span class="original-price">¥${product.originalPrice.toFixed(2)}</span>
                </div>
                <div class="product-stock">
                    <i class="fas fa-box"></i> 库存: ${product.stock}件
                </div>
                <div class="product-actions">
                    <div class="quantity-control">
                        <button class="qty-btn minus" onclick="changeQuantity(this, -1)">-</button>
                        <input type="number" class="quantity-input" value="1" min="1" max="${product.stock}" readonly>
                        <button class="qty-btn plus" onclick="changeQuantity(this, 1)">+</button>
                    </div>
                    <button class="btn btn-primary" onclick="addToCartHandler(${product.id}, ${globalIndex}, '${product.name.replace(/'/g, "\\'")}')">
                        <i class="fas fa-cart-plus"></i> 加入购物车
                    </button>
                </div>
            </div>
        `;
        productsGrid.appendChild(productCard);
    });
}

// 修改数量
function changeQuantity(button, change) {
    const input = button.parentElement.querySelector('.quantity-input');
    let value = parseInt(input.value) + change;
    const max = parseInt(input.max);
    const min = parseInt(input.min);

    if (value < min) value = min;
    if (value > max) value = max;

    input.value = value;
}

// 添加到购物车处理函数
async function addToCartHandler(productId, productIndex, productName) {
    const product = allProducts[productIndex];
    if (!product) return;

    const card = document.querySelectorAll('.product-card')[productIndex % productsPerPage];
    const quantityInput = card.querySelector('.quantity-input');
    const quantity = parseInt(quantityInput?.value) || 1;

    if (quantity > product.stock) {
        alert('库存不足！');
        return;
    }

    if (quantity <= 0) {
        alert('请选择至少1件商品');
        return;
    }

    // 调用全局购物车函数
    await window.addToCartGlobal(productId, quantity, productName);
}

// 更新分页
function updatePagination() {
    const pagination = document.getElementById('pagination');
    if (!pagination) return;

    const totalPages = Math.ceil(allProducts.length / productsPerPage);

    if (totalPages <= 1) {
        pagination.innerHTML = '';
        return;
    }

    let paginationHTML = '';

    // 上一页
    if (currentPage > 1) {
        paginationHTML += `<a href="#" class="page-btn" onclick="changePage(${currentPage - 1})"><i class="fas fa-chevron-left"></i></a>`;
    }

    // 页码（最多显示5个）
    const startPage = Math.max(1, currentPage - 2);
    const endPage = Math.min(totalPages, startPage + 4);

    if (startPage > 1) {
        paginationHTML += `<a href="#" class="page-btn" onclick="changePage(1)">1</a>`;
        if (startPage > 2) paginationHTML += `<span class="page-dots">...</span>`;
    }

    for (let i = startPage; i <= endPage; i++) {
        if (i === currentPage) {
            paginationHTML += `<a href="#" class="page-btn active">${i}</a>`;
        } else {
            paginationHTML += `<a href="#" class="page-btn" onclick="changePage(${i})">${i}</a>`;
        }
    }

    if (endPage < totalPages) {
        if (endPage < totalPages - 1) paginationHTML += `<span class="page-dots">...</span>`;
        paginationHTML += `<a href="#" class="page-btn" onclick="changePage(${totalPages})">${totalPages}</a>`;
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

// 添加动画样式
if (!document.querySelector('#cart-animation')) {
    const style = document.createElement('style');
    style.id = 'cart-animation';
    style.textContent = `
        @keyframes slideInRight {
            from { transform: translateX(100%); opacity: 0; }
            to { transform: translateX(0); opacity: 1; }
        }
        @keyframes slideOutRight {
            from { transform: translateX(0); opacity: 1; }
            to { transform: translateX(100%); opacity: 0; }
        }
        
        /* 分类标签样式 */
        .product-category-badge {
            display: inline-block;
            background: rgba(0, 123, 255, 0.1);
            color: #007bff;
            padding: 3px 10px;
            border-radius: 12px;
            font-size: 12px;
            margin-bottom: 8px;
        }
        
        /* 数量控制样式 */
        .quantity-control {
            display: flex;
            align-items: center;
            gap: 5px;
        }
        
        .qty-btn {
            width: 30px;
            height: 30px;
            border: 1px solid #ddd;
            background: white;
            border-radius: 4px;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 16px;
        }
        
        .qty-btn:hover {
            background: #f8f9fa;
        }
        
        .quantity-input {
            width: 50px;
            height: 30px;
            text-align: center;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        
        .product-actions {
            display: flex;
            gap: 10px;
            align-items: center;
        }
        
        .product-stock {
            color: #28a745;
            font-size: 13px;
            margin-bottom: 10px;
            display: flex;
            align-items: center;
            gap: 5px;
        }
    `;
    document.head.appendChild(style);
}
