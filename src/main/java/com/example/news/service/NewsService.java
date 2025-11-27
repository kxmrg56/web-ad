package com.example.news.service;

import com.example.news.model.News;
import java.util.*;
import java.util.stream.Collectors;

public class NewsService {
    private static List<News> mockNews = new ArrayList<>();

    static {
        // 初始化模拟数据
        mockNews.add(new News(1L, "人工智能技术新突破", "科学家在人工智能领域取得重大进展，为行业发展带来新机遇。",
                "详细内容：近日，国际研究团队在人工智能算法优化方面取得突破性进展，新的深度学习模型在图像识别和自然语言处理任务中表现出色。这项技术有望在医疗诊断、自动驾驶和智能客服等领域发挥重要作用，推动产业智能化转型。专家表示，这一突破将加速人工智能技术的商业化应用进程。", "tech",
                "/images/news/tech1.jpg", "科技前沿", new Date(), 234));

        mockNews.add(new News(2L, "国际政治领导人会晤", "多国元首举行高峰会谈，共商全球发展大计。",
                "详细内容：在复杂的国际形势下，各国领导人齐聚一堂，就全球经济复苏、气候变化、反恐合作等重大议题进行深入交流。会议期间，各方签署了多项合作协议，承诺加强多边主义，共同应对全球性挑战。此次会晤为国际关系注入了新的稳定性，展现了各国携手合作的决心。", "politics",
                "/images/news/politics1.jpg", "国际观察", new Date(), 189));

        mockNews.add(new News(3L, "全国运动会圆满落幕", "运动员们展现出色技艺，创造多项新纪录。",
                "详细内容：经过激烈角逐，本届运动会各项赛事结果揭晓。来自全国各地的优秀运动员在田径、游泳、体操等项目上展现了高超的竞技水平，共打破了8项全国纪录。组委会表示，本届运动会不仅促进了体育事业的发展，也为全民健身运动注入了新的活力。", "sports",
                "/images/news/sports1.jpg", "体育周刊", new Date(), 156));

        mockNews.add(new News(4L, "电影市场迎来黄金期", "多部优秀影片获得观众好评，票房成绩亮眼。",
                "详细内容：近期上映的几部电影在艺术和商业上都取得了成功。国产科幻片《星际征途》以精湛的特效和感人的故事情节赢得了观众青睐，累计票房突破30亿元。同时，多部文艺片也在国际电影节上获得奖项，展现了中国电影产业的多元化发展。", "entertainment",
                "/images/news/entertainment1.jpg", "影视快报", new Date(), 278));

        mockNews.add(new News(5L, "国防科技新成果展示", "我国自主研发的新型装备亮相国际防务展。",
                "详细内容：在最近的国际防务展览上，我国展示了多项军事科技新成果，包括新型无人机系统、智能指挥系统和先进雷达装备。这些装备在信息化、智能化方面达到了国际先进水平，展现了我国国防科技创新的强大实力，为维护国家安全提供了有力保障。", "military",
                "/images/news/military1.jpg", "国防时报", new Date(), 198));

        mockNews.add(new News(6L, "经济政策助力企业发展", "新一轮减税降费政策为企业发展注入活力。",
                "详细内容：政府出台系列经济政策，支持中小企业创新发展。通过降低增值税税率、扩大研发费用加计扣除范围等措施，预计全年将为市场主体减负超过万亿元。这些政策有效缓解了企业经营压力，激发了市场活力，为经济高质量发展奠定了坚实基础。", "economy",
                "/images/news/economy1.jpg", "经济观察", new Date(), 167));

        mockNews.add(new News(7L, "地方两会顺利召开", "各地人大代表齐聚一堂，审议政府工作报告。",
                "详细内容：在近期召开的地方两会上，代表们积极建言献策，围绕民生改善、经济发展、环境保护等议题展开热烈讨论。各地政府工作报告中明确提出了一系列惠民举措和发展目标，展现了推动地方经济社会发展的坚定决心和务实态度。", "politics",
                "/images/news/politics2.jpg", "政治周刊", new Date(), 145));

        mockNews.add(new News(8L, "外交政策新动向", "我国外交部就国际热点问题发表最新声明。",
                "详细内容：外交部发言人表示，我国将继续坚持和平发展道路，积极参与全球治理体系改革。在涉及核心利益问题上，我国将坚定维护国家主权和安全发展利益。同时，我国愿与各国一道，推动构建人类命运共同体，为世界和平与发展作出更大贡献。", "politics",
                "/images/news/politics3.jpg", "外交时报", new Date(), 132));

        mockNews.add(new News(9L, "军事演习圆满成功", "联合军事演习展示现代化作战能力。",
                "详细内容：近日举行的联合军事演习中，各兵种协同作战，展示了信息化条件下的联合作战能力。演习重点检验了新装备的性能和官兵的实战化训练水平，提升了部队应对多种安全威胁的能力，为维护地区和平稳定提供了有力支撑。", "military",
                "/images/news/military2.jpg", "军事观察", new Date(), 178));

        mockNews.add(new News(10L, "股市迎来新一轮上涨", "多重利好因素推动资本市场活跃度提升。",
                "详细内容：在政策利好的推动下，股市出现明显上涨行情。科技股、消费股表现尤为突出，多个板块轮番上涨。分析师认为，经济基本面的持续改善、流动性保持合理充裕以及市场信心的恢复是推动股市上涨的主要因素。投资者对后市持乐观态度。", "economy",
                "/images/news/economy2.jpg", "财经日报", new Date(), 223));

        mockNews.add(new News(11L, "足球联赛激烈进行中", "多支强队展现出色表现，冠军争夺日趋激烈。",
                "详细内容：本赛季足球联赛竞争异常激烈，多场比赛精彩纷呈。卫冕冠军在引进多名强援后实力进一步增强，而传统强队也展现出强劲的竞争力。年轻球员的出色表现成为联赛亮点，多位新星在关键比赛中发挥重要作用，为球队取胜立下汗马功劳。", "sports",
                "/images/news/sports2.jpg", "体育快报", new Date(), 167));

        mockNews.add(new News(12L, "5G技术应用广泛推进", "5G网络覆盖范围不断扩大，赋能各行业数字化转型。",
                "详细内容：5G技术正在各个领域发挥重要作用，推动产业升级。在智能制造领域，5G网络实现了生产设备的实时监控和精准控制；在医疗健康领域，远程手术和智能诊疗成为现实；在教育领域，沉浸式教学体验提升了学习效果。5G技术的深度应用正在改变人们的生活方式。", "tech",
                "/images/news/tech2.jpg", "通信技术", new Date(), 189));

        mockNews.add(new News(13L, "音乐节吸引数万乐迷", "知名音乐人齐聚一堂，为观众带来精彩演出。",
                "详细内容：近日举办的音乐节吸引了来自全国各地的乐迷，现场气氛热烈。多位知名歌手和乐队轮番登场，为观众奉献了一场视听盛宴。除了主舞台的精彩表演，音乐节还设置了多个特色区域，包括美食区、文创区和互动体验区，为乐迷提供了全方位的娱乐体验。", "entertainment",
                "/images/news/entertainment2.jpg", "娱乐周刊", new Date(), 156));
    }

    public List<News> getLatestNews(int limit) {
        return mockNews.subList(0, Math.min(limit, mockNews.size()));
    }

    public News getNewsById(Long id) {
        return mockNews.stream()
                .filter(news -> news.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<News> getNewsByCategory(String category) {
        List<News> result = new ArrayList<>();
        for (News news : mockNews) {
            if (news.getCategory().equals(category)) {
                result.add(news);
            }
        }
        return result;
    }

    public List<News> getAllNews() {
        return new ArrayList<>(mockNews);
    }

    public List<News> searchNews(String keyword) {
        String lowerKeyword = keyword.toLowerCase();

        return mockNews.stream()
                .filter(news ->
                        news.getTitle().toLowerCase().contains(lowerKeyword) ||
                                news.getContent().toLowerCase().contains(lowerKeyword) ||
                                news.getSummary().toLowerCase().contains(lowerKeyword) ||
                                news.getAuthor().toLowerCase().contains(lowerKeyword)
                )
                .collect(Collectors.toList());
    }

}

