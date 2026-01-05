// 购物车功能
class ShoppingCart {
    constructor() {
        this.cartItems = [];
        // 使用绝对路径（从根目录开始）
        this.baseUrl = '/shopping_web_war_exploded/api/cart';
        console.log('购物车API路径:', this.baseUrl);
        this.init();
    }

    init() {
        console.log('初始化购物车...');
        this.loadCart();
        this.bindEvents();
    }

    // 从数据库加载购物车数据
    async loadCart() {
        console.log('正在请求API:', this.baseUrl);

        try {
            const response = await fetch(this.baseUrl, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include' // 重要：包含cookie
            });

            console.log('API响应状态:', response.status, response.statusText);

            if (response.status === 404) {
                console.error('API路径不存在，请检查：');
                console.log('1. 后端Servlet是否正确部署？');
                console.log('2. Servlet URL映射是否为 /api/cart/*？');
                console.log('3. 项目是否部署在 /shopping_web_war_exploded 路径下？');

                // 显示用户友好的错误信息
                this.showErrorMessage('购物车服务暂时不可用，请稍后重试');
                return;
            }

            if (!response.ok) {
                throw new Error(`HTTP错误! 状态码: ${response.status}`);
            }

            const result = await response.json();
            console.log('API返回数据:', result);

            this.cartItems = result.items || [];
            console.log('加载到购物车商品数量:', this.cartItems.length);

            this.renderCart();

        } catch (error) {
            console.error('加载购物车失败:', error);
            this.showErrorMessage('无法连接到购物车服务: ' + error.message);
        }
    }

    // 渲染购物车
    renderCart() {
        const container = document.getElementById('cart-items-container');
        const emptyCart = document.getElementById('empty-cart');
        const cartActions = document.getElementById('cart-actions');
        const itemsCount = document.getElementById('cart-items-count');

        if (!container) {
            console.error('找不到购物车容器 #cart-items-container');
            return;
        }

        // 更新商品数量显示
        if (itemsCount) {
            itemsCount.textContent = this.getTotalItems();
        }

        if (this.cartItems.length === 0) {
            container.innerHTML = '';
            if (emptyCart) emptyCart.style.display = 'block';
            if (cartActions) cartActions.style.display = 'none';
            this.updateSummary();
            return;
        }

        if (emptyCart) emptyCart.style.display = 'none';
        if (cartActions) cartActions.style.display = 'flex';

        container.innerHTML = this.cartItems.map((item, index) => {
            // 处理商品图片
            let imageHtml = '';
            if (item.imageUrl && item.imageUrl !== '商品图片') {
                // 如果图片路径是相对路径，转换为绝对路径
                let imageUrl = item.imageUrl;
                if (!imageUrl.startsWith('http') && !imageUrl.startsWith('/')) {
                    imageUrl = '/shopping_web_war_exploded/' + imageUrl;
                }

                imageHtml = `
                    <img src="${imageUrl}" 
                         alt="${item.name}"
                         style="width: 100px; height: 100px; object-fit: cover; border-radius: 8px;"
                         onerror="this.onerror=null; this.src='https://via.placeholder.com/100x100?text=${encodeURIComponent(item.name.substring(0, 4))}';">
                `;
            } else {
                // 占位图
                const placeholderText = item.name ? item.name.substring(0, 4) : '商品';
                imageHtml = `
                    <div style="width: 100px; height: 100px; background: linear-gradient(45deg, #4facfe, #00f2fe); 
                                border-radius: 8px; display: flex; align-items: center; justify-content: center; color: white;">
                        ${placeholderText}
                    </div>
                `;
            }

            return `
            <div class="cart-item" data-product-id="${item.productId}">
                <input type="checkbox" class="cart-item-checkbox" checked onchange="cart.updateSummary()">
                <div class="cart-item-image">
                    ${imageHtml}
                </div>
                <div class="cart-item-details">
                    <h3 class="cart-item-name">${item.name}</h3>
                    <p class="cart-item-description">商品编号: ${item.productId}</p>
                    <div class="cart-item-price">¥${item.price ? item.price.toFixed(2) : '0.00'}</div>
                </div>
                <div class="cart-item-controls">
                    <div class="quantity-controls">
                        <button class="quantity-btn" onclick="cart.updateQuantity(${item.productId}, ${item.quantity - 1})">-</button>
                        <input type="number" class="quantity-input" value="${item.quantity}" min="1" max="99" 
                               onchange="cart.updateQuantity(${item.productId}, parseInt(this.value))">
                        <button class="quantity-btn" onclick="cart.updateQuantity(${item.productId}, ${item.quantity + 1})">+</button>
                    </div>
                    <div class="cart-item-total">¥${((item.price || 0) * item.quantity).toFixed(2)}</div>
                    <button class="remove-btn" onclick="cart.removeItem(${item.productId})" title="删除商品">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </div>
            `;
        }).join('');

        this.updateSummary();
        this.updateCartCount();
    }

    // 显示错误信息
    showErrorMessage(message) {
        const container = document.getElementById('cart-items-container');
        if (container) {
            container.innerHTML = `
                <div style="text-align: center; padding: 50px; color: #666;">
                    <div style="font-size: 48px; color: #ff6b6b; margin-bottom: 20px;">
                        <i class="fas fa-exclamation-circle"></i>
                    </div>
                    <h3 style="color: #333; margin-bottom: 10px;">购物车加载失败</h3>
                    <p style="margin-bottom: 20px;">${message}</p>
                    <button onclick="cart.loadCart()" 
                            style="padding: 10px 20px; background: #4CAF50; color: white; 
                                   border: none; border-radius: 4px; cursor: pointer;">
                        <i class="fas fa-redo"></i> 重新加载
                    </button>
                    <p style="margin-top: 20px; font-size: 12px; color: #999;">
                        如果问题持续存在，请联系客服
                    </p>
                </div>
            `;
        }
    }

    // 添加到购物车
    async addToCart(productId, quantity = 1, productName = '商品') {
        try {
            const response = await fetch(this.baseUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include',
                body: JSON.stringify({
                    productId: productId,
                    quantity: quantity
                })
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.error || '添加到购物车失败');
            }

            const result = await response.json();
            await this.loadCart(); // 重新加载购物车

            // 显示成功提示
            this.showNotification(`${productName} 已添加到购物车`, 'success');
            return result;

        } catch (error) {
            console.error('添加到购物车失败:', error);
            this.showNotification(error.message || '添加到购物车失败', 'error');
            throw error;
        }
    }

    // 更新商品数量
    async updateQuantity(productId, newQuantity) {
        if (newQuantity < 1) newQuantity = 1;
        if (newQuantity > 99) newQuantity = 99;

        try {
            const response = await fetch(`${this.baseUrl}/${productId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include',
                body: JSON.stringify({
                    quantity: newQuantity
                })
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.error || '更新数量失败');
            }

            await this.loadCart();

        } catch (error) {
            console.error('更新数量失败:', error);
            this.showNotification(error.message || '更新数量失败', 'error');
        }
    }

    // 删除商品
    async removeItem(productId) {
        if (!confirm('确定要删除这个商品吗？')) return;

        try {
            const response = await fetch(`${this.baseUrl}/${productId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include'
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.error || '删除商品失败');
            }

            await this.loadCart();
            this.showNotification('商品已从购物车移除', 'success');

        } catch (error) {
            console.error('删除商品失败:', error);
            this.showNotification(error.message || '删除商品失败', 'error');
        }
    }

    // 显示通知
    showNotification(message, type = 'success') {
        const notification = document.createElement('div');
        notification.className = 'cart-notification';
        notification.innerHTML = `
            <span>${message}</span>
            <button onclick="this.parentElement.remove()">&times;</button>
        `;

        notification.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 12px 20px;
            background: ${type === 'success' ? '#4CAF50' : '#f44336'};
            color: white;
            border-radius: 4px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.2);
            z-index: 1000;
            display: flex;
            align-items: center;
            justify-content: space-between;
            min-width: 300px;
            animation: slideIn 0.3s ease;
        `;

        document.body.appendChild(notification);

        setTimeout(() => {
            if (notification.parentNode) {
                notification.remove();
            }
        }, 3000);
    }

    // 其他方法保持不变...
    getTotalItems() {
        return this.cartItems.reduce((sum, item) => sum + item.quantity, 0);
    }

    updateSummary() {
        const subtotalElement = document.getElementById('subtotal-price');
        const totalElement = document.getElementById('total-price');
        const checkoutBtn = document.getElementById('checkout-btn');

        if (!subtotalElement || !totalElement) return;

        const selectedItems = this.getSelectedItems();
        const subtotal = selectedItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);

        subtotalElement.textContent = `¥${subtotal.toFixed(2)}`;
        totalElement.textContent = `¥${subtotal.toFixed(2)}`;

        if (checkoutBtn) {
            checkoutBtn.disabled = selectedItems.length === 0;
            checkoutBtn.innerHTML = selectedItems.length > 0 ?
                `<i class="fas fa-credit-card"></i> 立即结算 (${selectedItems.length})` :
                `<i class="fas fa-credit-card"></i> 立即结算`;
        }
    }

    getSelectedItems() {
        const checkboxes = document.querySelectorAll('.cart-item-checkbox');
        return this.cartItems.filter((item, index) => {
            const checkbox = checkboxes[index];
            return checkbox ? checkbox.checked : true;
        });
    }

    updateCartCount() {
        const totalItems = this.getTotalItems();
        document.querySelectorAll('.cart-count').forEach(element => {
            element.textContent = totalItems;
        });
    }

    bindEvents() {
        const selectAll = document.getElementById('selectAll');
        if (selectAll) {
            selectAll.addEventListener('change', (e) => {
                const checkboxes = document.querySelectorAll('.cart-item-checkbox');
                checkboxes.forEach(checkbox => {
                    checkbox.checked = e.target.checked;
                });
                this.updateSummary();
            });
        }
    }
}

// 初始化购物车
let cart;

document.addEventListener('DOMContentLoaded', function() {
    console.log('页面加载完成，初始化购物车...');
    cart = new ShoppingCart();

    // 添加CSS动画
    const style = document.createElement('style');
    style.textContent = `
        @keyframes slideIn {
            from { transform: translateX(100%); opacity: 0; }
            to { transform: translateX(0); opacity: 1; }
        }
    `;
    document.head.appendChild(style);
});

// 全局函数
function checkout() {
    if (cart) cart.checkout();
}

function moveToFavorites() {
    if (cart) cart.moveToFavorites();
}

function deleteSelected() {
    if (cart) cart.deleteSelected();
}

function clearCart() {
    if (cart) cart.clearCart();
}

// 快速添加到购物车
function quickAddToCart(productId, productName) {
    if (cart) {
        cart.addToCart(productId, 1, productName);
    } else {
        alert('购物车系统未初始化，请刷新页面');
    }
}