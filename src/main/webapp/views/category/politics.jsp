<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp" %>

<div class="content-sidebar">
    <%@ include file="../common/left-ad.jsp" %>

    <div class="main-content">
        <section class="category-news">
            <h2 class="section-title">政治新闻</h2>
            <p class="category-description">关注国内外政治动态，了解政策法规变化。</p>

            <div class="news-list">
                <article class="news-item">
                    <div class="news-image">
                        <img src="${pageContext.request.contextPath}/images/news/politics1.jpg"
                             alt="国际政治领导人会晤"
                             onerror="this.src='${pageContext.request.contextPath}/images/news/default.jpg'">
                    </div>
                    <div class="news-content">
                        <h3 class="news-title">
                            <a href="${pageContext.request.contextPath}/news/detail?id=2">
                                国际政治领导人会晤
                            </a>
                        </h3>
                        <p class="news-summary">多国元首举行高峰会谈，共商全球发展大计。</p>
                        <div class="news-meta">
                            <span class="news-author">国际观察</span>
                            <span class="news-date">2024-01-15</span>
                            <span class="news-category politics">政治</span>
                        </div>
                    </div>
                </article>

                <article class="news-item">
                    <div class="news-image">
                        <img src="${pageContext.request.contextPath}/images/news/politics2.jpg"
                             alt="地方两会顺利召开"
                             onerror="this.src='${pageContext.request.contextPath}/images/news/default.jpg'">
                    </div>
                    <div class="news-content">
                        <h3 class="news-title">
                            <a href="${pageContext.request.contextPath}/news/detail?id=7">
                                地方两会顺利召开
                            </a>
                        </h3>
                        <p class="news-summary">各地人大代表齐聚一堂，审议政府工作报告。</p>
                        <div class="news-meta">
                            <span class="news-author">政治周刊</span>
                            <span class="news-date">2024-01-14</span>
                            <span class="news-category politics">政治</span>
                        </div>
                    </div>
                </article>

                <article class="news-item">
                    <div class="news-image">
                        <img src="${pageContext.request.contextPath}/images/news/politics3.jpg"
                             alt="外交政策新动向"
                             onerror="this.src='${pageContext.request.contextPath}/images/news/default.jpg'">
                    </div>
                    <div class="news-content">
                        <h3 class="news-title">
                            <a href="${pageContext.request.contextPath}/news/detail?id=8">
                                外交政策新动向
                            </a>
                        </h3>
                        <p class="news-summary">我国外交部就国际热点问题发表最新声明。</p>
                        <div class="news-meta">
                            <span class="news-author">外交时报</span>
                            <span class="news-date">2024-01-13</span>
                            <span class="news-category politics">政治</span>
                        </div>
                    </div>
                </article>
            </div>
        </section>
    </div>

    <%@ include file="../common/right-ad.jsp" %>
</div>

<%@ include file="../common/footer.jsp" %>