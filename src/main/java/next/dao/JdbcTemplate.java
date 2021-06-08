package next.dao;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class JdbcTemplate {

    public List<?> query(String sql) {
        List<Object> objects = new ArrayList<>();
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                objects.add(mapRow(rs));
            }
            return objects;
        } catch (SQLException throwables) {
            throw new IllegalArgumentException("잘못된 sql입니다.");
        }
    }

    public Object executeQuery(String sql) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            setParameters(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                Object user = null;
                if (rs.next()) {
                    user = mapRow(rs);
                }
                return user;
            }

        } catch (SQLException sqlException) {
            throw new IllegalArgumentException("잘못된 sql입니다.");
        }
    }

    public int update(String sql) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            setParameters(pstmt);
            return pstmt.executeUpdate();
        } catch (SQLException sqlException) {
            throw new IllegalArgumentException("잘못된 SQL입니다.");
        }
    }

    public void insert(String sql) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            setParameters(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException sqlException) {
            throw new IllegalArgumentException("잘못된 SQL입니다.");
        }
    }


    public abstract void setParameters(PreparedStatement pstmt) throws SQLException;

    public abstract Object mapRow(ResultSet rs) throws SQLException;

}