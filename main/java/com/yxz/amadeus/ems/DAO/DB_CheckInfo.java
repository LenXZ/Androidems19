package com.yxz.amadeus.ems.DAO;

import android.util.Log;

import com.yxz.amadeus.ems.entity.CheckInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DB_CheckInfo {
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
    public static ArrayList<CheckInfo> findCheckInfo() {
        String sqlString = "select checkInfo.infoID, deviceID, user1.deviceName, lastCheck, nextCheck, lastRepair, state, inspector,account.username as inspectorName, user1.userID, user1.userName, cycle from checkInfo left join (select infoID,deviceName,account.userID,account.userName from checkInfo left join device on checkInfo.deviceID=device.deviceID left join account on device.userID=account.userID) as user1 on checkInfo.infoID=user1.infoID left join account on checkInfo.inspector=account.userID ";
        ArrayList<CheckInfo> list = new ArrayList<CheckInfo>();
        try {
            //连接数据库
            //使用预处理进行查询
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement(sqlString);
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String infoID = rSet.getString("infoID");
                String deviceID = rSet.getString("deviceID");
                String deviceName = rSet.getString("deviceName");
                String lastCheck = rSet.getString("lastCheck");
                String nextCheck = rSet.getString("nextCheck");
                String lastRepair = rSet.getString("lastRepair");
                String state = rSet.getString("state");
                String inspector = rSet.getString("inspector");
                String inspectorName = rSet.getString("inspectorName");
                String userID = rSet.getString("userID");
                String userName = rSet.getString("username");
                int cycle = rSet.getInt("cycle");
                CheckInfo checkInfo = new CheckInfo(infoID, deviceID, deviceName, lastCheck, nextCheck, lastRepair, state, inspector,inspectorName, userID, userName, cycle);
                //向list中添加
                list.add(checkInfo);
            }
            return list;
        } catch (Exception e) {
            // TODO: handle exception
            //打印堆栈信息
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<CheckInfo> findCheckInfoByID(String idString) {
        String sqlString = "select checkInfo.infoID, deviceID, user1.deviceName, lastCheck, nextCheck, lastRepair, state, inspector,account.username as inspectorName, user1.userID, user1.userName, cycle from checkInfo left join (select infoID,deviceName,account.userID,account.userName from checkInfo left join device on checkInfo.deviceID=device.deviceID left join account on device.userID=account.userID) as user1 on checkInfo.infoID=user1.infoID left join account on checkInfo.inspector=account.userID where user1.userID=?";
        ArrayList<CheckInfo> list = new ArrayList<CheckInfo>();
        try {
            //连接数据库
            //使用预处理进行查询
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement(sqlString);
            preparedStatement.setString(1, idString);
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String infoID = rSet.getString("infoID");
                String deviceID = rSet.getString("deviceID");
                String deviceName = rSet.getString("deviceName");
                String lastCheck = rSet.getString("lastCheck");
                String nextCheck = rSet.getString("nextCheck");
                String lastRepair = rSet.getString("lastRepair");
                String state = rSet.getString("state");
                String inspector = rSet.getString("inspector");
                String inspectorName = rSet.getString("inspectorName");
                String userID = rSet.getString("userID");
                String userName = rSet.getString("username");
                int cycle = rSet.getInt("cycle");
                CheckInfo checkInfo = new CheckInfo(infoID, deviceID, deviceName, lastCheck, nextCheck, lastRepair, state, inspector,inspectorName, userID, userName, cycle);
                //向list中添加
                list.add(checkInfo);
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
    public static boolean addCheckInfo(CheckInfo checkInfo) {
        String sqlString = "insert into checkInfo values (?,?,?,?,?,?,?,?)";
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, checkInfo.getInfoID());
            preparedStatement.setString(2, checkInfo.getDeviceID());
            preparedStatement.setInt(3, checkInfo.getCycle());
            preparedStatement.setString(4, checkInfo.getLastCheck());
            preparedStatement.setString(5, checkInfo.getNextCheck());
            preparedStatement.setString(6, checkInfo.getLastRepair());
            preparedStatement.setString(7, checkInfo.getState());
            preparedStatement.setString(8, checkInfo.getInspector());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //删除设备
    public static boolean deleteCheckInfo(String idString) {
        String sqlString = "delete from checkInfo where infoID=?";
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, idString);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //根据用户id/用户姓名/设备id/设备名称模糊查找设备信息
    public static ArrayList<CheckInfo> findByID(String selectBy,String idString) {
        String sqlString="";
        if (selectBy.equals("A")){
            sqlString = "select checkInfo.infoID, deviceID, user1.deviceName, lastCheck, nextCheck, lastRepair, state, inspector,account.username as inspectorName, user1.userID, user1.userName, cycle from checkInfo left join (select infoID,deviceName,account.userID,account.userName from checkInfo left join device on checkInfo.deviceID=device.deviceID left join account on device.userID=account.userID) as user1 on checkInfo.infoID=user1.infoID left join account on checkInfo.inspector=account.userID where checkInfo.infoID =?";
        }else if(selectBy.equals("B")){
            sqlString = "select checkInfo.infoID, deviceID, user1.deviceName, lastCheck, nextCheck, lastRepair, state, inspector,account.username as inspectorName, user1.userID, user1.userName, cycle from checkInfo left join (select infoID,deviceName,account.userID,account.userName from checkInfo left join device on checkInfo.deviceID=device.deviceID left join account on device.userID=account.userID) as user1 on checkInfo.infoID=user1.infoID left join account on checkInfo.inspector=account.userID where checkInfo.deviceID =? ";
        }else if(selectBy.equals("C")){//检查人
            sqlString = "select checkInfo.infoID, deviceID, user1.deviceName, lastCheck, nextCheck, lastRepair, state, inspector,account.username as inspectorName, user1.userID, user1.userName, cycle from checkInfo left join (select infoID,deviceName,account.userID,account.userName from checkInfo left join device on checkInfo.deviceID=device.deviceID left join account on device.userID=account.userID) as user1 on checkInfo.infoID=user1.infoID left join account on checkInfo.inspector=account.userID where  inspector =?";
        }else if(selectBy.equals("D")){//负责人
            sqlString = "select checkInfo.infoID, deviceID, user1.deviceName, lastCheck, nextCheck, lastRepair, state, inspector,account.username as inspectorName, user1.userID, user1.userName, cycle from checkInfo left join (select infoID,deviceName,account.userID,account.userName from checkInfo left join device on checkInfo.deviceID=device.deviceID left join account on device.userID=account.userID) as user1 on checkInfo.infoID=user1.infoID left join account on checkInfo.inspector=account.userID where  user1.userID =?";
        }
        ArrayList<CheckInfo> list = new ArrayList<CheckInfo>();
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, idString);
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String infoID = rSet.getString("infoID");
                String deviceID = rSet.getString("deviceID");
                String deviceName = rSet.getString("deviceName");
                String lastCheck = rSet.getString("lastCheck");
                String nextCheck = rSet.getString("nextCheck");
                String lastRepair = rSet.getString("lastRepair");
                String state = rSet.getString("state");
                String inspector = rSet.getString("inspector");
                String inspectorName = rSet.getString("inspectorName");
                String userID = rSet.getString("userID");
                String userName = rSet.getString("username");
                int cycle = rSet.getInt("cycle");
                CheckInfo checkInfo = new CheckInfo(infoID, deviceID, deviceName, lastCheck, nextCheck, lastRepair, state, inspector, inspectorName,userID, userName, cycle);
                //向list中添加
                list.add(checkInfo);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return null;
    }
    public static ArrayList<CheckInfo> selectMyInfo(String selectBy,String myID,String idString) {
        String sqlString="select checkInfo.infoID, deviceID, user1.deviceName, lastCheck, nextCheck, lastRepair, state, inspector,account.username as inspectorName, user1.userID, user1.userName, cycle from checkInfo left join (select infoID,deviceName,account.userID,account.userName from checkInfo left join device on checkInfo.deviceID=device.deviceID left join account on device.userID=account.userID) as user1 on checkInfo.infoID=user1.infoID left join account on checkInfo.inspector=account.userID where user1.userID =? and ";
        if (selectBy.equals("A")){
            sqlString = sqlString.concat(" checkInfo.infoID like ?");
        }else if(selectBy.equals("B")){
            sqlString = sqlString.concat( " (checkInfo.deviceID like ? or user1.deviceName like ?) ");
        }else if(selectBy.equals("C")){//检查人
            sqlString = sqlString.concat(" (inspector like ? or account.username like ?)");
        }
        ArrayList<CheckInfo> list = new ArrayList<CheckInfo>();
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
                String deviceName = rSet.getString("deviceName");
                String lastCheck = rSet.getString("lastCheck");
                String nextCheck = rSet.getString("nextCheck");
                String lastRepair = rSet.getString("lastRepair");
                String state = rSet.getString("state");
                String inspector = rSet.getString("inspector");
                String inspectorName = rSet.getString("inspectorName");
                String userID = rSet.getString("userID");
                String userName = rSet.getString("username");
                int cycle = rSet.getInt("cycle");
                CheckInfo checkInfo = new CheckInfo(infoID, deviceID, deviceName, lastCheck, nextCheck, lastRepair, state, inspector, inspectorName,userID, userName, cycle);
                //向list中添加
                list.add(checkInfo);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return null;
    }
    public static ArrayList<CheckInfo> findBy(String selectBy,String idString) {
        String sqlString="";
        if (selectBy.equals("A")){
            sqlString = "select checkInfo.infoID, deviceID, user1.deviceName, lastCheck, nextCheck, lastRepair, state, inspector,account.username as inspectorName, user1.userID, user1.userName, cycle from checkInfo left join (select infoID,deviceName,account.userID,account.userName from checkInfo left join device on checkInfo.deviceID=device.deviceID left join account on device.userID=account.userID) as user1 on checkInfo.infoID=user1.infoID left join account on checkInfo.inspector=account.userID where checkInfo.infoID like ?";
        }else if(selectBy.equals("B")){
            sqlString = "select checkInfo.infoID, deviceID, user1.deviceName, lastCheck, nextCheck, lastRepair, state, inspector,account.username as inspectorName, user1.userID, user1.userName, cycle from checkInfo left join (select infoID,deviceName,account.userID,account.userName from checkInfo left join device on checkInfo.deviceID=device.deviceID left join account on device.userID=account.userID) as user1 on checkInfo.infoID=user1.infoID left join account on checkInfo.inspector=account.userID where checkInfo.deviceID like ? or user1.deviceName like ? ";
        }else if(selectBy.equals("C")){
            sqlString = "select checkInfo.infoID, deviceID, user1.deviceName, lastCheck, nextCheck, lastRepair, state, inspector,account.username as inspectorName, user1.userID, user1.userName, cycle from checkInfo left join (select infoID,deviceName,account.userID,account.userName from checkInfo left join device on checkInfo.deviceID=device.deviceID left join account on device.userID=account.userID) as user1 on checkInfo.infoID=user1.infoID left join account on checkInfo.inspector=account.userID where  inspector like ? or account.username like ? or user1.userID like ? or user1.userName like ?";
        }
        ArrayList<CheckInfo> list = new ArrayList<CheckInfo>();
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, "%" + idString + "%");
            if(selectBy.equals("A")==false){
                preparedStatement.setString(2, "%"+idString+"%");
            }
            if(selectBy.equals("C")){
                preparedStatement.setString(3, "%"+idString+"%");
                preparedStatement.setString(4, "%"+idString+"%");
            }
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String infoID = rSet.getString("infoID");
                String deviceID = rSet.getString("deviceID");
                String deviceName = rSet.getString("deviceName");
                String lastCheck = rSet.getString("lastCheck");
                String nextCheck = rSet.getString("nextCheck");
                String lastRepair = rSet.getString("lastRepair");
                String state = rSet.getString("state");
                String inspector = rSet.getString("inspector");
                String inspectorName = rSet.getString("inspectorName");
                String userID = rSet.getString("userID");
                String userName = rSet.getString("username");
                int cycle = rSet.getInt("cycle");
                CheckInfo checkInfo = new CheckInfo(infoID, deviceID, deviceName, lastCheck, nextCheck, lastRepair, state, inspector, inspectorName,userID, userName, cycle);
                //向list中添加
                list.add(checkInfo);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return null;
    }
    //修改设备信息
    public static boolean modifyCheckInfo(CheckInfo checkInfo, String checkInfoID) {
        String sqlString = "update checkInfo set infoID=?, deviceID=?,cycle=?, lastCheck=?,nextCheck=?, lastRepair=?, state=?,inspector=? where infoID=?";
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, checkInfo.getInfoID());
            preparedStatement.setString(2, checkInfo.getDeviceID());
            preparedStatement.setInt(3, checkInfo.getCycle());
            preparedStatement.setString(4, checkInfo.getLastCheck());
            preparedStatement.setString(5, checkInfo.getNextCheck());
            preparedStatement.setString(6, checkInfo.getLastRepair());
            preparedStatement.setString(7, checkInfo.getState());
            preparedStatement.setString(8, checkInfo.getInspector());
            preparedStatement.setString(9, checkInfoID);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getID() {
        //获取当前时间戳
        SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmm");//yyyyMMddHHmmss
        String temp = sf.format(new Date());
        //获取4位随机数
        int random = (int) ((Math.random() + 1) * 1000);
        String id = temp + random;
        return id;
    }

    public static String getTime() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//yyyyMMddHHmmss
        String time = sf.format(new Date());
        return time;
    }
}