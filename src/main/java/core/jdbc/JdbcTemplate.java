package core.jdbc;

import core.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class JdbcTemplate {
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        List<T> objects = new ArrayList<>();
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                objects.add(rowMapper.mapRow(rs));
            }
            return objects;
        } catch (SQLException throwables) {
            throw new DataAccessException();
        }
    }

    public <T> T executeQuery(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmtst) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmtst.setParameters(pstmt);

            try (ResultSet rs = pstmt.executeQuery()) {
                T object = null;
                if (rs.next()) {
                    object = rowMapper.mapRow(rs);
                }
                return object;
            }

        } catch (SQLException sqlException) {
            throw new DataAccessException();
        }
    }

    public int update(String sql, PreparedStatementSetter pstmtst) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmtst.setParameters(pstmt);

            return pstmt.executeUpdate();
        } catch (SQLException sqlException) {
            throw new DataAccessException();
        }
    }

    public void insert(String sql, PreparedStatementSetter pstmtst) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmtst.setParameters(pstmt);

            pstmt.executeUpdate();
        } catch (SQLException sqlException) {
            throw new DataAccessException();
        }
    }
}