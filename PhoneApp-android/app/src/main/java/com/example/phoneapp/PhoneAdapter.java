package com.example.phoneapp;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 어댑터와 RecyclerView와 연결 ,(Databinding, MVVM 사용 x)
public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.PhoneViewHolder>{
    private static final String TAG = "PhoneAdapter";
    private final List<Phone> mPhoneList;
    private final MainActivity mMainActivity;

    public PhoneAdapter(List<Phone> mPhoneList, MainActivity mMainActivity) {
        this.mPhoneList = mPhoneList;
        this.mMainActivity = mMainActivity;
    }

    // addItem , removeItem 만들기
    public void addItem(Phone phone) {
        mPhoneList.add(phone);
        notifyDataSetChanged();
    }

    // Retrofit 호출할때 id값 필요해서 만듬
    public long getPhoneId(int position){
        return mPhoneList.get(position).getId().intValue();
    }

    // 상세보기 할때 Name 미리입력 되있는거 할려고 만듬
    public String getPhoneName(int position){
        return mPhoneList.get(position).getName();
    }

    // 상세보기 할때 Tel 미리입력 되있는거 할려고 만듬
    public String getPhoneTel(int position){
        return mPhoneList.get(position).getTel();
    }
    
    // 리스트 에서 삭제 (화면 갱신용)
    public void removeItem(long position) {
        mPhoneList.remove(position);
    }


    @NonNull
    @Override
    public PhoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhoneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_item, parent,false));
        // ViewHolder에 꼽아서 반환함
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: 아이템개수만큼 실행");
        Phone data = mPhoneList.get(position);
        holder.mTextName.setText(data.getName());
        holder.mTextTel.setText(data.getTel());

        // 데이터 바인딩할 때 이벤트 달기

    }

    @Override
    public int getItemCount() {
        return mPhoneList.size();
    }


    class PhoneViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextName;
        private TextView mTextTel;
        private LinearLayout phoneItem;

        public PhoneViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextName = itemView.findViewById(R.id.phone_name);
            mTextTel = itemView.findViewById(R.id.phone_tel);
            phoneItem = itemView.findViewById(R.id.layout_phone_item);
            phoneItem.setOnClickListener(v -> {
                Log.d(TAG, "PhoneViewHolder: 아이템 눌렀음! 어댑터번호: "+getAdapterPosition());
                Log.d(TAG, "PhoneViewHolder: 아이템 눌렀음! 실제 Id값? ( getPhoneId(getAdapterPosition()) ): "+ getPhoneId(getAdapterPosition()));
                Log.d(TAG, "PhoneViewHolder: 아이템 눌렀음! Name값? ( getPhoneName(getAdapterPosition()) ): "+ getPhoneName(getAdapterPosition()));
                View dialog = v.inflate(v.getContext(), R.layout.dialog_item, null);
                EditText editName = dialog.findViewById(R.id.edit_name);
                EditText editTel = dialog.findViewById(R.id.edit_tel);
                int pos = (int) getPhoneId(getAdapterPosition());

                editName.setText(getPhoneName(getAdapterPosition()));
                editTel.setText(getPhoneTel(getAdapterPosition()));

                AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
                dlg.setTitle("연락처 수정");
                dlg.setView(dialog);

                dlg.setNegativeButton("삭제", (dialog1, which) -> {
                    PhoneService phoneService = PhoneService.retrofit.create(PhoneService.class);
                    Call<Void> deleteCall = phoneService.deleteById(pos);
                    deleteCall.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            removeItem(pos);
                            mMainActivity.findAll();
                            Toast.makeText(itemView.getContext(), "삭제 성공", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(itemView.getContext(), "삭제 실패", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: pos(실제 ID값)" + pos);
                            //Log.d(TAG, "onFailure: Phone의 ID값" + getPhoneId(pos));
                            Log.d(TAG, "onFailure: " + t.getMessage());
                        }
                    });

                }); // End Negative (삭제)

                dlg.setPositiveButton("수정", (dialog1, which) -> {
                    Toast.makeText(itemView.getContext(), "수정 성공", Toast.LENGTH_SHORT).show();

                    Phone phone = new Phone(null, editName.getText().toString(), editTel.getText().toString());
                    PhoneService phoneService = PhoneService.retrofit.create(PhoneService.class);
                    Call<CMRespDto<Phone>> updateCall = phoneService.update(pos, phone);
                    updateCall.enqueue(new Callback<CMRespDto<Phone>>() {
                        @Override
                        public void onResponse(Call<CMRespDto<Phone>> call, Response<CMRespDto<Phone>> response) {

                            Phone phoneResponse = response.body().getData();
                            mTextName.setText(phoneResponse.getName());
                            mTextTel.setText(phoneResponse.getTel());
                            Log.d(TAG, "onResponse: update 성공");
                            mMainActivity.findAll();
                        }

                        @Override
                        public void onFailure(Call<CMRespDto<Phone>> call, Throwable t) {
                            Log.d(TAG, "onResponse: update 실패");
                        }
                    });
                }); // End Positive (수정)
                dlg.show();
            });
        }
    }
}
