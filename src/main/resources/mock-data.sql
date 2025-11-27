-- 模拟数据SQL（为后续连接数据库准备）
CREATE DATABASE IF NOT EXISTS news_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE news_db;

-- 新闻表
CREATE TABLE news (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      title VARCHAR(255) NOT NULL,
                      summary TEXT,
                      content TEXT,
                      category VARCHAR(50) NOT NULL,
                      image_url VARCHAR(500),
                      author VARCHAR(100),
                      publish_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                      view_count INT DEFAULT 0,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 分类表
CREATE TABLE categories (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(50) NOT NULL,
                            code VARCHAR(50) NOT NULL UNIQUE,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 插入分类数据
INSERT INTO categories (name, code) VALUES
                                        ('政治', 'politics'),
                                        ('军事', 'military'),
                                        ('经济', 'economy'),
                                        ('体育', 'sports'),
                                        ('科技', 'tech'),
                                        ('娱乐', 'entertainment');

-- 插入新闻模拟数据
INSERT INTO news (title, summary, content, category, image_url, author, view_count) VALUES
                                                                                        ('人工智能技术新突破', '科学家在人工智能领域取得重大进展，为行业发展带来新机遇。', '详细内容：近日，国际研究团队在人工智能算法优化方面取得突破性进展...', 'tech', '/images/news/tech1.jpg', '科技前沿', 234),
                                                                                        ('国际政治领导人会晤', '多国元首举行高峰会谈，共商全球发展大计。', '详细内容：在复杂的国际形势下，各国领导人齐聚一堂...', 'politics', '/images/news/politics1.jpg', '国际观察', 189),
                                                                                        ('全国运动会圆满落幕', '运动员们展现出色技艺，创造多项新纪录。', '详细内容：经过激烈角逐，本届运动会各项赛事结果揭晓...', 'sports', '/images/news/sports1.jpg', '体育周刊', 156),
                                                                                        ('电影市场迎来黄金期', '多部优秀影片获得观众好评，票房成绩亮眼。', '详细内容：近期上映的几部电影在艺术和商业上都取得了成功...', 'entertainment', '/images/news/entertainment1.jpg', '影视快报', 278),
                                                                                        ('国防科技新成果展示', '自主研发装备性能卓越，展现国家科技实力。', '详细内容：在最新军事展览中，多项国产装备首次公开亮相...', 'military', '/images/news/military1.jpg', '国防时报', 198),
                                                                                        ('经济政策助力企业发展', '减税降费措施见效，市场主体活力增强。', '详细内容：一系列经济刺激政策的实施效果逐渐显现...', 'economy', '/images/news/economy1.jpg', '经济观察', 167);