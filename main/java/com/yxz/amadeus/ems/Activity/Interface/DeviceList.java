package com.yxz.amadeus.ems.Activity.Interface;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.yxz.amadeus.ems.Activity.Update.In_CheckInfo;
import com.yxz.amadeus.ems.Activity.Update.In_Device;
import com.yxz.amadeus.ems.Activity.Update.In_RepairInfo;
import com.yxz.amadeus.ems.Activity.Update.In_UseInfo;
import com.yxz.amadeus.ems.Activity.Update.Up_CheckInfo;
import com.yxz.amadeus.ems.Activity.Update.Up_Device;
import com.yxz.amadeus.ems.Activity.Update.Up_RepairInfo;
import com.yxz.amadeus.ems.Activity.Update.Up_UseInfo;
import com.yxz.amadeus.ems.DAO.DB_CheckInfo;
import com.yxz.amadeus.ems.DAO.DB_Device;
import com.yxz.amadeus.ems.Table.base.RefreshParams;
import com.yxz.amadeus.ems.Table.base.adapter.AbsCommonAdapter;
import com.yxz.amadeus.ems.Table.base.adapter.AbsViewHolder;
import com.yxz.amadeus.ems.Table.bean.CheckInfoBean;
import com.yxz.amadeus.ems.Table.bean.DeviceBean;
import com.yxz.amadeus.ems.Table.bean.TableModel;
import com.yxz.amadeus.ems.Table.utils.WeakHandler;
import com.yxz.amadeus.ems.Table.widget.SyncHorizontalScrollView;
import com.yxz.amadeus.ems.Table.widget.pullrefresh.AbPullToRefreshView;
import com.yxz.amadeus.ems.entity.CheckInfo;
import com.yxz.amadeus.ems.entity.Device;

import com.yxz.amadeus.ems.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DeviceList extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private List<Device> data = new ArrayList<Device>();
    private RecyclerView recyclerview;
    private int pos = 0;
    private int jump = 0;
    private boolean bo_select = false;
    private String select = "", identity = "", myID = "", selectBy, findByID = "";
    //????????????
    private SparseArray<TextView> mTitleTvArray;
    private TextView tv_table_title_left;
    private LinearLayout right_title_container;
    private ListView leftListView;
    private ListView rightListView;
    private AbsCommonAdapter<TableModel> mLeftAdapter, mRightAdapter;
    private SyncHorizontalScrollView titleHorScv;
    private SyncHorizontalScrollView contentHorScv;
    private AbPullToRefreshView pulltorefreshview;
    private int pageNo = 0;
    private int NumTitle = 0;//?????????
    private WeakHandler Handler = new WeakHandler();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_layout);
        init();
        try {
            Bundle bundle = getIntent().getExtras();
            jump = bundle.getInt("jump");
            bo_select = bundle.getBoolean("bo_select");//??????????????????????????????????????????????????????
            select = bundle.getString("select");//????????????????????????
            try {
                identity = bundle.getString("identity");
                myID = bundle.getString("myID");
            } catch (Exception e) {

            }
            selectBy = bundle.getString("selectBy");
            findByID = bundle.getString("findByID");
        } catch (Exception e) {

        }
    }

    //??????????????????
    private void refresh() {
        //??????????????????bo_select???select??????????????????
        Intent intent = new Intent(DeviceList.this, DeviceList.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("bo_select", bo_select);
        bundle.putString("select", select);//?????????????????????????????????
        bundle.putString("myID", myID);
        bundle.putString("identity", identity);
        bundle.putString("selectBy", selectBy);
        intent.putExtras(bundle);
        startActivity(intent);
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("???????????????");
        dialog.show();
        finish();
    }

    private void JumpInfo() {
        AlertDialog dialog = new AlertDialog.Builder(DeviceList.this)
                .setTitle("????????????" + data.get(pos).getDeviceName() + "(" + data.get(pos).getDeviceID() + ")?")//????????????????????????
//                        .setMessage("????????????????????????")//????????????????????????
                //????????????????????????
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DeviceList.this, "?????????", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent1;
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("deviceID", data.get(pos).getDeviceID());
                        bundle1.putString("deviceName", data.get(pos).getDeviceName());
                        bundle1.putString("userID", data.get(pos).getUserID());
                        bundle1.putString("userName", data.get(pos).getUserName());
                        bundle1.putString("myID", myID);
                        bundle1.putString("identity", identity);
                        Toast.makeText(DeviceList.this, "????????????" + data.get(pos).getDeviceName() + "(" + data.get(pos).getDeviceID() + ")",
                                Toast.LENGTH_SHORT).show();
                        if (jump == 1) {//1.?????????In_RepairInfo????????????
                            intent1 = new Intent(DeviceList.this, In_RepairInfo.class);
                            intent1.putExtras(bundle1);
                            startActivity(intent1);
                        } else if (jump == 2) {//2.??????In_RepairInfo????????????
                            intent1 = new Intent(DeviceList.this, In_RepairInfo.class);
                            intent1.putExtras(bundle1);
                            setResult(Activity.RESULT_OK, intent1);
                        } else if (jump == 3) {//3.??????Up_RepairInfo????????????
                            intent1 = new Intent(DeviceList.this, Up_RepairInfo.class);
                            intent1.putExtras(bundle1);
                            setResult(Activity.RESULT_OK, intent1);
                        } else if (jump == 11) {
                            intent1 = new Intent(DeviceList.this, In_UseInfo.class);
                            intent1.putExtras(bundle1);
                            startActivity(intent1);
                        } else if (jump == 12) {//3.??????In????????????
                            intent1 = new Intent(DeviceList.this, In_UseInfo.class);
                            intent1.putExtras(bundle1);
                            setResult(Activity.RESULT_OK, intent1);
                        } else if (jump == 13) {//3.??????Up????????????
                            intent1 = new Intent(DeviceList.this, Up_UseInfo.class);
                            intent1.putExtras(bundle1);
                            setResult(Activity.RESULT_OK, intent1);
                        } else if (jump == 21) {
                            intent1 = new Intent(DeviceList.this, In_CheckInfo.class);
                            intent1.putExtras(bundle1);
                            startActivity(intent1);
                        } else if (jump == 22) {//3.??????In????????????
                            intent1 = new Intent(DeviceList.this, In_CheckInfo.class);
                            intent1.putExtras(bundle1);
                            setResult(Activity.RESULT_OK, intent1);
                        } else if (jump == 23) {//3.??????Up????????????
                            intent1 = new Intent(DeviceList.this, Up_CheckInfo.class);
                            intent1.putExtras(bundle1);
                            setResult(Activity.RESULT_OK, intent1);
                        }
                        dialog.dismiss();
                        if (jump!= 1) {
                            finish();
                        }
                    }
                }).create();
        dialog.show();
    }

    //????????????
    private void DeleteDevice() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                if (DB_Device.deleteDevice(data.get(pos).getDeviceID())) {
                    msg.what = 1001;
                } else {
                    msg.what = 1002;
                }
                handler.sendMessage(msg);
            }
        };
        new Thread(run).start();

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    Toast.makeText(DeviceList.this, " ????????????",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    Toast.makeText(DeviceList.this, " ????????????",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            //????????????
        }
    };

    /**
     * ????????????
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (jump == 0) {
            getMenuInflater().inflate(R.menu.menu_add, menu); //??????getMenuInflater()????????????MenuInflater????????????????????????inflate()??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????Menu???????????????
            return true; // true???????????????????????????????????????false????????????????????????????????????
        } else {
            return false;
        }
    }

    /**
     * ?????????????????????
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.id_add_item:
                insert();
                break;
            case R.id.id_refresh_item:
                refresh();
                break;
            case R.id.id_select_item:
                View view = getLayoutInflater().inflate(R.layout.half_dialog_view, null);
                final EditText editText = (EditText) view.findViewById(R.id.dialog_edit);
                AlertDialog dialog = new AlertDialog.Builder(DeviceList.this)
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
                                Toast.makeText(DeviceList.this, content, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                select = content;
                                bo_select = true;
                                selectBy = "B";
                                init();
                            }
                        })
                        .setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String content = editText.getText().toString();
                                Toast.makeText(DeviceList.this, content, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                select = content;
                                bo_select = true;
                                selectBy = "C";
                                init();
                            }
                        }).create();
                dialog.show();
                break;
            default:
                break;
        }

        return true;
    }

    //????????????
    private void insert() {
        if(identity.equals("B")){
            Intent intent1 = new Intent(DeviceList.this, In_Device.class);
            Bundle bundle = new Bundle();
            bundle.putString("myID", myID);
            bundle.putString("identity", identity);
            intent1.putExtras(bundle);
            startActivity(intent1);
        }else {
            AlertDialog dialog = new AlertDialog.Builder(DeviceList.this)
                    .setTitle("????????????????????????????????????????")//????????????????????????
                    //????????????????????????
                    .setNegativeButton("???", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent1 = new Intent(DeviceList.this, In_Device.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("myID", myID);
                            bundle.putString("identity", identity);
                            intent1.putExtras(bundle);
                            startActivity(intent1);
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("???", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(DeviceList.this, AccountList.class);
                            Bundle bundle1 = new Bundle();
                            bundle1.putInt("jump", 1);
                            bundle1.putString("myID", myID);
                            bundle1.putString("identity", identity);
                            intent.putExtras(bundle1);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }

    }

    //????????????????????????????????????
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // TODO Auto-generated method stub
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.insert_item:
                insert();
                break;
            case R.id.delete_item:
//                intent=new Intent(DeviceList.this,DeviceList.class);
                //data.get(pos)??????????????????Device
                AlertDialog dialog1 = new AlertDialog.Builder(this)
                        .setTitle("????????????????????????<" + data.get(pos).getDeviceID() + ">?")//????????????????????????
//                        .setMessage("????????????????????????")//????????????????????????
                        //????????????????????????
                        .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(DeviceList.this, "?????????", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteDevice();
//                                Toast.makeText(DeviceList.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                refresh();
                            }
                        }).create();
                dialog1.show();
                break;
            case R.id.update_item:
                intent = new Intent(DeviceList.this, Up_Device.class);
                Bundle bundle = new Bundle();
                bundle.putString("deviceID", data.get(pos).getDeviceID());
                bundle.putString("deviceName", data.get(pos).getDeviceName());
                bundle.putString("model", data.get(pos).getModel());
                bundle.putString("manufactor", data.get(pos).getManufactor());
                bundle.putString("recordDate", data.get(pos).getRecordDate());
                bundle.putString("userID", data.get(pos).getUserID());
                bundle.putString("userName", data.get(pos).getUserName());
                bundle.putInt("maintenanceTimes", data.get(pos).getMaintenanceTimes());
                bundle.putString("myID", myID);
                bundle.putString("identity", identity);
                intent.putExtras(bundle);
                break;
            case R.id.item_1:
                intent = new Intent(DeviceList.this, AccountList.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("identity", identity);
                bundle1.putBoolean("bo_select", true);
                bundle1.putString("select", data.get(pos).getUserID());
                bundle1.putString("findByID", "C");
                intent.putExtras(bundle1);
                break;
            case R.id.item_2:
                intent = new Intent(DeviceList.this, UseInfoList.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("identity", identity);
                bundle2.putBoolean("bo_select", true);
                bundle2.putString("select", data.get(pos).getDeviceID());
                bundle2.putString("findByID", "B");
                intent.putExtras(bundle2);
                break;
            case R.id.item_3:
                intent = new Intent(DeviceList.this, RepairInfoList.class);
                Bundle bundle3 = new Bundle();
                bundle3.putString("identity", identity);
                bundle3.putBoolean("bo_select", true);
                bundle3.putString("select", data.get(pos).getDeviceID());
                bundle3.putString("findByID", "B");
                intent.putExtras(bundle3);
                break;
            case R.id.item_4:
                intent = new Intent(DeviceList.this, CheckInfoList.class);
                Bundle bundle4 = new Bundle();
                bundle4.putString("identity", identity);
                bundle4.putBoolean("bo_select", true);
                bundle4.putString("select", data.get(pos).getDeviceID());
                bundle4.putString("findByID", "B");
                intent.putExtras(bundle4);
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void loading() {
        ProgressDialog dialog = new ProgressDialog(DeviceList.this);
        dialog.setMessage("???????????????");
        dialog.show();
    }

    public void init() {
        mContext = getApplicationContext();
        findByid();
        setListener();
        setData();
    }

    private void Jump(View view, int position, boolean left) {
        pos = position;
        if (jump == 0) {
            //??????????????????????????????????????????11???
            PopupMenu popup = new PopupMenu(DeviceList.this, view);//?????????????????????????????????view
            //?????????????????????
            MenuInflater inflater = popup.getMenuInflater();
            //????????????
            if (left) {
                inflater.inflate(R.menu.menu_item_4, popup.getMenu());
                MenuItem item1 = popup.getMenu().findItem(R.id.item_1);//??????popup?????????,??????item???????????????
                item1.setTitle("?????????");
            } else {
                inflater.inflate(R.menu.menu, popup.getMenu());
            }
            //??????????????????????????????
            popup.setOnMenuItemClickListener(DeviceList.this);
            //??????(??????????????????????????????)
            popup.show();
        } else if (jump == 1 || jump == 2 || jump == 3 || jump == 11 || jump == 12 || jump == 13 || jump == 21 || jump == 22 || jump == 23) {
            JumpInfo();
        }
    }

    public void findByid() {
        pulltorefreshview = (AbPullToRefreshView) findViewById(R.id.pulltorefreshview);
//        pulltorefreshview.setPullRefreshEnable(false);
        tv_table_title_left = (TextView) findViewById(R.id.tv_table_title_left);
        tv_table_title_left.setText("????????????");
        leftListView = (ListView) findViewById(R.id.left_container_listview);
        rightListView = (ListView) findViewById(R.id.right_container_listview);
        right_title_container = (LinearLayout) findViewById(R.id.right_title_container);
        getLayoutInflater().inflate(R.layout.table_right_title, right_title_container);

        TitleName_Right();

        titleHorScv = (SyncHorizontalScrollView) findViewById(R.id.title_horsv);
        contentHorScv = (SyncHorizontalScrollView) findViewById(R.id.content_horsv);
        // ?????????????????????????????????
        titleHorScv.setScrollView(contentHorScv);
        contentHorScv.setScrollView(titleHorScv);
        findTitleTextViewIds();
        initTableView();
    }

    private void TitleName_Right() {
        TextView tv_table_title_0 = (TextView) findViewById(R.id.tv_table_title_0);
        TextView tv_table_title_1 = (TextView) findViewById(R.id.tv_table_title_1);
        TextView tv_table_title_2 = (TextView) findViewById(R.id.tv_table_title_2);
        TextView tv_table_title_3 = (TextView) findViewById(R.id.tv_table_title_3);
        TextView tv_table_title_4 = (TextView) findViewById(R.id.tv_table_title_4);
        TextView tv_table_title_5 = (TextView) findViewById(R.id.tv_table_title_5);
        TextView tv_table_title_6 = (TextView) findViewById(R.id.tv_table_title_6);
        TextView tv_table_title_7 = (TextView) findViewById(R.id.tv_table_title_7);
        TextView tv_table_title_8 = (TextView) findViewById(R.id.tv_table_title_8);
        tv_table_title_0.setText("???????????? ");
        tv_table_title_1.setText("????????????");
        tv_table_title_2.setText("????????????");
        tv_table_title_3.setText("????????????");
        tv_table_title_4.setText("????????????");
        tv_table_title_5.setText("????????????");
        tv_table_title_6.setText("?????????");
        tv_table_title_7.setText("?????????");
        tv_table_title_8.setText("?????????");
    }

    /**
     * ??????????????????TextView???item??????
     */
    private void findTitleTextViewIds() {
        mTitleTvArray = new SparseArray<>();
        for (int i = 0; i <= 20; i++) {
            try {
                Field field = R.id.class.getField("tv_table_title_" + 0);
                int key = field.getInt(new R.id());
                TextView textView = (TextView) findViewById(key);
                mTitleTvArray.put(key, textView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initTableView() {
        mLeftAdapter = new AbsCommonAdapter<TableModel>(mContext, R.layout.table_left_item) {
            @Override
            public void convert(AbsViewHolder helper, TableModel item, int pos) {
                TextView tv_table_content_left = helper.getView(R.id.tv_table_content_item_left);
                tv_table_content_left.setText(item.getLeftTitle());
            }
        };
        mRightAdapter = new AbsCommonAdapter<TableModel>(mContext, R.layout.table_right_item) {
            @Override
            public void convert(AbsViewHolder helper, TableModel item, int pos) {
                TextView tv_table_content_right_item0 = helper.getView(R.id.tv_table_content_right_item0);
                TextView tv_table_content_right_item1 = helper.getView(R.id.tv_table_content_right_item1);
                TextView tv_table_content_right_item2 = helper.getView(R.id.tv_table_content_right_item2);
                TextView tv_table_content_right_item3 = helper.getView(R.id.tv_table_content_right_item3);
                TextView tv_table_content_right_item4 = helper.getView(R.id.tv_table_content_right_item4);
                TextView tv_table_content_right_item5 = helper.getView(R.id.tv_table_content_right_item5);
                TextView tv_table_content_right_item6 = helper.getView(R.id.tv_table_content_right_item6);
                TextView tv_table_content_right_item7 = helper.getView(R.id.tv_table_content_right_item7);
                TextView tv_table_content_right_item8 = helper.getView(R.id.tv_table_content_right_item8);
                TextView tv_table_content_right_item9 = helper.getView(R.id.tv_table_content_right_item9);
                tv_table_content_right_item0.setText(item.getText0());
                tv_table_content_right_item1.setText(item.getText1());
                tv_table_content_right_item2.setText(item.getText2());
                tv_table_content_right_item3.setText(item.getText3());
                tv_table_content_right_item4.setText(item.getText4());
                tv_table_content_right_item5.setText(item.getText5());
                tv_table_content_right_item6.setText(item.getText6());
                tv_table_content_right_item7.setVisibility(View.GONE);
                tv_table_content_right_item8.setVisibility(View.GONE);
                tv_table_content_right_item9.setVisibility(View.GONE);
                //???????????????????????????
                item.setTextColor(tv_table_content_right_item0, item.getText0());
                item.setTextColor(tv_table_content_right_item5, item.getText5());

                for (int i = 0; i < 7; i++) {
                    View view = ((LinearLayout) helper.getConvertView()).getChildAt(i);
                    view.setVisibility(View.VISIBLE);
                }
            }
        };
        leftListView.setAdapter(mLeftAdapter);
        rightListView.setAdapter(mRightAdapter);
    }


    public void setListener() {
        pulltorefreshview.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView view) {
                Handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageNo = 0;
                        doGetDatas(0, RefreshParams.REFRESH_DATA);
                    }
                }, 1000);
            }
        });
        pulltorefreshview.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView view) {
                Handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doGetDatas(pageNo, RefreshParams.LOAD_DATA);
                    }
                }, 1000);
            }

        });
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(identity.equals("A")){
                    Jump(view,position,true);//????????????
                }else {
                    Jump(view,position,false);//????????????
                }
            }
        });
        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Jump(view, position, false);//????????????
            }
        });
    }

    public void setData() {
        doGetDatas(0, RefreshParams.REFRESH_DATA);
    }

    private void FindAccountList(final int state, final int pageno) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                ArrayList<Device> list = null;
                if (bo_select) {
                    if (findByID==null) {
                        if (identity.equals("B")) {
                            list = DB_Device.selectMyInfo(selectBy, myID, select);
                        } else {
                            list = DB_Device.findBy(selectBy, select);//???????????????????????????
                        }
                    } else {
                            list = DB_Device.findByID(findByID, select);
                            findByID = "";
                    }
                } else {
                    if (identity.equals("B")) {
                        list = DB_Device.findDeviceByID(myID);
                    } else {
                        list = DB_Device.findDevice();//????????????
                    }
                }
                Bundle data = new Bundle();
                data.putParcelableArrayList("List", list);
                data.putInt("state", state);
                data.putInt("pageno", pageno);
                msg.setData(data);
                msg.what = 1001;
                handler3.sendMessage(msg);
            }
        };
        new Thread(run).start();

    }

    Handler handler3 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    final Bundle bundle = msg.getData();
                    ArrayList list = bundle.getParcelableArrayList("List");
                    int state = bundle.getInt("state");
                    int pageno = bundle.getInt("pageno");
                    List<DeviceBean> BeanList = new ArrayList<>();
                    if (list != null) {
                        ArrayList<Device> userslist = list;
                        for (int i = 0 + pageno * 20; i < 20 * (pageno + 1); i++) {
                            try {
                                Device user = userslist.get(i);
                                DeviceBean bean = new DeviceBean();
                                bean.setDeviceID(user.getDeviceID());
                                bean.setDeviceName(user.getDeviceName());
                                bean.setModel(user.getModel());
                                bean.setManufactor(user.getManufactor());
                                bean.setRecordDate(user.getRecordDate());
                                bean.setMaintenanceTimes(user.getMaintenanceTimes());
                                bean.setUserName(user.getUserName());
                                BeanList.add(bean);
                            } catch (Exception e) {

                            }
                        }
                    }
                    data = new ArrayList<Device>();
                    if (list != null) {
                        ArrayList<Device> userslist = list;
                        for (Device user : userslist) {
                            data.add(user);
                        }
                    }
                    setDatas(BeanList, state);
                    break;
                case 1002:
                    break;
                default:
                    break;
            }
        }
    };

    //??????????????????
    public void doGetDatas(int pageno, int state) {
        if (state == RefreshParams.REFRESH_DATA) {
            pulltorefreshview.onHeaderRefreshFinish();
            NumTitle = 0;
        } else {
            pulltorefreshview.onFooterLoadFinish();
        }
        FindAccountList(state, pageno);
    }

    private void setDatas(List<DeviceBean> onlineSaleBeanList, int type) {
        if (onlineSaleBeanList.size() > 0) {
            List<TableModel> mDatas = new ArrayList<>();
            for (int i = 0; i < onlineSaleBeanList.size(); i++) {
                DeviceBean bean = onlineSaleBeanList.get(i);
                TableModel tableMode = new TableModel();
//                tableMode.setOrgCode("A" + i);
                String state = null;
                tableMode.setLeftTitle("" + (++NumTitle));//??????
                tableMode.setText0(bean.getDeviceID() + "");//???1??????
                tableMode.setText1(bean.getDeviceName());
                tableMode.setText2(bean.getModel() + "");//???2??????
                tableMode.setText3(bean.getManufactor() + "");
                tableMode.setText4(bean.getRecordDate() + "");
                tableMode.setText5(bean.getMaintenanceTimes() + "");//
                tableMode.setText6(bean.getUserName());//
                mDatas.add(tableMode);
            }
            boolean isMore;
            if (type == RefreshParams.LOAD_DATA) {
                isMore = true;
            } else {
                isMore = false;
            }
            mLeftAdapter.addData(mDatas, isMore);
            mRightAdapter.addData(mDatas, isMore);
            //?????????????????????????????????
            pageNo++;
//            if (mDatas.size() < 20) {
//                pulltorefreshview.setLoadMoreEnable(false);
//            }
            mDatas.clear();
        } else {
            //?????????null
            if (type == RefreshParams.REFRESH_DATA) {
                mLeftAdapter.clearData(true);
                mRightAdapter.clearData(true);
                //???????????????????????????
                //                mEmpty.setShowErrorAndPic(getString(R.string.empty_null), 0);
            } else if (type == RefreshParams.LOAD_DATA) {
                Toast.makeText(mContext, "?????????", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
