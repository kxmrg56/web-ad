# 互联网广告投放生态平台 (Internet Advertising Ecological Platform)

> 基于 Jakarta EE 构建的全链路广告投放生态系统，涵盖广告中台(Ad Server)、视频流媒体(Video)、新闻资讯(News)与在线电商(Shopping)四大独立子系统。

![Java](https://img.shields.io/badge/Java-Jakarta%20EE-red) ![Servlet](https://img.shields.io/badge/Servlet-6.0-blue) ![MySQL](https://img.shields.io/badge/Database-MySQL%208.0-orange) ![Status](https://img.shields.io/badge/Status-Completed-success)

## 📖 项目简介

本项目旨在构建一个模拟真实互联网商业环境的广告投放生态系统。系统打破了单一 Web 应用的限制，采用**分布式微服务架构思想**，由一个核心的**广告管理中台**与三个独立的**媒体端（视频、新闻、购物）**共同组成。

核心目标是通过统一的广告 API 接口，实现**跨站点的用户身份识别 (Cross-Site Identity)**、**用户兴趣画像追踪 (User Profiling)** 以及基于**上下文的精准广告投放 (Contextual Advertising)**。

### 🌐 核心组成
* **Ad Server (广告中台)**: 全网控制塔，负责素材管理、推荐算法计算及跨域身份分发。
* **Video Site (视频网站)**: 支持 HTTP 206 断点续传的流媒体平台，实现了无缝视频中插广告。
* **News Site (新闻网站)**: 基于内容上下文（如体育、娱乐）的精准广告匹配平台。
* **Shopping Site (购物网站)**: 捕捉用户购买意图（兴趣探针），构建高权重用户画像。

---

## 🏗 系统架构

系统采用 **B/S 架构** 与 **星型拓扑结构**。四个子系统逻辑上完全独立，模拟分布式部署，通过 HTTP/REST API 进行通信。

```mermaid
graph TD
    User((User/Browser))
    
    subgraph "Media Clients (前端媒体矩阵)"
        Video[视频网站 (Video Streaming)]
        News[新闻网站 (Contextual News)]
        Shop[购物网站 (E-Commerce)]
    end
    
    subgraph "Core Backend (核心中台)"
        AdServer[广告管理中台 (Ad Server)]
        DB[(MySQL Database)]
    end
    
    User <--> Video
    User <--> News
    User <--> Shop
    
    Video -- "API: Get Ad / Stream" --> AdServer
    News -- "API: Get Context Ad" --> AdServer
    Shop -- "API: Sync Interest" --> AdServer
    
    AdServer <--> DB
