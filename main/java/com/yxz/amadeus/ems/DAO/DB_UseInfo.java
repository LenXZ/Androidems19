package com.yxz.amadeus.ems.DAO;

import android.util.Log;

import com.yxz.amadeus.ems.entity.UseInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DB_UseInfo {
    private String ip = "192.168.1.104";

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
    //查询所有设备的信息
    public static ArrayList<UseInfo> findUseInfo(String identity) {
        String sqlString = "select infoID, deviceID,deviceName, useDate, table2.userID, table2.userName, statusBefore, statusAfter, remarks,adopt,principal ,account.username principalName from (select infoID, deviceID,deviceName, useDate, table1.userID, userName, statusBefore, statusAfter, remarks,adopt,principal from (select infoID, useInfo.deviceID,deviceName, useDate, useInfo.userID, statusBefore, statusAfter, remarks,adopt, device.userID principal from useInfo left join device on useInfo.deviceID=device.deviceID ) as table1 left join account on table1.userID=account.userID ) as table2 left join account on table2.principal=account.userID where table2.adopt=?";
        ArrayList<UseInfo> list = new ArrayList<UseInfo>();
        try {
            //使用预处理进行查询
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement(sqlString);
            if(identity.equals("A")){
                preparedStatement.setString(1, "Y");
            }else {
                preparedStatement.setString(1, "N");
            }
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String infoID = rSet.getString("infoID");
                String deviceID = rSet.getString("deviceID");
                String deviceName=rSet.getString("deviceName");
                String useDate = rSet.getString("useDate");
                String statusBefore = rSet.getString("statusBefore");
                String statusAfter = rSet.getString("statusAfter");
                String remarks = rSet.getString("remarks");
                String userID = rSet.getString("userID");
                String userName = rSet.getString("username");
                String adopt=rSet.getString("adopt");
                String principal=rSet.getString("principal");
                String principalName=rSet.getString("principalName");
                UseInfo useInfo = new UseInfo(infoID, deviceID,deviceName, useDate, userID, userName, statusBefore, statusAfter, remarks,adopt,principal,principalName);
                list.add(useInfo);
            }
            return list;
        } catch (Exception e) {
            // TODO: handle exception
            //打印堆栈信息
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<UseInfo> findUseInfoByID(String user,String s_adopt) {
        String sqlString = "select infoID, deviceID,deviceName, useDate, table2.userID, table2.userName, statusBefore, statusAfter, remarks,adopt,principal ,account.username principalName from (select infoID, deviceID,deviceName, useDate, table1.userID, userName, statusBefore, statusAfter, remarks,adopt,principal from (select infoID, useInfo.deviceID,deviceName, useDate, useInfo.userID, statusBefore, statusAfter, remarks,adopt, device.userID principal from useInfo left join device on useInfo.deviceID=device.deviceID ) as table1 left join account on table1.userID=account.userID ) as table2 left join account on table2.principal=account.userID where table2.principal=? and table2.adopt=?";
        ArrayList<UseInfo> list = new ArrayList<UseInfo>();
        try {
            //连接数据库
            //使用预处理进行查询
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement(sqlString);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, s_adopt);
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String infoID = rSet.getString("infoID");
                String deviceID = rSet.getString("deviceID");
                String deviceName=rSet.getString("deviceName");
                String useDate = rSet.getString("useDate");
                String statusBefore = rSet.getString("statusBefore");
                String statusAfter = rSet.getString("statusAfter");
                String remarks = rSet.getString("remarks");
                String userID = rSet.getString("userID");
                String userName = rSet.getString("username");
                String adopt=rSet.getString("adopt");
                String principal=rSet.getString("principal");
                String principalName=rSet.getString("principalName");
                UseInfo useInfo = new UseInfo(infoID, deviceID,deviceName, useDate, userID, userName, statusBefore, statusAfter, remarks,adopt,principal,principalName);
                list.add(useInfo);
            }
            return list;
        } catch (Exception e) {
            // TODO: handle exception
            //打印堆栈信息
            e.printStackTrace();
        }
        return null;
    }
    //增加设备
    public static boolean addUseInfo(UseInfo useInfo) {
        String sqlString = "insert into useInfo values (?,?,?,?,?,?,?,?)";
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, useInfo.getInfoID());
            preparedStatement.setString(2, useInfo.getDeviceID());
            preparedStatement.setString(3, useInfo.getUseDate());
            preparedStatement.setString(4, useInfo.getUserID());
            preparedStatement.setString(5, useInfo.getStatusBefore());
            preparedStatement.setString(6, useInfo.getStatusAfter());
            preparedStatement.setString(7, useInfo.getRemarks());
            preparedStatement.setString(8, useInfo.getAdopt());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //删除设备
    public static boolean deleteUseInfo(String idString) {
        String sqlString = "delete from useInfo where infoID=?";
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
    public static ArrayList<UseInfo> selectMyInfo(String selectBy,String myID,String idString) {
        String  sqlString = "select infoID, deviceID,deviceName, useDate, table2.userID, table2.userName, statusBefore, statusAfter, remarks,adopt,principal ,account.username principalName from (select infoID, deviceID,deviceName, useDate, table1.userID, userName, statusBefore, statusAfter, remarks,adopt,principal from (select infoID, useInfo.deviceID,deviceName, useDate, useInfo.userID, statusBefore, statusAfter, remarks,adopt, device.userID principal from useInfo left join device on useInfo.deviceID=device.deviceID ) as table1 left join account on table1.userID=account.userID ) as table2 left join account on table2.principal=account.userID where table2.principal = ? and ";

        if (selectBy.equals("A")){
            sqlString = sqlString.concat(" table2.infoID like ?");
        }else if(selectBy.equals("B")){
            sqlString = sqlString.concat(" (table2.deviceID like ? or table2.deviceName like ?) ");
        }else if(selectBy.equals("C")){
            sqlString = sqlString.concat("  (table2.userID like ? or table2.userName like ? )");
        }
        ArrayList<UseInfo> list = new ArrayList<UseInfo>();
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, myID);
            preparedStatement.setString(2, "%"+idString+"%");
            if(selectBy.equals("A")==false){
                preparedStatement.setString(3, "%"+idString+"%");
            }
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String infoID = rSet.getString("infoID");
                String deviceID = rSet.getString("deviceID");
                String deviceName=rSet.getString("deviceName");
                String useDate = rSet.getString("useDate");
                String statusBefore = rSet.getString("statusBefore");
                String statusAfter = rSet.getString("statusAfter");
                String remarks = rSet.getString("remarks");
                String userID = rSet.getString("userID");
                String userName = rSet.getString("username");
                String adopt=rSet.getString("adopt");
                String principal=rSet.getString("principal");
                String principalName=rSet.getString("principalName");
                UseInfo useInfo = new UseInfo(infoID, deviceID,deviceName, useDate, userID, userName, statusBefore, statusAfter, remarks,adopt,principal,principalName);
                //向list中添加
                list.add(useInfo);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return null;
    }
    //根据用户id/用户姓名/设备id/设备名称模糊查找设备信息
    public static ArrayList<UseInfo> findByID(String selectBy,String idString) {
        String sqlString="";
        if (selectBy.equals("A")){
            sqlString = "select infoID, deviceID,deviceName, useDate, table2.userID, table2.userName, statusBefore, statusAfter, remarks,adopt,principal ,account.username principalName from (select infoID, deviceID,deviceName, useDate, table1.userID, userName, statusBefore, statusAfter, remarks,adopt,principal from (select infoID, useInfo.deviceID,deviceName, useDate, useInfo.userID, statusBefore, statusAfter, remarks,adopt, device.userID principal from useInfo left join device on useInfo.deviceID=device.deviceID ) as table1 left join account on table1.userID=account.userID ) as table2 left join account on table2.principal=account.userID where table2.infoID =?";
        }else if(selectBy.equals("B")){
            sqlString = "select infoID, deviceID,deviceName, useDate, table2.userID, table2.userName, statusBefore, statusAfter, remarks,adopt,principal ,account.username principalName from (select infoID, deviceID,deviceName, useDate, table1.userID, userName, statusBefore, statusAfter, remarks,adopt,principal from (select infoID, useInfo.deviceID,deviceName, useDate, useInfo.userID, statusBefore, statusAfter, remarks,adopt, device.userID principal from useInfo left join device on useInfo.deviceID=device.deviceID ) as table1 left join account on table1.userID=account.userID ) as table2 left join account on table2.principal=account.userID where table2.deviceID = ? ";
        }else if(selectBy.equals("C")){
            sqlString = "select infoID, deviceID,deviceName, useDate, table2.userID, table2.userName, statusBefore, statusAfter, remarks,adopt,principal ,account.username principalName from (select infoID, deviceID,deviceName, useDate, table1.userID, userName, statusBefore, statusAfter, remarks,adopt,principal from (select infoID, useInfo.deviceID,deviceName, useDate, useInfo.userID, statusBefore, statusAfter, remarks,adopt, device.userID principal from useInfo left join device on useInfo.deviceID=device.deviceID ) as table1 left join account on table1.userID=account.userID ) as table2 left join account on table2.principal=account.userID where table2.userID = ? ";
        }else if(selectBy.equals("D")){
            sqlString = "select infoID, deviceID,deviceName, useDate, table2.userID, table2.userName, statusBefore, statusAfter, remarks,adopt,principal ,account.username principalName from (select infoID, deviceID,deviceName, useDate, table1.userID, userName, statusBefore, statusAfter, remarks,adopt,principal from (select infoID, useInfo.deviceID,deviceName, useDate, useInfo.userID, statusBefore, statusAfter, remarks,adopt, device.userID principal from useInfo left join device on useInfo.deviceID=device.deviceID ) as table1 left join account on table1.userID=account.userID ) as table2 left join account on table2.principal=account.userID where table2.principal = ? ";
        }
        ArrayList<UseInfo> list = new ArrayList<UseInfo>();
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, idString);
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String infoID = rSet.getString("infoID");
                String deviceID = rSet.getString("deviceID");
                String deviceName=rSet.getString("deviceName");
                String useDate = rSet.getString("useDate");
                String statusBefore = rSet.getString("statusBefore");
                String statusAfter = rSet.getString("statusAfter");
                String remarks = rSet.getString("remarks");
                String userID = rSet.getString("userID");
                String userName = rSet.getString("username");
                String adopt=rSet.getString("adopt");
                String principal=rSet.getString("principal");
                String principalName=rSet.getString("principalName");
                UseInfo useInfo = new UseInfo(infoID, deviceID,deviceName, useDate, userID, userName, statusBefore, statusAfter, remarks,adopt,principal,principalName);
                //向list中添加
                list.add(useInfo);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return null;
    }
    public static ArrayList<UseInfo> findBy(String selectBy,String idString) {
        String sqlString="";
        if (selectBy.equals("A")){
            sqlString = "select infoID, deviceID,deviceName, useDate, table2.userID, table2.userName, statusBefore, statusAfter, remarks,adopt,principal ,account.username principalName from (select infoID, deviceID,deviceName, useDate, table1.userID, userName, statusBefore, statusAfter, remarks,adopt,principal from (select infoID, useInfo.deviceID,deviceName, useDate, useInfo.userID, statusBefore, statusAfter, remarks,adopt, device.userID principal from useInfo left join device on useInfo.deviceID=device.deviceID ) as table1 left join account on table1.userID=account.userID ) as table2 left join account on table2.principal=account.userID where table2.infoID like ?";
        }else if(selectBy.equals("B")){
            sqlString = "select infoID, deviceID,deviceName, useDate, table2.userID, table2.userName, statusBefore, statusAfter, remarks,adopt,principal ,account.username principalName from (select infoID, deviceID,deviceName, useDate, table1.userID, userName, statusBefore, statusAfter, remarks,adopt,principal from (select infoID, useInfo.deviceID,deviceName, useDate, useInfo.userID, statusBefore, statusAfter, remarks,adopt, device.userID principal from useInfo left join device on useInfo.deviceID=device.deviceID ) as table1 left join account on table1.userID=account.userID ) as table2 left join account on table2.principal=account.userID where table2.deviceID like ? or table2.deviceName like ? ";
        }else if(selectBy.equals("C")){
            sqlString = "select infoID, deviceID,deviceName, useDate, table2.userID, table2.userName, statusBefore, statusAfter, remarks,adopt,principal ,account.username principalName from (select infoID, deviceID,deviceName, useDate, table1.userID, userName, statusBefore, statusAfter, remarks,adopt,principal from (select infoID, useInfo.deviceID,deviceName, useDate, useInfo.userID, statusBefore, statusAfter, remarks,adopt, device.userID principal from useInfo left join device on useInfo.deviceID=device.deviceID ) as table1 left join account on table1.userID=account.userID ) as table2 left join account on table2.principal=account.userID where table2.userID like ? or account.userName like ? or principal like ? or account.username like ? ";
        }
        ArrayList<UseInfo> list = new ArrayList<UseInfo>();
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, "%"+idString+"%");
            if(selectBy.equals("A")==false){
                preparedStatement.setString(2, "%"+idString+"%");
            }if(selectBy.equals("C")){
                preparedStatement.setString(3, "%"+idString+"%");
                preparedStatement.setString(4, "%"+idString+"%");
            }
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String infoID = rSet.getString("infoID");
                String deviceID = rSet.getString("deviceID");
                String deviceName=rSet.getString("deviceName");
                String useDate = rSet.getString("useDate");
                String statusBefore = rSet.getString("statusBefore");
                String statusAfter = rSet.getString("statusAfter");
                String remarks = rSet.getString("remarks");
                String userID = rSet.getString("userID");
                String userName = rSet.getString("username");
                String adopt=rSet.getString("adopt");
                String principal=rSet.getString("principal");
                String principalName=rSet.getString("principalName");
                UseInfo useInfo = new UseInfo(infoID, deviceID,deviceName, useDate, userID, userName, statusBefore, statusAfter, remarks,adopt,principal,principalName);
                //向list中添加
                list.add(useInfo);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return null;
    }
    //修改设备信息
    public static boolean modifyUseInfo(UseInfo useInfo,String useInfoID) {
        String sqlString = "update useInfo set infoID=?, deviceID=?, useDate=?, userID=?, statusBefore=?, statusAfter=?, remarks=?,adopt=? where infoID=?";
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, useInfo.getInfoID());
            preparedStatement.setString(2, useInfo.getDeviceID());
            preparedStatement.setString(3, useInfo.getUseDate());
            preparedStatement.setString(4, useInfo.getUserID());
            preparedStatement.setString(5, useInfo.getStatusBefore());
            preparedStatement.setString(6, useInfo.getStatusAfter());
            preparedStatement.setString(7, useInfo.getRemarks());
            preparedStatement.setString(8, useInfo.getAdopt());
            preparedStatement.setString(9,useInfoID);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }
    //修改设备信息
    public static boolean modifyAdopt(String useInfoID) {
        String sqlString = "update useInfo set adopt='Y' where infoID=?";
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1,useInfoID);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static String getID(){
        //获取当前时间戳
        SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmm");//yyyyMMddHHmmss
        String temp = sf.format(new Date());
        //获取4位随机数
        int random=(int) ((Math.random()+1)*1000);
        String id=temp+random;
        return id;
    }
    public static String getTime(){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//yyyyMMddHHmmss
        String time = sf.format(new Date());
        return time;
    }
}