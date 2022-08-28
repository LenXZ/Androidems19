package com.yxz.amadeus.ems.Activity.Interface;

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

import com.yxz.amadeus.ems.Activity.Update.In_RepairInfo;
import com.yxz.amadeus.ems.Activity.Update.Up_RepairInfo;
import com.yxz.amadeus.ems.DAO.DB_RepairInfo;
import com.yxz.amadeus.ems.Table.base.RefreshParams;
import com.yxz.amadeus.ems.Table.base.adapter.AbsCommonAdapter;
import com.yxz.amadeus.ems.Table.base.adapter.AbsViewHolder;
import com.yxz.amadeus.ems.Table.bean.RepairInfoBean;
import com.yxz.amadeus.ems.Table.bean.TableModel;
import com.yxz.amadeus.ems.Table.utils.WeakHandler;
import com.yxz.amadeus.ems.Table.widget.SyncHorizontalScrollView;
import com.yxz.amadeus.ems.Table.widget.pullrefresh.AbPullToRefreshView;
import com.yxz.amadeus.ems.entity.Device;
import com.yxz.amadeus.ems.entity.RepairInfo;

import com.yxz.amadeus.ems.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class RepairInfoList extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private List<RepairInfo> data = new ArrayList<RepairInfo>();
    private RecyclerView recyclerview;
    private int pos = 0;
    private boolean bo_select = false;
    private String select = "", identity = "", myID = "", selectBy, findByID = "";
    //表格部分
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
    private int NumTitle = 0;//左标题
    private WeakHandler Handler = new WeakHandler();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_layout);
        init();
        try {
            Bundle bundle = getIntent().getExtras();
            bo_select = bundle.getBoolean("bo_select");//根据此数据判断是搜索全表还是具体搜索
            select = bundle.getString("select");//接收要搜索的字段
            identity = bundle.getString("identity");
            myID = bundle.getString("myID");
            selectBy = bundle.getString("selectBy");
            findByID = bundle.getString("findByID");
        } catch (Exception e) {

        }
    }

    //刷新当前页面
    private void refresh() {
        //获取当前界面bo_select和select的值进行刷新
        Intent intent = new Intent(RepairInfoList.this, RepairInfoList.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("bo_select", bo_select);
        bundle.putString("myID", myID);
        bundle.putString("identity", identity);
        bundle.putString("select", select);//向列表界面传递搜索内容
        bundle.putString("selectBy", selectBy);
        intent.putExtras(bundle);
        startActivity(intent);
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载中");
        dialog.show();
        finish();
    }

    private void DeleteRepairInfo() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                if (DB_RepairInfo.deleteRepairInfo(data.get(pos).getInfoID())) {
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
                    Toast.makeText(RepairInfoList.this, " 删除成功",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    Toast.makeText(RepairInfoList.this, " 删除失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            //刷新界面
        }
    };

    /**
     * 创建菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu); //通过getMenuInflater()方法得到MenuInflater对象，再调用它的inflate()方法就可以给当前活动创建菜单了，第一个参数：用于指定我们通过哪一个资源文件来创建菜单；第二个参数：用于指定我们的菜单项将添加到哪一个Menu对象当中。
        return true; // true：允许创建的菜单显示出来，false：创建的菜单将无法显示。
    }

    /**
     * 菜单的点击事件
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
                AlertDialog dialog = new AlertDialog.Builder(RepairInfoList.this)
                        .setTitle("请输入与维修信息相关的负责人ID/姓名/设备编号/设备名称/维修编号")//设置对话框的标题
                        .setView(view)
                        .setNeutralButton("信息编号", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String content = editText.getText().toString();
                                Toast.makeText(RepairInfoList.this, content, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                select = content;
                                bo_select = true;
                                selectBy = "A";
                                init();
                            }
                        })
                        .setNegativeButton("相关设备", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String content = editText.getText().toString();
                                Toast.makeText(RepairInfoList.this, content, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                select = content;
                                bo_select = true;
                                selectBy = "B";
                                init();
                            }
                        })
                        .setPositiveButton("相关用户", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String content = editText.getText().toString();
                                Toast.makeText(RepairInfoList.this, content, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                select = content;
                                bo_select = true;
                                selectBy = "C";
                                init();
                            }
                        })
                        .create();
                dialog.show();
                break;
            default:
                break;
        }

        return true;
    }

    //插入数据
    private void insert() {
        AlertDialog dialog = new AlertDialog.Builder(RepairInfoList.this)
                .setTitle("是否转到设备列表选择设备?")//设置对话框的标题
                //设置对话框的按钮
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent1 = new Intent(RepairInfoList.this, In_RepairInfo.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("myID", myID);
                        bundle.putString("identity", identity);
                        intent1.putExtras(bundle);
                        startActivity(intent1);
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(RepairInfoList.this, DeviceList.class);
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

    //弹出式菜单的单击事件处理
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // TODO Auto-generated method stub
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.insert_item:
                insert();
                break;
            case R.id.delete_item:
//                intent=new Intent(RepairInfoList.this,RepairInfoList.class);
                //data.get(pos)得到被点击的RepairInfo
                AlertDialog dialog1 = new AlertDialog.Builder(this)
                        .setTitle("确定删除维修信息<" + data.get(pos).getInfoID() + ">?")//设置对话框的标题
                        //设置对话框的按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(RepairInfoList.this, "已取消", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteRepairInfo();
                                dialog.dismiss();
                                refresh();
                            }
                        }).create();
                dialog1.show();
                break;
            case R.id.update_item:
                intent = new Intent(RepairInfoList.this, Up_RepairInfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("infoID", data.get(pos).getInfoID());
                bundle.putString("deviceID", data.get(pos).getDeviceID());
                bundle.putString("deviceName", data.get(pos).getDeviceName());
                bundle.putString("damageDate", data.get(pos).getDamageDate());
                bundle.putString("damageDegree", data.get(pos).getDamageDegree());
                bundle.putString("damageCause", data.get(pos).getDamageCause());
                bundle.putString("repairDate", data.get(pos).getRepairDate());
                bundle.putString("repairPersonnel", data.get(pos).getRepairPersonnel());
                bundle.putString("state", data.get(pos).getState());
                bundle.putString("userID", data.get(pos).getUserID());
                bundle.putString("userName", data.get(pos).getUsername());
                bundle.putString("principalName", data.get(pos).getPrincipalName());
                bundle.putFloat("cost", data.get(pos).getCost());
                bundle.putString("myID", myID);
                bundle.putString("identity", identity);
                intent.putExtras(bundle);
                break;
            case R.id.item_1:
                intent = new Intent(RepairInfoList.this, AccountList.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("identity", identity);
                bundle1.putBoolean("bo_select", true);
                bundle1.putString("select", data.get(pos).getUserID());
                bundle1.putString("findByID", "C");
                intent.putExtras(bundle1);
                break;
            case R.id.item_2:
                intent = new Intent(RepairInfoList.this, AccountList.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("identity", identity);
                bundle2.putBoolean("bo_select", true);
                bundle2.putString("select", data.get(pos).getPrincipal());
                bundle2.putString("findByID", "D");
                intent.putExtras(bundle2);
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
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载中");
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
        //创建弹出式菜单对象（最低版本11）
        PopupMenu popup = new PopupMenu(RepairInfoList.this, view);//第二个参数是绑定的那个view
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        if (left) {
            inflater.inflate(R.menu.menu_item_2, popup.getMenu());
            MenuItem item1 = popup.getMenu().findItem(R.id.item_1);//获取popup的菜单,找到item设置其标题
            item1.setTitle("维修负责人");
            MenuItem item2 = popup.getMenu().findItem(R.id.item_2);//获取popup的菜单,找到item设置其标题
            item2.setTitle("设备负责人");
        } else {
            inflater.inflate(R.menu.menu, popup.getMenu());
        }
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(RepairInfoList.this);
        //显示(这一行代码不要忘记了)
        popup.show();
    }

    public void findByid() {
        pulltorefreshview = (AbPullToRefreshView) findViewById(R.id.pulltorefreshview);
//        pulltorefreshview.setPullRefreshEnable(false);
        tv_table_title_left = (TextView) findViewById(R.id.tv_table_title_left);
        tv_table_title_left.setText("维修信息");
        leftListView = (ListView) findViewById(R.id.left_container_listview);
        rightListView = (ListView) findViewById(R.id.right_container_listview);
        right_title_container = (LinearLayout) findViewById(R.id.right_title_container);
        getLayoutInflater().inflate(R.layout.table_right_title, right_title_container);

        TitleName_Right();

        titleHorScv = (SyncHorizontalScrollView) findViewById(R.id.title_horsv);
        contentHorScv = (SyncHorizontalScrollView) findViewById(R.id.content_horsv);
        // 设置两个水平控件的联动
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
        TextView tv_table_title_9 = (TextView) findViewById(R.id.tv_table_title_9);
        tv_table_title_0.setText("信息编号 ");
        tv_table_title_1.setText("设备名称");
        tv_table_title_2.setText("损坏日期");
        tv_table_title_3.setText("损坏程度");
        tv_table_title_4.setText("损坏原因");
        tv_table_title_5.setText("维修日期");
        tv_table_title_6.setText("维修人员");
        tv_table_title_7.setText("维修花费");
        tv_table_title_8.setText("处理状态");
        tv_table_title_9.setText("负责人");
    }

    /**
     * 初始化标题的TextView的item引用
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
                tv_table_content_right_item7.setText(item.getText7());
                tv_table_content_right_item8.setText(item.getText8());
                tv_table_content_right_item9.setText(item.getText9());
                //部分行设置颜色凸显
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
                if (identity.equals("A")) {
                    Jump(view, position, true);//跳转界面
                } else {
                    Jump(view, position, false);//跳转界面
                }
            }
        });
        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Jump(view, position, false);//跳转界面
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
                ArrayList<RepairInfo> list = null;
                if (bo_select) {
                    if (findByID == null) {
                        if (identity.equals("B")) {
                            list = DB_RepairInfo.selectMyInfo(selectBy, myID, select);
                        } else {
                            list = DB_RepairInfo.findBy(selectBy, select);//根据传递的字段搜索
                        }
                    } else {
                        list = DB_RepairInfo.findByID(findByID, select);//根据传递的字段搜索
                        findByID = "";
                    }
                } else {
                    if (identity.equals("B")) {
                        list = DB_RepairInfo.findRepairInfoByID(myID);
                    } else {
                        list = DB_RepairInfo.findRepairInfo();//搜索全表
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
                    List<RepairInfoBean> BeanList = new ArrayList<>();
                    if (list != null) {
                        ArrayList<RepairInfo> userslist = list;
                        for (int i = 0 + pageno * 20; i < 20 * (pageno + 1); i++) {
                            try {
                                RepairInfo user = userslist.get(i);
                                RepairInfoBean bean = new RepairInfoBean();
                                bean.setInfoID(user.getInfoID());
                                bean.setDeviceName(user.getDeviceName());
                                bean.setDamageDate(user.getDamageDate());
                                bean.setDamageDegree(user.getDamageDegree());
                                bean.setDamageCause(user.getDamageCause());
                                bean.setRepairDate(user.getRepairDate());
                                bean.setRepairPersonnel(user.getRepairPersonnel());
                                bean.setCost(user.getCost());
                                bean.setState(user.getState());
                                bean.setUsername(user.getUsername());
                                BeanList.add(bean);
                            } catch (Exception e) {

                            }
                        }
                    }
                    data = new ArrayList<RepairInfo>();
                    if (list != null) {
                        ArrayList<RepairInfo> userslist = list;
                        for (RepairInfo user : userslist) {
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

    //模拟网络请求
    public void doGetDatas(int pageno, int state) {
        if (state == RefreshParams.REFRESH_DATA) {
            pulltorefreshview.onHeaderRefreshFinish();
            NumTitle = 0;
        } else {
            pulltorefreshview.onFooterLoadFinish();
        }
        FindAccountList(state, pageno);
    }

    private void setDatas(List<RepairInfoBean> onlineSaleBeanList, int type) {
        if (onlineSaleBeanList.size() > 0) {
            List<TableModel> mDatas = new ArrayList<>();
            for (int i = 0; i < onlineSaleBeanList.size(); i++) {
                RepairInfoBean bean = onlineSaleBeanList.get(i);
                TableModel tableMode = new TableModel();
//                tableMode.setOrgCode("A" + i);
                String state = null;
                if (bean.getDamageDegree().equals("A")) {
                    state = "轻微损坏";
                } else if (bean.getDamageDegree().equals("B")) {
                    state = "中度损坏";
                } else if (bean.getDamageDegree().equals("C")) {
                    state = "重度损坏";
                } else {
                    state = "NULL";
                }
                tableMode.setLeftTitle("" + (++NumTitle));//行名
                tableMode.setText0(bean.getInfoID() + "");//列1内容
                tableMode.setText1(bean.getDeviceName());
                tableMode.setText2(bean.getDamageDate() + "");//列2内容
                tableMode.setText3(state + "");
                tableMode.setText4(bean.getDamageCause() + "");
                tableMode.setText5(bean.getRepairDate() + "");//
                tableMode.setText6(bean.getRepairPersonnel());//
                tableMode.setText7(bean.getCost() + "");//
                if (bean.getState().equals("A")) {
                    state = "轻微损坏";
                } else if (bean.getState().equals("B")) {
                    state = "中度损坏";
                } else if (bean.getState().equals("C")) {
                    state = "重度损坏";
                } else {
                    state = "NULL";
                }
                tableMode.setText8(state + "");//
                tableMode.setText9(bean.getUsername() + "");//
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
            //加载数据成功，增加页数
            pageNo++;
//            if (mDatas.size() < 20) {
//                pulltorefreshview.setLoadMoreEnable(false);
//            }
            mDatas.clear();
        } else {
            //数据为null
            if (type == RefreshParams.REFRESH_DATA) {
                mLeftAdapter.clearData(true);
                mRightAdapter.clearData(true);
                //显示数据为空的视图
                //                mEmpty.setShowErrorAndPic(getString(R.string.empty_null), 0);
            } else if (type == RefreshParams.LOAD_DATA) {
                Toast.makeText(mContext, "到头了", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
