package com.example.app_phone_store_manager_nhom_3.ui.NguoiDung;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.app_phone_store_manager_nhom_3.R;

import org.jetbrains.annotations.NotNull;

public class DoiMatKhauFragment extends Fragment {
    private AppCompatActivity appCompatActivity;
    private NavController navController;
    private Drawable drawable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appCompatActivity = (AppCompatActivity) getActivity();
        navController = Navigation.findNavController(view);
        drawable = appCompatActivity.getDrawable(R.drawable.ic_backspace);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);
        appCompatActivity.getSupportActionBar().setTitle("Đổi mật khẩu");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doi_mat_khau, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                navController.navigate(R.id.doiMatKhau_to_chiTietNguoiDung);
                return true;

            case R.id.menu_save:
                navController.navigate(R.id.doiMatKhau_to_chiTietNguoiDung);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}