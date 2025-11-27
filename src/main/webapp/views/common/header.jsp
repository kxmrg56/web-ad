<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>新闻网站</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/news.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ad.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/responsive.css">
    <link rel="icon" href="data:,">
</head>
<body>
<header class="header">
    <div class="container">
        <div class="header-content">
        <div class="logo">
            <h1>新闻快讯</h1>
        </div>

            <div class="header-search">
                <form action="${pageContext.request.contextPath}/search" method="get" class="search-form">
                    <input type="text" name="keyword" placeholder="搜索新闻..." class="search-input">
                    <button type="submit" class="search-btn">搜索</button>
                </form>
            </div>
        </div>
    </div>
</header>

<nav class="navigation">
    <div class="container">
        <ul class="nav-list">
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/" class="nav-link">首页</a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/news?category=politics" class="nav-link">政治</a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/news?category=military" class="nav-link">军事</a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/news?category=economy" class="nav-link">经济</a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/news?category=sports" class="nav-link">体育</a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/news?category=tech" class="nav-link">科技</a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/news?category=entertainment" class="nav-link">娱乐</a>
            </li>
        </ul>
    </div>
</nav>

<main class="main container">
    <div class="layout">