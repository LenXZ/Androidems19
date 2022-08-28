package com.yxz.amadeus.ems.DAO;

import android.util.Log;

import com.yxz.amadeus.ems.entity.RepairInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DB_RepairInfo {
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
    //repairInfoID, repairInfoName, model, manufactor, recordDate, userID, username,maintenanceTimes
    //查询所有设备的信息
    public static ArrayList<RepairInfo> findRepairInfo() {
        String sqlString = "select infoID, table1.deviceID,deviceName, damageDate, damageDegree, damageCause, repairDate, repairPersonnel, state, table1.userID, username,cost,principal, principalName from (select infoID, repairInfo.deviceID,deviceName, damageDate, damageDegree, damageCause, repairDate, repairPersonnel, state, repairInfo.userID, username,cost\n" +
                " from repairInfo left join device on repairInfo.deviceID=device.deviceID left join account on device.userID=account.userID ) as table1 left join\n" +
                " (select deviceID,device.userID principal,account.username principalName from device left join account on device.userID=account.userID)as table2 on table1.deviceID=table2.deviceID";
        ArrayList<RepairInfo> list = new ArrayList<RepairInfo>();
        try {
            //连接数据库
            //使用预处理进行查询
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement(sqlString);
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {

                String infoID = rSet.getString("infoID");
                String deviceID = rSet.getString("deviceID");
                String deviceName=rSet.getString("deviceName");
                String damageDate = rSet.getString("damageDate");
                String damageDegree = rSet.getString("damageDegree");
                String damageCause = rSet.getString("damageCause");
                String repairDate = rSet.getString("repairDate");
                String repairPersonnel = rSet.getString("repairPersonnel");
                String state = rSet.getString("state");
                String userID = rSet.getString("userID");
                String username = rSet.getString("username");
                float cost=rSet.getFloat("cost");
                String principal=rSet.getString("principal");
                String principalName=rSet.getString("principalName");
                RepairInfo repairInfo = new RepairInfo(infoID, deviceID,deviceName, damageDate, damageDegree, damageCause, repairDate, repairPersonnel, state, userID, username,cost,principal,principalName);
                //向list中添加
                list.add(repairInfo);
            }
            return list;
        } catch (Exception e) {
            // TODO: handle exception
            //打印堆栈信息
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<RepairInfo> findRepairInfoByID(String idString) {
        String sqlString = "select infoID, table1.deviceID,deviceName, damageDate, damageDegree, damageCause, repairDate, repairPersonnel, state, table1.userID, username,cost,principal, principalName from (select infoID, repairInfo.deviceID,deviceName, damageDate, damageDegree, damageCause, repairDate, repairPersonnel, state, repairInfo.userID, username,cost\n" +
                " from repairInfo left join device on repairInfo.deviceID=device.deviceID left join account on device.userID=account.userID ) as table1 left join\n" +
                " (select deviceID,device.userID principal,account.username principalName from device left join account on device.userID=account.userID)as table2 on table1.deviceID=table2.deviceID where table2.principal=?";
        ArrayList<RepairInfo> list = new ArrayList<RepairInfo>();
        try {
            //连接数据库
            //使用预处理进行查询
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement(sqlString);
            preparedStatement.setString(1, idString);
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String infoID = rSet.getString("infoID");
                String deviceID = rSet.getString("deviceID");
                String deviceName=rSet.getString("deviceName");
                String damageDate = rSet.getString("damageDate");
                String damageDegree = rSet.getString("damageDegree");
                String damageCause = rSet.getString("damageCause");
                String repairDate = rSet.getString("repairDate");
                String repairPersonnel = rSet.getString("repairPersonnel");
                String state = rSet.getString("state");
                String userID = rSet.getString("userID");
                String username = rSet.getString("username");
                float cost=rSet.getFloat("cost");
                String principal=rSet.getString("principal");
                String principalName=rSet.getString("principalName");
                RepairInfo repairInfo = new RepairInfo(infoID, deviceID,deviceName, damageDate, damageDegree, damageCause, repairDate, repairPersonnel, state, userID, username,cost,principal,principalName);
                //向list中添加
                list.add(repairInfo);
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
    public static boolean addRepairInfo(RepairInfo repairInfo) {
        String sqlString = "insert into repairInfo values (?,?,?,?,?,?,?,?,?,?)";
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, repairInfo.getInfoID());
            preparedStatement.setString(2, repairInfo.getDeviceID());
            preparedStatement.setString(3, repairInfo.getDamageDate());
            preparedStatement.setString(4, repairInfo.getDamageDegree());
            preparedStatement.setString(5, repairInfo.getDamageCause());
            preparedStatement.setString(6, repairInfo.getRepairDate());
            preparedStatement.setString(7, repairInfo.getRepairPersonnel());
            preparedStatement.setFloat(8, repairInfo.getCost());
            preparedStatement.setString(9, repairInfo.getState());
            preparedStatement.setString(10, repairInfo.getUserID());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //删除设备
    public static boolean deleteRepairInfo(String idString) {
        String sqlString = "delete from repairInfo where infoID=?";
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
    public static ArrayList<RepairInfo> findByID(String selectBy,String idString) {
        String sqlString="select infoID, table1.deviceID,deviceName, damageDate, damageDegree, damageCause, repairDate, repairPersonnel, state, table1.userID, username,cost,principal, principalName from (select infoID, repairInfo.deviceID,deviceName, damageDate, damageDegree, damageCause, repairDate, repairPersonnel, state, repairInfo.userID, username,cost\n" +
                " from repairInfo left join device on repairInfo.deviceID=device.deviceID left join account on device.userID=account.userID ) as table1 left join\n" +
                " (select deviceID,device.userID principal,account.username principalName from device left join account on device.userID=account.userID)as table2 on table1.deviceID=table2.deviceID";
        if (selectBy.equals("A")){
            sqlString = sqlString.concat(" where table1.infoID=?");
        }else if(selectBy.equals("B")){
            sqlString = sqlString.concat(" where table1.deviceID = ? ");
        }else if(selectBy.equals("C")){
            sqlString = sqlString.concat(" where table1.userID =? ");
        }else if(selectBy.equals("D")){
            sqlString =sqlString.concat( " where table2.principal =? ");
        }
        ArrayList<RepairInfo> list = new ArrayList<RepairInfo>();
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, idString);
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                String infoID = rSet.getString("infoID");
                String deviceID = rSet.getString("deviceID");
                String deviceName=rSet.getString("deviceName");
                String damageDate = rSet.getString("damageDate");
                String damageDegree = rSet.getString("damageDegree");
                String damageCause = rSet.getString("damageCause");
                String repairDate = rSet.getString("repairDate");
                String repairPersonnel = rSet.getString("repairPersonnel");
                String state = rSet.getString("state");
                String userID = rSet.getString("userID");
                String username = rSet.getString("username");
                float cost=rSet.getFloat("cost");
                String principal=rSet.getString("principal");
                String principalName=rSet.getString("principalName");
                RepairInfo repairInfo = new RepairInfo(infoID, deviceID,deviceName, damageDate, damageDegree, damageCause, repairDate, repairPersonnel, state, userID, username,cost,principal,principalName);
                //向list中添加
                list.add(repairInfo);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return null;
    }
    public static ArrayList<RepairInfo> selectMyInfo(String selectBy,String myID,String idString) {
        String sqlString="select infoID, table1.deviceID,deviceName, damageDate, damageDegree, damageCause, repairDate, repairPersonnel, state, table1.userID, username,cost,principal, principalName from (select infoID, repairInfo.deviceID,deviceName, damageDate, damageDegree, damageCause, repairDate, repairPersonnel, state, repairInfo.userID, username,cost\n" +
                " from repairInfo left join device on repairInfo.deviceID=device.deviceID left join account on device.userID=account.userID ) as table1 left join\n" +
                " (select deviceID,device.userID principal,account.username principalName from device left join account on device.userID=account.userID)as table2 on table1.deviceID=table2.deviceID where table2.principal=? and ";
        if (selectBy.equals("A")){
            sqlString = sqlString.concat( "  (table1.infoID like ? )");
        }else if(selectBy.equals("B")){
            sqlString = sqlString.concat("  (table1.deviceID like ? or table1.deviceName like ?) ");
        }else if(selectBy.equals("C")){
            sqlString = sqlString.concat( "  (table1.userID like ? or table1.userName like ? )");
        }
        ArrayList<RepairInfo> list = new ArrayList<RepairInfo>();
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
                String damageDate = rSet.getString("damageDate");
                String damageDegree = rSet.getString("damageDegree");
                String damageCause = rSet.getString("damageCause");
                String repairDate = rSet.getString("repairDate");
                String repairPersonnel = rSet.getString("repairPersonnel");
                String state = rSet.getString("state");
                String userID = rSet.getString("userID");
                String username = rSet.getString("username");
                float cost=rSet.getFloat("cost");
                String principal=rSet.getString("principal");
                String principalName=rSet.getString("principalName");
                RepairInfo repairInfo = new RepairInfo(infoID, deviceID,deviceName, damageDate, damageDegree, damageCause, repairDate, repairPersonnel, state, userID, username,cost,principal,principalName);
                //向list中添加
                list.add(repairInfo);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return null;
    }
    public static ArrayList<RepairInfo> findBy(String selectBy,String idString) {
        String sqlString="select infoID, table1.deviceID,deviceName, damageDate, damageDegree, damageCause, repairDate, repairPersonnel, state, table1.userID, username,cost,principal, principalName from (select infoID, repairInfo.deviceID,deviceName, damageDate, damageDegree, damageCause, repairDate, repairPersonnel, state, repairInfo.userID, username,cost\n" +
                " from repairInfo left join device on repairInfo.deviceID=device.deviceID left join account on device.userID=account.userID ) as table1 left join\n" +
                " (select deviceID,device.userID principal,account.username principalName from device left join account on device.userID=account.userID)as table2 on table1.deviceID=table2.deviceID";
        if (selectBy.equals("A")){
            sqlString =sqlString.concat( " where table1.infoID like ? ");
        }else if(selectBy.equals("B")){
            sqlString = sqlString.concat(" where table1.deviceID like ? or table1.deviceName like ? ");
        }else if(selectBy.equals("C")){
            sqlString =sqlString.concat( " where table1.userID like ? or table1.userName like ? or table2.principal like ? or table2.principalName like ?");
        }
        ArrayList<RepairInfo> list = new ArrayList<RepairInfo>();
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, "%"+idString+"%");
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
                String deviceName=rSet.getString("deviceName");
                String damageDate = rSet.getString("damageDate");
                String damageDegree = rSet.getString("damageDegree");
                String damageCause = rSet.getString("damageCause");
                String repairDate = rSet.getString("repairDate");
                String repairPersonnel = rSet.getString("repairPersonnel");
                String state = rSet.getString("state");
                String userID = rSet.getString("userID");
                String username = rSet.getString("username");
                float cost=rSet.getFloat("cost");
                String principal=rSet.getString("principal");
                String principalName=rSet.getString("principalName");
                RepairInfo repairInfo = new RepairInfo(infoID, deviceID,deviceName, damageDate, damageDegree, damageCause, repairDate, repairPersonnel, state, userID, username,cost,principal,principalName);
                //向list中添加
                list.add(repairInfo);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return null;
    }
    //修改设备信息
    public static boolean modifyRepairInfo(RepairInfo repairInfo,String repairInfoID) {
        String sqlString = "update repairInfo set infoID=?, deviceID=?, damageDate=?, damageDegree=?, damageCause=?, repairDate=?, repairPersonnel=?,cost=?, state=?, userID=? where infoID=?";
        try {
            Connection connection = getSQLConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, repairInfo.getInfoID());
            preparedStatement.setString(2, repairInfo.getDeviceID());
            preparedStatement.setString(3, repairInfo.getDamageDate());
            preparedStatement.setString(4, repairInfo.getDamageDegree());
            preparedStatement.setString(5, repairInfo.getDamageCause());
            preparedStatement.setString(6, repairInfo.getRepairDate());
            preparedStatement.setString(7, repairInfo.getRepairPersonnel());
            preparedStatement.setFloat(8, repairInfo.getCost());
            preparedStatement.setString(9, repairInfo.getState());
            preparedStatement.setString(10, repairInfo.getUserID());
            preparedStatement.setString(11,repairInfoID);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static String findLateRepair(String deviceID) {
        String sqlString = "select * from repairInfo A  where repairDate=(select max(repairDate) from repairInfo B where deviceID= ? ) ";
        String repairDate="";
        try {
            //连接数据库
            //使用预处理进行查询
            PreparedStatement preparedStatement = getSQLConnection().prepareStatement(sqlString);
            preparedStatement.setString(1, deviceID);
            ResultSet rSet = preparedStatement.executeQuery();
            while (rSet.next()) {
                repairDate = rSet.getString("repairDate");
            }
            return repairDate;
        } catch (Exception e) {
            // TODO: handle exception
            //打印堆栈信息
            e.printStackTrace();
        }
        return null;
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