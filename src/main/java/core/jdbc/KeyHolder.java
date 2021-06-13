package core.jdbc;

public class KeyHolder {
    private long rowKey;


    public long getRowKey() {
        return rowKey;
    }

    public void setLastedKey(long aLong) {
        this.rowKey = aLong;
    }
}
