// 购物车功能
class ShoppingCart {
    constructor() {
        this.cartItems = [];
        this.init();
    }

    init() {
        this.loadCart();
        this.renderCart();
        this.bindEvents();
    }

    // 加载购物车数据
    loadCart() {
        const cartData = localStorage.getItem('shoppingCart');
        this.cartItems = cartData ? JSON.parse(cartData) : [];
    }

    // 保存购物车数据
    saveCart() {
        localStorage.setItem('shoppingCart', JSON.stringify(this.cartItems));
        this.updateCartCount();
    }

    // 渲染购物车
    renderCart() {
        const container = document.getElementById('cart-items-container');
        const emptyCart = document.getElementById('empty-cart');
        const cartActions = document.getElementById('cart-actions');
        const itemsCount = document.getElementById('cart-items-count');

        if (!container) return;

        // 更新商品数量
        if (itemsCount) {
            itemsCount.textContent = this.getTotalItems();
        }

        if (this.cartItems.length === 0) {
            container.innerHTML = '';
            emptyCart.style.display = 'block';
            cartActions.style.display = 'none';
            this.updateSummary();
            return;
        }

        emptyCart.style.display = 'none';
        cartActions.style.display = 'flex';

        container.innerHTML = this.cartItems.map((item, index) => `
            <div class="cart-item" data-product-id="${item.id}">
                <input type="checkbox" class="cart-item-checkbox" checked onchange="cart.updateSummary()">
                <div class="cart-item-image">
                    <div style="width: 100px; height: 100px; background: linear-gradient(45deg, #4facfe, #00f2fe); border-radius: 8px; display: flex; align-items: center; justify-content: center; color: white;">
                        商品图片
                    </div>
                </div>
                <div class="cart-item-details">
                    <h3 class="cart-item-name">${item.name}</h3>
                    <p class="cart-item-description">商品编号: ${item.id}</p>
                    <div class="cart-item-price">¥${item.price}</div>
                </div>
                <div class="cart-item-controls">
                    <div class="quantity-controls">
                        <button class="quantity-btn" onclick="cart.updateQuantity(${index}, ${item.quantity - 1})">-</button>
                        <input type="number" class="quantity-input" value="${item.quantity}" min="1" max="99" 
                               onchange="cart.updateQuantity(${index}, parseInt(this.value))">
                        <button class="quantity-btn" onclick="cart.updateQuantity(${index}, ${item.quantity + 1})">+</button>
                    </div>
                    <div class="cart-item-total">¥${(item.price * item.quantity).toFixed(2)}</div>
                    <button class="remove-btn" onclick="cart.removeItem(${index})" title="删除商品">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </div>
        `).join('');

        this.updateSummary();
    }

    // 更新商品数量
    updateQuantity(index, newQuantity) {
        if (newQuantity < 1) newQuantity = 1;
        if (newQuantity > 99) newQuantity = 99;

        this.cartItems[index].quantity = newQuantity;
        this.saveCart();
        this.renderCart();
    }

    // 删除商品
    removeItem(index) {
        if (confirm('确定要删除这个商品吗？')) {
            this.cartItems.splice(index, 1);
            this.saveCart();
            this.renderCart();
        }
    }

    // 删除选中的商品
    deleteSelected() {
        const checkboxes = document.querySelectorAll('.cart-item-checkbox:checked');
        if (checkboxes.length === 0) {
            alert('请先选择要删除的商品');
            return;
        }

        if (confirm(`确定要删除选中的 ${checkboxes.length} 件商品吗？`)) {
            const selectedIds = Array.from(checkboxes).map(checkbox =>
                parseInt(checkbox.closest('.cart-item').dataset.productId)
            );

            this.cartItems = this.cartItems.filter(item =>
                !selectedIds.includes(item.id)
            );

            this.saveCart();
            this.renderCart();
        }
    }

    // 移入收藏
    moveToFavorites() {
        alert('功能开发中...');
    }

    // 更新订单汇总
    updateSummary() {
        const subtotalElement = document.getElementById('subtotal-price');
        const totalElement = document.getElementById('total-price');
        const checkoutBtn = document.getElementById('checkout-btn');

        if (!subtotalElement || !totalElement) return;

        const selectedItems = this.getSelectedItems();
        const subtotal = selectedItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);

        subtotalElement.textContent = `¥${subtotal.toFixed(2)}`;
        totalElement.textContent = `¥${subtotal.toFixed(2)}`;

        // 更新结算按钮状态
        if (checkoutBtn) {
            checkoutBtn.disabled = selectedItems.length === 0;
            checkoutBtn.textContent = selectedItems.length > 0 ?
                `<i class="fas fa-credit-card"></i> 立即结算 (${selectedItems.length})` :
                `<i class="fas fa-credit-card"></i> 立即结算`;
        }
    }

    // 获取选中的商品
    getSelectedItems() {
        const checkboxes = document.querySelectorAll('.cart-item-checkbox');
        return this.cartItems.filter((item, index) => {
            const checkbox = checkboxes[index];
            return checkbox ? checkbox.checked : true;
        });
    }

    // 获取商品总数量
    getTotalItems() {
        return this.cartItems.reduce((sum, item) => sum + item.quantity, 0);
    }

    // 更新购物车数量显示
    updateCartCount() {
        const totalItems = this.getTotalItems();
        document.querySelectorAll('.cart-count').forEach(element => {
            element.textContent = totalItems;
        });
    }

    // 绑定事件
    bindEvents() {
        // 全选功能
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

    // 应用优惠券
    applyCoupon() {
        const couponInput = document.getElementById('coupon-code');
        const discountElement = document.getElementById('discount-amount');

        if (!couponInput || !discountElement) return;

        const couponCode = couponInput.value.trim();
        if (!couponCode) {
            alert('请输入优惠码');
            return;
        }

        // 模拟优惠券验证
        let discount = 0;
        if (couponCode === 'SAVE10') {
            discount = 10;
        } else if (couponCode === 'SAVE20') {
            discount = 20;
        } else {
            alert('优惠码无效');
            return;
        }

        discountElement.textContent = `-¥${discount}.00`;
        this.updateSummary();
        alert(`优惠券应用成功！减免 ¥${discount}.00`);
    }

    // 结算
    checkout() {
        const selectedItems = this.getSelectedItems();
        if (selectedItems.length === 0) {
            alert('请先选择要结算的商品');
            return;
        }

        const totalAmount = selectedItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);
        if (confirm(`确定要结算 ${selectedItems.length} 件商品，总金额 ¥${totalAmount.toFixed(2)} 吗？`)) {
            alert('订单提交成功！跳转支付页面...');
            // 这里可以添加实际的支付跳转逻辑
        }
    }
}

// 初始化购物车
let cart;

document.addEventListener('DOMContentLoaded', function() {
    cart = new ShoppingCart();
});

// 全局函数供HTML调用
function applyCoupon() {
    cart.applyCoupon();
}

function checkout() {
    cart.checkout();
}

function moveToFavorites() {
    cart.moveToFavorites();
}

function deleteSelected() {
    cart.deleteSelected();
}