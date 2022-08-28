package com.yxz.amadeus.ems.Activity.Interface;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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

import com.yxz.amadeus.ems.Activity.Update.In_Account;
import com.yxz.amadeus.ems.Activity.Update.In_CheckInfo;
import com.yxz.amadeus.ems.Activity.Update.In_Device;
import com.yxz.amadeus.ems.Activity.Update.In_RepairInfo;
import com.yxz.amadeus.ems.Activity.Update.In_UseInfo;
import com.yxz.amadeus.ems.Activity.Update.Up_Account;
import com.yxz.amadeus.ems.Activity.Update.Up_CheckInfo;
import com.yxz.amadeus.ems.Activity.Update.Up_Device;
import com.yxz.amadeus.ems.Activity.Update.Up_RepairInfo;
import com.yxz.amadeus.ems.DAO.DB_Account;
import com.yxz.amadeus.ems.Table.TableActivity;
import com.yxz.amadeus.ems.Table.base.RefreshParams;
import com.yxz.amadeus.ems.Table.base.adapter.AbsCommonAdapter;
import com.yxz.amadeus.ems.Table.base.adapter.AbsViewHolder;
import com.yxz.amadeus.ems.Table.bean.AccountBean;
import com.yxz.amadeus.ems.Table.bean.OnlineSaleBean;
import com.yxz.amadeus.ems.Table.bean.TableModel;
import com.yxz.amadeus.ems.Table.utils.WeakHandler;
import com.yxz.amadeus.ems.Table.widget.SyncHorizontalScrollView;
import com.yxz.amadeus.ems.Table.widget.pullrefresh.AbPullToRefreshView;
import com.yxz.amadeus.ems.entity.Account;

import com.yxz.amadeus.ems.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AccountList extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    //    private Account account;
    private List<Account> data = new ArrayList<Account>();
    private RecyclerView recyclerview;
    private int pos = 0;//点击位置,据此定位data中的账户实体
    private int jump = 0;//0.用户管理1.开启新In_Device插入设备信息/2.返回In_Device插入设备信息/3.返回Up_Device更新设备信息
    private boolean bo_select = false;
    private String select, myID, identity, selectBy, findByID = "";
    private SparseArray<TextView> mTitleTvArray;
    //表格部分
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
            jump = bundle.getInt("jump");
            bo_select = bundle.getBoolean("bo_select");//根据此数据判断是搜索全表还是具体搜索
            select = bundle.getString("select");//接收要搜索的字段
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

    //刷新当前页面
    private void refresh() {
        //获取当前界面bo_select和select的值进行刷新
        Intent intent = new Intent(AccountList.this, AccountList.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("bo_select", bo_select);
        bundle.putString("select", select);//向用户列表界面传递搜索内容
        bundle.putString("myID", myID);
        bundle.putString("identity", identity);
        intent.putExtras(bundle);
        startActivity(intent);
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载中");
        dialog.show();
        finish();
        dialog.dismiss();
    }

    private void Jump(View view, int position, boolean left) {
        pos = position;
        if (jump == 0) {
            //创建弹出式菜单对象（最低版本11）
            PopupMenu popup = new PopupMenu(AccountList.this, view);//第二个参数是绑定的那个view
            //获取菜单填充器
            MenuInflater inflater = popup.getMenuInflater();
            //填充菜单
            if (left) {
                inflater.inflate(R.menu.menu_item_4, popup.getMenu());
                MenuItem item1 = popup.getMenu().findItem(R.id.item_1);//获取popup的菜单,找到item设置其标题
                item1.setTitle("负责设备");
            } else {
                inflater.inflate(R.menu.menu, popup.getMenu());
            }
            //绑定菜单项的点击事件
            popup.setOnMenuItemClickListener(AccountList.this);
            //显示(这一行代码不要忘记了)
            popup.show();
        } else if (jump == 1 || jump == 2 || jump == 3 || jump == 12 || jump == 13 || jump == 22 || jump == 23|| jump == 32 || jump == 33) {//12.返回In插入信息/13.返回Up更新信息<使用信息>
            JumpInfo();
        }
    }

    private void JumpInfo() {
        AlertDialog dialog = new AlertDialog.Builder(AccountList.this)
                .setTitle("是否选择" + data.get(pos).getUsername() + "(" + data.get(pos).getUserID() + ")?")//设置对话框的标题
//                        .setMessage("我是对话框的内容")//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AccountList.this, "已取消", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent1;
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("userID", data.get(pos).getUserID());
                        bundle1.putString("userName", data.get(pos).getUsername());
                        bundle1.putString("myID", myID);
                        bundle1.putString("identity", identity);
                        Toast.makeText(AccountList.this, "你选择了" + data.get(pos).getUsername() + "(" + data.get(pos).getUserID() + ")",
                                Toast.LENGTH_SHORT).show();
                        if (jump == 1) {//1.开启新In_Device插入设备信息
                            intent1 = new Intent(AccountList.this, In_Device.class);
                            intent1.putExtras(bundle1);
                            startActivity(intent1);
                        } else if (jump == 2) {//2.返回In_Device插入设备信息
                            intent1 = new Intent(AccountList.this, In_Device.class);
                            intent1.putExtras(bundle1);
                            setResult(Activity.RESULT_OK, intent1);
                        } else if (jump == 3) {//3.返回Up_Device更新设备信息
                            intent1 = new Intent(AccountList.this, Up_Device.class);
                            intent1.putExtras(bundle1);
                            setResult(Activity.RESULT_OK, intent1);
                        } else if (jump == 12) {
                            intent1 = new Intent(AccountList.this, In_UseInfo.class);
                            intent1.putExtras(bundle1);
                            setResult(Activity.RESULT_OK, intent1);
                        } else if (jump == 13) {
                            intent1 = new Intent(AccountList.this, In_UseInfo.class);
                            intent1.putExtras(bundle1);
                            setResult(Activity.RESULT_OK, intent1);
                        } else if (jump == 22) {
                            intent1 = new Intent(AccountList.this, In_CheckInfo.class);
                            intent1.putExtras(bundle1);
                            setResult(Activity.RESULT_OK, intent1);
                        } else if (jump == 23) {
                            intent1 = new Intent(AccountList.this, Up_CheckInfo.class);
                            intent1.putExtras(bundle1);
                            setResult(Activity.RESULT_OK, intent1);
                        }else if (jump == 32) {
                            intent1 = new Intent(AccountList.this, In_RepairInfo.class);
                            intent1.putExtras(bundle1);
                            setResult(Activity.RESULT_OK, intent1);
                        } else if (jump == 33) {
                            intent1 = new Intent(AccountList.this, Up_RepairInfo.class);
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

    //删除用户
    private void DeleteAccount() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                if (DB_Account.deleteAccount(data.get(pos).getUserID())) {
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
                    Toast.makeText(AccountList.this, " 删除成功",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    Toast.makeText(AccountList.this, " 删除失败",
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
        if (jump == 0) {
            getMenuInflater().inflate(R.menu.menu_add, menu); //通过getMenuInflater()方法得到MenuInflater对象，再调用它的inflate()方法就可以给当前活动创建菜单了，第一个参数：用于指定我们通过哪一个资源文件来创建菜单；第二个参数：用于指定我们的菜单项将添加到哪一个Menu对象当中。
            return true; // true：允许创建的菜单显示出来，false：创建的菜单将无法显示。
        } else {
            return false;
        }
    }

    /**
     * 菜单的点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.id_add_item:
                startActivity(new Intent(AccountList.this, In_Account.class));
                break;
            case R.id.id_refresh_item:
                refresh();
                break;
            case R.id.id_select_item:
                View view = getLayoutInflater().inflate(R.layout.half_dialog_view, null);
                final EditText editText = (EditText) view.findViewById(R.id.dialog_edit);
                AlertDialog dialog = new AlertDialog.Builder(AccountList.this)
                        .setTitle("请输入要搜索的用户ID或姓名")//设置对话框的标题
                        .setView(view)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String content = editText.getText().toString();
                                Toast.makeText(AccountList.this, content, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                select = content;
                                bo_select = true;
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


    //弹出式菜单的单击事件处理
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // TODO Auto-generated method stub
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.insert_item:
                intent = new Intent(AccountList.this, In_Account.class);
                break;
            case R.id.delete_item:
//                intent=new Intent(AccountList.this,AccountList.class);
                //data.get(pos)得到被点击的Account
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("确定删除用户<" + data.get(pos).getUserID() + ">?")//设置对话框的标题
//                        .setMessage("我是对话框的内容")//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AccountList.this, "已取消", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteAccount();
//                                Toast.makeText(AccountList.this, "点击了确定的按钮", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                refresh();
                            }
                        }).create();
                dialog.show();
                break;
            case R.id.update_item:
                intent = new Intent(AccountList.this, Up_Account.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("register", true);
                bundle.putString("type", data.get(pos).getType());
                bundle.putString("userID", data.get(pos).getUserID());
                bundle.putString("pwd", data.get(pos).getPassword());
                bundle.putString("name", data.get(pos).getUsername());
                bundle.putString("grade", data.get(pos).getGrade());
                bundle.putString("dept", data.get(pos).getDept());
                bundle.putString("tele", data.get(pos).getTelephone());
                bundle.putString("myID", myID);
                bundle.putString("identity", identity);
                intent.putExtras(bundle);
                break;
            case R.id.item_1:
                if (data.get(pos).getType().equals("C")) {
                    Toast.makeText(mContext, "使用人无负责设备", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(AccountList.this, DeviceList.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("identity", identity);
                    bundle1.putBoolean("bo_select", true);
                    bundle1.putString("select", data.get(pos).getUserID());
                    bundle1.putString("findByID", "C");
                    intent.putExtras(bundle1);
                }
                break;
            case R.id.item_2:
                intent = new Intent(AccountList.this, UseInfoList.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("identity", identity);
                bundle2.putBoolean("bo_select", true);
                bundle2.putString("select", data.get(pos).getUserID());
                bundle2.putString("findByID", "C");
                intent.putExtras(bundle2);
                break;
            case R.id.item_3:
                intent = new Intent(AccountList.this, RepairInfoList.class);
                Bundle bundle3 = new Bundle();
                bundle3.putString("identity", identity);
                bundle3.putBoolean("bo_select", true);
                bundle3.putString("select", data.get(pos).getUserID());
                bundle3.putString("findByID", "C");
                intent.putExtras(bundle3);
                break;
            case R.id.item_4:
                intent = new Intent(AccountList.this, CheckInfoList.class);
                Bundle bundle4 = new Bundle();
                bundle4.putString("identity", identity);
                bundle4.putBoolean("bo_select", true);
                bundle4.putString("select", data.get(pos).getUserID());
                bundle4.putString("findByID", "C");
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

    public void init() {
        mContext = getApplicationContext();
        findByid();
        setListener();
        setData();
    }

    public void findByid() {
        pulltorefreshview = (AbPullToRefreshView) findViewById(R.id.pulltorefreshview);
//        pulltorefreshview.setPullRefreshEnable(false);
        tv_table_title_left = (TextView) findViewById(R.id.tv_table_title_left);
        tv_table_title_left.setText("用户列表");
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
        tv_table_title_0.setText("类型");
        tv_table_title_1.setText("账户名");
        tv_table_title_2.setText("姓名");
        tv_table_title_3.setText("密码");
        tv_table_title_4.setText("年级");
        tv_table_title_5.setText("系别");
        tv_table_title_6.setText("电话");
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
                tv_table_content_right_item7.setVisibility(View.GONE);
                tv_table_content_right_item8.setVisibility(View.GONE);
                tv_table_content_right_item9.setVisibility(View.GONE);
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
                Jump(view, position, true);//跳转界面
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
                ArrayList<Account> list = null;
                if (bo_select) {
                    if (findByID==null) {
                        list = DB_Account.findBy(select);
                    } else {
                        list = DB_Account.findById(select);//根据传递的字段搜索用户表
                        findByID="";
                    }
                } else {
                    if(jump==1||jump==2||jump==3||jump==22||jump==23||jump==32||jump==33){
                        list = DB_Account.findAccount("C");//搜索全表管理员和负责人
                    }else {
                        list = DB_Account.findAccount("A");//搜索全表
                    }
                }
                Bundle data = new Bundle();
                data.putParcelableArrayList("Account", list);
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
                    ArrayList list = bundle.getParcelableArrayList("Account");
                    int state = bundle.getInt("state");
                    int pageno = bundle.getInt("pageno");
                    List<AccountBean> accountBeanList = new ArrayList<>();
                    if (list != null) {
                        ArrayList<Account> userslist = list;
                        for (int i = 0 + pageno * 20; i < 20 * (pageno + 1); i++) {
                            try {
                                Account user = userslist.get(i);
                                AccountBean accountBean = new AccountBean();
                                accountBean.setUserID(user.getUserID());
                                accountBean.setUserID(user.getUserID());
                                accountBean.setUsername(user.getUsername());
                                accountBean.setPassword(user.getPassword());
                                accountBean.setType(user.getType());
                                accountBean.setGrade(user.getGrade());
                                accountBean.setDept(user.getDept());
                                accountBean.setTelephone(user.getTelephone());
                                accountBeanList.add(accountBean);
                            } catch (Exception e) {

                            }
                        }
                    }
                    data = new ArrayList<Account>();
                    if (list != null) {
                        ArrayList<Account> userslist = list;
                        for (Account user : userslist) {
                            data.add(user);
                        }
                    }
                    setDatas(accountBeanList, state);
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

    private void setDatas(List<AccountBean> onlineSaleBeanList, int type) {
        if (onlineSaleBeanList.size() > 0) {
            List<TableModel> mDatas = new ArrayList<>();
            for (int i = 0; i < onlineSaleBeanList.size(); i++) {
                AccountBean bean = onlineSaleBeanList.get(i);
                TableModel tableMode = new TableModel();
//                tableMode.setOrgCode("A" + i);
                tableMode.setLeftTitle("" + (++NumTitle));//行名
                if (bean.getType().equals("A")) {
                    tableMode.setText0("管理员");
                } else if (bean.getType().equals("B")) {
                    tableMode.setText0("负责人");
                } else {
                    tableMode.setText0("使用人");
                }
                tableMode.setText1(bean.getUserID() + "");//列1内容
                tableMode.setText2(bean.getUsername() + "");//列2内容
                if (jump == 0) {
                    tableMode.setText3(bean.getPassword() + "");
                } else {
                    tableMode.setText3("HIDE");
                }
                tableMode.setText4(bean.getGrade() + "");
                tableMode.setText5(bean.getDept() + "");//
                tableMode.setText6(bean.getTelephone() + "");//
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
