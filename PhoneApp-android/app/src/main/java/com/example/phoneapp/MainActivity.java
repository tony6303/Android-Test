package com.example.phoneapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity2";

    //리사이클러뷰 = 아이템 나오는 레이아웃
    private RecyclerView mMainRecyclerview;

    //리사이클러뷰 어댑터
    private PhoneAdapter mPhoneAdapter;
    private List<Phone> mPhoneList = new ArrayList<>();

    private PhoneService phoneService;

    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findAll();

        fab = findViewById(R.id.fab_save);
        fab.setOnClickListener(v -> {
            View dialog = v.inflate(v.getContext(), R.layout.dialog_item, null);
            EditText editName = dialog.findViewById(R.id.edit_name);
            EditText editTel = dialog.findViewById(R.id.edit_tel);

            AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
            dlg.setTitle("연락처 등록");
            dlg.setView(dialog);
            dlg.setNegativeButton("닫기", null);
            dlg.setPositiveButton("등록", (dialog1, which) -> {
                Phone phone = new Phone(editName.getText().toString(), editTel.getText().toString());

                savePost(phone);
            });
            dlg.show();
        });

    } // End OnCreate

    private void init(){

        mMainRecyclerview = findViewById(R.id.main_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mMainRecyclerview.setLayoutManager(layoutManager);

        Log.d(TAG, "getData: findAll() " + mPhoneList);
        mPhoneAdapter = new PhoneAdapter(mPhoneList, this);
        mMainRecyclerview.setAdapter(mPhoneAdapter);
    }


    public void findAll(){
        phoneService = PhoneService.retrofit.create(PhoneService.class);
        Call<CMRespDto<List<Phone>>> call = phoneService.findAll();
        call.enqueue(new Callback<CMRespDto<List<Phone>>>() {
            @Override
            public void onResponse(Call<CMRespDto<List<Phone>>> call, Response<CMRespDto<List<Phone>>> response) {
                CMRespDto<List<Phone>> cmRespDto = response.body();
                mPhoneList = cmRespDto.getData();
                // 어댑터에게 넘기기
                Log.d(TAG, "onResponse: findAll() callback data: " + mPhoneList);

                init();
            }

            @Override
            public void onFailure(Call<CMRespDto<List<Phone>>> call, Throwable t) {
                Log.d(TAG, "onFailure: findAll() 실패" + t.getMessage());
            }
        });
    }

    private void savePost(Phone phone){
        phoneService = PhoneService.retrofit.create(PhoneService.class);
        Call<CMRespDto<Phone>> saveCall = phoneService.save(phone);
        saveCall.enqueue(new Callback<CMRespDto<Phone>>() {
            @Override
            public void onResponse(Call<CMRespDto<Phone>> call, Response<CMRespDto<Phone>> response) {
                mPhoneList.add(phone);
                findAll();
                Log.d(TAG, "onResponse: post 성공");
                Toast.makeText(MainActivity.this, "등록 성공", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CMRespDto<Phone>> call, Throwable t) {
                Log.d(TAG, "onFailure: post 실패" + t.getMessage());
            }
        });
    }

}