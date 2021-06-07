package next.dao;

import core.jdbc.ConnectionManager;
import next.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {
    public void insert(User user) throws SQLException {
        JdbcTemplate insertJdbcTemplate = new JdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());

            }

            @Override
            public User mapRow(ResultSet rs) throws SQLException {
                return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
        };

        insertJdbcTemplate.insert("INSERT INTO USERS VALUES (?, ?, ?, ?)");
    }


    public int update(User user) throws SQLException {
        JdbcTemplate update = new JdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
                pstmt.setString(5, user.getUserId());
            }

            @Override
            public Object mapRow(ResultSet rs) throws SQLException {
                return null;
            }
        };
        return update.update("UPDATE USERS SET userId = ?, password = ?, name = ?, email = ? WHERE userid=?");
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

    public List<User> findAll() throws SQLException {
        JdbcTemplate JdbcTemplate = new JdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement pstmt) throws SQLException {

            }

            @Override
            public User mapRow(ResultSet rs) throws SQLException {
                return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }

        };
        return (List<User>) JdbcTemplate.query("SELECT userId, password, name, email FROM USERS");
    }

    public User findByUserId(String userId) throws SQLException {
        JdbcTemplate JdbcTemplate = new JdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, userId);
            }

            @Override
            public User mapRow(ResultSet rs) throws SQLException {
                return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
        };

        return (User) JdbcTemplate.executeQuery("SELECT userId, password, name, email FROM USERS WHERE userid=?");
    }


}


