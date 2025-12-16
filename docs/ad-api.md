# Web-Ad 广告投放系统（API 文档）

## 项目简介

本项目是一个 **基于 Java Servlet 的广告投放系统**，
用于为多个独立网站提供 **匿名用户广告投放服务**。

- 支持匿名用户识别（Cookie）
- 支持多网站调用同一广告 API
- 支持后续精准广告投放扩展

------

## 一、广告获取 API

### 1️⃣ 接口说明

用于获取一条可展示的广告内容，
广告系统会自动识别匿名访客并记录访问行为。

------

### 2️⃣ 接口地址

```http
GET http://10.100.164.33:8080/adproj-1.0-SNAPSHOT/ads/api/getAd
```

------

### 3️⃣ 请求说明

- 请求方式：`GET`
- 请求参数：无
- 是否需要登录：否
- 是否需要鉴权：否

------

### 4️⃣ Cookie 机制（重要）

广告系统会自动使用以下 Cookie：

```
visitor_id = UUID
```

- 用于匿名用户识别
- 浏览器自动携带
- 接入网站无需处理用户 ID

------

### 5️⃣ 返回数据示例（JSON）

```json
{
  "visitorId": "514c3fae-68d6-4716-9b5e-62984e320476",
  "adId": 1,
  "title": "Digital Product Sale",
  "image": "http://example.com/ad.jpg",
  "link": "http://example.com/product"
}
```

#### 字段说明

| 字段      | 说明             |
| --------- | ---------------- |
| visitorId | 匿名用户唯一标识 |
| adId      | 广告 ID          |
| title     | 广告标题         |
| image     | 广告图片地址     |
| link      | 广告跳转链接     |

------

### 6️⃣ 前端调用示例

```js
fetch("http://10.100.164.33:8080/adproj-1.0-SNAPSHOT/ads/api/getAd")
  .then(res => res.json())
  .then(ad => {
    document.getElementById("ad-title").innerText = ad.title;
    document.getElementById("ad-img").src = ad.image;
    document.getElementById("ad-link").href = ad.link;
  });
```

------

## 二、系统架构说明（简要）

```
[ 新闻站 ]   \
[ 商城站 ] ——> 广告系统 API ——> MySQL
[ 视频站 ]   /
```

- 多个网站独立部署
- 不使用反向代理
- 统一通过广告 API 获取广告

------

## 三、后续计划

- 用户行为上报接口
- 精准广告投放算法
- 广告点击统计
- 投放效果分析

------

## 维护说明

- 广告系统服务器：10.100.164.33
- 项目用途：课程设计 / 实验项目