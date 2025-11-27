<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp" %>

<div class="content-sidebar">
    <%@ include file="../common/left-ad.jsp" %>

    <div class="main-content">
        <section class="category-news">
            <h2 class="section-title">经济新闻</h2>
            <p class="category-description">追踪经济动态，把握市场脉搏。</p>

            <div class="news-list">
                <article class="news-item">
                    <div class="news-image">
                        <img src="${pageContext.request.contextPath}/images/news/economy1.jpg"
                             alt="经济政策助力企业发展"
                             onerror="this.src='${pageContext.request.contextPath}/images/news/default.jpg'">
                    </div>
                    <div class="news-content">
                        <h3 class="news-title">
                            <a href="${pageContext.request.contextPath}/news/detail?id=6">
                                经济政策助力企业发展
                            </a>
                        </h3>
                        <p class="news-summary">减税降费措施见效，市场主体活力增强。</p>
                        <div class="news-meta">
                            <span class="news-author">经济观察</span>
                            <span class="news-date">2024-01-10</span>
                            <span class="news-category economy">经济</span>
                        </div>
                    </div>
                </article>

                <article class="news-item">
                    <div class="news-image">
                        <img src="${pageContext.request.contextPath}/images/news/economy2.jpg"
                             alt="股市迎来新一轮上涨"
                             onerror="this.src='${pageContext.request.contextPath}/images/news/default.jpg'">
                    </div>
                    <div class="news-content">
                        <h3 class="news-title">
                            <a href="${pageContext.request.contextPath}/news/detail?id=10">
                                股市迎来新一轮上涨
                            </a>
                        </h3>
                        <p class="news-summary">多重利好因素推动资本市场活跃度提升。</p>
                        <div class="news-meta">
                            <span class="news-author">财经日报</span>
                            <span class="news-date">2024-01-09</span>
                            <span class="news-category economy">经济</span>
                        </div>
                    </div>
                </article>
            </div>
        </section>
    </div>

    <%@ include file="../common/right-ad.jsp" %>
</div>

<%@ include file="../common/footer.jsp" %>