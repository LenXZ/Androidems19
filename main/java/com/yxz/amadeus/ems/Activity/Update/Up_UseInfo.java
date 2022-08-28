package com.yxz.amadeus.ems.Activity.Update;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yxz.amadeus.ems.Activity.Interface.AccountList;
import com.yxz.amadeus.ems.Activity.Interface.DeviceList;
import com.yxz.amadeus.ems.Activity.Interface.UseInfoList;
import com.yxz.amadeus.ems.DAO.DB_UseInfo;
import com.yxz.amadeus.ems.entity.Device;
import com.yxz.amadeus.ems.entity.UseInfo;
import com.yxz.amadeus.ems.R;

public class Up_UseInfo extends AppCompatActivity {
    private Spinner statusBefore, statusAfter;
    private String select="",identity="",myID="";
    private List<String> list_Before,list_After;
    private ArrayAdapter<String> adapter_Before,adapter_After;
    private UseInfo useInfo;//记录修改后的数据
    private String previousID;//记录原设备ID
    private Button confirm, cancel, selectDevice, selectUser,useDate_Date,useDate_Time;
    private EditText infoID, deviceID,  userID, remarks;
    private TextView userName, deviceName,useDate;
    DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//多态
    Calendar calendar = Calendar.getInstance(Locale.CHINA);//获取日期格式器对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_useinfo);
        confirm = (Button) findViewById(R.id.in_content);
        cancel = (Button) findViewById(R.id.in_cancel);
        selectDevice = (Button) findViewById(R.id.selectDevice);
        selectUser = (Button) findViewById(R.id.selectUser);
        useDate_Date = (Button) findViewById(R.id.useDate_Date);
        useDate_Time = (Button) findViewById(R.id.useDate_Time);
        infoID = (EditText) findViewById(R.id.infoID);
        deviceID = (EditText) findViewById(R.id.deviceID);
        deviceName = (TextView) findViewById(R.id.deviceName);
        useDate = (TextView) findViewById(R.id.useDate);
        statusBefore = (Spinner) findViewById(R.id.statusBefore);
        statusAfter = (Spinner) findViewById(R.id.statusAfter);
        remarks = (EditText) findViewById(R.id.remarks);
        userID = (EditText) findViewById(R.id.userID);
        userName = (TextView) findViewById(R.id.userName);

        useInfo = new UseInfo();
        try{
            //接收点击的数据
            Bundle bundle=getIntent().getExtras();
            try{
                identity=bundle.getString("identity");
                myID=bundle.getString("myID");
            }catch (Exception e){

            }
            previousID=bundle.getString("infoID");
            useInfo.setInfoID(bundle.getString("infoID"));
            useInfo.setDeviceID(bundle.getString("deviceID"));
            useInfo.setDeviceName(bundle.getString("deviceName"));
            useInfo.setUseDate(bundle.getString("useDate"));
            useInfo.setStatusBefore(bundle.getString("statusBefore"));
            useInfo.setStatusAfter(bundle.getString("statusAfter"));
            useInfo.setRemarks(bundle.getString("remarks"));
            useInfo.setAdopt(bundle.getString("adopt"));
            useInfo.setUserID(bundle.getString("userID"));
            useInfo.setUserName(bundle.getString("userName"));
            infoID.setText(bundle.getString("infoID"));
            deviceID.setText(bundle.getString("deviceID"));
            deviceName.setText(bundle.getString("deviceName"));
            useDate.setText(bundle.getString("useDate"));
            remarks.setText(bundle.getString("remarks"));
            userID.setText(bundle.getString("userID"));
            userName.setText(bundle.getString("userName"));
            if(identity.equals("A")){
                useInfo.setAdopt("Y");
            }else if(identity.equals("B")||identity.equals("BC")){
                deviceID.setFocusableInTouchMode(false);
                useInfo.setAdopt("Y");
            }else if(identity.equals("C")){
                useInfo.setAdopt("N");
            }
        }catch (Exception e){
            Log.d("ERROR_Up_UseInfo",""+e);
        }
        //数据
        list_Before = new ArrayList<String>();
        if (useInfo.getStatusBefore().equals("A")){
            list_Before.add("正常");
        }else if (useInfo.getStatusBefore().equals("B")){
            list_Before.add("异常");
        }else if (useInfo.getStatusBefore().equals("C")){
            list_Before.add("损坏");
        }else if (useInfo.getStatusBefore().equals("D")){
            list_Before.add("报废");
        }else {
            list_Before.add("请选择设备状态");
        }
        list_Before.add("正常");//012
        list_Before.add("异常");
        list_Before.add("损坏");
        list_Before.add("报废");
        list_After = new ArrayList<String>();
        if (useInfo.getStatusAfter().equals("A")){
            list_After.add("正常");
        }else if (useInfo.getStatusAfter().equals("B")){
            list_After.add("异常");
        }else if (useInfo.getStatusAfter().equals("C")){
            list_After.add("损坏");
        }else if (useInfo.getStatusAfter().equals("D")){
            list_After.add("报废");
        }else {
            list_After.add("请选择设备状态");
        }
        list_After.add("正常");//012
        list_After.add("异常");
        list_After.add("损坏");
        list_After.add("报废");
        //适配器
        adapter_Before = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_Before);
        //设置样式
        adapter_Before.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        statusBefore.setAdapter(adapter_Before);
        statusBefore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //识别下拉列表
                if (position == 1) {
                    useInfo.setStatusBefore("A");
                } else if (position == 2) {
                    useInfo.setStatusBefore("B");
                } else if (position == 3) {
                    useInfo.setStatusBefore("C");
                } else if (position == 4) {
                    useInfo.setStatusBefore("D");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //适配器
        adapter_After = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_After);
        //设置样式
        adapter_After.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        statusAfter.setAdapter(adapter_After);
        statusAfter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //识别下拉列表
                if (position == 1) {
                    useInfo.setStatusAfter("A");
                } else if (position == 2) {
                    useInfo.setStatusAfter("B");
                } else if (position == 3) {
                    useInfo.setStatusAfter("C");
                } else if (position == 4) {
                    useInfo.setStatusAfter("D");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        infoID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                useInfo.setInfoID(s.toString());
            }
        });
        deviceID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                useInfo.setDeviceID(s.toString());
            }
        });
        userID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                useInfo.setUserID(s.toString());
            }
        });
        remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                useInfo.setRemarks(s.toString());
            }
        });
        //点击确定
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (useInfo.getInfoID() == null) {
                    Toast.makeText(Up_UseInfo.this, "信息编号不能为空",
                            Toast.LENGTH_SHORT).show();
                } else if (useInfo.getDeviceID() == null) {
                    Toast.makeText(Up_UseInfo.this, "设备编号不能为空",
                            Toast.LENGTH_SHORT).show();
                }else if (useInfo.getUserID()==null){
                    Toast.makeText(Up_UseInfo.this, "使用人不能为空",
                            Toast.LENGTH_SHORT).show();
                } else  {
                    ModifyUseInfo();
                }

            }
        });
        //点击取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(identity.equals("B")||identity.equals("BC")){
//                    Intent intent=new Intent(Up_UseInfo.this, UseInfoList.class);
//                    Bundle bundle=new Bundle();
//                    bundle.putString("identity",identity);
//                    bundle.putString("myID",myID);
//                    bundle.putBoolean("bo_select",false);
//                    bundle.putString("select","");//向用户列表界面传递搜索内容
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }else {
//                    startActivity(new Intent(Up_UseInfo.this, UseInfoList.class));
//                }
                loading();
                finish();
            }
        });
        selectDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Up_UseInfo.this, DeviceList.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("jump", 13);
                bundle1.putString("identity",identity);
                bundle1.putString("myID",myID);
                intent.putExtras(bundle1);
                startActivityForResult(intent, 1);
            }
        });
        selectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Up_UseInfo.this, AccountList.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("jump", 13);
                intent.putExtras(bundle1);
                startActivityForResult(intent, 2);
            }
        });
        useDate_Date.setOnClickListener(new View.OnClickListener() {//设置按钮的点击事件监听器
            @Override
            public void onClick(View v) {
                //生成一个DatePickerDialog对象，并显示。显示的DatePickerDialog控件可以选择年月日，并设置
                DatePickerDialog datePickerDialog = new DatePickerDialog(Up_UseInfo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //修改日历控件的年，月，日
                        //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDate();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        useDate_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(Up_UseInfo.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //同DatePickerDialog控件
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        updateDate(); //将页面TextView的显示更新为最新时间
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });
    }
    private void loading(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载中");
        dialog.show();
    }
    private void updateDate() { //将页面TextView的显示更新为最新时间
        Date date=calendar.getTime();
        useDate.setText(format.format(date));
        useInfo.setUseDate(format.format(date));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Bundle bundle = data.getExtras();
            if (requestCode==1){
                useInfo.setDeviceID(bundle.getString("deviceID"));
                useInfo.setDeviceName(bundle.getString("deviceName"));
                deviceID.setText(bundle.getString("deviceID"));
                deviceName.setText(bundle.getString("deviceName"));
            }else if (requestCode==2) {
                useInfo.setUserID(bundle.getString("userID"));
                useInfo.setUserName(bundle.getString("userName"));
                userName.setText(bundle.getString("userName"));
                userID.setText(bundle.getString("userID"));
            }
        }catch (Exception e){
            Log.d("ERROR_In_UseInfo",""+e);
        }
    }
    /**通过子线程联网访问数据*/
    private void ModifyUseInfo() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                //若方法返回为true,显示添加成功
                if (DB_UseInfo.modifyUseInfo(useInfo,previousID)) {
                    msg.what = 1001;
                }else {
                    msg.what = 1002;
                }
                mHandler.sendMessage(msg);
            }
        };
        new Thread(run).start();

    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    Toast.makeText(Up_UseInfo.this, " 修改成功",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    Toast.makeText(Up_UseInfo.this, " 修改失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

}
