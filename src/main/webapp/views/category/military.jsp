<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp" %>

<div class="content-sidebar">
    <%@ include file="../common/left-ad.jsp" %>

    <div class="main-content">
        <section class="category-news">
            <h2 class="section-title">军事新闻</h2>
            <p class="category-description">关注国防建设，了解军事科技发展。</p>

            <div class="news-list">
                <article class="news-item">
                    <div class="news-image">
                        <img src="${pageContext.request.contextPath}/images/news/military1.jpg"
                             alt="国防科技新成果展示"
                             onerror="this.src='${pageContext.request.contextPath}/images/news/default.jpg'">
                    </div>
                    <div class="news-content">
                        <h3 class="news-title">
                            <a href="${pageContext.request.contextPath}/news/detail?id=5">
                                国防科技新成果展示
                            </a>
                        </h3>
                        <p class="news-summary">自主研发装备性能卓越，展现国家科技实力。</p>
                        <div class="news-meta">
                            <span class="news-author">国防时报</span>
                            <span class="news-date">2024-01-12</span>
                            <span class="news-category military">军事</span>
                        </div>
                    </div>
                </article>

                <article class="news-item">
                    <div class="news-image">
                        <img src="${pageContext.request.contextPath}/images/news/military2.jpg"
                             alt="军事演习圆满成功"
                             onerror="this.src='${pageContext.request.contextPath}/images/news/default.jpg'">
                    </div>
                    <div class="news-content">
                        <h3 class="news-title">
                            <a href="${pageContext.request.contextPath}/news/detail?id=9">
                                军事演习圆满成功
                            </a>
                        </h3>
                        <p class="news-summary">联合军事演习展示现代化作战能力。</p>
                        <div class="news-meta">
                            <span class="news-author">军事观察</span>
                            <span class="news-date">2024-01-11</span>
                            <span class="news-category military">军事</span>
                        </div>
                    </div>
                </article>
            </div>
        </section>
    </div>

    <%@ include file="../common/right-ad.jsp" %>
</div>

<%@ include file="../common/footer.jsp" %>