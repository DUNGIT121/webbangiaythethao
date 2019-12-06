package com.annguyen.giaythethao.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.annguyen.giaythethao.R;
import com.annguyen.giaythethao.model.KhachHangModel;
import com.annguyen.giaythethao.ultil.CheckConnection;
import com.annguyen.giaythethao.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CapNhatTaiKhoan extends AppCompatActivity {
    private Toolbar toolbarCapNhatTK;
    private EditText editTextTen, editTextMatKhau, editTextEmail, editTextDienThoai, editTextDiaChi;
    private RadioButton radioButtonNam, radioButtonNu;
    private Button buttonXacNhanCapNhatTK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_tai_khoan);

        widget();
        setMyActionBar();
    }

    private void widget() {
        toolbarCapNhatTK = findViewById(R.id.toolbarCapNhatTK);
        editTextTen = findViewById(R.id.editTextHoTenCN);
        editTextMatKhau = findViewById(R.id.editTextMatKhauCN);
        editTextEmail = findViewById(R.id.editTextEmailCN);
        editTextDienThoai = findViewById(R.id.editTextDienThoaiCN);
        editTextDiaChi = findViewById(R.id.editTextDiaChiCN);
        radioButtonNam = findViewById(R.id.radioButtonNamCN);
        radioButtonNu = findViewById(R.id.radioButtonNuCN);
        buttonXacNhanCapNhatTK = findViewById(R.id.buttonXacNhanCapNhatTK);
        setDataOfActivity();

        //Sự kiện khi click vào Button "Cập nhật"
        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            setCapNhatTaiKhoan();//cài đặt chức năng đăng nhập
        }else {
            CheckConnection.showToast(getApplicationContext(), "Vui lòng kiểm tra kết nối Internet !");
        }
    }

    private void setMyActionBar() {
        setSupportActionBar(toolbarCapNhatTK);
        getSupportActionBar().setHomeButtonEnabled(true);//Bật nút Home cho ActionBar
        toolbarCapNhatTK.setNavigationIcon(android.R.drawable.ic_media_previous);//set Icon cho ActionBar
        //Cài đặt sự kiện click
        toolbarCapNhatTK.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//Đóng Activity hiện tại
            }
        });
    }

    private void setDataOfActivity() {
        KhachHangModel khachHangModel = MainActivity.mapKhachHang.get("TaiKhoan");
        editTextTen.setText(khachHangModel.getTenKH());
        editTextMatKhau.setText(khachHangModel.getMatKhau());
        editTextEmail.setText(khachHangModel.getEmail());
        editTextDienThoai.setText(khachHangModel.getDienThoai());
        editTextDiaChi.setText(khachHangModel.getDiaChi());
        String gioiTinh = khachHangModel.getGioTinh();
        if(gioiTinh.equals("Nam")){
            radioButtonNam.setChecked(true);
        }else if(gioiTinh.equals("Nữ")){
            radioButtonNu.setChecked(true);
        }
    }

    private void setCapNhatTaiKhoan(){
        //Sự kiện khi click button "Cập nhật"
        buttonXacNhanCapNhatTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int maKH = MainActivity.mapKhachHang.get("TaiKhoan").getMaKH();
                final String tenKH = editTextTen.getText().toString().trim();
                final String matKhau = editTextMatKhau.getText().toString().trim();
                final String dienThoai = editTextDienThoai.getText().toString().trim();
                final String email = editTextEmail.getText().toString().trim();
                final String diaChi = editTextDiaChi.getText().toString().trim();

                String gioiTinh = "";
                if(radioButtonNam.isChecked()){
                    gioiTinh = "Nam";
                }else if(radioButtonNu.isChecked()){
                    gioiTinh = "Nữ";
                }
                final String finalGioiTinh = gioiTinh;

                if( tenKH.length() > 0 && matKhau.length() > 0 && dienThoai.length() > 0 && email.length() > 0 && diaChi.length() > 0){
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlCapNhatTaiKhoan,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response != null && response.length() != 2 ) {
                                        int makh = 0;
                                        String tenkh = "";
                                        String matkhau = "";
                                        String dienthoai = "";
                                        String email = "";
                                        String diachi = "";
                                        String gioitinh = "";

                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            MainActivity.mapKhachHang.clear();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                                makh = jsonObject.getInt("makh");
                                                tenkh = jsonObject.getString("tenkh");
                                                matkhau = jsonObject.getString("matkhau");
                                                dienthoai = jsonObject.getString("dienthoai");
                                                email = jsonObject.getString("email");
                                                diachi = jsonObject.getString("diachi");
                                                gioitinh = jsonObject.getString("gioitinh");

                                                KhachHangModel khachHangModel = new KhachHangModel(makh, tenkh, matkhau, dienthoai, email, diachi, gioitinh);

                                                //Lưu thong tin dang nhap
                                                MainActivity.mapKhachHang.put("TaiKhoan", khachHangModel);
                                            }

                                            //Chuyển về màn hình trang chủ
                                            Toast.makeText(CapNhatTaiKhoan.this, "Cập nhật tài khoản thành công !", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), TaiKhoanKhachHang.class);
                                            startActivity(intent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }else{
                                        Toast.makeText(CapNhatTaiKhoan.this, "Lối data khi get Data table KhachHang !", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(getClass().getSimpleName(), "Lỗi: "+ error.toString());
                                    CheckConnection.showToast(getApplicationContext(), "Lỗi khi get data table KhachHang !!!");
                                }
                            }
                    ){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("maKH",String.valueOf(maKH));
                            params.put("tenKH",tenKH);
                            params.put("matKhau",matKhau);
                            params.put("dienThoai",dienThoai);
                            params.put("email",email);
                            params.put("diaChi",diaChi);
                            params.put("gioiTinh",finalGioiTinh);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }else {
                    Toast.makeText(CapNhatTaiKhoan.this, "Thông tin cập nhật không được rỗng !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
