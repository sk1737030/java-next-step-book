package core.exception;

public class DataAccessException extends RuntimeException {
    public DataAccessException() {
        super("데이터 생성을 잘못하였습니다.");
    }


    public DataAccessException(Throwable cause) {
        super(cause);
    }

    public DataAccessException(String message) {
        super(message);
    }
}
