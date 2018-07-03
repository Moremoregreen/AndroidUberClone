package com.moremoregreen.androiduberclone;
//Add lib to use
//FireBase Authentication
//FireBase Database
//Support Design
//Material Edit Text
//Calligraphy(客製化文字跟View)
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moremoregreen.androiduberclone.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends Activity {
    RelativeLayout rootLayout;
    Button btnSingIn, btnRegister;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference usersRef;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //要在setContentView之前
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        usersRef = db.getReference("Users");

        rootLayout = findViewById(R.id.rootLayout);
        btnSingIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });
        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });
    }

    private void showLoginDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("登入");
        dialog.setMessage("請使用信箱登入");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login,null);

        final MaterialEditText etEmail = login_layout.findViewById(R.id.etEmail);
        final MaterialEditText etPassword = login_layout.findViewById(R.id.etPassword);


        dialog.setView(login_layout);

        dialog.setPositiveButton("登入", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        //檢查有無輸入資料
                        if (TextUtils.isEmpty(etEmail.getText().toString())) {
                            Snackbar.make(rootLayout, "請輸入信箱", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        if (TextUtils.isEmpty(etPassword.getText().toString())) {
                            Snackbar.make(rootLayout, "請輸入密碼", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        if (etPassword.getText().toString().length() < 6 || etPassword.getText().toString().length() > 12) {
                            Snackbar.make(rootLayout, "密碼需介於6到12位數", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        auth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        startActivity(new Intent(MainActivity.this, Welcome.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(rootLayout, "登入失敗:" + e.getMessage(), Snackbar.LENGTH_SHORT)
                                                .show();
                                        return;
                                    }
                                });

                    }
                });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void showRegisterDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("註冊帳戶");
        dialog.setMessage("請使用信箱註冊");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register,null);

        final MaterialEditText etEmail = register_layout.findViewById(R.id.etEmail);
        final MaterialEditText etPassword = register_layout.findViewById(R.id.etPassword);
        final MaterialEditText etName = register_layout.findViewById(R.id.etName);
        final MaterialEditText etPhone = register_layout.findViewById(R.id.etPhone);

        dialog.setView(register_layout);

        dialog.setPositiveButton("註冊", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //檢查有無輸入資料
                if(TextUtils.isEmpty(etEmail.getText().toString())){
                    Snackbar.make(rootLayout, "請輸入信箱", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(TextUtils.isEmpty(etName.getText().toString())){
                    Snackbar.make(rootLayout, "請輸入姓名", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(TextUtils.isEmpty(etPassword.getText().toString())){
                    Snackbar.make(rootLayout, "請輸入密碼", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(etPassword.getText().toString().length() < 6 || etPassword.getText().toString().length() > 12){
                    Snackbar.make(rootLayout, "密碼需介於6到12位數", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(TextUtils.isEmpty(etPhone.getText().toString())){
                    Snackbar.make(rootLayout, "請輸入電話", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                auth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //把USER存到db
                                User user = new User();
                                user.setEmail(etEmail.getText().toString());
                                user.setName(etName.getText().toString());
                                user.setPassword(etPassword.getText().toString());
                                user.setPhone(etPhone.getText().toString());

                                //信箱當KEY會出錯(因為有"@"...特殊符號都不行)
                                usersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(rootLayout, "註冊成功!!", Snackbar.LENGTH_SHORT)
                                                        .show();
                                                return;
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(rootLayout, "註冊失敗:" + e.getMessage(), Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(rootLayout, "註冊失敗:" + e.getMessage(), Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        });;
            }
        });

        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialogInterface.dismiss(); 不知道差別在哪
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
