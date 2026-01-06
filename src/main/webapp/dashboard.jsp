<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>管理大盘 - 广告资产管理</title>
    <style>
        table { border-collapse: collapse; width: 100%; text-align: center; }
        th, td { padding: 8px; border: 1px solid #ddd; }
        th { background-color: #f2f2f2; }
        .preview-img {
            width: 100px;
            height: 60px;
            object-fit: cover;
            border: 1px solid #ddd;
            padding: 2px;
            border-radius: 4px;
            cursor: pointer;
        }
        .preview-video {
            width: 100px;
            height: 60px;
            border: 1px solid #ddd;
            padding: 2px;
            border-radius: 4px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-weight: bold;
        }
        .category-tag {
            color: blue;
            font-weight: bold;
            padding: 3px 8px;
            background: #e6f7ff;
            border-radius: 12px;
            font-size: 12px;
        }
        .material-type {
            color: green;
            font-size: 11px;
            font-weight: bold;
        }
        .view-count {
            font-size: 16px;
            font-weight: bold;
            color: #2c3e50;
        }
        .view-count-label {
            font-size: 11px;
            color: #666;
            margin-top: 2px;
        }
        .action-links {
            display: flex;
            justify-content: center;
            gap: 8px;
        }
        .action-links a {
            padding: 4px 10px;
            border-radius: 4px;
            text-decoration: none;
            font-size: 13px;
            transition: all 0.3s;
        }
        .action-links a:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .delete-btn {
            background-color: #dc3545;
            color: white;
        }
        .delete-btn:hover {
            background-color: #c82333;
            color: white;
        }
        .play-btn {
            background-color: #17a2b8;
            color: white;
        }
        .play-btn:hover {
            background-color: #138496;
            color: white;
        }
        .link-btn {
            background-color: #28a745;
            color: white;
        }
        .link-btn:hover {
            background-color: #218838;
            color: white;
        }
        .upload-btn {
            display: inline-block;
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            margin: 10px 0;
            transition: all 0.3s;
        }
        .upload-btn:hover {
            background-color: #45a049;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .navigation {
            margin: 20px 0;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 4px;
            display: flex;
            align-items: center;
            gap: 20px;
        }
        .navigation a {
            text-decoration: none;
            color: #007bff;
            transition: color 0.3s;
        }
        .navigation a:hover {
            text-decoration: underline;
            color: #0056b3;
        }
        /* 视频预览模态框样式 */
        .video-modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.8);
            z-index: 1000;
        }
        .modal-content {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            max-width: 800px;
            width: 90%;
            max-height: 90vh;
        }
        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 1px solid #eee;
        }
        .modal-header h3 {
            margin: 0;
            color: #333;
        }
        .close-btn {
            background: none;
            border: none;
            font-size: 28px;
            cursor: pointer;
            color: #666;
            line-height: 1;
        }
        .close-btn:hover {
            color: #333;
        }
        .modal-video {
            width: 100%;
            max-height: 70vh;
            border-radius: 4px;
        }
        /* 无预览样式 */
        .no-preview {
            width: 100px;
            height: 60px;
            display: flex;
            align-items: center;
            justify-content: center;
            border: 1px dashed #ddd;
            border-radius: 4px;
            color: #666;
            font-size: 12px;
        }
        /* 热门标签 */
        .hot-badge {
            display: inline-block;
            padding: 2px 6px;
            background-color: #ff6b6b;
            color: white;
            font-size: 10px;
            border-radius: 10px;
            margin-left: 5px;
            vertical-align: middle;
        }
        .popular-badge {
            display: inline-block;
            padding: 2px 6px;
            background-color: #ffa726;
            color: white;
            font-size: 10px;
            border-radius: 10px;
            margin-left: 5px;
            vertical-align: middle;
        }
    </style>
</head>
<body>
<h2>欢迎回来: ${sessionScope.username}</h2>

<div class="navigation">
    <a href="${pageContext.request.contextPath}/index.jsp">返回首页</a>
    <a href="${pageContext.request.contextPath}/ads/upload/form" class="upload-btn">上传新素材</a>
</div>

<h3>我的广告资产 (共 ${ads.size()} 个)</h3>
<table>
    <thead>
    <tr>
        <th width="25%">广告标题</th>
        <th width="15%">分类</th>
        <th width="10%">素材类型</th>
        <th width="10%">浏览量</th>
        <th width="20%">预览</th>
        <th width="20%">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:choose>
        <c:when test="${empty ads}">
            <tr>
                <td colspan="6" style="text-align: center; padding: 40px;">
                    <p>暂无广告素材</p>
                    <a href="${pageContext.request.contextPath}/ads/upload/form" class="upload-btn">
                        点击上传第一个素材
                    </a>
                </td>
            </tr>
        </c:when>
        <c:otherwise>
            <c:forEach items="${ads}" var="ad">
                <tr>
                    <td style="text-align: left;">
                        <strong style="display: block; margin-bottom: 5px;">${ad.title}</strong>
                        <c:if test="${not empty ad.materialName}">
                            <small style="color: #666; font-size: 12px;">${ad.materialName}</small>
                        </c:if>
                        <c:if test="${not empty ad.id}">
                            <div style="font-size: 11px; color: #999; margin-top: 3px;">ID: ${ad.id}</div>
                        </c:if>
                    </td>
                    <td><span class="category-tag">${ad.category}</span></td>
                    <td>
                        <c:choose>
                            <c:when test="${ad.imageUrl.contains('videos/')}">
                                <span class="material-type">视频</span>
                            </c:when>
                            <c:otherwise>
                                <span class="material-type">图片</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <div style="text-align: center;">
                            <span class="view-count">${ad.viewCount}</span>
                            <div class="view-count-label">
                                <c:choose>
                                    <c:when test="${ad.viewCount == 0}">暂无展示</c:when>
                                    <c:otherwise>次展示</c:otherwise>
                                </c:choose>
                            </div>
                            <c:if test="${ad.viewCount > 5000}">
                                <span class="hot-badge">🔥 爆款</span>
                            </c:if>
                            <c:if test="${ad.viewCount > 1000 && ad.viewCount <= 5000}">
                                <span class="popular-badge">⭐ 热门</span>
                            </c:if>
                        </div>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${ad.imageUrl.contains('videos/')}">
                                <!-- 视频预览 -->
                                <div class="preview-video" title="点击预览视频"
                                     onclick="showVideoPreview('${pageContext.request.contextPath}/${ad.imageUrl}')">
                                    ▶ 视频
                                </div>
                            </c:when>
                            <c:otherwise>
                                <!-- 图片预览 -->
                                <c:if test="${not empty ad.imageUrl}">
                                    <img src="${pageContext.request.contextPath}/${ad.imageUrl}"
                                         alt="${ad.title}"
                                         class="preview-img"
                                         title="点击查看大图"
                                         onclick="showImagePreview('${pageContext.request.contextPath}/${ad.imageUrl}', '${ad.title}')"
                                         onerror="this.style.display='none'; this.parentNode.innerHTML='<div class=\'no-preview\'>图片加载失败</div>';">
                                </c:if>
                                <c:if test="${empty ad.imageUrl}">
                                    <div class="no-preview">
                                        无预览
                                    </div>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <div class="action-links">
                            <a href="${pageContext.request.contextPath}/ads/manage/delete?id=${ad.id}"
                               onclick="return confirm('确定删除广告吗？\n标题: ${ad.title}')"
                               class="delete-btn">删除</a>

                            <c:if test="${ad.imageUrl.contains('videos/')}">
                                <a href="javascript:void(0)"
                                   onclick="showVideoPreview('${pageContext.request.contextPath}/${ad.imageUrl}')"
                                   class="play-btn">播放</a>
                            </c:if>

                            <c:if test="${not empty ad.linkUrl}">
                                <a href="${ad.linkUrl}" target="_blank" class="link-btn">跳转</a>
                            </c:if>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </c:otherwise>
    </c:choose>
    </tbody>
</table>

<!-- 图片预览模态框 -->
<div id="imageModal" class="video-modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3 id="imageTitle">图片预览</h3>
            <button class="close-btn" onclick="closeImageModal()">×</button>
        </div>
        <img id="modalImage" src="" alt="" style="width: 100%; max-height: 70vh; object-fit: contain;">
    </div>
</div>

<!-- 视频预览模态框 -->
<div id="videoModal" class="video-modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3>视频预览</h3>
            <button class="close-btn" onclick="closeVideoModal()">×</button>
        </div>
        <video id="previewVideo" controls class="modal-video">
            您的浏览器不支持视频播放
        </video>
    </div>
</div>

<script>
    // 图片预览功能
    function showImagePreview(imageUrl, title) {
        const modal = document.getElementById('imageModal');
        const modalImage = document.getElementById('modalImage');
        const imageTitle = document.getElementById('imageTitle');

        modalImage.src = imageUrl;
        imageTitle.textContent = title || '图片预览';
        modal.style.display = 'block';
    }

    function closeImageModal() {
        document.getElementById('imageModal').style.display = 'none';
    }

    // 视频预览功能
    function showVideoPreview(videoUrl) {
        const modal = document.getElementById('videoModal');
        const video = document.getElementById('previewVideo');

        video.src = videoUrl;
        modal.style.display = 'block';
        video.play().catch(e => console.log('视频自动播放失败:', e));
    }

    function closeVideoModal() {
        const video = document.getElementById('previewVideo');
        video.pause();
        video.currentTime = 0;
        document.getElementById('videoModal').style.display = 'none';
    }

    // 点击模态框背景关闭
    document.getElementById('imageModal').addEventListener('click', function(e) {
        if (e.target === this) {
            closeImageModal();
        }
    });

    document.getElementById('videoModal').addEventListener('click', function(e) {
        if (e.target === this) {
            closeVideoModal();
        }
    });

    // 页面加载完成后的处理
    window.addEventListener('DOMContentLoaded', function() {
        // 显示消息提示（如果有）
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.has('upload_success')) {
            alert('上传成功！');
            // 移除参数
            window.history.replaceState({}, document.title, window.location.pathname);
        }

        if (urlParams.has('msg')) {
            if (urlParams.get('msg') === 'deleted') {
                alert('删除成功！');
                window.history.replaceState({}, document.title, window.location.pathname);
            }
        }

        // 按ESC键关闭模态框
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                closeImageModal();
                closeVideoModal();
            }
        });
    });
</script>
</body>
</html>