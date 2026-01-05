class ShoppingCartManager {
    constructor() {
        this.API_BASE = '/shopping_web_war_exploded/api/cart';
        this.cartItems = [];
        this.cartCount = 0;
        this.initialized = false;
    }

    // 初始化购物车
    async init() {
        if (this.initialized) return;

        console.log('初始化购物车管理器...');
        try {
            await this.loadCart();
            this.initialized = true;
            console.log('购物车管理器初始化完成');
        } catch (error) {
            console.error('购物车管理器初始化失败:', error);
        }
    }

    // 从服务器加载购物车
    async loadCart() {
        try {
            const response = await fetch(this.API_BASE, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error(`HTTP错误! 状态码: ${response.status}`);
            }

            const result = await response.json();
            this.cartItems = result.items || [];
            this.cartCount = result.totalItems || 0;

            console.log('购物车加载成功，商品数量:', this.cartCount);
            return this.cartItems;

        } catch (error) {
            console.error('加载购物车失败:', error);
            this.cartItems = [];
            this.cartCount = 0;
            return [];
        }
    }

    // 添加到购物车
    async addToCart(productId, quantity = 1, productName = '商品') {
        try {
            const response = await fetch(this.API_BASE, {
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

            // 重新加载购物车数据
            await this.loadCart();
            this.updateCartUI();

            return { success: true, message: `${productName} 已添加到购物车` };

        } catch (error) {
            console.error('添加到购物车失败:', error);
            return { success: false, message: error.message };
        }
    }

    // 删除购物车商品
    async removeFromCart(productId) {
        try {
            const response = await fetch(`${this.API_BASE}/${productId}`, {
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
            this.updateCartUI();

            return { success: true, message: '商品已移除' };

        } catch (error) {
            console.error('删除商品失败:', error);
            return { success: false, message: error.message };
        }
    }

    // 更新商品数量
    async updateQuantity(productId, quantity) {
        try {
            const response = await fetch(`${this.API_BASE}/${productId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include',
                body: JSON.stringify({
                    quantity: quantity
                })
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.error || '更新数量失败');
            }

            await this.loadCart();
            this.updateCartUI();

            return { success: true, message: '数量已更新' };

        } catch (error) {
            console.error('更新数量失败:', error);
            return { success: false, message: error.message };
        }
    }

    // 获取购物车总数
    getCartCount() {
        return this.cartCount;
    }

    // 获取购物车商品
    getCartItems() {
        return this.cartItems;
    }

    // 更新所有页面的购物车UI
    updateCartUI() {
        // 更新购物车角标
        const cartCountElements = document.querySelectorAll('.cart-count');
        cartCountElements.forEach(element => {
            element.textContent = this.cartCount;
        });

        // 如果当前是购物车页面，重新渲染购物车
        if (document.getElementById('cart-items-container')) {
            this.renderCartPage();
        }
    }

    // 渲染购物车页面（如果有的话）
    renderCartPage() {
        const container = document.getElementById('cart-items-container');
        if (!container) return;

        // 这里可以添加购物车页面的渲染逻辑
        // 或者调用已有的购物车页面渲染函数
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
}

// 创建全局购物车实例
window.shoppingCart = new ShoppingCartManager();

// 全局初始化函数
window.initShoppingCart = async function() {
    await window.shoppingCart.init();
};

// 全局添加购物车函数
window.addToCartGlobal = async function(productId, quantity = 1, productName) {
    const result = await window.shoppingCart.addToCart(productId, quantity, productName);
    if (result.success) {
        window.shoppingCart.showNotification(result.message, 'success');
    } else {
        window.shoppingCart.showNotification(result.message, 'error');
    }
    return result;
};

// 页面加载时自动初始化
document.addEventListener('DOMContentLoaded', function() {
    if (window.shoppingCart) {
        window.initShoppingCart();
    }
});

// 添加CSS动画
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from { transform: translateX(100%); opacity: 0; }
        to { transform: translateX(0); opacity: 1; }
    }
`;
document.head.appendChild(style);
