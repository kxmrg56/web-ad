# 🚀 全平台广告系统（adproj）集成手册

本手册用于指导 **视频站**、**购物站** 及 **新闻站** 队友接入统一的广告 API，实现跨站身份同步与精准推荐。

## 1. 核心接入参数对照表

接入时最关键的区别在于 `siteType` 字段，这决定了服务器返回的是视频（.mp4）还是图片（.jpg/.png）。

| **配置项**   | **📺 视频网站**                               | **🛒 购物网站 & 📰 新闻网站** |
| ------------ | -------------------------------------------- | --------------------------- |
| **siteType** | **必须固定为 `video`**                       | **必须固定为 `shop/news`**  |
| **素材类型** | 返回 `.mp4` 视频文件                         | 返回图片素材                |
| **路径处理** | **必须执行 `encodeURI()`** 以处理中文路径    | 直接拼接服务器前缀即可      |
| **展示标签** | 使用 `<video>` 标签，需设 `muted` 以自动播放 | 使用 `<img>` 标签           |

------

## 2. 分类映射表（Semantic Mapping）

请根据各站点的业务分类，将 `channel` 参数映射如下：

| **业务站分类（视频/购物/新闻）** | **映射为 channel 参数值** |
| -------------------------------- | ------------------------- |
| 电影                             | **电影**                  |
| 手机数码 / 科技                  | **数码**                  |
| 电脑办公                         | **电脑办公**              |
| 体育 / 运动专区                  | **体育**                  |
| 服装精选 / 综艺                  | **时尚**                  |
| 教育 / 美食 / 旅游               | **教育 / 美食 / 旅游**    |
| 美妆护理                         | **美妆**                  |
| 生活家具 / 居家厨具              | **家居**                  |

------

## 3. 前端代码实现

### 第一步：提取身份 ID（全站通用）

确保能够获取到用于跨站追踪的 `visitor_id`。

JavaScript

```
function getStableId() {
    // 从本地 Cookie 中提取名为 visitor_id 的值
    let match = document.cookie.match(new RegExp('(^| )visitor_id=([^;]*)'));
    return match ? match[2] : "";
}
```

### 第二步：发起请求与渲染逻辑

#### 📺 视频网站（专用逻辑）

由于视频存储路径包含中文，**务必使用 `encodeURI()`**。

JavaScript

```
const server = "http://10.100.164.33:8080/adproj-1.0-SNAPSHOT";
const visitorId = getStableId();
const channel = "电影"; // 示例映射

fetch(`${server}/ads/api/getAd?siteType=video&channel=${encodeURIComponent(channel)}&visitorId=${visitorId}`, {
    method: 'GET',
    credentials: 'include' // 必须包含，用于跨域身份凭证
})
.then(res => res.json())
.then(data => {
    if (data && data.image && data.image.toLowerCase().endsWith('.mp4')) {
        // 拼接路径并进行 URL 编码
        const relativePath = data.image.startsWith('/') ? data.image : "/" + data.image;
        const fullPath = encodeURI(server + relativePath);
        
        document.getElementById('AD_VIDEO_PLAYER').src = fullPath;
        document.getElementById('AD_VIDEO_TITLE').innerText = data.title;
    }
});
```

#### 🛒 购物网站 & 📰 新闻网站（通用逻辑）

直接使用图片展示，无需复杂的编码处理。

JavaScript

```
const server = "http://10.100.164.33:8080/adproj-1.0-SNAPSHOT";
const visitorId = getStableId();
const channel = "数码"; // 示例映射

fetch(`${server}/ads/api/getAd?siteType=shop&channel=${encodeURIComponent(channel)}&visitorId=${visitorId}`, {
    method: 'GET',
    credentials: 'include'
})
.then(res => res.json())
.then(data => {
    if (data && data.image) {
        const imgPath = data.image.startsWith('/') ? data.image : "/" + data.image;
        document.getElementById('AD_IMG_ID').src = server + imgPath;
        document.getElementById('AD_TITLE_ID').innerText = data.title;
        document.getElementById('AD_LINK_ID').href = data.link;
    }
});
```

------

## 4. 注意事项

1. **图片/视频显示不出来？** 请检查服务器前缀 `http://10.100.164.33:8080...` 是否补全。
2. **默认分类**：如果映射失败，API 将默认返回“军事”类广告。
3. **精准推送说明**：系统会优先根据用户的“历史最爱”推送广告。如果新闻站刷了“政治”，在购物/视频站看到相关推荐，说明跨站追踪已成功。

------

