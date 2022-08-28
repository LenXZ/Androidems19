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
import com.yxz.amadeus.ems.Activity.Interface.CheckInfoList;
import com.yxz.amadeus.ems.DAO.DB_CheckInfo;
import com.yxz.amadeus.ems.DAO.DB_RepairInfo;
import com.yxz.amadeus.ems.entity.CheckInfo;
import com.yxz.amadeus.ems.R;

public class Up_CheckInfo extends AppCompatActivity {
    private Spinner state0,state1;
    private List<String> data_list0,data_list1;
    private ArrayAdapter<String> arr_adapter0,arr_adapter1;
    private CheckInfo checkInfo;//记录修改后的数据
    private String previousID;//记录原设备ID
    private String select="",identity="",myID="";
    private String state="HV",s_deviceID="";
    private Button confirm, cancel, selectDevice, selectUser;
    private EditText infoID, deviceID,inspector, cycle;
    private TextView userName, deviceName, inspectorName;
    private TextView lastCheck,nextCheck, lastRepair;//定义一个TextView控件对象,显示得到的时间日期
    private Button  lastCheck_Date, lastCheck_Time,nextCheck_Date,nextCheck_Time,lastRepair_Date,lastRepair_Time;
    DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//多态
    Calendar calendar0 = Calendar.getInstance(Locale.CHINA);//获取日期格式器对象
    Calendar calendar1 = Calendar.getInstance(Locale.CHINA);//获取日期格式器对象
    Calendar calendar2 = Calendar.getInstance(Locale.CHINA);//获取日期格式器对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_checkinfo);
        confirm = (Button) findViewById(R.id.in_content);
        cancel = (Button) findViewById(R.id.in_cancel);
        selectDevice = (Button) findViewById(R.id.selectDevice);
        selectUser = (Button) findViewById(R.id.selectUser);
        nextCheck_Date = (Button) findViewById(R.id.nextCheck_Date);
        nextCheck_Time = (Button) findViewById(R.id.nextCheck_Time);
        lastRepair_Date = (Button) findViewById(R.id.lastRepair_Date);
        lastRepair_Time = (Button) findViewById(R.id.lastRepair_Time);
        lastCheck_Date = ((Button) findViewById(R.id.lastCheck_Date));//得到页面设定日期的按钮控件对象
        lastCheck_Time = ((Button) findViewById(R.id.lastCheck_Time));
        infoID = (EditText) findViewById(R.id.infoID);
        deviceID = (EditText) findViewById(R.id.deviceID);
        deviceName = (TextView) findViewById(R.id.deviceName);
        cycle = (EditText) findViewById(R.id.cycle);
        cycle.setInputType(InputType.TYPE_CLASS_NUMBER);
        state0 = (Spinner) findViewById(R.id.state0);
        state1 = (Spinner) findViewById(R.id.state1);
        inspector = (EditText) findViewById(R.id.inspectorID);
        inspectorName=(TextView)findViewById(R.id.inspectorName);
        userName = (TextView) findViewById(R.id.userName);
        lastCheck = (TextView) findViewById(R.id.lastCheck);
        nextCheck = (TextView) findViewById(R.id.nextCheck);
        lastRepair = (TextView) findViewById(R.id.lastRepair);
        checkInfo = new CheckInfo();
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
            checkInfo.setInfoID(bundle.getString("infoID"));
            checkInfo.setDeviceID(bundle.getString("deviceID"));
            s_deviceID=(bundle.getString("deviceID"));
            FindLastDate();
            checkInfo.setDeviceName(bundle.getString("deviceName"));
            checkInfo.setCycle(bundle.getInt("cycle"));
            checkInfo.setLastCheck(bundle.getString("lastCheck"));
            checkInfo.setNextCheck(bundle.getString("nextCheck"));
            checkInfo.setLastRepair(bundle.getString("lastRepair"));
            checkInfo.setState(bundle.getString("state"));
            checkInfo.setInspector(bundle.getString("inspector"));
            checkInfo.setInspectorName(bundle.getString("inspectorName"));
            checkInfo.setUserID(bundle.getString("userID"));
            checkInfo.setUserName(bundle.getString("userName"));
            infoID.setText(bundle.getString("infoID"));
            deviceID.setText(bundle.getString("deviceID"));
            deviceName.setText(bundle.getString("deviceName"));
            cycle.setText(String.valueOf(bundle.getInt("cycle")));
            lastCheck.setText(bundle.getString("lastCheck"));
            nextCheck.setText(bundle.getString("nextCheck"));
            lastRepair.setText(bundle.getString("lastRepair"));
            inspector.setText(bundle.getString("inspector"));
            inspectorName.setText(bundle.getString("inspectorName"));
            userName.setText(bundle.getString("userName"));
        }catch (Exception e){
            Log.d("ERROR_Up_CheckInfo",""+e);
        }
        //数据
        data_list0 = new ArrayList<String>();
        try{
            if(checkInfo.getState().indexOf("A")!=-1){
                data_list0.add("正常");
            }else if(checkInfo.getState().indexOf("B")!=-1){
                data_list0.add("异常");
            }else if(checkInfo.getState().indexOf("C")!=-1){
                data_list0.add("损坏");
            }else if(checkInfo.getState().indexOf("D")!=-1){
                data_list0.add("报废");
            }else if(checkInfo.getState().indexOf("E")!=-1){
                data_list0.add("数量缺失");
            }else {
                data_list0.add("请选择设备状态");
            }
        }catch (Exception e){

        }
        data_list0.add("正常");//012
        data_list0.add("异常");
        data_list0.add("损坏");
        data_list0.add("报废");
        data_list0.add("数量缺失");
        data_list1 = new ArrayList<String>();
        try{
            if(checkInfo.getState().indexOf("X")!=-1){
                data_list1.add("待清洁");
            }else if(checkInfo.getState().indexOf("Y")!=-1){
                data_list1.add("已清洁");
            }else {
                data_list1.add("请选择清洁状态");
            }
        }catch (Exception e){

        }
        data_list1.add("待清洁");
        data_list1.add("已清洁");
        //适配器
        arr_adapter0 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list0);
        //设置样式
        arr_adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        state0.setAdapter(arr_adapter0);
        state0.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //识别下拉列表
                if (position == 1) {
                    state=state.replaceAll("[A-H]","A");
                } else if (position == 2) {
                    state=state.replaceAll("[A-H]","B");
                } else if (position == 3) {
                    state=state.replaceAll("[A-H]","C");
                } else if (position == 4) {
                    state=state.replaceAll("[A-H]","D");
                }else if (position == 5) {
                    state=state.replaceAll("[A-H]","E");
                }
                checkInfo.setState(state);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //适配器
        arr_adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list1);
        //设置样式
        arr_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        state1.setAdapter(arr_adapter1);
        state1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //识别下拉列表
                if (position == 1) {
                    state=state.replaceAll("[V-Z]","X");
                } else if (position == 2) {
                    state=state.replaceAll("[V-Z]","Y");
                }
                checkInfo.setState(state);
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
                checkInfo.setInfoID(s.toString());
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
                checkInfo.setDeviceID(s.toString());
            }
        });
        cycle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkInfo.setCycle(Integer.valueOf(s.toString()));
            }
        });
        inspector.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkInfo.setInspector(s.toString());
            }
        });
        //点击确定
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInfo.getInfoID() == null) {
                    Toast.makeText(Up_CheckInfo.this, "信息编号不能为空",
                            Toast.LENGTH_SHORT).show();
                } else if (checkInfo.getDeviceID() == null) {
                    Toast.makeText(Up_CheckInfo.this, "设备编号不能为空",
                            Toast.LENGTH_SHORT).show();
                } else if(checkInfo.getInspector()==null){
                    Toast.makeText(Up_CheckInfo.this, "检验人不能为空",
                            Toast.LENGTH_SHORT).show();
                }else {
                    ModifyCheckInfo();
                }

            }
        });
        //点击取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(identity.equals("B")){
//                    Intent intent=new Intent(Up_CheckInfo.this, CheckInfoList.class);
//                    Bundle bundle=new Bundle();
//                    bundle.putString("identity",identity);
//                    bundle.putString("myID",myID);
//                    bundle.putBoolean("bo_select",false);
//                    bundle.putString("select","");//向用户列表界面传递搜索内容
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }else {
//                    startActivity(new Intent(Up_CheckInfo.this, CheckInfoList.class));
//                }
                loading();
                finish();
            }
        });
        selectDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Up_CheckInfo.this, DeviceList.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("jump", 23);
                bundle1.putString("identity",identity);
                bundle1.putString("myID",myID);
                intent.putExtras(bundle1);
                startActivityForResult(intent, 1);
            }
        });
        selectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Up_CheckInfo.this, AccountList.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("jump", 23);
                intent.putExtras(bundle1);
                startActivityForResult(intent, 2);
            }
        });
        lastCheck_Date.setOnClickListener(new View.OnClickListener() {//设置按钮的点击事件监听器
            @Override
            public void onClick(View v) {
                //生成一个DatePickerDialog对象，并显示。显示的DatePickerDialog控件可以选择年月日，并设置
                DatePickerDialog datePickerDialog = new DatePickerDialog(Up_CheckInfo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //修改日历控件的年，月，日
                        //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
                        calendar0.set(Calendar.YEAR, year);
                        calendar0.set(Calendar.MONTH, month);
                        calendar0.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLastCheck();
                    }
                }, calendar0.get(Calendar.YEAR), calendar0.get(Calendar.MONTH), calendar0.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        lastCheck_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(Up_CheckInfo.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //同DatePickerDialog控件
                        calendar0.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar0.set(Calendar.MINUTE, minute);
                        updateLastCheck(); //将页面TextView的显示更新为最新时间
                    }
                }, calendar0.get(Calendar.HOUR_OF_DAY), calendar0.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });
        nextCheck_Date.setOnClickListener(new View.OnClickListener() {//设置按钮的点击事件监听器
            @Override
            public void onClick(View v) {
                //生成一个DatePickerDialog对象，并显示。显示的DatePickerDialog控件可以选择年月日，并设置
                DatePickerDialog datePickerDialog = new DatePickerDialog(Up_CheckInfo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //修改日历控件的年，月，日
                        //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
                        calendar1.set(Calendar.YEAR, year);
                        calendar1.set(Calendar.MONTH, month);
                        calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateNextCheck();
                    }
                }, calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        nextCheck_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(Up_CheckInfo.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //同DatePickerDialog控件
                        calendar1.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar1.set(Calendar.MINUTE, minute);
                        updateNextCheck(); //将页面TextView的显示更新为最新时间
                    }
                }, calendar1.get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });
        lastRepair_Date.setOnClickListener(new View.OnClickListener() {//设置按钮的点击事件监听器
            @Override
            public void onClick(View v) {
                //生成一个DatePickerDialog对象，并显示。显示的DatePickerDialog控件可以选择年月日，并设置
                DatePickerDialog datePickerDialog = new DatePickerDialog(Up_CheckInfo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //修改日历控件的年，月，日
                        //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
                        calendar2.set(Calendar.YEAR, year);
                        calendar2.set(Calendar.MONTH, month);
                        calendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLastRepair();
                    }
                }, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        lastRepair_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(Up_CheckInfo.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //同DatePickerDialog控件
                        calendar2.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar2.set(Calendar.MINUTE, minute);
                        updateLastRepair(); //将页面TextView的显示更新为最新时间
                    }
                }, calendar2.get(Calendar.HOUR_OF_DAY), calendar2.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });
    }
    private void loading(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载中");
        dialog.show();
    }
    private void updateLastCheck() { //将页面TextView的显示更新为最新时间
        Date date=calendar0.getTime();
        lastCheck.setText(format.format(date));
        checkInfo.setLastCheck(format.format(date));
        calendar0.setTime(date);//设置起时间
        calendar0.add(Calendar.DATE, checkInfo.getCycle());
        nextCheck.setText(format.format(calendar0.getTime()));
        checkInfo.setNextCheck(format.format(calendar0.getTime()));
    }
    private void updateNextCheck() { //将页面TextView的显示更新为最新时间
        Date date=calendar1.getTime();
        nextCheck.setText(format.format(date));
        checkInfo.setNextCheck(format.format(date));
    }
    private void updateLastRepair() { //将页面TextView的显示更新为最新时间
        Date date=calendar2.getTime();
        lastRepair.setText(format.format(date));
        checkInfo.setLastRepair(format.format(date));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Bundle bundle = data.getExtras();
            if (requestCode==1){
                checkInfo.setDeviceID(bundle.getString("deviceID"));
                checkInfo.setDeviceName(bundle.getString("deviceName"));
                deviceID.setText(bundle.getString("deviceID"));
                deviceName.setText(bundle.getString("deviceName"));
                s_deviceID=(bundle.getString("deviceID"));
                FindLastDate();
            }else if (requestCode==2) {
                checkInfo.setUserID(bundle.getString("userID"));
                checkInfo.setUserName(bundle.getString("userName"));
                inspector.setText(bundle.getString("userID"));
                inspectorName.setText(bundle.getString("userName"));
            }
        }catch (Exception e){
            Log.d("ERROR_In_CheckInfo",""+e);
        }
    }
    /**通过子线程联网访问数据*/
    private void ModifyCheckInfo() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                //若方法返回为true,显示添加成功
                if (DB_CheckInfo.modifyCheckInfo(checkInfo,previousID)) {
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
                    Toast.makeText(Up_CheckInfo.this, " 修改成功",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    Toast.makeText(Up_CheckInfo.this, " 修改失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private void FindLastDate() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                Bundle data = new Bundle();
                msg.what=1001;
                data.putString("LateRepair",DB_RepairInfo.findLateRepair(s_deviceID));
                msg.setData(data);
                mHandler2.sendMessage(msg);
            }
        };
        new Thread(run).start();

    }

    Handler mHandler2 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    Bundle bundle = msg.getData();
                    lastRepair.setText(bundle.getString("LateRepair"));
                    checkInfo.setLastRepair(bundle.getString("LateRepair"));
                    break;
                default:
                    break;
            }
        }
    };
}
