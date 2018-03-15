package io.code;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * mysql test data appliction
 *
 * @author songkejun
 * @create 2018-02-01 11:49
 **/
public class MysqlTestData {

    private static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager
                    .getConnection("jdbc:mySql://localhost/test?user=root&password=root");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /***
     *
     * @param conn
     */
    private static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("==========test insert start======");
        long start = System.currentTimeMillis();
        Connection conn = getConnection();
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            String sql = "insert into e2 (e1,e2) values('222aaaddd222','e3bbbbe3')";

            for (int i = 0; i < 1000; i++) {
                stmt.executeUpdate(sql);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                close(conn);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("==========test insert end======" + (end - start) / 1000);
    }
}
