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
import android.text.InputType;
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
import com.yxz.amadeus.ems.Activity.LoginActivity;
import com.yxz.amadeus.ems.DAO.DB_Device;
import com.yxz.amadeus.ems.entity.Device;
import com.yxz.amadeus.ems.R;

public class Up_Device extends AppCompatActivity {
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private Device device;//记录修改后的数据
    private String previousID;//记录原设备ID
    private String identity,myID;
    private Button confirm, cancel,selectUser,recordDate_Date, recordDate_Time;
    private EditText deviceID, deviceName, model, manufactor, userID,maintenanceTimes;
    private TextView userName, recordDate;
    DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//多态
    Calendar calendar = Calendar.getInstance(Locale.CHINA);//获取日期格式器对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_device);
        spinner = (Spinner) findViewById(R.id.in_type);
        confirm = (Button) findViewById(R.id.in_content);
        cancel = (Button) findViewById(R.id.in_cancel);
        selectUser=(Button) findViewById(R.id.selectUser);
        recordDate_Date = (Button) findViewById(R.id.recordDate_Date);
        recordDate_Time = (Button) findViewById(R.id.recordDate_Time);
        deviceID = (EditText) findViewById(R.id.deviceID);
        deviceName = (EditText) findViewById(R.id.deviceName);
        model = (EditText) findViewById(R.id.model);
        manufactor = (EditText) findViewById(R.id.manufactor);
        recordDate = (TextView) findViewById(R.id.recordDate);
        userID = (EditText) findViewById(R.id.userID);
        userName=(TextView)findViewById(R.id.userName);
        maintenanceTimes = (EditText) findViewById(R.id.maintenanceTimes);
        maintenanceTimes.setInputType( InputType.TYPE_CLASS_NUMBER);

        device = new Device();
        try{
            //接收点击的数据
            Bundle bundle=getIntent().getExtras();
            device.setDeviceID(bundle.getString("deviceID"));
            device.setDeviceName(bundle.getString("deviceName"));
            device.setModel(bundle.getString("model"));
            device.setManufactor(bundle.getString("manufactor"));
            device.setRecordDate(bundle.getString("recordDate"));
            device.setUserName(bundle.getString("userName"));
            device.setUserID(bundle.getString("userID"));
            device.setMaintenanceTimes(bundle.getInt("maintenanceTimes"));
            //在EditText上显示数据
            deviceID.setText(bundle.getString("deviceID"));
            deviceName.setText(bundle.getString("deviceName"));
            model.setText(bundle.getString("model"));
            manufactor.setText(bundle.getString("manufactor"));
            recordDate.setText(bundle.getString("recordDate"));
            userID.setText(bundle.getString("userID"));
            userName.setText(bundle.getString("userName"));
            maintenanceTimes.setText(String.valueOf(bundle.getInt("maintenanceTimes")));
            previousID=device.getDeviceID();
            try {
                identity = bundle.getString("identity");
                myID = bundle.getString("myID");
                if(identity.equals("B")){
                    userID.setText(myID);
                    userID.setFocusableInTouchMode(false);
                }
            } catch (Exception e) {

            }
        }catch (Exception e){
            Log.d("ERROR_Up_Device",""+e);
        }
        deviceID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                device.setDeviceID(s.toString());
            }
        });
        deviceName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                device.setDeviceName(s.toString());
            }
        });
        model.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                device.setModel(s.toString());
            }
        });
        manufactor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                device.setManufactor(s.toString());
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
                device.setUserID(s.toString());
            }
        });
        maintenanceTimes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    device.setMaintenanceTimes(Integer.parseInt(s.toString()));
                }catch (Exception e){
                    Log.d("ERROR_Up_Device",""+e);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    device.setMaintenanceTimes(Integer.parseInt(s.toString()));
                }catch (Exception e){
                    Log.d("ERROR_Up_Device",""+e);
                }
            }
        });
        //点击确定
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (device.getDeviceID()==null){
                    Toast.makeText(Up_Device.this, "设备编号不能为空",
                            Toast.LENGTH_SHORT).show();
                }else if (device.getDeviceName()==null){
                    Toast.makeText(Up_Device.this, "设备名称不能为空",
                            Toast.LENGTH_SHORT).show();
                }else if (device.getUserID()==null){
                    Toast.makeText(Up_Device.this, "负责人不能为空",
                            Toast.LENGTH_SHORT).show();
                }else {
                    ModifyDevice();
                }

            }
        });
        //点击取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(Up_Device.this,DeviceList.class));
                loading();
                finish();
            }
        });
        selectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(identity.equals("B")){
                    Toast.makeText(Up_Device.this,"不可更改负责人",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(Up_Device.this, AccountList.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("jump", 3);
                    intent.putExtras(bundle1);
                    startActivityForResult(intent, 3);
                }

            }
        });
        recordDate_Date.setOnClickListener(new View.OnClickListener() {//设置按钮的点击事件监听器
            @Override
            public void onClick(View v) {
                //生成一个DatePickerDialog对象，并显示。显示的DatePickerDialog控件可以选择年月日，并设置
                DatePickerDialog datePickerDialog = new DatePickerDialog(Up_Device.this, new DatePickerDialog.OnDateSetListener() {
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
        recordDate_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(Up_Device.this, new TimePickerDialog.OnTimeSetListener() {
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
    private void updateDate() { //将页面TextView的显示更新为最新时间
        Date date=calendar.getTime();
        recordDate.setText(format.format(date));
        device.setRecordDate(format.format(date));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Bundle bundle = data.getExtras();
            device.setUserID(bundle.getString("userID"));
            device.setUserName(bundle.getString("userName"));
            userName.setText(bundle.getString("userName"));
            userID.setText(bundle.getString("userID"));
        }catch (Exception e){
            Log.d("ERROR_In_Device",""+e);
        }
    }
    private void loading(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载中");
        dialog.show();
    }
    /**通过子线程联网访问数据*/
    /***/
    private void ModifyDevice() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                //若方法返回为true,显示添加成功
                if (DB_Device.modifyDevice(device,previousID)) {
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
                    Toast.makeText(Up_Device.this, " 修改成功",
                                Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    Toast.makeText(Up_Device.this, " 修改失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

}
