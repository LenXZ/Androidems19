package com.yxz.amadeus.ems.Activity.Interface;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.yxz.amadeus.ems.Activity.Update.Up_Account;
import com.yxz.amadeus.ems.DAO.DB_Account;
import com.yxz.amadeus.ems.R;
import com.yxz.amadeus.ems.entity.Account;

import java.util.ArrayList;
import java.util.List;

public class InterfaceB extends AppCompatActivity {
    Button btn_device,btn_useInfo,btn_repairInfo,btn_checkInfo,btn_checkUseInfo,btn_modifyMyInfo;
    String myID;
    private List<Account> data = new ArrayList<Account>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface_b);
        btn_device=(Button)findViewById(R.id.btn_device);
        btn_useInfo=(Button)findViewById(R.id.btn_useInfo);
        btn_repairInfo=(Button)findViewById(R.id.btn_repairInfo);
        btn_checkInfo=(Button)findViewById(R.id.btn_checkInfo);
        btn_checkUseInfo=(Button)findViewById(R.id.btn_checkUseInfo);
        btn_modifyMyInfo=(Button)findViewById(R.id.btn_modifyMyInfo);
        try{
            Bundle bundle=getIntent().getExtras();
            myID=bundle.getString("myID");
        }catch (Exception e){

        }
        btn_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InterfaceB.this,DeviceList.class);
                startAct(intent);
            }
        });
        btn_useInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InterfaceB.this,UseInfoList.class);
                startAct(intent);
            }
        });
        btn_repairInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InterfaceB.this,RepairInfoList.class);
                startAct(intent);
            }
        });
        btn_checkInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InterfaceB.this,CheckInfoList.class);
                startAct(intent);
            }
        });
        btn_checkUseInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InterfaceB.this,UseInfoList.class);
                Bundle bundle=new Bundle();
                bundle.putString("identity","BC");
                bundle.putString("myID",myID);
                bundle.putBoolean("bo_select",false);
                bundle.putString("select","");//向用户列表界面传递搜索内容
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btn_modifyMyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyMyInfo();
            }
        });
    }
    private void startAct(Intent intent){
        Bundle bundle=new Bundle();
        bundle.putString("identity","B");
        bundle.putString("myID",myID);
        bundle.putBoolean("bo_select",false);
        bundle.putString("select","");//向用户列表界面传递搜索内容
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void ModifyMyInfo() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                ArrayList<Account> list =null;
                list=DB_Account.findById(myID);//根据传递的字段搜索用户表
                Message msg = new Message();
                msg.what = 1001;
                Bundle data = new Bundle();
                data.putParcelableArrayList("Account",list);
                msg.setData(data);
                mHandler.sendMessage(msg);
            }
        };
        new Thread(run).start();

    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    final Bundle bundle1 = msg.getData();
                    ArrayList list =bundle1.getParcelableArrayList("Account");
                    data = new ArrayList<Account>();
                    if(list!=null) {
                        ArrayList<Account> userslist = list;
                        for (Account user : userslist) {
                            data.add(user);
                        }
                    }
                    Intent intent = new Intent(InterfaceB.this, Up_Account.class);
                    Bundle bundle = new Bundle();
                    int pos=0;
                    bundle.putBoolean("register", false);
                    bundle.putString("type", data.get(pos).getType());
                    bundle.putString("userID", data.get(pos).getUserID());
                    bundle.putString("pwd", data.get(pos).getPassword());
                    bundle.putString("name", data.get(pos).getUsername());
                    bundle.putString("grade", data.get(pos).getGrade());
                    bundle.putString("dept", data.get(pos).getDept());
                    bundle.putString("tele", data.get(pos).getTelephone());
                    bundle.putString("myID", myID);
                    bundle.putString("identity", "B");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    private void loading(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载中");
        dialog.show();
    }
}
