package next.dao;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SelectJdbcTemplate {

    public List<?> query(String str) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List objects = new ArrayList<>();
        try {
            con = ConnectionManager.getConnection();
            String sql = createSelectQuery();

            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                objects.add(mapRow(rs));
            }
            return objects;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new IllegalArgumentException("잘못된 sql입니다.");
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public Object queryForObject(String str) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = createSelectQuery();
            pstmt = con.prepareStatement(sql);
            setValues(pstmt);

            rs = pstmt.executeQuery();

            Object user = null;
            if (rs.next()) {
                user = mapRow(rs);
            }

            return user;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new IllegalArgumentException("잘못된 sql입니다.");
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public void setValues(PreparedStatement pstmt) throws SQLException {
        //pstmt.setString(1, userId);
    }

    public Object mapRow(ResultSet rs) throws SQLException {
        return null;
    }

    public String createSelectQuery() {
        //SELECT userId, password, name, email FROM USERS WHERE userid=?
        return "";
    }

}