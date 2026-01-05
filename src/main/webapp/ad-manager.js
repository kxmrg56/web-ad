// 1. 配置信息
const AD_API_BASE = 'http://10.100.164.33:8080/adproj-1.0-SNAPSHOT';
const AD_API_URL = AD_API_BASE + '/ads/api/getAd';
const STORAGE_KEY = 'global_visitor_id';

// 2. 格式校验函数：只认 UUID 格式
function isValidUUID(id) {
    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;
    return id && uuidRegex.test(id);
}

// 3. 获取稳定的访客ID（增加双重校验逻辑）
function getStableId() {
    // 优先级 1: 本地存储 (最稳定)
    let localId = localStorage.getItem(STORAGE_KEY);
    if (isValidUUID(localId)) return localId;

    // 优先级 2: Cookie 提取 (并过滤非 UUID 格式的干扰项)
    let match = document.cookie.match(new RegExp('(^| )visitor_id=([^;]*)'));
    let cookieId = match ? match[2] : "";

    if (isValidUUID(cookieId)) {
        localStorage.setItem(STORAGE_KEY, cookieId); // 顺手同步到本地
        return cookieId;
    }

    return ""; // 如果都不符合，传空让后端生成
}

// 4. 获取当前页面的分类
function getCurrentCategory() {
    const pathname = window.location.pathname;
    const categoryMap = {
        'category-electronics.html': '数码',
        'category-computer.html': '电脑办公',
        'category-appliances.html': '家用电器',
        'category-furniture.html': '家居',
        'category-sports.html': '体育',
        'category-beauty.html': '美妆',
        'category-kitchen.html': '家居',
        'category-daily.html': '日用文创',
        'category-clothing.html': '时尚',
        'product.html': '数码',
        'index.html': '数码',
        'cart.html': '数码'
    };
    const filename = pathname.substring(pathname.lastIndexOf('/') + 1);
    return categoryMap[filename] || '数码';
}

// 5. 加载广告函数
async function loadAd(adContainerId) {
    try {
        const vid = getStableId();
        const channel = getCurrentCategory();

        console.log(`[ShopAd] 请求身份: ${vid || "新用户"} | 分类: ${channel}`);

        const response = await fetch(
            `${AD_API_URL}?siteType=shop&channel=${encodeURIComponent(channel)}&visitorId=${encodeURIComponent(vid)}`,
            {
                method: 'GET',
                credentials: 'include' // 允许跨域携带/接收 Cookie
            }
        );

        if (!response.ok) throw new Error(`API响应错误: ${response.status}`);

        const adData = await response.json();

        // 【关键】将后端确定或新生成的 ID 永久保存到本地
        if (adData && adData.visitorId && isValidUUID(adData.visitorId)) {
            localStorage.setItem(STORAGE_KEY, adData.visitorId);
        }

        const adContainer = document.getElementById(adContainerId);
        if (!adContainer) return;

        if (adData && adData.image) {
            const imgPath = adData.image.startsWith('/') ? adData.image : '/' + adData.image;
            const fullImageUrl = AD_API_BASE + imgPath;

            adContainer.innerHTML = `
                <div class="ad-container" style="border: 1px solid #ddd; margin-bottom: 15px; padding: 10px;">
                    <a href="${adData.link || '#'}" target="_blank" class="ad-link" style="text-decoration: none; color: #333;">
                        <img src="${fullImageUrl}" 
                             alt="${adData.title}" 
                             style="width: 100%; height: auto; display: block; margin-bottom: 8px;"
                             onerror="this.src='https://via.placeholder.com/250x150?text=图片加载失败'">
                        <div class="ad-content">
                            <div class="ad-title" style="font-weight: bold; font-size: 14px;">${adData.title}</div>
                            ${adData.description ? `<div class="ad-desc" style="font-size: 12px; color: #666;">${adData.description}</div>` : ''}
                        </div>
                    </a>
                </div>
            `;
        }
    } catch (error) {
        console.error(`[ShopAd] 加载失败:`, error);
    }
}

// 6. 初始化与自动刷新
document.addEventListener('DOMContentLoaded', function() {
    const containers = ['left-ad', 'right-ad'];

    containers.forEach(id => {
        if (document.getElementById(id)) loadAd(id);
    });

    // 每30秒刷新一次广告，维持兴趣实时更新
    setInterval(() => {
        containers.forEach(id => {
            if (document.getElementById(id)) loadAd(id);
        });
    }, 30000);
});