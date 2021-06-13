package core.jdbc;

import core.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);


    public <T> List<T> executeListQuery(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        List<T> objects = new ArrayList<>();
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql);) {
            pss.setParameters(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    objects.add(rowMapper.mapRow(rs));
                }
            }
            return objects;
        } catch (SQLException e) {
            log.error("sqlException : ", e);
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
            log.error("sqlException : ", sqlException);
            throw new DataAccessException();
        }
    }

    /*public void insert(String sql, PreparedStatementSetter pstmtst) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmtst.setParameters(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException sqlException) {
            throw new DataAccessException();
        }
    }*/

    public void insert(String sql, PreparedStatementSetter pstmtst, KeyHolder keyHolder) {
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmtst.setParameters(pstmt);
            pstmt.executeUpdate();
            final ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                keyHolder.setLastedKey(generatedKeys.getLong(1));
            }
        } catch (SQLException sqlException) {
            log.error("sqlException : ", sqlException);
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