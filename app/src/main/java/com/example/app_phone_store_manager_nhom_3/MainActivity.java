package com.example.app_phone_store_manager_nhom_3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.app_phone_store_manager_nhom_3.dao.DaoNhanVien;
import com.example.app_phone_store_manager_nhom_3.model.NhanVien;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_phone_store_manager_nhom_3.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NhanVien nhanVien;
    private DaoNhanVien dao;

    View mHeaderView;
    TextView tvUser;
    TextView tvHello;
    ImageView imgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.tlbMain);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_hoaDonXuat, R.id.nav_hoaDonNhap, R.id.nav_hang, R.id.nav_sanPham,
                R.id.nav_khachHang, R.id.nav_doanhThu, R.id.nav_topSanPham, R.id.nav_account,
                R.id.nav_info, R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull @NotNull NavController controller, @NonNull @NotNull NavDestination destination, @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
                if (destination.getId() == R.id.nav_logout) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        mHeaderView = navigationView.getHeaderView(0);
        tvUser = (TextView) mHeaderView.findViewById(R.id.tvMember);
        tvHello = (TextView) mHeaderView.findViewById(R.id.tvHello);
        imgUser = (ImageView) mHeaderView.findViewById(R.id.imgUserName);
        dao = new DaoNhanVien(this);
        dao.openNV();
        Intent intent = getIntent();
        String user = intent.getStringExtra("user");
        Log.d("ad",user);
        nhanVien = dao.gettaiKhoan(user);
        if (nhanVien.getHinhAnh() == null) {
            String taiK = nhanVien.getTaiKhoan();
            TextDrawable drawable = TextDrawable.builder().beginConfig().width(64).height(64).endConfig().buildRound(taiK.substring(0, 1).toUpperCase(), getRandomColor());
            imgUser.setImageDrawable(drawable);
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(nhanVien.getHinhAnh(), 0, nhanVien.getHinhAnh().length);
            imgUser.setImageBitmap(bitmap);
        }
        tvUser.setText(user);
        if (user.equalsIgnoreCase("admin")) {
            tvHello.setText("Xin chào Quản trị viên! ");
            navigationView.getMenu().findItem(R.id.nav_account).setVisible(true);
        } else {
            tvHello.setText("Xin chào Member! ");
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}