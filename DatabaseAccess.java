
package com.jplus.database;

import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.sql.Statement;
        import java.util.logging.Level;
        import java.util.logging.Logger;

/**
 * 数据库基本操作类
 *
 * @author
 */
public class DatabaseAccess {

    private static final String url = "jdbc:mysql://localhost/mysql";
    private static final String driver = "com.mysql.jdbc.Driver";
    private Connection conn = null;
    private Statement stm = null;
    private ResultSet rs = null;

    /**
     * 构造方法 开启数据库连接
     */
    public DatabaseAccess() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, "root", "admin");
            stm = conn.createStatement();
        } catch (Exception Exception) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, Exception);
        }
        System.out.println(stm);
    }

    /**
     * 数据库查询
     *
     * @param sql sql语句
     * @return 查询集合
     */
    public ResultSet query(String sql) {
        try {
            rs = stm.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    /**
     * 数据库更新操作
     *
     * @param sql sql语句
     * @return 是否成功
     */
    public boolean update(String sql) {
        boolean b = false;
        try {
            stm.execute(sql);
            b = true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return b;
    }

    /**
     * 判断查询集合是否为空
     *
     * @return 如果为空false有数据库返回true
     */
    public boolean isResultSetNull() {
        try {
            if (rs != null && rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * 关闭数据库连接
     */
    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args){
        DatabaseAccess dat=new DatabaseAccess();

    }
}