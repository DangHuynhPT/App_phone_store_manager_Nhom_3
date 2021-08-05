package com.example.app_phone_store_manager_nhom_3.ui.SanPham;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.app_phone_store_manager_nhom_3.R;
import com.example.app_phone_store_manager_nhom_3.dao.DaoHang;
import com.example.app_phone_store_manager_nhom_3.dao.DaoSanPham;
import com.example.app_phone_store_manager_nhom_3.dao.DaoThuocTinhSanPham;
import com.example.app_phone_store_manager_nhom_3.databinding.FragmentAddSanPhamBinding;
import com.example.app_phone_store_manager_nhom_3.model.SanPham;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class AddSanPhamFragment extends Fragment {
    private Drawable drawable;
    private AppCompatActivity appCompatActivity;
    private NavController navController;
    private FragmentAddSanPhamBinding binding;
    private DaoSanPham daoSanPham;
    private DaoHang daoHang;
    private DaoThuocTinhSanPham daoTTSP;
    private Bitmap bitmapOld, bitmapNew;
    private LinearLayout lnlChupAnh, lnlChonFile;
    private ActivityResultLauncher<Intent> launcherCamera;
    private ActivityResultLauncher<Intent> launcherFlie;
    private String maSP;
    private String maHang;
    private String tenHang;
    private String tenSP;
    private byte[] hinhAnh;
    private int phanLoai;
    private int tinhTrang;
    private double giaTien;
    private int trangThai;
    private String moTa;
    private String maTT;
    private String boNho;
    private String RAM;
    private String chipSet;
    private String heDieuHanh;
    private String manHinh;
    private String dungLuongPin;
    private String congSac;
    private String loaiPhuKien;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        appCompatActivity = (AppCompatActivity) getActivity();

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("Thêm sản phẩm");
        drawable = getActivity().getDrawable(R.drawable.ic_backspace);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(drawable);

        launcherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == appCompatActivity.RESULT_OK && result.getData() != null) {
                            Bundle bundle = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap) bundle.get("data");
                            binding.imgSP.setImageBitmap(bitmap);
                        }
                    }
                });
        launcherFlie = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == appCompatActivity.RESULT_OK && result.getData() != null) {
                            Uri uri = result.getData().getData();
                            try {
                                InputStream inputStream = appCompatActivity.getContentResolver().openInputStream(uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                binding.imgSP.setImageBitmap(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        phanLoaiSP();

        bitmapOld = ((BitmapDrawable) binding.imgSP.getDrawable()).getBitmap();
        binding.imgSelectPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOpenBottomShet();
            }
        });
        daoSanPham = new DaoSanPham(appCompatActivity);
        daoTTSP = new DaoThuocTinhSanPham(appCompatActivity);
        daoHang = new DaoHang(appCompatActivity);

        daoSanPham.open();
        daoTTSP.open();
        daoHang.open();

        binding.edHangSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edHangSP.getText().length() == 0){
                    navController.navigate(R.id.addSP_to_chonHang);
                } else{
                    Bundle bundle = new Bundle();
                    bundle.putString("maHang", maHang);
                    navController.navigate(R.id.addSP_to_chonHang, bundle);
                }
            }
        });
        if (getArguments() != null) {
            maHang = getArguments().getString("maHangSelected");
            tenHang = daoHang.getMaHang(maHang).getTenHang();
            binding.edHangSP.setText(tenHang);
        }
    }

    public void phanLoaiSP() {
        binding.cbSPDienThoai.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.tilLoaiPKSP.setVisibility(View.GONE);
                } else {
                    binding.tilLoaiPKSP.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.cbSPPhuKien.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.tilBoNhoSP.setVisibility(View.GONE);
                    binding.tilRAMSP.setVisibility(View.GONE);
                    binding.tilChipSetSP.setVisibility(View.GONE);
                    binding.tilOSSP.setVisibility(View.GONE);
                    binding.tilMaHinhSP.setVisibility(View.GONE);
                    binding.tilPinSP.setVisibility(View.GONE);
                    binding.tilTypeSP.setVisibility(View.GONE);
                } else {
                    binding.tilBoNhoSP.setVisibility(View.VISIBLE);
                    binding.tilRAMSP.setVisibility(View.VISIBLE);
                    binding.tilChipSetSP.setVisibility(View.VISIBLE);
                    binding.tilOSSP.setVisibility(View.VISIBLE);
                    binding.tilMaHinhSP.setVisibility(View.VISIBLE);
                    binding.tilPinSP.setVisibility(View.VISIBLE);
                    binding.tilTypeSP.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.cbSPDienThoai.setChecked(true);
        binding.cbSPNew.setChecked(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddSanPhamBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
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
                navController.navigate(R.id.addSP_to_listSP);
                return true;
            case R.id.menu_reset:

                drawable = appCompatActivity.getDrawable(R.drawable.image_defaut);
                binding.imgSP.setImageDrawable(drawable);
                binding.edMaSP.setText("");
                binding.edTenSP.setText("");
                binding.edHangSP.setText("");
                binding.cbSPDienThoai.setChecked(true);
                binding.cbSPPhuKien.setChecked(false);
                binding.cbSPOld.setChecked(false);
                binding.cbSPLikeNew.setChecked(false);
                binding.cbSPNew.setChecked(true);
                binding.edGiaTienSP.setText("");
                binding.edTrangThaiSP.setText("");
                binding.edBoNhoSP.setText("");
                binding.edRAMSP.setText("");
                binding.edChipSetSP.setText("");
                binding.edOSSP.setText("");
                binding.edManHinhSP.setText("");
                binding.edPinSP.setText("");
                binding.edLoaiPKSP.setText("");
                binding.edMoTaSP.setText("");

                return true;
            case R.id.menu_save:

                if (checkSP()) {
                    bitmapNew = ((BitmapDrawable) binding.imgSP.getDrawable()).getBitmap();
                    maSP = binding.edMaSP.getText().toString();
                    tenSP = binding.edTenSP.getText().toString();
                    giaTien = Double.parseDouble(binding.edTenSP.getText().toString());
                    moTa = binding.edMoTaSP.getText().toString();

                    SanPham sanPham = new SanPham();
                    sanPham.setMaSP(maSP);
                    sanPham.setTenSP(maSP);
                    sanPham.setTenSP(maSP);

                    if (bitmapNew != bitmapOld) {
                        convertImage();

                    }
                }

                navController.navigate(R.id.addSP_to_listSP);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void clickOpenBottomShet() {
        View viewDialog = appCompatActivity.getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(appCompatActivity);
        bottomSheetDialog.setContentView(viewDialog);

        lnlChupAnh = viewDialog.findViewById(R.id.lnlChupAnh);
        lnlChonFile = viewDialog.findViewById(R.id.lnlChonAnh);

        lnlChupAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                launcherCamera.launch(intent);
                bottomSheetDialog.cancel();
            }
        });
        lnlChonFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                launcherFlie.launch(intent);
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.show();
    }

    public boolean valeDateDienThoai() {
        if (binding.edMaSP.getText().length() == 0 ||
                binding.edTenSP.getText().length() == 0 ||
                binding.edHangSP.getText().length() == 0 ||
                binding.edGiaTienSP.getText().length() == 0 ||
                binding.edTrangThaiSP.getText().length() == 0 ||
                binding.edMoTaSP.getText().length() == 0 ||
                binding.edBoNhoSP.getText().length() == 0 ||
                binding.edRAMSP.getText().length() == 0 ||
                binding.edChipSetSP.getText().length() == 0 ||
                binding.edOSSP.getText().length() == 0 ||
                binding.edManHinhSP.getText().length() == 0 ||
                binding.edPinSP.getText().length() == 0 ||
                binding.edTypeSP.getText().length() == 0) {
            Toast.makeText(appCompatActivity, "Bạn phải nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.edMaSP.getText().length() < 6 || binding.edMaSP.getText().length() > 10) {
            Toast.makeText(appCompatActivity, "Mã sản phẩm có độ dài tối thiểu 6, tối đa 10.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!binding.edTenSP.getText().toString().substring(0, 1).toUpperCase().equals(binding.edTenSP.getText().toString().substring(0, 1))) {
            Toast.makeText(appCompatActivity, "Chữ cái đầu tiên tên sản phảm phải viết hoa", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean checkSP() {
        try {
            if (binding.cbSPDienThoai.isChecked()) {
                valeDateDienThoai();
            } else {
                valeDatePhuKien();
            }
            giaTien = Double.parseDouble(binding.edGiaTienSP.getText().toString());
            if (giaTien < 0) {
                Toast.makeText(appCompatActivity, "Giá tiền không phải là số âm", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        } catch (Exception e) {
            Toast.makeText(appCompatActivity, "Giá tiền phải là số", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean valeDatePhuKien() {
        if (binding.edMaSP.getText().length() == 0 ||
                binding.edTenSP.getText().length() == 0 ||
                binding.edHangSP.getText().length() == 0 ||
                binding.edGiaTienSP.getText().length() == 0 ||
                binding.edTrangThaiSP.getText().length() == 0 ||
                binding.edMoTaSP.getText().length() == 0 ||
                binding.edLoaiPKSP.getText().length() == 0) {
            Toast.makeText(appCompatActivity, "Bạn phải nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.edMaSP.getText().length() < 6 || binding.edMaSP.getText().length() > 10) {
            Toast.makeText(appCompatActivity, "Mã sản phẩm có độ dài tối thiểu 6, tối đa 10.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!binding.edTenSP.getText().toString().substring(0, 1).toUpperCase().equals(binding.edTenSP.getText().toString().substring(0, 1))) {
            Toast.makeText(appCompatActivity, "Chữ cái đầu tiên tên sản phảm phải viết hoa", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void formatPhanLoai() {
        if (binding.cbSPDienThoai.isChecked()) {
            phanLoai = 0;
        }
        if (binding.cbSPDienThoai.isChecked()) {
            phanLoai = 1;
        }
    }

    public void formatTinhTrang() {
        if (binding.cbSPOld.isChecked()) {
            tinhTrang = -1;
        }
        if (binding.cbSPLikeNew.isChecked()) {
            tinhTrang = 0;
        }
        if (binding.cbSPLikeNew.isChecked()) {
            tinhTrang = 1;
        }
    }

    public void convertImage() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.imgSP.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        hinhAnh = byteArray.toByteArray();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        daoSanPham.close();
        daoTTSP.close();
        daoHang.close();
    }
}