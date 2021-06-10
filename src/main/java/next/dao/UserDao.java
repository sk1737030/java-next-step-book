package next.dao;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;
import core.jdbc.PreparedStatementSetter;
import core.jdbc.RowMapper;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UserDao {
    public void insert(User user) {
        final PreparedStatementSetter preparedStatementSetter = pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        };

        new JdbcTemplate().insert("INSERT INTO USERS VALUES (?, ?, ?, ?)", preparedStatementSetter);
    }


    public int update(User user) {
        final PreparedStatementSetter preparedStatementSetter = pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getUserId());
        };

        return new JdbcTemplate().update("UPDATE USERS SET userId = ?, password = ?, name = ?, email = ? WHERE userid=?", preparedStatementSetter);
    }


    public void delete(String userId) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = "DELETE FROM USERS WHERE userId = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);

            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }

            if (con != null) {
                con.close();
            }
        }
    }

    public void deleteAll() throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = "DELETE FROM USERS";
            pstmt = con.prepareStatement(sql);

            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }

            if (con != null) {
                con.close();
            }
        }
    }

    public List<User> findAll() {
        final RowMapper<User> userRowMapper = rs ->
                new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));

        return new JdbcTemplate().executeListQuery("SELECT userId, password, name, email FROM USERS", userRowMapper);
    }

    public User findByUserId(String userId) {
        final RowMapper<User> userRowMapper = rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                rs.getString("email"));

        return new JdbcTemplate().executeQuery("SELECT userId, password, name, email FROM USERS WHERE userid=?",
                userRowMapper, userId);
    }
}