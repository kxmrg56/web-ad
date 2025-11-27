<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp" %>

<div class="content-sidebar">
    <%@ include file="../common/left-ad.jsp" %>

    <div class="main-content">
        <section class="category-news">
            <h2 class="section-title">体育新闻</h2>
            <p class="category-description">关注体育赛事，感受运动魅力。</p>

            <div class="news-list">
                <article class="news-item">
                    <div class="news-image">
                        <img src="${pageContext.request.contextPath}/images/news/sports1.jpg"
                             alt="全国运动会圆满落幕"
                             onerror="this.src='${pageContext.request.contextPath}/images/news/default.jpg'">
                    </div>
                    <div class="news-content">
                        <h3 class="news-title">
                            <a href="${pageContext.request.contextPath}/news/detail?id=3">
                                全国运动会圆满落幕
                            </a>
                        </h3>
                        <p class="news-summary">运动员们展现出色技艺，创造多项新纪录。</p>
                        <div class="news-meta">
                            <span class="news-author">体育周刊</span>
                            <span class="news-date">2024-01-08</span>
                            <span class="news-category sports">体育</span>
                        </div>
                    </div>
                </article>

                <article class="news-item">
                    <div class="news-image">
                        <img src="${pageContext.request.contextPath}/images/news/sports2.jpg"
                             alt="足球联赛激烈进行中"
                             onerror="this.src='${pageContext.request.contextPath}/images/news/default.jpg'">
                    </div>
                    <div class="news-content">
                        <h3 class="news-title">
                            <a href="${pageContext.request.contextPath}/news/detail?id=11">
                                足球联赛激烈进行中
                            </a>
                        </h3>
                        <p class="news-summary">多支强队展现出色表现，冠军争夺日趋激烈。</p>
                        <div class="news-meta">
                            <span class="news-author">体育快报</span>
                            <span class="news-date">2024-01-07</span>
                            <span class="news-category sports">体育</span>
                        </div>
                    </div>
                </article>
            </div>
        </section>
    </div>

    <%@ include file="../common/right-ad.jsp" %>
</div>

<%@ include file="../common/footer.jsp" %>