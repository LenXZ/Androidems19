package com.yxz.amadeus.ems.Activity;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yxz.amadeus.ems.Activity.Interface.InterfaceA;
import com.yxz.amadeus.ems.Activity.Interface.InterfaceB;
import com.yxz.amadeus.ems.Activity.Interface.InterfaceC;
import com.yxz.amadeus.ems.Activity.Update.In_Account;
import com.yxz.amadeus.ems.Activity.Update.Up_Account;
import com.yxz.amadeus.ems.DAO.DB_Account;
import com.yxz.amadeus.ems.R;
import com.yxz.amadeus.ems.entity.Account;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
//    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private String userID = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        //注册
        Button btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, In_Account.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("register", false);
                intent.putExtras(bundle);
                startActivity(intent);
                ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                dialog.setMessage("正在加载中");
                dialog.show();
                finish();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }
    /**通过子线程联网访问数据*/
    /***/
    private void checkpwd() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                ArrayList<Account> list =DB_Account.userLogin(userID, password);
                Message msg = new Message();
                if(list!=null){
                    msg.what = 1001;
                    Bundle data = new Bundle();
                    data.putParcelableArrayList("Account",list);
                    msg.setData(data);
                    mHandler.sendMessage(msg);
                }else {
                    msg.what = 1002;
                    mHandler.sendMessage(msg);
                }
            }
        };
        new Thread(run).start();

    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    Bundle bundle = msg.getData();
                    Boolean check = false;
                    ArrayList list =bundle.getParcelableArrayList("Account");
                    if(list!=null) {
                        ArrayList<Account> userslist = list;
                        for (Account user : userslist) {
                            if (user.getType().equals("A")) {
                                Intent intent1 = new Intent(LoginActivity.this, InterfaceA.class);
                                startActivity(intent1);
                                loading();
                                finish();
                                break;
                            } else if (user.getType().equals("B")) {
                                Intent intent1 = new Intent(LoginActivity.this, InterfaceB.class);
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("myID", user.getUserID());
                                intent1.putExtras(bundle1);
                                startActivity(intent1);
                                loading();
                                finish();
                                break;
                            } else {
                                Intent intent1 = new Intent(LoginActivity.this, InterfaceC.class);
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("myID", user.getUserID());
                                bundle1.putString("myName", user.getUsername());
                                intent1.putExtras(bundle1);
                                startActivity(intent1);
                                loading();
                                finish();
                                break;
                            }
//                            if(userID.equals(user.getUserID())&&password.equals(user.getPassword())){
//                                check=true;
//                                if(user.getType().equals("A")){
//                                    //跳转至管理员界面
//                                    Intent intent1 = new Intent(LoginActivity.this,InterfaceA.class);
//                                    startActivity(intent1);
//                                    loading();
//                                    finish();
//                                    break;
//
//                                }else if(user.getType().equals("B")){
//                                    //跳转至负责人界面
//                                    Intent intent1 = new Intent(LoginActivity.this,InterfaceB.class);
//                                    Bundle bundle1=new Bundle();
//                                    bundle1.putString("myID",user.getUserID());
//                                    intent1.putExtras(bundle1);
//                                    startActivity(intent1);
//                                    loading();
//                                    finish();
//                                    break;
//
//                                }else if(user.getType().equals("C")){
//                                    //跳转至使用人界面
//                                    Intent intent1 = new Intent(LoginActivity.this,InterfaceC.class);
//                                    Bundle bundle1=new Bundle();
//                                    bundle1.putString("myID",user.getUserID());
//                                    bundle1.putString("myName",user.getUsername());
//                                    intent1.putExtras(bundle1);
//                                    startActivity(intent1);
//                                    loading();
//                                    finish();
//                                    break;
//                                }
//                            }else{
                            }
                        }
//                    if ((check == false) && (!TextUtils.isEmpty(password))) {
//                        View focusView;
//                        mPasswordView.setError(getString(R.string.error_incorrect_password));
//                        focusView = mPasswordView;
//                        focusView.requestFocus();
//                    }
//                    }
                    break;
                case 1002:
                    View focusView;
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    focusView = mPasswordView;
                    focusView.requestFocus();
                    break;
                default:
                    break;
            }
        }
    };

    private void loading() {
        ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("正在加载中");
        dialog.show();
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the Account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     * 登陆操作
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        userID = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            focusView.requestFocus();
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(userID)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            focusView.requestFocus();
        }
        checkpwd();
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

}

