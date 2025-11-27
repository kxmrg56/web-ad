// 主要JavaScript功能
document.addEventListener('DOMContentLoaded', function() {
    // 页面加载完成后的初始化操作
    console.log('新闻网站初始化完成');

    // 图片加载失败处理
    const images = document.querySelectorAll('img');
    images.forEach(img => {
        img.addEventListener('error', function() {
            this.src = '/news-website/images/news/default.jpg';
        });
    });
});