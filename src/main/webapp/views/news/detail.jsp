<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp" %>

<div class="content-sidebar">
    <%@ include file="../common/left-ad.jsp" %>

    <div class="main-content">
        <article class="news-detail">
            <div class="breadcrumb">
                <a href="${pageContext.request.contextPath}/home">首页</a>
                &gt;
                <a href="${pageContext.request.contextPath}/news?category=${news.category}">
                    ${news.category}新闻
                </a>
                &gt;
                <span>正文</span>
            </div>

            <header class="news-header">
                <h1 class="news-title">${news.title}</h1>
                <div class="news-meta">
                    <span class="news-author">作者：${news.author}</span>
                    <span class="news-date">发布时间：${news.publishDate}</span>
                    <span class="news-views">阅读量：${news.viewCount}</span>
                </div>
            </header>

            <div class="news-image-large">
                <img src="${pageContext.request.contextPath}${news.imageUrl}"
                     alt="${news.title}"
                     onerror="this.src='${pageContext.request.contextPath}/images/news/default.jpg'">
            </div>

            <div class="news-content">
                <p>${news.content}</p>
            </div>
        </article>
    </div>

    <%@ include file="../common/right-ad.jsp" %>
</div>

<%@ include file="../common/footer.jsp" %>