package com.yxz.amadeus.ems.Activity.Interface;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.yxz.amadeus.ems.Activity.Update.In_UseInfo;
import com.yxz.amadeus.ems.Activity.Update.Up_Account;
import com.yxz.amadeus.ems.DAO.DB_Account;
import com.yxz.amadeus.ems.R;
import com.yxz.amadeus.ems.entity.Account;

import java.util.ArrayList;
import java.util.List;

public class InterfaceC extends AppCompatActivity {
    Button btn_addUseInfo,btn_modifyMyInfo;
    String myID,myName;
    private List<Account> data = new ArrayList<Account>();
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface_c);
        btn_addUseInfo=(Button)findViewById(R.id.btn_addUseInfo);
        btn_modifyMyInfo=(Button)findViewById(R.id.btn_modifyMyInfo);
        try{
            Bundle bundle=getIntent().getExtras();
            myID=bundle.getString("myID");
            myName=bundle.getString("myName");
        }catch (Exception e){

        }
        btn_addUseInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InterfaceC.this,In_UseInfo.class);
                startAct(intent);
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
        Bundle bundle2=new Bundle();
        bundle2.putString("identity","C");
        bundle2.putString("myID",myID);
        bundle2.putString("myName",myName);
        bundle2.putBoolean("bo_select",false);
        bundle2.putString("select","");//向用户列表界面传递搜索内容
        intent.putExtras(bundle2);
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
                    Intent intent = new Intent(InterfaceC.this, Up_Account.class);
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
                    bundle.putString("identity", "C");
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
