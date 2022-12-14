package com.yxz.amadeus.ems.Activity.Interface;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yxz.amadeus.ems.Activity.LoginActivity;
import com.yxz.amadeus.ems.R;

public class InterfaceA extends AppCompatActivity {

    Button btn_account, btn_device, btn_repair, btn_use, btn_check,btn_checkUseInfo, sl_account, sl_device, sl_repair, sl_use, sl_check;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface_a_);
        btn_account=(Button)findViewById(R.id.btn_account);
        btn_device  =(Button)findViewById(R.id.btn_device);
        btn_repair   =(Button)findViewById(R.id.btn_repair);
        btn_use  =(Button)findViewById(R.id.btn_use);
        btn_check  =(Button)findViewById(R.id.btn_check);
        btn_checkUseInfo=(Button)findViewById(R.id.btn_checkUseInfo);
        sl_account  =(Button)findViewById(R.id.sl_account);
        sl_device  =(Button)findViewById(R.id.sl_device);
        sl_repair =(Button)findViewById(R.id.sl_repair);
        sl_use =(Button)findViewById(R.id.sl_use);
        sl_check=(Button)findViewById(R.id.sl_check);
        btn_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InterfaceA.this,AccountList.class);
                startAct(intent);
            }
        });
        btn_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InterfaceA.this,DeviceList.class);
                startAct(intent);
            }
        });
        btn_repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InterfaceA.this,RepairInfoList.class);
                startAct(intent);
            }
        });
        btn_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InterfaceA.this,UseInfoList.class);
                startAct(intent);
            }
        });
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InterfaceA.this,CheckInfoList.class);
                startAct(intent);
            }
        });
        sl_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.half_dialog_view, null);
                final EditText editText = (EditText) view.findViewById(R.id.dialog_edit);
                AlertDialog dialog = new AlertDialog.Builder(InterfaceA.this)
                        .setTitle("???????????????????????????ID?????????")//????????????????????????
                        .setView(view)
                        .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String content = editText.getText().toString();
                                Toast.makeText(InterfaceA.this, content, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Intent intent=new Intent(InterfaceA.this,AccountList.class);
                                Bundle bundle=new Bundle();
                                bundle.putBoolean("bo_select",true);
                                bundle.putString("select",content);//???????????????????????????????????????
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }).create();
                dialog.show();
            }
        });

        sl_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.half_dialog_view, null);
                final EditText editText = (EditText) view.findViewById(R.id.dialog_edit);
                AlertDialog dialog = new AlertDialog.Builder(InterfaceA.this)
                        .setTitle("??????????????????????????????????????????ID/??????/????????????/????????????")//????????????????????????
                        .setView(view)
                        .setNeutralButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("????????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String content = editText.getText().toString();
                                Toast.makeText(InterfaceA.this, content, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Intent intent=new Intent(InterfaceA.this,DeviceList.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("identity","A");
                                bundle.putBoolean("bo_select",true);
                                bundle.putString("selectBy","B");
                                bundle.putString("select",content);//??????????????????
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String content = editText.getText().toString();
                                Toast.makeText(InterfaceA.this, content, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Intent intent=new Intent(InterfaceA.this,DeviceList.class);                                Bundle bundle=new Bundle();
                                bundle.putBoolean("bo_select",true);
                                bundle.putString("identity","A");
                                bundle.putString("selectBy","C");
                                bundle.putString("select",content);//???????????????????????????????????????
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }).create();
                dialog.show();
            }
        });
        sl_repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InterfaceA.this,RepairInfoList.class);
                AlertDialog("??????????????????????????????????????????ID/??????/????????????/????????????/????????????",intent);
            }
        });
        sl_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InterfaceA.this,UseInfoList.class);
                AlertDialog("??????????????????????????????????????????ID/??????/????????????/????????????/????????????",intent);
            }
        });
        sl_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InterfaceA.this,CheckInfoList.class);
                AlertDialog("??????????????????????????????????????????ID/??????/????????????/????????????/????????????",intent);
            }
        });
        btn_checkUseInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InterfaceA.this,UseInfoList.class);
                Bundle bundle=new Bundle();
                bundle.putString("identity","AC");
                bundle.putBoolean("bo_select",false);
                bundle.putString("select","");//???????????????????????????????????????
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    private void startAct(Intent intent){
        Bundle bundle=new Bundle();
        bundle.putString("identity","A");
        bundle.putBoolean("bo_select",false);
        bundle.putString("select","");//???????????????????????????????????????
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void AlertDialog(final String string,final Intent intent){
        View view = getLayoutInflater().inflate(R.layout.half_dialog_view, null);
        final EditText editText = (EditText) view.findViewById(R.id.dialog_edit);
        AlertDialog dialog = new AlertDialog.Builder(InterfaceA.this)
                .setTitle(string)//????????????????????????
                .setView(view)
                .setNeutralButton("????????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = editText.getText().toString();
                        Toast.makeText(InterfaceA.this, content, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Bundle bundle=new Bundle();
                        bundle.putString("identity","A");
                        bundle.putBoolean("bo_select",true);
                        bundle.putString("selectBy","A");
                        bundle.putString("select",content);//??????????????????
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("????????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String content = editText.getText().toString();
                        Toast.makeText(InterfaceA.this, content, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Bundle bundle=new Bundle();
                        bundle.putString("identity","A");
                        bundle.putBoolean("bo_select",true);
                        bundle.putString("selectBy","B");
                        bundle.putString("select",content);//??????????????????
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String content = editText.getText().toString();
                        Toast.makeText(InterfaceA.this, content, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Bundle bundle=new Bundle();
                        bundle.putString("identity","A");
                        bundle.putBoolean("bo_select",true);
                        bundle.putString("selectBy","C");
                        bundle.putString("select",content);//??????????????????
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                })
                .create();
        dialog.show();
    }
    private void loading(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("???????????????");
        dialog.show();
    }
}
