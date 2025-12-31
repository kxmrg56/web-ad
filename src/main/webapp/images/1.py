import os

# 获取当前脚本所在目录
current_dir = os.path.dirname(os.path.abspath(__file__))

# 读取所有 jpg 文件
jpg_files = [f for f in os.listdir(current_dir) if f.lower().endswith('.jpg')]

# 输出文件名
for name in jpg_files:
    print(name)

print("共读取到图片数量：", len(jpg_files))
