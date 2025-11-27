// 工具函数
const Utils = {
    // 格式化日期
    formatDate: function(date) {
        if (!date) return '';
        const d = new Date(date);
        return d.toLocaleDateString('zh-CN') + ' ' + d.toLocaleTimeString('zh-CN', {
            hour: '2-digit',
            minute: '2-digit'
        });
    },

    // 防抖函数
    debounce: function(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    },

    // 获取URL参数
    getUrlParameter: function(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    },

    // 设置页面标题
    setPageTitle: function(title) {
        document.title = title + ' - 新闻快讯';
    },

    // 显示加载提示
    showLoading: function() {
        // 可以在这里实现加载动画
        console.log('加载中...');
    },

    // 隐藏加载提示
    hideLoading: function() {
        // 隐藏加载动画
        console.log('加载完成');
    }
};

// 为新闻卡片添加悬停效果
document.addEventListener('DOMContentLoaded', function() {
    const newsCards = document.querySelectorAll('.news-card, .news-item');

    newsCards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px)';
            this.style.boxShadow = '0 5px 15px rgba(0,0,0,0.2)';
        });

        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
            this.style.boxShadow = '0 2px 8px rgba(0,0,0,0.1)';
        });
    });
});