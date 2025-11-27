<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp" %>

<div class="content-sidebar">
    <%@ include file="../common/left-ad.jsp" %>

    <div class="main-content">
        <section class="category-news">
            <h2 class="section-title">科技新闻</h2>
            <p class="category-description">探索科技前沿，见证创新力量。</p>

            <div class="news-list">
                <article class="news-item">
                    <div class="news-image">
                        <img src="${pageContext.request.contextPath}/images/news/tech1.jpg"
                             alt="人工智能技术新突破"
                             onerror="this.src='${pageContext.request.contextPath}/images/news/default.jpg'">
                    </div>
                    <div class="news-content">
                        <h3 class="news-title">
                            <a href="${pageContext.request.contextPath}/news/detail?id=1">
                                人工智能技术新突破
                            </a>
                        </h3>
                        <p class="news-summary">科学家在人工智能领域取得重大进展，为行业发展带来新机遇。</p>
                        <div class="news-meta">
                            <span class="news-author">科技前沿</span>
                            <span class="news-date">2024-01-06</span>
                            <span class="news-category tech">科技</span>
                        </div>
                    </div>
                </article>

                <article class="news-item">
                    <div class="news-image">
                        <img src="${pageContext.request.contextPath}/images/news/tech2.jpg"
                             alt="5G技术应用广泛推进"
                             onerror="this.src='${pageContext.request.contextPath}/images/news/default.jpg'">
                    </div>
                    <div class="news-content">
                        <h3 class="news-title">
                            <a href="${pageContext.request.contextPath}/news/detail?id=12">
                                5G技术应用广泛推进
                            </a>
                        </h3>
                        <p class="news-summary">5G网络覆盖范围不断扩大，赋能各行业数字化转型。</p>
                        <div class="news-meta">
                            <span class="news-author">通信技术</span>
                            <span class="news-date">2024-01-05</span>
                            <span class="news-category tech">科技</span>
                        </div>
                    </div>
                </article>
            </div>
        </section>
    </div>

    <%@ include file="../common/right-ad.jsp" %>
</div>

<%@ include file="../common/footer.jsp" %>