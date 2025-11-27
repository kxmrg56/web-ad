<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ include file="../common/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="content-sidebar">
    <%@ include file="../common/left-ad.jsp" %>

    <div class="main-content">
        <section class="category-news">
            <h2 class="section-title">
                <c:choose>
                    <c:when test="${not empty currentCategory}">
                        ${currentCategory.name}新闻
                    </c:when>
                    <c:otherwise>
                        所有新闻
                    </c:otherwise>
                </c:choose>
            </h2>
            <div class="news-list">
                <c:forEach var="news" items="${newsList}">
                    <article class="news-item">
                        <div class="news-image">
                            <img src="${pageContext.request.contextPath}/images/default-news.jpg"
                                 alt="${news.title}"
                                 onerror="this.src='${pageContext.request.contextPath}/images/default-news.jpg'">
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
                                <span class="news-date">${news.publishDate}</span>
                                <span class="news-category ${news.category}">${news.category}</span>
                            </div>
                        </div>
                    </article>
                </c:forEach>
            </div>
        </section>
    </div>

    <%@ include file="../common/right-ad.jsp" %>
</div>

<%@ include file="../common/footer.jsp" %>