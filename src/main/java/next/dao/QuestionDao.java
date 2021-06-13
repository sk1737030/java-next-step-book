package next.dao;

import core.jdbc.*;
import next.model.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class QuestionDao {
    public long insert(Question question) {
        final PreparedStatementSetter preparedStatementSetter = pstmt -> {
            pstmt.setString(1, question.getWriter());
            pstmt.setString(2, question.getTitle());
            pstmt.setString(3, question.getContents());
        };

        final KeyHolder keyHolder = new KeyHolder();
        new JdbcTemplate().insert("INSERT INTO QUESTIONS(writer,title,contents,createdDate,countOfAnswer) " +
                "VALUES (?, ?, ?, sysdate(), 0)", preparedStatementSetter, keyHolder);
        return keyHolder.getRowKey();
    }


    public void deleteAll() throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = "DELETE FROM QUESTIONS";
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

    public List<Question> findAll() {
        final RowMapper<Question> questionRowMapper = rs ->
                new Question(rs.getString("writer"), rs.getString("title"), rs.getString("contents"));

        return new JdbcTemplate().executeListQuery("SELECT questionId,userId, password, name, email FROM QUESTIONS", questionRowMapper);
    }

    public Question findByQuestionId(String questionId) {
        final RowMapper<Question> questionRowMapper = rs ->
                new Question(rs.getLong("questionId"), rs.getString("writer"), rs.getString("title"),
                        rs.getString("contents"), rs.getTimestamp("createdDate"), rs.getInt("countOfAnswer"));

        return new JdbcTemplate().executeQuery("SELECT questionId,writer,title,contents,createdDate,countOfAnswer FROM QUESTIONS WHERE questionId=?",
                questionRowMapper, questionId);
    }

    public void delete(String userId) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = "DELETE FROM QUESTIONS WHERE questionId = ?";
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


    public int update(Question question) {
        final PreparedStatementSetter preparedStatementSetter = pstmt -> {
            pstmt.setString(1, question.getTitle());
            pstmt.setString(2, question.getContents());
            pstmt.setLong(3, question.getQuestionId());
        };

        return new JdbcTemplate().update("UPDATE QUESTIONS SET title = ?, contents = ? WHERE questionId = ?", preparedStatementSetter);
    }


}