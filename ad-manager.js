// 广告API地址
const AD_API_BASE = 'http://10.100.164.33:8080/adproj-1.0-SNAPSHOT';
const AD_API_URL = AD_API_BASE + '/ads/api/getAd';

// 获取稳定的访客ID
function getStableId() {
    // 从本地 Cookie 中提取名为 visitor_id 的值
    let match = document.cookie.match(new RegExp('(^| )visitor_id=([^;]*)'));
    return match ? match[2] : "";
}

// 获取当前页面的分类（根据URL判断）
function getCurrentCategory() {
    const pathname = window.location.pathname;

    // 购物网站分类映射表
    const categoryMap = {
        'category-electronics.html': '数码',
        'category-computer.html': '电脑办公',
        'category-appliances.html': '家用电器',
        'category-furniture.html': '家居',
        'category-sports.html': '体育',
        'category-beauty.html': '美妆',
        'category-kitchen.html': '家居', // 居家厨具也映射为家居
        'category-daily.html': '日用文创',
        'category-clothing.html': '时尚',
        'product.html': '数码', // 商品列表页默认显示数码
        'index.html': '数码',   // 首页默认显示数码
        'cart.html': '数码'     // 购物车页默认显示数码
    };

    // 从路径中提取文件名
    const filename = pathname.substring(pathname.lastIndexOf('/') + 1);

    return categoryMap[filename] || '数码'; // 默认返回数码
}

// 加载广告函数
async function loadAd(adContainerId) {
    try {
        const visitorId = getStableId();
        const channel = getCurrentCategory();

        console.log(`加载广告参数: visitorId=${visitorId}, channel=${channel}, siteType=shop`);

        const response = await fetch(
            `${AD_API_URL}?siteType=shop&channel=${encodeURIComponent(channel)}&visitorId=${visitorId}`,
            {
                method: 'GET',
                credentials: 'include' // 必须包含，用于跨域身份凭证
            }
        );

        if (!response.ok) {
            throw new Error(`广告API请求失败: ${response.status}`);
        }

        const adData = await response.json();
        const adContainer = document.getElementById(adContainerId);

        if (adData && adData.image) {
            // 图片路径补全
            const imgPath = adData.image.startsWith('/') ? adData.image : '/' + adData.image;
            const fullImageUrl = AD_API_BASE + imgPath;

            // 创建广告HTML结构
            adContainer.innerHTML = `
                <div class="ad-container">
                    <a href="${adData.link || '#'}" target="_blank" class="ad-link">
                        <img src="${fullImageUrl}"
                             alt="${adData.title || '广告'}"
                             class="ad-image"
                             onerror="this.onerror=null; this.src='https://via.placeholder.com/250x300?text=广告加载中'">
                        <div class="ad-content">
                            <div class="ad-title">${adData.title || '推荐广告'}</div>
                            ${adData.description ? `<div class="ad-desc">${adData.description}</div>` : ''}
                        </div>
                    </a>
                </div>
            `;

            console.log(`广告加载成功 (${adContainerId}):`, adData);

        } else {
            // 没有广告数据时显示占位符
            adContainer.innerHTML = `
                <div class="ad-placeholder">
                    <div class="placeholder-image"></div>
                    <div class="placeholder-text">广告加载中...</div>
                </div>
            `;
        }

    } catch (error) {
        console.error(`广告加载失败 (${adContainerId}):`, error);
        const adContainer = document.getElementById(adContainerId);
        adContainer.innerHTML = `
            <div class="ad-placeholder">
                <div class="placeholder-image"></div>
                <div class="placeholder-text">广告加载失败</div>
            </div>
        `;
    }
}

// 页面加载完成后获取广告
document.addEventListener('DOMContentLoaded', function() {
    console.log('页面加载完成，开始加载广告...');

    // 加载左侧广告
    if (document.getElementById('left-ad')) {
        loadAd('left-ad');
    }

    // 加载右侧广告
    if (document.getElementById('right-ad')) {
        loadAd('right-ad');
    }

    // 每30秒刷新一次广告
    setInterval(() => {
        console.log('刷新广告...');
        if (document.getElementById('left-ad')) {
            loadAd('left-ad');
        }
        if (document.getElementById('right-ad')) {
            loadAd('right-ad');
        }
    }, 30000);
});