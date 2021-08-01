package com.example.app_phone_store_manager_nhom_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app_phone_store_manager_nhom_3.dao.DaoNhanVien;
import com.example.app_phone_store_manager_nhom_3.model.NhanVien;

import java.io.ByteArrayOutputStream;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin, btnHuy;
    private EditText edUser, edPass;
    private DaoNhanVien dao;
    private Intent intent;
    private CheckBox ckbLuuTK;
    private byte bit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnHuy = findViewById(R.id.btnReset);
        btnLogin = findViewById(R.id.btnLogin);
        edUser = findViewById(R.id.edUserName);
        edPass = findViewById(R.id.edPassWorld);
        ckbLuuTK = findViewById(R.id.cbRemember);

        dao = new DaoNhanVien(this);
        dao.openNV();
        if (dao.getUserName("admin") < 0) {
            NhanVien nhanVien = new NhanVien();
            nhanVien.setMaNV("Admin");
            nhanVien.setHoTen("admin");
            nhanVien.setDienThoai("0123456789");
            nhanVien.setTaiKhoan("admin");
            nhanVien.setMatKhau("admin");
            dao.addNV(nhanVien);
        }
        SharedPreferences preferences = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        edUser.setText(preferences.getString("USER", ""));
        edPass.setText(preferences.getString("PASS", ""));
        ckbLuuTK.setChecked(preferences.getBoolean("REMEMBER", false));
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edUser.setText("");
                edPass.setText("");
            }
        });

    }

    public void logIn() {
        String user = edUser.getText().toString();
        String pass = edPass.getText().toString();
        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(getBaseContext(), "Nhập đầy đủ thông tin \n " +
                    "Không được để trống", Toast.LENGTH_SHORT).show();
        } else {
            if (dao.getlogin(user, pass) > 0 ||
                    (user.equalsIgnoreCase("admin")) &&
                            (pass.equalsIgnoreCase("admin")) ||
                    dao.getlogin(user, pass) > 0 ||
                    (user.equalsIgnoreCase("user")) &&
                            (pass.equalsIgnoreCase("user"))
            ) {
                rememberUser(user, pass, ckbLuuTK.isChecked());
                startActivity(intent = new Intent(this, MainActivity.class).putExtra("admin", user));
                Toast.makeText(getBaseContext(), "Đăng Nhập Thành Công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getBaseContext(), "            Đăng Nhập Thất Bại! \n " +
                        "Sai thông tin tài khoản, mật khẩu", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void rememberUser(String user, String pass, boolean status) {
        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (!status) {
            editor.clear();
        } else {
            // Lưu dữ liệu
            editor.putString("USER", user);
            editor.putString("PASS", pass);
            editor.putBoolean("REMEMBER", status);
        }
        // LƯu lại toàn bộ dữ liệu
        editor.commit();
    }

}