package com.video.dao;
import com.video.model.Video;
import com.video.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideoDao {
    public List<Video> getAllVideos() {
        return queryVideos("SELECT * FROM videos ORDER BY id DESC");
    }

    public List<Video> getVideosByCategory(String category) {
        List<Video> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM videos WHERE category = ? ORDER BY id DESC")) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public Video getVideoById(int id) {
        Video v = null;
        String sql = "SELECT v.*, a.ad_file_path, a.insert_time_sec FROM videos v LEFT JOIN ad_configs a ON v.id = a.video_id WHERE v.id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    v = mapRow(rs);
                    if (rs.getString("ad_file_path") != null) {
                        v.setHasAd(true);
                        v.setAdFilePath(rs.getString("ad_file_path"));
                        v.setAdInsertTime(rs.getInt("insert_time_sec"));
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return v;
    }

    public void addVideo(Video video) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO videos(title, description, file_path, category) VALUES(?,?,?,?)")) {
            ps.setString(1, video.getTitle());
            ps.setString(2, video.getDescription());
            ps.setString(3, video.getFilePath());
            ps.setString(4, video.getCategory());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void addAdConfig(int videoId, String adPath, int time) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO ad_configs(video_id, ad_file_path, insert_time_sec) VALUES(?,?,?)")) {
            ps.setInt(1, videoId);
            ps.setString(2, adPath);
            ps.setInt(3, time);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private List<Video> queryVideos(String sql) {
        List<Video> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    private Video mapRow(ResultSet rs) throws SQLException {
        Video v = new Video();
        v.setId(rs.getInt("id"));
        v.setTitle(rs.getString("title"));
        v.setDescription(rs.getString("description"));
        v.setFilePath(rs.getString("file_path"));
        v.setCategory(rs.getString("category"));
        return v;
    }
}