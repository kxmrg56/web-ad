package com.video.model;

public class AdConfig {
    private int id;
    private int videoId;
    private String adFilePath;
    private int insertTimeSec;

    // 此处省略 Getter/Setter，请利用IDE自动生成
    // Alt+Insert -> Getter and Setter
    public String getAdFilePath() { return adFilePath; }
    public void setAdFilePath(String path) { this.adFilePath = path; }
    public int getInsertTimeSec() { return insertTimeSec; }
    public void setInsertTimeSec(int time) { this.insertTimeSec = time; }
}