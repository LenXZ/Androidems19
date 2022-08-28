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
import android.text.TextUtils;
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
import com.yxz.amadeus.ems.Activity.Interface.RepairInfoList;
import com.yxz.amadeus.ems.DAO.DB_RepairInfo;
import com.yxz.amadeus.ems.entity.Device;
import com.yxz.amadeus.ems.entity.RepairInfo;
import com.yxz.amadeus.ems.R;

public class Up_RepairInfo extends AppCompatActivity {
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private RepairInfo repairInfo;//记录修改后的数据
    private String previousID;//记录原设备ID
    private String select="",identity="",myID="";
    private Button confirm, cancel, selectDevice,selectUser, damageDate_Date, damageDate_Time,repairDate_Date,repairDate_Time;
    private EditText infoID, deviceID,  damageDegree, damageCause,  repairPersonnel, state, userID, username, cost;
    private TextView userName, deviceName,damageDate,repairDate,principalName;
    DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//多态
    Calendar calendar0 = Calendar.getInstance(Locale.CHINA);//获取日期格式器对象
    Calendar calendar1 = Calendar.getInstance(Locale.CHINA);//获取日期格式器对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_repairinfo);
        confirm = (Button) findViewById(R.id.in_content);
        cancel = (Button) findViewById(R.id.in_cancel);
        selectUser = (Button) findViewById(R.id.selectUser);
        selectDevice=(Button) findViewById(R.id.selectDevice);
        damageDate_Date = (Button) findViewById(R.id.damageDate_Date);
        damageDate_Time = (Button) findViewById(R.id.damageDate_Time);
        repairDate_Date = (Button) findViewById(R.id.repairDate_Date);
        repairDate_Time = (Button) findViewById(R.id.repairDate_Time);
        infoID = (EditText) findViewById(R.id.infoID);
        deviceID=(EditText) findViewById(R.id.deviceID);
        deviceName = (TextView) findViewById(R.id.deviceName);
        damageDate = (TextView) findViewById(R.id.damageDate);
        spinner = (Spinner) findViewById(R.id.damageDegree);
        damageCause = (EditText) findViewById(R.id.damageCause);
        repairDate = (TextView) findViewById(R.id.repairDate);
        repairPersonnel = (EditText) findViewById(R.id.repairPersonnel);
        state = (EditText) findViewById(R.id.state);
        cost = (EditText) findViewById(R.id.cost);
//        cost.setInputType(InputType.TYPE_CLASS_NUMBER);
        userID = (EditText) findViewById(R.id.userID);
        userName = (TextView) findViewById(R.id.userName);
        principalName = (TextView) findViewById(R.id.principalName);
        repairInfo = new RepairInfo();
        try{
            //接收点击的数据
            Bundle bundle=getIntent().getExtras();
            try{
                identity=bundle.getString("identity");
                myID=bundle.getString("myID");
            }catch (Exception e){

            }
            if(identity.equals("B")){
                deviceID.setFocusableInTouchMode(false);
            }
            previousID=bundle.getString("infoID");
            repairInfo.setInfoID(bundle.getString("infoID"));
            repairInfo.setDeviceID(bundle.getString("deviceID"));
            repairInfo.setDeviceName(bundle.getString("deviceName"));
            repairInfo.setDamageDate(bundle.getString("damageDate"));
            repairInfo.setDamageDegree(bundle.getString("damageDegree"));
            repairInfo.setDamageCause(bundle.getString("damageCause"));
            repairInfo.setRepairDate(bundle.getString("repairDate"));
            repairInfo.setRepairPersonnel(bundle.getString("repairPersonnel"));
            repairInfo.setState(bundle.getString("state"));
            repairInfo.setUserID(bundle.getString("userID"));
            repairInfo.setUsername(bundle.getString("userName"));
            repairInfo.setCost(bundle.getFloat("cost"));
            repairInfo.setPrincipalName("principalName");
            infoID.setText(bundle.getString("infoID"));
            deviceID.setText(bundle.getString("deviceID"));
            deviceName.setText(bundle.getString("deviceName"));
            damageDate.setText(bundle.getString("damageDate"));
            damageCause.setText(bundle.getString("damageCause"));
            repairDate.setText(bundle.getString("repairDate"));
            repairPersonnel.setText(bundle.getString("repairPersonnel"));
            state.setText(bundle.getString("state"));
            userID.setText(bundle.getString("userID"));
            userName.setText(bundle.getString("userName"));
            cost.setText(String.valueOf(bundle.getFloat("cost")));
            principalName.setText(bundle.getString("principalName"));
        }catch (Exception e){
            Log.d("ERROR_Up_RepairInfo",""+e);
        }
        //数据
        data_list = new ArrayList<String>();
        if (repairInfo.getDamageDegree().equals("A")){
            data_list.add("轻微损坏");
        }else if (repairInfo.getDamageDegree().equals("B")){
            data_list.add("中度损坏");
        }else if (repairInfo.getDamageDegree().equals("C")){
            data_list.add("重度损坏");
        }else {
            data_list.add("请选择损坏程度");
        }
        data_list.add("轻微损坏");//012
        data_list.add("中度损坏");
        data_list.add("重度损坏");
        //适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //识别下拉列表,向Account返回用户类型
                if (position==1){
                    repairInfo.setDamageDegree("A");
                }else if (position==2){
                    repairInfo.setDamageDegree("B");
                }else if (position==3){
                    repairInfo.setDamageDegree("C");
                }else {
                }
//                Log.d("" + position, "您选择的是" + arr_adapter.getItem(position));
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
                repairInfo.setInfoID(s.toString());
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
                repairInfo.setDeviceID(s.toString());
            }
        });
        damageCause.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                repairInfo.setDamageCause(s.toString());
            }
        });
        repairPersonnel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    repairInfo.setRepairPersonnel(s.toString());
                }catch (Exception e){
                    Log.d("ERROR_In_RepairInfo",""+e);
                }
            }
        });
        state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                repairInfo.setState(s.toString());
            }
        });
        cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    repairInfo.setCost(Float.valueOf(s.toString()));
                } catch (Exception e) {
                    Log.d("ERROR_In_RepairInfo", "" + e);
                }
            }
        });
        //点击确定
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repairInfo.getInfoID() == null) {
                    Toast.makeText(Up_RepairInfo.this, "信息编号不能为空",
                            Toast.LENGTH_SHORT).show();
                } else if (repairInfo.getDeviceID() == null) {
                    Toast.makeText(Up_RepairInfo.this, "设备编号不能为空",
                            Toast.LENGTH_SHORT).show();
                }else if (repairInfo.getUserID()==null){
                    Toast.makeText(Up_RepairInfo.this, "维修负责人不能为空",
                            Toast.LENGTH_SHORT).show();
                }else {
                    ModifyRepairInfo();
                }

            }
        });
        //点击取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(identity.equals("B")){
//                    Intent intent=new Intent(Up_RepairInfo.this, RepairInfoList.class);
//                    Bundle bundle=new Bundle();
//                    bundle.putString("identity",identity);
//                    bundle.putString("myID",myID);
//                    bundle.putBoolean("bo_select",false);
//                    bundle.putString("select","");//向用户列表界面传递搜索内容
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }else {
//                    startActivity(new Intent(Up_RepairInfo.this, RepairInfoList.class));
//                }
                loading();
                finish();
            }
        });
        selectDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Up_RepairInfo.this,DeviceList.class);
                Bundle bundle1=new Bundle();
                bundle1.putInt("jump",3);
                bundle1.putString("identity",identity);
                bundle1.putString("myID",myID);
                bundle1.putBoolean("bo_select",false);
                bundle1.putString("select","");//向用户列表界面传递搜索内容
                intent.putExtras(bundle1);
                startActivityForResult(intent,3);
            }
        });
        selectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Up_RepairInfo.this, AccountList.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("jump", 33);
                intent.putExtras(bundle1);
                startActivityForResult(intent, 2);
            }
        });
        damageDate_Date.setOnClickListener(new View.OnClickListener() {//设置按钮的点击事件监听器
            @Override
            public void onClick(View v) {
                //生成一个DatePickerDialog对象，并显示。显示的DatePickerDialog控件可以选择年月日，并设置
                DatePickerDialog datePickerDialog = new DatePickerDialog(Up_RepairInfo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //修改日历控件的年，月，日
                        //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
                        calendar0.set(Calendar.YEAR, year);
                        calendar0.set(Calendar.MONTH, month);
                        calendar0.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDamageDate();
                    }
                }, calendar0.get(Calendar.YEAR), calendar0.get(Calendar.MONTH), calendar0.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        damageDate_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(Up_RepairInfo.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //同DatePickerDialog控件
                        calendar0.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar0.set(Calendar.MINUTE, minute);
                        updateDamageDate(); //将页面TextView的显示更新为最新时间
                    }
                }, calendar0.get(Calendar.HOUR_OF_DAY), calendar0.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });
        repairDate_Date.setOnClickListener(new View.OnClickListener() {//设置按钮的点击事件监听器
            @Override
            public void onClick(View v) {
                //生成一个DatePickerDialog对象，并显示。显示的DatePickerDialog控件可以选择年月日，并设置
                DatePickerDialog datePickerDialog = new DatePickerDialog(Up_RepairInfo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //修改日历控件的年，月，日
                        //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
                        calendar1.set(Calendar.YEAR, year);
                        calendar1.set(Calendar.MONTH, month);
                        calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateRepairDate();
                    }
                }, calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        repairDate_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(Up_RepairInfo.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //同DatePickerDialog控件
                        calendar1.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar1.set(Calendar.MINUTE, minute);
                        updateRepairDate(); //将页面TextView的显示更新为最新时间
                    }
                }, calendar1.get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });

    }
    private void loading(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载中");
        dialog.show();
    }
    private void updateDamageDate() { //将页面TextView的显示更新为最新时间
        Date date=calendar0.getTime();
        damageDate.setText(format.format(date));
        repairInfo.setDamageDate(format.format(date));
    }
    private void updateRepairDate() { //将页面TextView的显示更新为最新时间
        Date date=calendar1.getTime();
        repairDate.setText(format.format(date));
        repairInfo.setRepairDate(format.format(date));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Bundle bundle = data.getExtras();
            if (requestCode==2){
                repairInfo.setUserID(bundle.getString("userID"));
                repairInfo.setUsername(bundle.getString("userName"));
                userName.setText(bundle.getString("userName"));
                userID.setText(bundle.getString("userID"));
            }else {
                repairInfo.setDeviceID(bundle.getString("deviceID"));
                repairInfo.setDeviceName(bundle.getString("deviceName"));
                repairInfo.setUserID(bundle.getString("userID"));
                repairInfo.setUsername(bundle.getString("userName"));
                deviceID.setText(bundle.getString("deviceID"));
                deviceName.setText(bundle.getString("deviceName"));
                principalName.setText(bundle.getString("userName"));
            }
        }catch (Exception e){
            Log.d("ERROR_In_RepairInfo",""+e);
        }
    }
    /**通过子线程联网访问数据*/
    private void ModifyRepairInfo() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                //若方法返回为true,显示添加成功
                if (DB_RepairInfo.modifyRepairInfo(repairInfo,previousID)) {
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
                    Toast.makeText(Up_RepairInfo.this, " 修改成功",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    Toast.makeText(Up_RepairInfo.this, " 修改失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

}
