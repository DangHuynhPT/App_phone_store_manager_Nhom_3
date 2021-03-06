package com.example.app_phone_store_manager_nhom_3.ui.KhachHang;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.app_phone_store_manager_nhom_3.R;
import com.example.app_phone_store_manager_nhom_3.dao.DaoKhachHang;
import com.example.app_phone_store_manager_nhom_3.model.KhachHang;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Pattern;


public class EditKhachHangFragment extends Fragment {
    private AppCompatActivity appCompatActivity;
    private Drawable drawable;
    private NavController navController;
    private DaoKhachHang dao;
    private List<KhachHang> list;
    private EditText edMaKHChange, edHoTenKHChange, edDienThoaiChange, edDiaChiChange;
    private KhachHang khachHang;
    private String maKHOld;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_khach_hang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        maKHOld = getArguments().getString("maKH");

        edMaKHChange = view.findViewById(R.id.edMaKhachHangchange);
        edHoTenKHChange = view.findViewById(R.id.edHoTenKHchange);
        edDienThoaiChange = view.findViewById(R.id.edSoDTKHchange);
        edDiaChiChange = view.findViewById(R.id.edDiachiKHchange);

        appCompatActivity = (AppCompatActivity) getActivity();
        drawable = appCompatActivity.getDrawable(R.drawable.ic_backspace);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);
        appCompatActivity.getSupportActionBar().setTitle("C???p nh???p Kh??ch H??ng");

        dao = new DaoKhachHang(getActivity());
        dao.open();

        khachHang = dao.getMaKH(maKHOld);

        edMaKHChange.setText(khachHang.getMaKH());
        edHoTenKHChange.setText(khachHang.getHoTen());
        edDienThoaiChange.setText(khachHang.getDienThoai());
        edDiaChiChange.setText(khachHang.getDiaChi());


    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
                builder.setTitle("Tho??t c???p nh???p");
                builder.setMessage("B???n c?? ch???c ch???n mu???n tho??t kh??ng. \nD??? li???u s??? kh??ng b??? thay ?????i!");
                builder.setNegativeButton("Kh??ng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("C??", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (maKHOld != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString("maKH", maKHOld);
                            navController.navigate(R.id.action_editKH_to_chitietKH, bundle);
                        } else {
                            navController.navigate(R.id.action_editKH_to_listKH);
                        }
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                return true;
            case R.id.menu_reset:
                edMaKHChange.setText("");
                edHoTenKHChange.setText("");
                edDienThoaiChange.setText("");
                edDiaChiChange.setText("");
                return true;
            case R.id.menu_save:
                if (valiDate()) {
                    if (khachHang.getMaKH().equals(edMaKHChange.getText().toString()) &&
                            khachHang.getHoTen().equals(edHoTenKHChange.getText().toString()) &&
                            khachHang.getDienThoai().equals(edDienThoaiChange.getText().toString()) &&
                            khachHang.getDiaChi().equals(edDiaChiChange.getText().toString())) {
                        Toast.makeText(appCompatActivity, "Kh??ng c?? thay ?????i ????? c???p nh???p!", Toast.LENGTH_SHORT).show();
                    } else {
                        khachHang.setMaKH(edMaKHChange.getText().toString().replaceAll(" ", ""));
                        khachHang.setHoTen(edHoTenKHChange.getText().toString());
                        khachHang.setDienThoai(edDienThoaiChange.getText().toString());
                        khachHang.setDiaChi(edDiaChiChange.getText().toString());
                        int kq = dao.updateKH(khachHang, maKHOld);
                        if (kq > 0) {
                            Toast.makeText(getContext(), "C???p nh???p th??nh c??ng", Toast.LENGTH_SHORT).show();
                            navController.navigate(R.id.action_editKH_to_listKH);
                        } else {
                            Toast.makeText(getContext(), "C???p nh???p th???t B???i", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean valiDate() {
        if (edMaKHChange.getText().length() == 0 ||
                edHoTenKHChange.getText().length() == 0 ||
                edDienThoaiChange.getText().length() == 0 ||
                edDiaChiChange.getText().length() == 0) {
            Toast.makeText(appCompatActivity, "B???n c???n nh???p ?????y ????? th??ng tin!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edMaKHChange.getText().length() < 6 || edMaKHChange.getText().length() > 10) {
            Toast.makeText(appCompatActivity, "M?? kh??ch h??ng c?? ????? d??i t???i thi???u 6, t???i ??a 10.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!edMaKHChange.getText().toString().substring(0, 1).toUpperCase().equals(edMaKHChange.getText().toString().substring(0, 1)) ||
                !edHoTenKHChange.getText().toString().substring(0, 1).toUpperCase().equals(edHoTenKHChange.getText().toString().substring(0, 1))
        ) {
            Toast.makeText(appCompatActivity, "Ch??? c??i ?????u ti??n t??n h??ng ph???i vi???t hoa", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edDienThoaiChange.getText().length() < 10 ||
                edDienThoaiChange.getText().length() > 11 ||
                Pattern.matches("[a-zA-Z]+", edDienThoaiChange.getText().toString())) {
            Toast.makeText(appCompatActivity, "Sai ????? d??i s??? ??i???n tho???i", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}