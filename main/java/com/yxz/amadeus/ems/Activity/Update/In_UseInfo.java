package com.yxz.amadeus.ems.Activity.Update;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yxz.amadeus.ems.Activity.Interface.AccountList;
import com.yxz.amadeus.ems.Activity.Interface.DeviceList;
import com.yxz.amadeus.ems.Activity.Interface.InterfaceB;
import com.yxz.amadeus.ems.Activity.Interface.UseInfoList;
import com.yxz.amadeus.ems.DAO.DB_UseInfo;
import com.yxz.amadeus.ems.entity.UseInfo;
import com.yxz.amadeus.ems.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class In_UseInfo extends AppCompatActivity {
    private Spinner statusBefore, statusAfter;
    private String select="",identity="",myID="",myName="";
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private UseInfo useInfo;
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
        try {
            //接收点击的数据
            Bundle bundle = getIntent().getExtras();
            try{
                identity=bundle.getString("identity");
                myID=bundle.getString("myID");
                myName=bundle.getString("myName");
            }catch (Exception e){

            }
            useInfo.setDeviceID(bundle.getString("deviceID"));
            useInfo.setDeviceName(bundle.getString("deviceName"));
            deviceID.setText(bundle.getString("deviceID"));
            deviceName.setText(bundle.getString("deviceName"));
            if(identity.equals("A")){
                useInfo.setAdopt("Y");
            }else if(identity.equals("B")){
                deviceID.setFocusableInTouchMode(false);
                useInfo.setAdopt("Y");
            }else if(identity.equals("C")){
                userID.setFocusableInTouchMode(false);
                useInfo.setUserID(myID);
                useInfo.setUserName(myName);
                userID.setText(myID);
                userName.setText(myName);
                useInfo.setAdopt("N");
            }
        } catch (Exception e) {

        }
        useInfo.setInfoID(DB_UseInfo.getID());
        infoID.setText(useInfo.getInfoID());
        //数据A正常B异常C损坏D报废
        data_list = new ArrayList<String>();
        data_list.add("正常");//012
        data_list.add("异常");
        data_list.add("损坏");
        data_list.add("报废");
        //适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        statusBefore.setAdapter(arr_adapter);
        statusBefore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //识别下拉列表
                if (position == 0) {
                    useInfo.setStatusBefore("A");
                } else if (position == 1) {
                    useInfo.setStatusBefore("B");
                } else if (position == 2) {
                    useInfo.setStatusBefore("C");
                } else if (position == 3) {
                    useInfo.setStatusBefore("D");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        statusAfter.setAdapter(arr_adapter);
        statusAfter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //识别下拉列表
                if (position == 0) {
                    useInfo.setStatusAfter("A");
                } else if (position == 1) {
                    useInfo.setStatusAfter("B");
                } else if (position == 2) {
                    useInfo.setStatusAfter("C");
                } else if (position == 3) {
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
                    Toast.makeText(In_UseInfo.this, "信息编号不能为空",
                            Toast.LENGTH_SHORT).show();
                } else if (useInfo.getDeviceID() == null) {
                    Toast.makeText(In_UseInfo.this, "设备编号不能为空",
                            Toast.LENGTH_SHORT).show();
                }else if (useInfo.getUserID()==null){
                    Toast.makeText(In_UseInfo.this, "使用人不能为空",
                            Toast.LENGTH_SHORT).show();
                }else {
                    AddUseInfo();
                }

            }
        });
        //点击取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(identity.equals("B")||identity.equals("BC")){
//                    Intent intent=new Intent(In_UseInfo.this, UseInfoList.class);
//                    Bundle bundle=new Bundle();
//                    bundle.putString("identity",identity);
//                    bundle.putString("myID",myID);
//                    bundle.putBoolean("bo_select",false);
//                    bundle.putString("select","");//向用户列表界面传递搜索内容
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }else if(identity.equals("C")){
//                    Intent intent=new Intent(In_UseInfo.this, InterfaceB.class);
//                    Bundle bundle=new Bundle();
//                    bundle.putString("myID",myID);
//                    intent.putExtras(bundle);
//                } else{
//                    startActivity(new Intent(In_UseInfo.this, UseInfoList.class));
//                }
                loading();
                finish();
            }
        });
        selectDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(In_UseInfo.this, DeviceList.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("jump", 12);
                bundle1.putString("identity",identity);
                bundle1.putString("myID",myID);
                intent.putExtras(bundle1);
                startActivityForResult(intent, 1);
            }
        });
        selectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(identity.equals("C")){
                    Toast.makeText(In_UseInfo.this, "你不能选择其他用户",
                            Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(In_UseInfo.this, AccountList.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("jump", 12);
                    intent.putExtras(bundle1);
                    startActivityForResult(intent, 2);
                }
            }
        });
        useDate_Date.setOnClickListener(new View.OnClickListener() {//设置按钮的点击事件监听器
            @Override
            public void onClick(View v) {
                //生成一个DatePickerDialog对象，并显示。显示的DatePickerDialog控件可以选择年月日，并设置
                DatePickerDialog datePickerDialog = new DatePickerDialog(In_UseInfo.this, new DatePickerDialog.OnDateSetListener() {
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(In_UseInfo.this, new TimePickerDialog.OnTimeSetListener() {
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
        } catch (Exception e) {
            Log.d("ERROR_In_UseInfo", "" + e);
        }
    }

    /**
     * 通过子线程联网访问数据
     */
    private void AddUseInfo() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                //若方法返回为true,显示添加成功
                if (DB_UseInfo.addUseInfo(useInfo)) {
                    msg.what = 1001;
                } else {
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
                    Toast.makeText(In_UseInfo.this, " 添加成功",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    Toast.makeText(In_UseInfo.this, " 添加失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

}
