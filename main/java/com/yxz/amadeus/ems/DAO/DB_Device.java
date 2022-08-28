package com.yxz.amadeus.ems.DAO;

import android.util.Log;

import com.yxz.amadeus.ems.entity.Device;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DB_Device {
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
    //deviceID, deviceName, model, manufactor, recordDate, userID, username,maintenanceTimes
    //查询所有设备的信息
    public static ArrayList<Device> findDevice() {
        String sqlString = "select * from device  left join account on device.userID=account.userID";
        ArrayList<Device> list = new ArrayList<Device>();
        try {
            //连接数据库
            //使用预处理进行查询
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement(sqlString);
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String deviceID = rSet.getString("deviceID");
                String deviceName = rSet.getString("deviceName");
                String model = rSet.getString("model");
                String manufactor = rSet.getString("manufactor");
                String recordDate = rSet.getString("recordDate");
                String userID = rSet.getString("userID");
                int maintenanceTimes=rSet.getInt("maintenanceTimes");
                String userName = rSet.getString("username");
                Device device = new Device(deviceID, deviceName, model, manufactor, recordDate, userID, userName,maintenanceTimes);
                //向list中添加
                list.add(device);
            }
            return list;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<Device> findDeviceByID(String idString) {
        String sqlString = "select * from device  left join account on device.userID=account.userID where device.userID=?";
        ArrayList<Device> list = new ArrayList<Device>();
        try {
            //连接数据库
            //使用预处理进行查询
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement(sqlString);
            preparedStatement.setString(1, idString);
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String deviceID = rSet.getString("deviceID");
                String deviceName = rSet.getString("deviceName");
                String model = rSet.getString("model");
                String manufactor = rSet.getString("manufactor");
                String recordDate = rSet.getString("recordDate");
                String userID = rSet.getString("userID");
                int maintenanceTimes=rSet.getInt("maintenanceTimes");
                String userName = rSet.getString("username");
                Device device = new Device(deviceID, deviceName, model, manufactor, recordDate, userID, userName,maintenanceTimes);
                //向list中添加
                list.add(device);
            }
            return list;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }
    //增加设备
    public static boolean addDevice(Device device) {
        String sqlString = "insert into device values (?,?,?,?,?,?,?)";
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, device.getDeviceID());
            preparedStatement.setString(2, device.getDeviceName());
            preparedStatement.setString(3, device.getModel());
            preparedStatement.setString(4, device.getManufactor());
            preparedStatement.setString(5, device.getRecordDate());
            preparedStatement.setInt(6, device.getMaintenanceTimes());
            preparedStatement.setString(7, device.getUserID());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //删除设备
    public static boolean deleteDevice(String idString) {
        String sqlString = "delete from device where deviceID=?";
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
    public static ArrayList<Device> findByID(String selectBy,String idString) {
        String sqlString="";
        if (selectBy.equals("A")){

        }else if(selectBy.equals("B")){
            sqlString = "select * from device left join account on device.userID=account.userID where deviceID =?";
        }else if(selectBy.equals("C")){
            sqlString = "select * from device left join account on device.userID=account.userID where account.userID = ?";
        }
        ArrayList<Device> list = new ArrayList<Device>();
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, idString);
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String deviceID = rSet.getString("deviceID");
                String deviceName = rSet.getString("deviceName");
                String model = rSet.getString("model");
                String manufactor = rSet.getString("manufactor");
                String recordDate = rSet.getString("recordDate");
                String userID = rSet.getString("userID");
                int maintenanceTimes=rSet.getInt("maintenanceTimes");
                String userName = rSet.getString("username");
                Device device = new Device(deviceID, deviceName, model, manufactor, recordDate, userID, userName,maintenanceTimes);
                //向list中添加
                list.add(device);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return null;
    }
    public static ArrayList<Device> selectMyInfo(String selectBy,String myID,String idString) {
        String sqlString="select * from device left join account on device.userID=account.userID where device.userID = ? and ";
        if (selectBy.equals("A")){
            sqlString="select * from device left join account on device.userID=account.userID where device.userID = ?";
        }else if(selectBy.equals("B")){
            sqlString = sqlString.concat(" deviceID like ?");
        }else if(selectBy.equals("C")){
            sqlString = sqlString.concat(" device.userID like ?");
        }
        ArrayList<Device> list = new ArrayList<Device>();
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, myID);
            if(selectBy.equals("A")){

            }else {
                preparedStatement.setString(2, "%"+idString+"%");
            }
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String deviceID = rSet.getString("deviceID");
                String deviceName = rSet.getString("deviceName");
                String model = rSet.getString("model");
                String manufactor = rSet.getString("manufactor");
                String recordDate = rSet.getString("recordDate");
                String userID = rSet.getString("userID");
                int maintenanceTimes=rSet.getInt("maintenanceTimes");
                String userName = rSet.getString("username");
                Device device = new Device(deviceID, deviceName, model, manufactor, recordDate, userID, userName,maintenanceTimes);
                //向list中添加
                list.add(device);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return null;
    }
    public static ArrayList<Device> findBy(String selectBy,String idString) {
        String sqlString="";
        if (selectBy.equals("A")){

        }else if(selectBy.equals("B")){
            sqlString = "select * from device left join account on device.userID=account.userID where deviceID like ? or deviceName like ?";
        }else if(selectBy.equals("C")){
            sqlString = "select * from device left join account on device.userID=account.userID where account.userID like ? or account.userName like ?";
        }
        ArrayList<Device> list = new ArrayList<Device>();
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, "%"+idString+"%");
            preparedStatement.setString(2, "%"+idString+"%");
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String deviceID = rSet.getString("deviceID");
                String deviceName = rSet.getString("deviceName");
                String model = rSet.getString("model");
                String manufactor = rSet.getString("manufactor");
                String recordDate = rSet.getString("recordDate");
                String userID = rSet.getString("userID");
                int maintenanceTimes=rSet.getInt("maintenanceTimes");
                String userName = rSet.getString("username");
                Device device = new Device(deviceID, deviceName, model, manufactor, recordDate, userID, userName,maintenanceTimes);
                //向list中添加
                list.add(device);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return null;
    }
    public static ArrayList<Device> findByDevice(String idString) {
        String sqlString = "select * from device left join account on device.userID=account.userID where deviceID like ? or deviceName like ?";
        ArrayList<Device> list = new ArrayList<Device>();
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, "%"+idString+"%");
            preparedStatement.setString(2, "%"+idString+"%");
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String deviceID = rSet.getString("deviceID");
                String deviceName = rSet.getString("deviceName");
                String model = rSet.getString("model");
                String manufactor = rSet.getString("manufactor");
                String recordDate = rSet.getString("recordDate");
                String userID = rSet.getString("userID");
                int maintenanceTimes=rSet.getInt("maintenanceTimes");
                String userName = rSet.getString("username");
                Device device = new Device(deviceID, deviceName, model, manufactor, recordDate, userID, userName,maintenanceTimes);
                //向list中添加
                list.add(device);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return null;
    }
    //修改设备信息
    public static boolean modifyDevice(Device device,String deviceID) {
        String sqlString = "update device set deviceID=?, deviceName=?, model=?, manufactor=?, recordDate=?, userID=?,maintenanceTimes=? where deviceID=?";
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, device.getDeviceID());
            preparedStatement.setString(2, device.getDeviceName());
            preparedStatement.setString(3, device.getModel());
            preparedStatement.setString(4, device.getManufactor());
            preparedStatement.setString(5, device.getRecordDate());
            preparedStatement.setString(6, device.getUserID());
            preparedStatement.setInt(7, device.getMaintenanceTimes());
            preparedStatement.setString(8,deviceID);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static String getID(){
        String id="";
        //获取当前时间戳
        SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmm");//yyyyMMddHHmmss
        String temp = sf.format(new Date());
        //获取4位随机数
        int random=(int) ((Math.random()+1)*1000);
        id=temp+random;
        return id;
    }

}