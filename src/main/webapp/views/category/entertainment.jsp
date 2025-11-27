<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp" %>

<div class="content-sidebar">
    <%@ include file="../common/left-ad.jsp" %>

    <div class="main-content">
        <section class="category-news">
            <h2 class="section-title">娱乐新闻</h2>
            <p class="category-description">追踪娱乐动态，品味艺术人生。</p>

            <div class="news-list">
                <article class="news-item">
                    <div class="news-image">
                        <img src="${pageContext.request.contextPath}/images/news/entertainment1.jpg"
                             alt="电影市场迎来黄金期"
                             onerror="this.src='${pageContext.request.contextPath}/images/news/default.jpg'">
                    </div>
                    <div class="news-content">
                        <h3 class="news-title">
                            <a href="${pageContext.request.contextPath}/news/detail?id=4">
                                电影市场迎来黄金期
                            </a>
                        </h3>
                        <p class="news-summary">多部优秀影片获得观众好评，票房成绩亮眼。</p>
                        <div class="news-meta">
                            <span class="news-author">影视快报</span>
                            <span class="news-date">2024-01-04</span>
                            <span class="news-category entertainment">娱乐</span>
                        </div>
                    </div>
                </article>

                <article class="news-item">
                    <div class="news-image">
                        <img src="${pageContext.request.contextPath}/images/news/entertainment2.jpg"
                             alt="音乐节吸引数万乐迷"
                             onerror="this.src='${pageContext.request.contextPath}/images/news/default.jpg'">
                    </div>
                    <div class="news-content">
                        <h3 class="news-title">
                            <a href="${pageContext.request.contextPath}/news/detail?id=13">
                                音乐节吸引数万乐迷
                            </a>
                        </h3>
                        <p class="news-summary">知名音乐人齐聚一堂，为观众带来精彩演出。</p>
                        <div class="news-meta">
                            <span class="news-author">娱乐周刊</span>
                            <span class="news-date">2024-01-03</span>
                            <span class="news-category entertainment">娱乐</span>
                        </div>
                    </div>
                </article>
            </div>
        </section>
    </div>

    <%@ include file="../common/right-ad.jsp" %>
</div>

<%@ include file="../common/footer.jsp" %>