package Dbutils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Dbutils {
    //根据指定数据表名称查询所有记录(返回一个集合)
    public ArrayList getList(Object ob) {
        ArrayList list = new ArrayList();
        Connection conn = C3P0Util.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Class cls = ob.getClass();
//要求:数据表中的字段必须与类中的属性一一对应
        Field[] fi = cls.getDeclaredFields();//获取类中的所有的属性
        String sql = "select * from " + cls.getSimpleName();//获取类名(类名必须与数据表一致)
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Object obj = cls.newInstance();//创建指定类的实例化对象
                for (Field f : fi) {
                    f.setAccessible(true);//表示可以访问类中的私有属性
                    f.set(obj, rs.getObject(f.getName()));//调用类中指定属性的set方法赋值
                }
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            C3P0Util.release(conn, pstmt, rs);
        }
        return list;
    }

    //根据数据表中的主键获取到指定数据表对应的一条记录(对应一个对象)
    public Object getObjectById(Class cl, int id) {
        Connection conn = C3P0Util.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Object obj = null;
        Field[] fi = cl.getDeclaredFields();//获取类中的所有的属性
        String sql = "select * from " + cl.getSimpleName() + " where " + fi[0].getName() + " = " + id;//fi[0].getName()获取数据表中第一列字段
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                obj = cl.newInstance();//创建指定类的实例化对象
                for (Field f : fi) {
                    f.setAccessible(true);//表示可以访问类中的私有属性
                    f.set(obj, rs.getObject(f.getName()));//调用类中指定属性的set方法赋值
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            C3P0Util.release(conn, pstmt, rs);
        }
        return obj;
    }

    //根据数据表中的一个字段和对应的值查询
    public ArrayList getListBySome(Class cl, String name, Object value) {


        ArrayList list = new ArrayList();
        Connection conn = C3P0Util.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Field[] fi = cl.getDeclaredFields();//获取类中的所有的属性
        String sql = "select * from " + cl.getSimpleName() + " where " + name + " = '" + value + "'";
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Object obj = cl.newInstance();
                for (Field f : fi) {
                    f.setAccessible(true);//表示可以访问类中的私有属性
                    f.set(obj, rs.getObject(f.getName()));//调用类中指定属性的set方法赋值
                }
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            C3P0Util.release(conn, pstmt, rs);
        }
        return list;
    }

    //编写万能的DAO添加方法
    public boolean insert(Object obj) {
        boolean flag = false;
        Connection conn = C3P0Util.getConnection();
        PreparedStatement pstmt = null;
        Class clz = obj.getClass();
        Field[] fi = clz.getDeclaredFields();//获取类中的所有的属性
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ");
        sb.append(clz.getSimpleName());
        sb.append("(");
//主键为第一列，是自增的，有默认值，不需要添加
        for (int i = 1; i < fi.length; i++) {
            sb.append(fi[i].getName());//获取列名
            if (i != fi.length - 1)//最后一列不用加逗号
                sb.append(",");
        }
        sb.append(") values(");
        for (int i = 1; i < fi.length; i++) {
            sb.append("?");
            if (i != fi.length - 1)//最后一列不用加逗号
                sb.append(",");
        }
        sb.append(")");
        String sql = sb.toString();
        System.out.println(sql);
        try {
            pstmt = conn.prepareStatement(sql);
            for (int i = 1; i < fi.length; i++) {
                fi[i].setAccessible(true);
                pstmt.setObject(i, fi[i].get(obj));//第一列为主键，不用添加(对应数组下标是0)
            }
            int row = pstmt.executeUpdate();
            if (row > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            C3P0Util.release(conn, pstmt);
        }
        return flag;
    }

    //万能的更新方法
    public boolean update(Object obj) {
        boolean flag = false;
        Connection conn = C3P0Util.getConnection();
        PreparedStatement pstmt = null;
        Class clz = obj.getClass();
        Field[] fi = clz.getDeclaredFields();//获取类中的所有的属性
        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(clz.getSimpleName());
        sb.append(" set ");
        for (int i = 1; i < fi.length; i++) {
            sb.append(fi[i].getName());//获取列名
            sb.append(" =? ");
            if (i != fi.length - 1)//最后一列不用加逗号
                sb.append(",");
        }
        sb.append(" where ");
        sb.append(fi[0].getName());
        sb.append(" =? ");
        try {
            pstmt = conn.prepareStatement(sb.toString());
            for (int i = 1; i < fi.length; i++) {
                fi[i].setAccessible(true);
                pstmt.setObject(i, fi[i].get(obj));
            }
            fi[0].setAccessible(true);
            pstmt.setObject(fi.length, fi[0].get(obj));//主键是第一列
            int row = pstmt.executeUpdate();
            if (row > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            C3P0Util.release(conn, pstmt);
        }
        return flag;
    }

    //万能的删除方法
    public boolean delete(Class clz, int id) {
        boolean flag = false;
        Connection conn = C3P0Util.getConnection();
        PreparedStatement pstmt = null;
        Field[] fi = clz.getDeclaredFields();//获取类中的所有的属性
        String sql = "delete from " + clz.getSimpleName() + " where " + fi[0].getName() + "=?";
        System.out.println(sql);
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, id);
            int row = pstmt.executeUpdate();
            if (row > 0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            C3P0Util.release(conn, pstmt);
        }
        return flag;
    }

}



