package com.yxz.amadeus.ems.Activity.Interface;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.yxz.amadeus.ems.Activity.Update.Up_CheckInfo;
import com.yxz.amadeus.ems.DAO.DB_Account;
import com.yxz.amadeus.ems.DAO.DB_CheckInfo;
import com.yxz.amadeus.ems.Table.base.RefreshParams;
import com.yxz.amadeus.ems.Table.base.adapter.AbsCommonAdapter;
import com.yxz.amadeus.ems.Table.base.adapter.AbsViewHolder;
import com.yxz.amadeus.ems.Table.bean.AccountBean;
import com.yxz.amadeus.ems.Table.bean.CheckInfoBean;
import com.yxz.amadeus.ems.Table.bean.TableModel;
import com.yxz.amadeus.ems.Table.utils.WeakHandler;
import com.yxz.amadeus.ems.Table.widget.SyncHorizontalScrollView;
import com.yxz.amadeus.ems.Table.widget.pullrefresh.AbPullToRefreshView;
import com.yxz.amadeus.ems.entity.Account;
import com.yxz.amadeus.ems.entity.CheckInfo;

import com.yxz.amadeus.ems.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CheckInfoList extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private List<CheckInfo> data = new ArrayList<CheckInfo>();
    private RecyclerView recyclerview;
    private int pos=0;
    private boolean bo_select=false;
    private String select="",identity="",myID="",selectBy,findByID="";
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
    private int NumTitle=0;//?????????
    private WeakHandler Handler = new WeakHandler();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_layout);
        init();
        try {
            Bundle bundle=getIntent().getExtras();
            bo_select=bundle.getBoolean("bo_select");//??????????????????????????????????????????????????????
            identity=bundle.getString("identity");
            myID=bundle.getString("myID");
            select=bundle.getString("select");//????????????????????????
            try{
                selectBy=bundle.getString("selectBy");
            }catch (Exception e){

            }
            findByID=bundle.getString("findByID");
        }catch (Exception e){

        }
    }
    //??????????????????
    private void refresh(){
        //??????????????????bo_select???select??????????????????
        Intent intent=new Intent(CheckInfoList.this,CheckInfoList.class);
        Bundle bundle=new Bundle();
        bundle.putBoolean("bo_select",bo_select);
        bundle.putString("myID",myID);
        bundle.putString("identity",identity);
        bundle.putString("select",select);//?????????????????????????????????
        bundle.putString("selectBy",selectBy);
        intent.putExtras(bundle);
        startActivity(intent);
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("???????????????");
        dialog.show();
        finish();
    }
    private void loading(){
        ProgressDialog dialog = new ProgressDialog(CheckInfoList.this);
        dialog.setMessage("???????????????");
        dialog.show();
    }
    private void Jump(View view, int position,boolean left){
        pos = position;
        //??????????????????????????????????????????11???
        PopupMenu popup = new PopupMenu(CheckInfoList.this, view);//?????????????????????????????????view
        //?????????????????????
        MenuInflater inflater = popup.getMenuInflater();
        //????????????
        if (left){
            inflater.inflate(R.menu.menu_item_2, popup.getMenu());
            MenuItem item1 = popup.getMenu().findItem(R.id.item_1);//??????popup?????????,??????item???????????????
            item1.setTitle("?????????");
            MenuItem item2 = popup.getMenu().findItem(R.id.item_2);//??????popup?????????,??????item???????????????
            item2.setTitle("???????????????");
        }else {
            inflater.inflate(R.menu.menu, popup.getMenu());
        }
        //??????????????????????????????
        popup.setOnMenuItemClickListener(CheckInfoList.this);
        //??????(??????????????????????????????)
        popup.show();
    }
    private void DeleteCheckInfo() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                if (DB_CheckInfo.deleteCheckInfo(data.get(pos).getInfoID())) {
                    msg.what = 1001;
                }else {
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
                    Toast.makeText(CheckInfoList.this, " ????????????",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    Toast.makeText(CheckInfoList.this, " ????????????",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    /**
     *????????????
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add,menu); //??????getMenuInflater()????????????MenuInflater????????????????????????inflate()??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????Menu???????????????
        return true; // true???????????????????????????????????????false????????????????????????????????????
    }

    /**
     *?????????????????????
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.id_add_item:
                insert();
                break;
            case R.id.id_refresh_item:
                refresh();
                break;
            case R.id.id_select_item:
                View view = getLayoutInflater().inflate(R.layout.half_dialog_view, null);
                final EditText editText = (EditText) view.findViewById(R.id.dialog_edit);
                android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(CheckInfoList.this)
                        .setTitle("??????????????????????????????????????????ID/??????/????????????/????????????/????????????")//????????????????????????
                        .setView(view)
                        .setNeutralButton("????????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String content = editText.getText().toString();
                                Toast.makeText(CheckInfoList.this, content, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                select=content;
                                bo_select=true;
                                selectBy="A";
                                init();
                            }
                        })
                        .setNegativeButton("????????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String content = editText.getText().toString();
                                Toast.makeText(CheckInfoList.this, content, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                select=content;
                                bo_select=true;
                                selectBy="B";
                                init();
                            }
                        })
                        .setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String content = editText.getText().toString();
                                Toast.makeText(CheckInfoList.this, content, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                select=content;
                                bo_select=true;
                                selectBy="C";
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

    //????????????
    private void insert(){
        AlertDialog dialog = new AlertDialog.Builder(CheckInfoList.this)
                .setTitle("?????????????????????????????????????")//????????????????????????
                //????????????????????????
                .setNegativeButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent1=new Intent(CheckInfoList.this,In_CheckInfo.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("myID",myID);
                        bundle.putString("identity",identity);
                        intent1.putExtras(bundle);
                        startActivity(intent1);
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(CheckInfoList.this,DeviceList.class);
                        Bundle bundle1=new Bundle();
                        bundle1.putInt("jump",21);
                        bundle1.putString("myID",myID);
                        bundle1.putString("identity",identity);
                        intent.putExtras(bundle1);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    //????????????????????????????????????
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // TODO Auto-generated method stub
        Intent intent=null;
        switch (item.getItemId()) {
            case R.id.insert_item:
                insert();
                break;
            case R.id.delete_item:
//                intent=new Intent(CheckInfoList.this,CheckInfoList.class);
                //data.get(pos)??????????????????CheckInfo
                AlertDialog dialog1 = new AlertDialog.Builder(this)
                        .setTitle("????????????????????????<"+data.get(pos).getInfoID()+">?")//????????????????????????
                        //????????????????????????
                        .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(CheckInfoList.this, "?????????", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteCheckInfo();
                                dialog.dismiss();
                                refresh();
                            }
                        }).create();
                dialog1.show();
                break;
            case R.id.update_item:
                intent=new Intent(CheckInfoList.this,Up_CheckInfo.class);
                Bundle bundle=new Bundle();
                bundle.putString("infoID",data.get(pos).getInfoID());
                bundle.putString("deviceID",data.get(pos).getDeviceID());
                bundle.putString("deviceName",data.get(pos).getDeviceName());
                bundle.putString("lastCheck",data.get(pos).getLastCheck());
                bundle.putString("nextCheck",data.get(pos).getNextCheck());
                bundle.putString("lastRepair",data.get(pos).getLastRepair());
                bundle.putString("state",data.get(pos).getState());
                bundle.putString("inspector",data.get(pos).getInspector());
                bundle.putString("inspectorName",data.get(pos).getInspectorName());
                bundle.putString("userID",data.get(pos).getUserID());
                bundle.putString("userName",data.get(pos).getUserName());
                bundle.putInt("cycle",data.get(pos).getCycle());
                bundle.putString("myID",myID);
                bundle.putString("identity",identity);
                intent.putExtras(bundle);
                break;
            case R.id.item_1:
                intent = new Intent(CheckInfoList.this, AccountList.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("identity", identity);
                bundle1.putBoolean("bo_select", true);
                bundle1.putString("select", data.get(pos).getInspector());
                bundle1.putString("findByID", "C");
                intent.putExtras(bundle1);
                break;
            case R.id.item_2:
                intent = new Intent(CheckInfoList.this, AccountList.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("identity", identity);
                bundle2.putBoolean("bo_select", true);
                bundle2.putString("select", data.get(pos).getUserID());
                bundle2.putString("findByID", "D");
                intent.putExtras(bundle2);
                break;
            default:
                break;
        }
        if (intent!=null) {
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

    private void TitleName_Right(){
        TextView tv_table_title_0=(TextView)findViewById(R.id.tv_table_title_0);
        TextView tv_table_title_1=(TextView)findViewById(R.id.tv_table_title_1);
        TextView tv_table_title_2=(TextView)findViewById(R.id.tv_table_title_2);
        TextView tv_table_title_3=(TextView)findViewById(R.id.tv_table_title_3);
        TextView tv_table_title_4=(TextView)findViewById(R.id.tv_table_title_4);
        TextView tv_table_title_5=(TextView)findViewById(R.id.tv_table_title_5);
        TextView tv_table_title_6=(TextView)findViewById(R.id.tv_table_title_6);
        TextView tv_table_title_7=(TextView)findViewById(R.id.tv_table_title_7);
        TextView tv_table_title_8=(TextView)findViewById(R.id.tv_table_title_8);
        tv_table_title_0.setText("????????????");
        tv_table_title_1.setText("????????????");
        tv_table_title_2.setText("????????????");
        tv_table_title_3.setText("????????????");
        tv_table_title_4.setText("????????????");
        tv_table_title_5.setText("????????????");
        tv_table_title_6.setText("????????????");
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
                tv_table_content_right_item7.setText(item.getText7());
                tv_table_content_right_item8.setText(item.getText8());
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
                Jump(view,position,false);//????????????
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
                ArrayList<CheckInfo> list = null;
                if (bo_select){
                    if(findByID==null){
                        if (identity.equals("B")) {
                            list = DB_CheckInfo.selectMyInfo(selectBy, myID, select);
                        } else {
                            list = DB_CheckInfo.findBy(selectBy, select);//???????????????????????????
                        }
                    }else {
                        list=DB_CheckInfo.findByID(findByID,select);
                        findByID="";
                    }

                }else {
                    if(identity.equals("B")){
                        list = DB_CheckInfo.findCheckInfoByID(myID);
                    }else {
                        list = DB_CheckInfo.findCheckInfo();//????????????
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
                    List<CheckInfoBean> BeanList = new ArrayList<>();
                    if (list != null) {
                        ArrayList<CheckInfo> userslist = list;
                        for (int i = 0 + pageno * 20; i < 20 * (pageno + 1); i++) {
                            try {
                                CheckInfo user=userslist.get(i);
                                CheckInfoBean bean = new CheckInfoBean();
                                bean.setInfoID(user.getInfoID());
                                bean.setDeviceName(user.getDeviceName());
                                bean.setCycle(user.getCycle());
                                bean.setLastCheck(user.getLastCheck());
                                bean.setNextCheck(user.getNextCheck());
                                bean.setLastRepair(user.getLastRepair());
                                bean.setState(user.getState());
                                bean.setInspectorName(user.getInspectorName());
                                bean.setUserName(user.getUserName());
                                BeanList.add(bean);
                            } catch (Exception e) {

                            }
                        }
                    }
                    data = new ArrayList<CheckInfo>();
                    if (list != null) {
                        ArrayList<CheckInfo> userslist = list;
                        for (CheckInfo user : userslist) {
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
            NumTitle=0;
        } else {
            pulltorefreshview.onFooterLoadFinish();
        }
        FindAccountList(state, pageno);
    }

    private void setDatas(List<CheckInfoBean> onlineSaleBeanList, int type) {
        if (onlineSaleBeanList.size() > 0) {
            List<TableModel> mDatas = new ArrayList<>();
            for (int i = 0; i < onlineSaleBeanList.size(); i++) {
                CheckInfoBean bean = onlineSaleBeanList.get(i);
                TableModel tableMode = new TableModel();
//                tableMode.setOrgCode("A" + i);
                String state=null;
                try {
                    if(bean.getState().indexOf("A")!=-1){
                        state="??????";
                    }else if(bean.getState().indexOf("B")!=-1){
                        state="??????";
                    }else if(bean.getState().indexOf("C")!=-1){
                        state="??????";
                    }else if(bean.getState().indexOf("D")!=-1){
                        state="??????";
                    }else if(bean.getState().indexOf("E")!=-1){
                        state="????????????";
                    }
                    if (state==null){

                    }else {
                        state=state.concat("&");
                    }
                    if(bean.getState().indexOf("X")!=-1){
                        state=state.concat("?????????");
                    }else if(bean.getState().indexOf("Y")!=-1){
                        state=state.concat("?????????");
                    }
                }catch (Exception e){

                }
                tableMode.setLeftTitle(""+(++NumTitle));//??????
                tableMode.setText0(bean.getInfoID() + "");//???1??????
                tableMode.setText1(bean.getDeviceName());
                tableMode.setText2(bean.getCycle() + "");//???2??????
                tableMode.setText3(bean.getLastCheck() + "");
                tableMode.setText4(bean.getNextCheck() + "");
                tableMode.setText5(bean.getLastRepair() + "");//
                tableMode.setText6(state+ "");//
                tableMode.setText7(bean.getInspectorName() + "");//
                tableMode.setText8(bean.getUserName() + "");//
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
