<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../common/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="content-sidebar">
    <%@ include file="../common/left-ad.jsp" %>

    <div class="main-content">
        <section class="search-results">
            <h2 class="section-title">搜索结果</h2>

            <div class="search-info">
                <p>搜索关键词: "<strong>${searchKeyword}</strong>"</p>
                <p>找到 <strong>${searchResults.size()}</strong> 条相关新闻</p>
            </div>

            <c:if test="${not empty searchResults}">
                <div class="news-list">
                    <c:forEach var="news" items="${searchResults}">
                        <article class="news-item">
                            <div class="news-image">
                                <img src="${pageContext.request.contextPath}/images/default-news.jpg"
                                     alt="${news.title}">
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
            </c:if>

            <c:if test="${empty searchResults}">
                <div class="no-results">
                    <p>抱歉，没有找到与 "<strong>${searchKeyword}</strong>" 相关的新闻。</p>
                    <p>建议您：</p>
                    <ul>
                        <li>检查关键词拼写</li>
                        <li>尝试其他相关词汇</li>
                        <li>减少关键词数量</li>
                    </ul>
                </div>
            </c:if>
        </section>
    </div>

    <%@ include file="../common/right-ad.jsp" %>
</div>

<%@ include file="../common/footer.jsp" %>