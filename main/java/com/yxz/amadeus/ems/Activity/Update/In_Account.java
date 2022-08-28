package com.yxz.amadeus.ems.Activity.Update;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yxz.amadeus.ems.Activity.LoginActivity;
import com.yxz.amadeus.ems.DAO.DB_Account;
import com.yxz.amadeus.ems.entity.Account;
import com.yxz.amadeus.ems.R;

public class In_Account extends AppCompatActivity {
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private Account account;
    private boolean bo_register=true;//true为管理员,false为注册
    private Button confirm, cancel;
    private EditText id, name, pwd, grade, dept, tele;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_account);
        spinner = (Spinner) findViewById(R.id.in_type);
        confirm = (Button) findViewById(R.id.in_content);
        cancel = (Button) findViewById(R.id.in_cancel);
        id = (EditText) findViewById(R.id.in_userID);
        name = (EditText) findViewById(R.id.in_username);
        pwd = (EditText) findViewById(R.id.in_pwd);
        grade = (EditText) findViewById(R.id.in_grade);
        dept = (EditText) findViewById(R.id.in_dept);
        tele = (EditText) findViewById(R.id.in_tele);

        pwd.setInputType(7);//可见密码
        try{
            Bundle bundle=getIntent().getExtras();
            bo_register=bundle.getBoolean("register");
        }catch (Exception e){

        }
        account = new Account();
        //数据
        data_list = new ArrayList<String>();
        data_list.add("使用人");//012
        //若是注册,则不显示后面两项
        if(bo_register){
            data_list.add("负责人");
            data_list.add("管理员");
        }else {
            account.setType("C");
        }
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
                if (position==0){
                    account.setType("C");
                }else if (position==1){
                    account.setType("B");
                }else if (position==2){
                    account.setType("A");
                }else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                account.setUserID(s.toString());
            }
        });
        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                account.setPassword(s.toString());
            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                account.setUsername(s.toString());
            }
        });
        grade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                account.setGrade(s.toString());
            }
        });
        dept.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                account.setDept(s.toString());
            }
        });
        tele.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                account.setTelephone(s.toString());
            }
        });
        //点击确定
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account.getType()==null){
                    Toast.makeText(In_Account.this, "账户类型不能为空",Toast.LENGTH_SHORT).show();
                }else if(account.getUserID()==null){
                    Toast.makeText(In_Account.this, "账户ID不能为空",Toast.LENGTH_SHORT).show();
                }else if(account.getUsername()==null){
                    Toast.makeText(In_Account.this, "姓名不能为空",Toast.LENGTH_SHORT).show();
                }else if(account.getPassword()==null){
                    Toast.makeText(In_Account.this, "密码不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    AddAccount();
                }

            }
        });
        //点击取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading();
                if(bo_register==false){
                    startActivity(new Intent(In_Account.this,LoginActivity.class));
                }
                finish();
            }
        });
    }
    private void loading(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载中");
        dialog.show();
    }
    /**通过子线程联网访问数据*/
    /***/
    private void AddAccount() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                //若方法返回为true,显示添加成功
                if (DB_Account.addAccount(account)) {
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
                    if(bo_register){
                        Toast.makeText(In_Account.this, " 添加成功",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(In_Account.this, " 注册成功,请登录",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1002:
                    if (bo_register){
                        Toast.makeText(In_Account.this, " 添加失败",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(In_Account.this, " 注册失败,可能是用户名重复",
                                Toast.LENGTH_SHORT).show();
                    }

                    break;
                default:
                    break;
            }
        }
    };

}
