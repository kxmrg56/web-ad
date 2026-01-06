<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>上传广告素材</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; background-color: #f4f7f6; }
        .upload-container { max-width: 600px; margin: 0 auto; background: white; padding: 25px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        h2 { color: #333; border-bottom: 2px solid #007bff; padding-bottom: 10px; }
        .nav-link { display: inline-block; margin-bottom: 20px; color: #007bff; text-decoration: none; }
        .form-group { margin-bottom: 20px; }
        .form-label { display: block; margin-bottom: 8px; font-weight: bold; color: #555; }
        .form-control {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .radio-group { margin: 10px 0; padding: 10px; background: #f9f9f9; border-radius: 4px; }
        .radio-label { margin-right: 20px; cursor: pointer; }
        .btn {
            width: 100%;
            padding: 12px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
        }
        .btn:hover { background-color: #0056b3; }
        .preview-box {
            border: 2px dashed #ddd;
            margin-top: 10px;
            padding: 10px;
            text-align: center;
            min-height: 150px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #fafafa;
        }
        .preview-img, .preview-video { max-width: 100%; max-height: 200px; border-radius: 4px; }
        .message { padding: 15px; margin-bottom: 20px; border-radius: 4px; display: none; }
        .success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .required::after { content: " *"; color: red; }
    </style>
</head>
<body>
<div class="upload-container">
    <h2>上传广告素材</h2>
    <a href="${pageContext.request.contextPath}/ads/manage/list" class="nav-link">← 返回管理后台</a>

    <div id="message" class="message"></div>
    <% if (request.getAttribute("error") != null) { %>
    <div class="message error" style="display:block;"><%= request.getAttribute("error") %></div>
    <% } %>

    <form id="uploadForm" action="${pageContext.request.contextPath}/ads/upload/doUpload"
          method="post" enctype="multipart/form-data">

        <div class="form-group">
            <label class="form-label required">素材类型:</label>
            <div class="radio-group">
                <label class="radio-label">
                    <input type="radio" name="materialType" value="image" checked
                           onclick="toggleOptions('image')"> 图片广告
                </label>
                <label class="radio-label">
                    <input type="radio" name="materialType" value="video"
                           onclick="toggleOptions('video')"> 视频广告
                </label>
            </div>
        </div>

        <div class="form-group">
            <label class="form-label required" for="title">广告标题:</label>
            <input type="text" id="title" name="title" class="form-control" required placeholder="例如：夏季清仓大促">
        </div>

        <div class="form-group">
            <label class="form-label" for="materialName">素材名称:</label>
            <input type="text" id="materialName" name="materialName" class="form-control" placeholder="内部识别名（可选）">
        </div>

        <div class="form-group">
            <label class="form-label required" for="category">广告分类:</label>
            <select id="category" name="category" class="form-control" required>
                <option value="">-- 请选择分类 --</option>
                <option value="体育">体育</option><option value="医疗">医疗</option>
                <option value="家居">家居</option><option value="教育">教育</option>
                <option value="数码">数码</option><option value="时尚">时尚</option>
                <option value="游戏">游戏</option><option value="美食">美食</option>
                <option value="金融">金融</option>
            </select>
        </div>

        <div class="form-group">
            <label class="form-label" for="targetUrl">落地页链接 (Target URL):</label>
            <input type="url" id="targetUrl" name="targetUrl" class="form-control" placeholder="https://example.com">
        </div>

        <div id="imageOptions" class="form-group">
            <label class="form-label required" for="imageFile">选择图片:</label>
            <input type="file" id="imageFile" name="imageFile" class="form-control"
                   accept="image/*" onchange="previewFile(this, 'image')">
            <div id="imagePreview" class="preview-box">
                <span style="color:#999">支持 JPG, PNG, GIF</span>
            </div>
        </div>

        <div id="videoOptions" class="form-group" style="display: none;">
            <label class="form-label required" for="videoFile">选择视频:</label>
            <input type="file" id="videoFile" name="videoFile" class="form-control"
                   accept="video/*" onchange="previewFile(this, 'video')">
            <div id="videoPreview" class="preview-box">
                <span style="color:#999">支持 MP4, AVI 等</span>
            </div>

            <label class="form-label" for="thumbnail" style="margin-top:15px;">视频封面图 (可选):</label>
            <input type="file" id="thumbnail" name="thumbnail" class="form-control" accept="image/*">
        </div>

        <button type="submit" class="btn">立即发布素材</button>
    </form>
</div>

<script>
    // 切换图片/视频视图
    function toggleOptions(type) {
        const isImage = (type === 'image');
        document.getElementById('imageOptions').style.display = isImage ? 'block' : 'none';
        document.getElementById('videoOptions').style.display = isImage ? 'none' : 'block';

        // 动态切换必填项，防止提交冲突
        document.getElementById('imageFile').required = isImage;
        document.getElementById('videoFile').required = !isImage;
    }

    // 通用预览函数
    function previewFile(input, type) {
        const previewId = type === 'image' ? 'imagePreview' : 'videoPreview';
        const preview = document.getElementById(previewId);

        if (input.files && input.files[0]) {
            const file = input.files[0];
            const reader = new FileReader();

            if (type === 'image') {
                reader.onload = e => {
                    preview.innerHTML = `<img src="${e.target.result}" class="preview-img">`;
                };
                reader.readAsDataURL(file);
            } else {
                const videoUrl = URL.createObjectURL(file);
                preview.innerHTML = `<video src="${videoUrl}" class="preview-video" controls></video>`;
            }
        }
    }

    // 表单提交前校验
    document.getElementById('uploadForm').onsubmit = function() {
        showMessage('文件正在上传中，请勿刷新页面...', 'success');
        return true;
    };

    function showMessage(text, type) {
        const messageDiv = document.getElementById('message');
        messageDiv.textContent = text;
        messageDiv.className = 'message ' + type;
        messageDiv.style.display = 'block';
    }

    // 初始化状态与数据恢复
    window.onload = function() {
        toggleOptions('image');

        // 上传成功处理
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.has('upload_success')) {
            showMessage('恭喜！素材已成功上传并存入数据库。', 'success');
            localStorage.removeItem('uploadFormData');
        }

        // 自动保存草稿逻辑
        const inputs = document.querySelectorAll('input[type="text"], select, input[type="url"]');
        inputs.forEach(input => {
            input.onchange = () => {
                const data = {
                    title: document.getElementById('title').value,
                    category: document.getElementById('category').value,
                    targetUrl: document.getElementById('targetUrl').value
                };
                localStorage.setItem('uploadFormData', JSON.stringify(data));
            };
        });

        // 恢复草稿
        const saved = localStorage.getItem('uploadFormData');
        if (saved) {
            const data = JSON.parse(saved);
            if(data.title) document.getElementById('title').value = data.title;
            if(data.category) document.getElementById('category').value = data.category;
            if(data.targetUrl) document.getElementById('targetUrl').value = data.targetUrl;
        }
    };
</script>
</body>
</html>