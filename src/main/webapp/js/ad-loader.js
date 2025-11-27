// 广告加载逻辑（更新版）
class AdLoader {
    constructor() {
        this.userContext = this.getUserContext();
        this.init();
    }

    // 从页面获取用户上下文信息
    getUserContext() {
        const context = {
            anonymousId: this.getAnonymousId(),
            pageCategory: this.getPageCategory(),
            userRegion: this.getUserRegion(),
            interests: this.getUserInterests()
        };

        // 从隐藏的用户上下文信息中获取更多数据
        const contextInfo = document.querySelector('.user-context-info');
        if (contextInfo) {
            // 可以解析更多的用户上下文信息
        }

        return context;
    }

    // 获取或生成匿名用户ID
    getAnonymousId() {
        let anonymousId = this.getCookie('anonymousId');
        if (!anonymousId) {
            anonymousId = 'user_' + Math.random().toString(36).substr(2, 9);
            this.setCookie('anonymousId', anonymousId, 365);
        }
        return anonymousId;
    }

    // 设置Cookie
    setCookie(name, value, days) {
        const expires = new Date();
        expires.setTime(expires.getTime() + days * 24 * 60 * 60 * 1000);
        document.cookie = name + '=' + value + ';expires=' + expires.toUTCString() + ';path=/';
    }

    // 获取Cookie
    getCookie(name) {
        const nameEQ = name + "=";
        const ca = document.cookie.split(';');
        for(let i=0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) === ' ') c = c.substring(1, c.length);
            if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
        }
        return null;
    }

    // 获取页面分类
    getPageCategory() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get('category') || 'home';
    }

    // 获取用户区域（简化版）
    getUserRegion() {
        // 实际可以从IP地址或用户配置获取
        return 'unknown';
    }

    // 获取用户兴趣（简化版）
    getUserInterests() {
        const interests = {};
        // 从Cookie中读取兴趣信息
        const cookies = document.cookie.split(';');
        cookies.forEach(cookie => {
            const [name, value] = cookie.trim().split('=');
            if (name.startsWith('interest_')) {
                const category = name.substring(9);
                interests[category] = value;
            }
        });
        return interests;
    }

    // 模拟广告加载
    async loadAd(container, position) {
        const adContext = {
            ...this.userContext,
            position: position,
            timestamp: new Date().toISOString(),
            pageUrl: window.location.href,
            referrer: document.referrer
        };

        try {
            // 显示加载状态
            this.showLoading(container);

            // 模拟API调用延迟
            await new Promise(resolve => setTimeout(resolve, 800));

            // 模拟广告内容（基于用户上下文）
            const adContent = this.generateContextualAd(adContext);
            this.renderAd(container, adContent);
            this.recordImpression(position, adContext);

        } catch (error) {
            console.error('广告加载失败:', error);
            this.renderAd(container, '<div class="ad-error"><p>广告加载失败，请刷新页面重试</p></div>');
        }
    }

    // 基于上下文生成广告内容
    generateContextualAd(context) {
        const category = context.pageCategory;
        const interests = Object.keys(context.interests);

        const contextualAds = {
            politics: '<div class="mock-ad political"><h4>政策解读</h4><p>了解最新政策动向</p><button class="ad-btn">查看详情</button></div>',
            tech: '<div class="mock-ad tech"><h4>科技产品</h4><p>最新科技产品推荐</p><button class="ad-btn">立即了解</button></div>',
            sports: '<div class="mock-ad sports"><h4>运动装备</h4><p>专业运动装备优惠</p><button class="ad-btn">选购装备</button></div>',
            economy: '<div class="mock-ad economy"><h4>投资理财</h4><p>专业理财建议</p><button class="ad-btn">了解更多</button></div>',
            entertainment: '<div class="mock-ad entertainment"><h4>影视娱乐</h4><p>热门影视推荐</p><button class="ad-btn">观看预告</button></div>',
            military: '<div class="mock-ad military"><h4>国防知识</h4><p>了解国防动态</p><button class="ad-btn">阅读更多</button></div>',
            default: '<div class="mock-ad default"><h4>精选推荐</h4><p>为您推荐优质内容</p><button class="ad-btn">探索发现</button></div>'
        };

        return contextualAds[category] || contextualAds.default;
    }

    // 显示加载状态
    showLoading(container) {
        const loadingElement = container.querySelector('.ad-loading');
        const contentElement = container.querySelector('.ad-content');

        if (loadingElement) loadingElement.style.display = 'block';
        if (contentElement) contentElement.style.display = 'none';
    }

    // 渲染广告内容
    renderAd(container, content) {
        const loadingElement = container.querySelector('.ad-loading');
        const contentElement = container.querySelector('.ad-content');

        if (loadingElement) loadingElement.style.display = 'none';
        if (contentElement) {
            contentElement.style.display = 'block';
            contentElement.innerHTML = content;
            this.bindAdEvents(contentElement);
        }
    }

    // 绑定广告事件
    bindAdEvents(container) {
        const adButtons = container.querySelectorAll('.ad-btn');
        adButtons.forEach(button => {
            button.addEventListener('click', (e) => {
                e.preventDefault();
                this.recordClick();
                this.trackUserInterest();
                alert('广告点击已记录（模拟）\n用户兴趣已更新');
            });
        });
    }

    // 记录广告展示
    recordImpression(position, context) {
        console.log('广告展示统计:', {
            position: position,
            userContext: context,
            timestamp: new Date().toISOString()
        });

        // 模拟发送到统计服务器
        this.sendBeacon('/ad/impression', {
            adPosition: position,
            anonymousId: context.anonymousId,
            category: context.pageCategory
        });
    }

    // 记录广告点击
    recordClick() {
        console.log('广告点击统计:', {
            userContext: this.userContext,
            timestamp: new Date().toISOString()
        });

        this.sendBeacon('/ad/click', {
            anonymousId: this.userContext.anonymousId,
            pageCategory: this.userContext.pageCategory
        });
    }

    // 跟踪用户兴趣
    trackUserInterest() {
        const currentCategory = this.userContext.pageCategory;
        if (currentCategory && currentCategory !== 'home') {
            this.setCookie(`interest_${currentCategory}`, 'true', 30);
            console.log(`更新用户兴趣: ${currentCategory}`);
        }
    }

    // 发送统计信标
    sendBeacon(url, data) {
        // 模拟发送统计数据
        console.log(`发送统计到 ${url}:`, data);
        // 实际使用时可以使用 navigator.sendBeacon() 或 fetch()
    }

    // 初始化广告加载
    init() {
        const leftAdContainer = document.getElementById('ad-container-left');
        const rightAdContainer = document.getElementById('ad-container-right');

        if (leftAdContainer) {
            this.loadAd(leftAdContainer, 'left');
        }
        if (rightAdContainer) {
            this.loadAd(rightAdContainer, 'right');
        }
    }
}

// 页面加载完成后初始化广告
document.addEventListener('DOMContentLoaded', () => {
    const adLoader = new AdLoader();
});