<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="views/common/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="content-sidebar">
    <%@ include file="views/common/left-ad.jsp" %>

    <div class="main-content">
        <section class="hot-news">
            <h2 class="section-title">热门新闻</h2>
            <div class="news-grid">
                <c:forEach var="news" items="${latestNews}">
                    <article class="news-card">
                        <div class="news-image">
                            <img src="${pageContext.request.contextPath}${news.imageUrl}"
                                 alt="${news.title}"
                                 onerror="this.src='${pageContext.request.contextPath}/images/news/default.jpg'">
                        </div>
                        <div class="news-content">
                            <h3 class="news-title">
                                <a href="${pageContext.request.contextPath}/news/detail?id=${news.id}">
                                        ${news.title}
                                </a>
                            </h3>
                            <p class="news-summary">${news.summary}</p>
                            <div class="news-meta">
                                <span class="news-author">${news.author}</span>
                                <span class="news-category ${news.category}">${news.category}</span>
                            </div>
                        </div>
                    </article>
                </c:forEach>
            </div>
        </section>
    </div>

    <%@ include file="views/common/right-ad.jsp" %>
</div>

<%@ include file="views/common/footer.jsp" %>