# 互联网广告投放生态平台 (Internet Advertising Ecological Platform)

> **Project Version:** 1.0.0-SNAPSHOT  
> **Tech Stack:** Jakarta EE 10, Servlet 6.0, MySQL 8.0, HTML5/ES6+  
> **Architecture:** Distributed Microservices Simulation (B/S Star Topology)

## 📖 1. 项目深度解析 (Project Overview)

### 1.1 系统背景与设计理念
在当今的互联网商业环境中，单一的 Web 应用已无法满足复杂的流量变现需求。本项目构建了一个高度仿真的**全链路广告投放生态系统**，打破了传统单体应用的孤岛效应。系统采用**分布式微服务架构思想**，模拟了真实的互联网广告联盟（Ad Network）运作模式。

系统的核心由一个中央控制节点和多个边缘媒体节点组成：
* **Ad Server (广告管理中台)**：作为生态系统的“大脑”，负责全网流量的分发、广告素材的存储、推荐算法的实时计算以及跨域用户身份的分发。
* **Media Matrix (前端媒体矩阵)**：由三个业务逻辑完全独立的子系统组成——**视频流媒体站 (Video)**、**新闻资讯站 (News)**、**在线电商站 (Shopping)**。

### 1.2 核心技术突破
通过统一的 RESTful API 接口，本系统实现了以下关键技术指标，这也是本项目区别于普通 Web 项目的核心亮点：

1.  **跨站点的用户身份识别 (Cross-Site Identity Tracking)**：
    利用 Cookie 和后端算法，在不同域名、不同端口的子系统间（如从购物站跳转到视频站）保持用户身份的唯一性 (`visitor_id`)，实现全网行为追踪。
2.  **基于上下文的精准广告投放 (Contextual Advertising)**：
    媒体端通过 API 上报当前页面的内容分类（如“电影”、“数码”），中台根据上下文动态匹配最相关的广告素材，而非随机展示。
3.  **用户兴趣画像追踪 (User Profiling)**：
    购物站作为“兴趣探针”，捕捉用户的购买意图并同步至中台数据库。当该用户访问其他站点时，系统会优先推送其感兴趣的商品广告，实现千人千面的推荐效果。

---

## 🏗 2. 系统架构与拓扑 (System Architecture)

系统采用经典的 **B/S 架构** 结合 **星型网络拓扑**。四个子系统在逻辑上完全独立，模拟分布式部署环境，通过 HTTP/REST API 进行松耦合通信。

```mermaid
graph TD
    User((User / Browser))
    
    subgraph "Media Client Matrix (前端媒体矩阵)"
        Video[视频网站 <br/> (Video Streaming & Ad Player)]
        News[新闻网站 <br/> (Contextual Content Delivery)]
        Shop[购物网站 <br/> (E-Commerce & Interest Probe)]
    end
    
    subgraph "Core Backend Services (核心中台)"
        AdServer[广告管理中台 <br/> (Ad Server & Recommendation Engine)]
        DB[(MySQL Database <br/> User Profiles & Ad Assets)]
    end
    
    %% 用户交互流
    User <==> Video
    User <==> News
    User <==> Shop
    
    %% 数据交互流



💻 3. 客户端集成指南与核心代码解析 (Client Integration & Code Analysis)本节详细阐述各媒体子系统（视频、新闻、购物）如何接入统一广告 API。为了确保跨域追踪和素材加载的稳定性，请务必严格遵守以下代码规范。3.1 核心 API 协议规范接入时最关键的区别在于 siteType 字段，这直接决定了服务器返回的 MIME 类型（视频流 vs 图片流）以及前端的处理逻辑。配置项📺 视频网站 (Video Site)🛒 购物网站 & 📰 新闻网站 (Shop/News)API 参数siteType=videositeType=shop 或 siteType=news返回素材.mp4 视频文件.jpg / .png 图片文件编码要求强制执行 encodeURI() (处理中文路径)直接拼接服务器前缀即可HTML 载体<video muted autoplay><img> 标签3.2 关键代码模块一：跨站身份 ID 提取为了在不同的子系统间维持同一个用户身份，我们需要从 Cookie 中提取持久化的 visitor_id。所有客户端页面必须包含此函数。JavaScript/**
 * [Core Function] 获取全站通用的唯一身份标识
 * * 原理：
 * 广告中台在首次响应时会写入一个 HttpOnly 之外的 Cookie (visitor_id)。
 * 本函数通过正则匹配，从 document.cookie 字符串中提取该 ID。
 * * @returns {string} visitor_id 字符串，若未找到则返回空字符串
 */
function getStableId() {
    // 正则表达式解析：
    // (^| )       -> 匹配行首或空格（处理 Cookie 拼接时的空格分隔符）
    // visitor_id= -> 匹配目标 Key
    // ([^;]*)     -> 捕获组，匹配除分号以外的任意字符（即 Value 部分）
    let match = document.cookie.match(new RegExp('(^| )visitor_id=([^;]*)'));
    return match ? match[2] : "";
}
3.3 关键代码模块二：视频网站专用接入逻辑场景挑战：中文路径问题：视频素材的文件名通常包含中文（如 /ads/华为手机.mp4），直接放入 src 会导致 URL 乱码或 404 错误。MIME 类型校验：必须确保返回的是视频流而非图片。完整代码实现：JavaScript// 1. 定义广告中台服务器地址 (生产环境需替换为实际域名)
const server = "[http://10.100.164.33:8080/adproj-1.0-SNAPSHOT](http://10.100.164.33:8080/adproj-1.0-SNAPSHOT)";

// 2. 获取用户身份
const visitorId = getStableId();

// 3. 定义当前视频内容的分类 (用于上下文推荐)
// 映射规则：电影->channel=电影; 数码评测->channel=数码
const channel = "电影"; 

// 4. 发起异步请求
// 注意：channel 参数可能包含中文，必须使用 encodeURIComponent 进行参数级编码
fetch(`${server}/ads/api/getAd?siteType=video&channel=${encodeURIComponent(channel)}&visitorId=${visitorId}`, {
    method: 'GET',
    
    // [CRITICAL] 跨域凭证配置
    // 必须设置为 'include'，否则浏览器不会携带跨域 Cookie，导致 server 无法识别老用户，
    // 从而无法进行基于历史画像的精准推荐。
    credentials: 'include' 
})
.then(res => res.json()) // 解析 JSON 响应
.then(data => {
    // 5. 校验数据有效性及文件后缀
    if (data && data.image && data.image.toLowerCase().endsWith('.mp4')) {
        
        // 6. 路径标准化处理
        // 后端返回的路径可能是 "video/ad.mp4" 或 "/video/ad.mp4"，统一处理为绝对路径
        const relativePath = data.image.startsWith('/') ? data.image : "/" + data.image;
        
        // 7. [CRITICAL] 完整 URL 编码
        // 使用 encodeURI 对整个 URL 进行编码，它会转义中文字符但保留 URL 结构符号（如 ://）
        // 例如：.../华为.mp4 -> .../%E5%8D%8E%E4%B8%BA.mp4
        const fullPath = encodeURI(server + relativePath);
        
        // 8. DOM 操作与渲染
        const videoPlayer = document.getElementById('AD_VIDEO_PLAYER');
        videoPlayer.src = fullPath;
        
        // 自动播放策略：现代浏览器通常要求静音(muted)才能自动播放
        videoPlayer.muted = true; 
        videoPlayer.play();
        
        document.getElementById('AD_VIDEO_TITLE').innerText = data.title;
        console.log(`[AdSystem] Video ad loaded: ${data.title}`);
    }
})
.catch(error => {
    console.error("[AdSystem] Failed to fetch video ad:", error);
});
3.4 关键代码模块三：购物与新闻站通用接入逻辑场景挑战：点击跳转：图片广告的核心是引导用户点击，因此除了 src 外，必须正确绑定 href 跳转链接。简单化处理：图片路径通常兼容性较好，无需强制全路径编码，但仍需处理前缀拼接。完整代码实现：JavaScript// 1. 定义服务器与身份
const server = "[http://10.100.164.33:8080/adproj-1.0-SNAPSHOT](http://10.100.164.33:8080/adproj-1.0-SNAPSHOT)";
const visitorId = getStableId();

// 2. 定义页面分类 (如新闻站的"科技"版块，或购物站的"数码"类目)
const channel = "数码"; 

// 3. 发起请求 (siteType 设为 shop 或 news)
fetch(`${server}/ads/api/getAd?siteType=shop&channel=${encodeURIComponent(channel)}&visitorId=${visitorId}`, {
    method: 'GET',
    credentials: 'include' // 同样需要携带 Cookie 以更新用户画像
})
.then(res => res.json())
.then(data => {
    // 4. 数据校验
    if (data && data.image) {
        // 5. 路径拼接
        // 图片资源通常直接拼接即可被浏览器正确解析
        const imgPath = data.image.startsWith('/') ? data.image : "/" + data.image;
        
        // 6. 渲染图片元素
        const adImage = document.getElementById('AD_IMG_ID');
        adImage.src = server + imgPath;
        adImage.alt = data.title; // 增强可访问性
        
        // 7. 渲染标题
        document.getElementById('AD_TITLE_ID').innerText = data.title;
        
        // 8. [Core] 绑定点击跳转链接
        // data.link 通常指向广告主的落地页 (Landing Page)
        const adLink = document.getElementById('AD_LINK_ID');
        if (adLink) {
            adLink.href = data.link;
            adLink.target = "_blank"; // 建议新窗口打开
        }
        
        console.log(`[AdSystem] Display ad loaded: ${data.title}, Link: ${data.link}`);
    }
})
.catch(error => {
    console.error("[AdSystem] Failed to fetch display ad:", error);
});
4. ⚙️ 配置与语义映射 (Configuration & Mapping)为了保证推荐算法的准确性，前端必须将各站点的业务分类正确映射为后端 Ad Server 能识别的标准 channel 参数。4.1 语义映射表 (Semantic Mapping Table)前端业务场景 (Frontend Context)后端标准参数 (Backend Channel Param)推荐逻辑说明视频站：动作片、爱情片、院线大片电影推送票务、流媒体会员、影视周边购物/新闻：手机、相机、智能设备、黑科技数码推送新款手机、耳机、科技产品办公：笔记本、鼠标键盘、办公软件电脑办公推送生产力工具、办公耗材体育：NBA、足球联赛、奥运会资讯体育推送运动鞋服、健身器材、能量饮料生活：服装精选、综艺节目、时尚杂志时尚推送美妆、服饰、奢侈品教育：K12、职业培训、网课教育推送课程、文具、教育硬件其他：地方菜系、零食测评、旅游攻略美食 / 旅游推送餐饮优惠券、机票酒店兜底策略：无法分类或映射失败(无)默认返回“军事”或“公益”类广告4.2 注意事项 (Troubleshooting)图片/视频显示不出来 (404/Broken Image)：检查点 1：服务器前缀变量 server 是否遗漏了端口号（如 :8080）或上下文路径（如 /adproj-1.0-SNAPSHOT）。检查点 2：在视频站代码中，确认是否遗漏了 encodeURI()。浏览器对包含中文的 URL 请求极其敏感，未编码通常会导致请求失败。跨站追踪失效 (Identity Sync Failed)：现象：在新闻站刷了大量“数码”新闻，去购物站却依然看到“美妆”广告（无关联推荐）。检查点：检查 Fetch 请求中的 credentials: 'include' 是否缺失。如果缺失，服务器无法读取到同一个 visitor_id，会将每次请求视为新用户。默认分类问题：如果 API 返回的数据总是“军事”或“公益”广告，说明 channel 参数传递有误，或者该 visitor_id 尚无任何历史行为数据，系统触发了冷启动兜底机制。
    Video -- "API Request (JSON)" --> AdServer
    News -- "API Request (JSON)" --> AdServer
    Shop -- "API Request (JSON)" --> AdServer
    
    %% 数据库交互
    AdServer <--> DB
