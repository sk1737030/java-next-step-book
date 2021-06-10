package core.jdbc;

import core.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class JdbcTemplate {
    public <T> List<T> executeListQuery(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        List<T> objects = new ArrayList<>();
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql);) {
            try (ResultSet rs = pstmt.executeQuery()) {
                pss.setParameters(pstmt);
                while (rs.next()) {
                    objects.add(rowMapper.mapRow(rs));
                }
            }
            return objects;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> List<T> executeListQuery(String sql, RowMapper<T> rowMapper, Object... parameters) {
        return executeListQuery(sql, rowMapper, createPreparedStatementSetter(parameters));
    }

    public <T> T executeQuery(String sql, RowMapper<T> rowMapper, Object... parameters) {
        final List<T> list = executeListQuery(sql, rowMapper, parameters);

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
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

    private PreparedStatementSetter createPreparedStatementSetter(Object... parameters) {
        return pstmt -> {
            for (int i = 0; i < parameters.length; i++) {
                pstmt.setObject(i + 1, parameters[i]);
            }
        };
    }
}