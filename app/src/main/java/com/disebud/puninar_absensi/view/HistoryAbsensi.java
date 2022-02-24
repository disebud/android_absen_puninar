package com.disebud.puninar_absensi.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.disebud.puninar_absensi.R;
import com.disebud.puninar_absensi.helper.Api;
import com.disebud.puninar_absensi.helper.RetrofitClient;
import com.disebud.puninar_absensi.model.getData.ResGetData;
import com.disebud.puninar_absensi.model.getData.ResultItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryAbsensi extends AppCompatActivity {
    RecyclerView rvPresensi;
    List<ResultItem> items;
    ListPresensi listPresensi;
    TextView dataNotFound,tvTgl;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_absensi);
        dataNotFound = findViewById(R.id.not_found);
        rvPresensi = findViewById(R.id.rvPresensi);
        tvTgl = findViewById(R.id.tglAbsen);
        api = RetrofitClient.getInstance().getApi();

        rvPresensi.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvPresensi.setLayoutManager(layoutManager);

        Calendar newDate = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        String strDate = format.format(newDate.getTime());
        tvTgl.setText(strDate);

        getList();
    }

    private void getList( ) {

        api.dataAbsensi().enqueue(new Callback<ResGetData>() {
            @Override
            public void onResponse(Call<ResGetData> call, Response<ResGetData> response) {
                ResGetData jsonResponse = response.body();
                if (response.isSuccessful()) {
                    items = jsonResponse.getResult();
//                    Log.d("ISI", String.valueOf(items));
//                    jlhData.setText(String.valueOf("JUMLAH DATA : "+items.size() + " ITEM"));
                    if (items.size() > 0) {
                        listPresensi = new ListPresensi(getApplicationContext(), items);
//

                        if (listPresensi.getItemCount() != 0) {
                            rvPresensi.setVisibility(View.VISIBLE);
                            rvPresensi.setAdapter(listPresensi);
                        } else {
                            rvPresensi.setVisibility(View.GONE);
                        }
                    } else {
                        dataNotFound.setVisibility(View.VISIBLE);
                        rvPresensi.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "No Data Available.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResGetData> call, Throwable t) {
            }
        });
    }

    class ListPresensi extends RecyclerView.Adapter<ListPresensi.ViewHolder>{
        //        private List<ItemsItem> lis;
        List<ResultItem> lis;
        Context context;
        String cat;
        ImageView iFoto;

        public ListPresensi(Context context, List<ResultItem> lis) {
            this.context    = context;
            this.lis        = lis;
        }

        @Override
        public ListPresensi.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_presensi, parent, false);
            return new ListPresensi.ViewHolder(view, viewType);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(ListPresensi.ViewHolder holder, int position) {

            int a = position+1;
            holder.nomorUrut.setText(a+"");
            holder.tanggal.setText(lis.get(position).getCurrentDatetime());
            holder.val_cek.setText(lis.get(position).getStatus());
            if(lis.get(position).getStatus().equalsIgnoreCase("CHECK IN")){
                holder.val_cek.setBackgroundResource(R.color.main_green_color);
            }else{
                holder.val_cek.setBackgroundResource(R.color.red_btn_bg_color);
            }
            holder.val_lat.setText(lis.get(position).getLatitude());
            holder.val_long.setText(lis.get(position).getLongitude());



        }

        @Override
        public int getItemCount() {
            return lis.size();
        }



        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


            TextView nomorUrut , tanggal , val_cek , val_lat , val_long;
            CardView cardHistory;

            public ViewHolder(final View DataView, int viewType) {
                super(DataView);

                this.nomorUrut   = (TextView) DataView.findViewById(R.id.no_json);
                this.tanggal   = (TextView) DataView.findViewById(R.id.val_date);
                this.val_cek   = (TextView) DataView.findViewById(R.id.val_cek);
                this.val_lat   = (TextView) DataView.findViewById(R.id.val_lat);
                this.val_long   = (TextView) DataView.findViewById(R.id.val_long);
                this.cardHistory   =  DataView.findViewById(R.id.card_item_history);

                cardHistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final int pos1 = getAdapterPosition();
                        Intent detail = new Intent(context, CheckAbsensi.class);
                        detail.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        detail.putExtra("img", lis.get(pos1).getImg());
                        detail.putExtra("long", String.valueOf(lis.get(pos1).getLongitude()));
                        detail.putExtra("lat", lis.get(pos1).getLatitude());
                        detail.putExtra("address", lis.get(pos1).getAddress());
                        detail.putExtra("date", lis.get(pos1).getCurrentDatetime());
                        detail.putExtra("status", "1");
                        context.startActivity(detail);
                    }
                });


            }

            @Override
            public void onClick(View view) {

            }
        }





    }
}