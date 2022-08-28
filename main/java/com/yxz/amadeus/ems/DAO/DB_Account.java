package com.yxz.amadeus.ems.DAO;

import android.util.Log;

import com.yxz.amadeus.ems.entity.Account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DB_Account {
    private String ip = "192.168.1.101";

    private static Connection getSQLConnection() {
        String ip = DB_IP.ip;
        String user = "sa";
        String pwd = "admin123";
        String db = "db_ems";
        Connection con = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            /**SqlServer编码是GBK*/
            con = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":1433/" + db + ";charset=GBK", user, pwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
    public static ArrayList<Account> userLogin(String userID,String password) {
        String sqlString = "select * from Account where userID ='"+userID+"' and password='"+password+"'";
        try {
            ArrayList<Account> list = new ArrayList<Account>();
            //连接数据库
            //使用预处理进行查询
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement(sqlString);
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String type = rSet.getString("type");
                String id = rSet.getString("userID");
                String name = rSet.getString("username");//用户名
                String pwd = rSet.getString("password");
                String grade = rSet.getString("grade");//
                String dept = rSet.getString("dept");//
                String tele = rSet.getString("telephone");
                Account user = new Account(type, id, pwd, name, grade, dept, tele);
                //向list中添加用户
                list.add(user);
            }
            return list;
        } catch (Exception e) {
            // TODO: handle exception
            //打印堆栈信息
            e.printStackTrace();
        }
        return null;
    }


    //查询所有用户的信息
    public static ArrayList<Account> findAccount(String typeC) {
        String sqlString = "select * from Account";
        if (typeC.equals("C")){
            sqlString=sqlString.concat(" where type = 'A' or type = 'B'");
        }
        ArrayList<Account> list = new ArrayList<Account>();
        try {
            //连接数据库
            //使用预处理进行查询
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement(sqlString);
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String type = rSet.getString("type");
                String id = rSet.getString("userID");
                String name = rSet.getString("username");//用户名
                String pwd = rSet.getString("password");
//                pwdString="****";type, id,name, pwd, grade,dept,tele
                String grade = rSet.getString("grade");//
                String dept = rSet.getString("dept");//
                String tele = rSet.getString("telephone");
                Account user = new Account(type, id, pwd, name, grade, dept, tele);
                //向list中添加用户
                list.add(user);
            }
            return list;
        } catch (Exception e) {
            // TODO: handle exception
            //打印堆栈信息
            e.printStackTrace();
        }
        return null;
    }

    //增加用户
    public static boolean addAccount(Account user) {
        String sqlString = "insert into account values (?,?,?,?,?,?,?)";
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, user.getType());
            preparedStatement.setString(2, user.getUserID());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getUsername());
            preparedStatement.setString(5, user.getGrade());
            preparedStatement.setString(6, user.getDept());
            preparedStatement.setString(7, user.getTelephone());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //删除用户
    public static boolean deleteAccount(String idString) {
        String sqlString = "delete from account where userID=?";
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, idString);
            preparedStatement.executeUpdate();
            System.out.println("删除成功");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }
    //根据id和用户姓名模糊查找用户
    public static ArrayList<Account> findBy(String idString) {
        String sqlString = "select * from account where userID like ? or username like ?";
        ArrayList<Account> list = new ArrayList<Account>();
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, "%"+idString+"%");
            preparedStatement.setString(2, "%"+idString+"%");
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String type = rSet.getString("type");
                String id = rSet.getString("userID");
                String name = rSet.getString("username");//用户名
                String pwd = rSet.getString("password");
                String grade = rSet.getString("grade");//
                String dept = rSet.getString("dept");//
                String tele = rSet.getString("telephone");
                list.add( new Account(type, id, pwd, name, grade, dept, tele));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return null;
    }
    public static ArrayList<Account> findById(String idString) {
        String sqlString = "select * from account where userID = ?";
        ArrayList<Account> list = new ArrayList<Account>();
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, idString);
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String type = rSet.getString("type");
                String id = rSet.getString("userID");
                String name = rSet.getString("username");//用户名
                String pwd = rSet.getString("password");
                String grade = rSet.getString("grade");//
                String dept = rSet.getString("dept");//
                String tele = rSet.getString("telephone");
                list.add( new Account(type, id, pwd, name, grade, dept, tele));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return null;
    }

    //修改用户信息
    public static boolean modifyAccount(Account user,String userID) {
        String sqlString = "update account set type=?,userID=?,password=?,username=?,grade=?,dept=?,telephone=? where userID=?";
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, user.getType());
            preparedStatement.setString(2, user.getUserID());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getUsername());
            preparedStatement.setString(5, user.getGrade());
            preparedStatement.setString(6, user.getDept());
            preparedStatement.setString(7, user.getTelephone());
            preparedStatement.setString(8,userID);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String QuerySQL() {
        String result = "";
        try {
            String sql = "select * from Account";
            Statement stmt = null;
            stmt = getSQLConnection().createStatement();//
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String s1 = rs.getString("userID");
                String s2 = rs.getString("password");
                result += "用户名：" + s1 + "  密码：" + s2 + "\n";
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            result += "查询数据异常!" + e.getMessage();
        }
        return result;
    }

//    public static void main(String[] args) {
//        QuerySQL();
//    }
}