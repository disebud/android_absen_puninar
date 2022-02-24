package com.disebud.puninar_absensi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.disebud.puninar_absensi.R;
import com.disebud.puninar_absensi.helper.Api;
import com.disebud.puninar_absensi.helper.RetrofitClient;
import com.disebud.puninar_absensi.model.ResTambah;
import com.disebud.puninar_absensi.util.SharePrefManager;
import com.disebud.puninar_absensi.util.classes.CameraViewDialog;
import com.disebud.puninar_absensi.util.interfaces.OnCaptureCancelled;
import com.disebud.puninar_absensi.util.interfaces.OnCaptureDone;
import com.disebud.puninar_absensi.util.interfaces.OnRecapturing;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckAbsensi extends AppCompatActivity {

    String statusIntent,encodedImage,valImg,valLong,valLat,valAddress,valDate,strDate;
    TextView statusAbsen ,tvLat , tvLong , tvAddress ,tvTgl;
    Bitmap bitmap ;
    int statusCap = 0;
    OnRecapturing onRecapturing = null;
    ImageView capture_foto_selfie;
    ImageView foto_selfie;
    private int ALL_PERMISSIONS = 1;
    SharePrefManager sharePrefManager;
    private Api api;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_absensi);


        api = RetrofitClient.getInstance().getApi();
        statusIntent = getIntent().getStringExtra("status");
        sharePrefManager = new SharePrefManager(this);
        tvTgl = findViewById(R.id.tglAbsen);
        btnSave = findViewById(R.id.save_status);
        statusAbsen = findViewById(R.id.statusAbsen);
        foto_selfie = findViewById(R.id.foto_selfie);
        tvLat = findViewById(R.id.valLat);
        tvLong = findViewById(R.id.valLong);
        capture_foto_selfie = findViewById(R.id.capture_foto_selfie);
        tvAddress = findViewById(R.id.valAddress);
        foto_selfie = findViewById(R.id.foto_selfie);



        Calendar newDate = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");
       strDate = format.format(newDate.getTime());


        if(statusIntent.equalsIgnoreCase("1")){
            valDate = getIntent().getStringExtra("date");
            String[] historyDate = valDate.split(" ");
            valImg = getIntent().getStringExtra("img");
            valLong = getIntent().getStringExtra("long");
            valLat = getIntent().getStringExtra("lat");
            valAddress = getIntent().getStringExtra("address");
            statusAbsen.setText("DETAIL HISTORY");
            tvLat.setText(valLat);
            tvLong.setText(valLong);
            tvAddress.setText(valAddress);
            capture_foto_selfie.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);
            tvTgl.setText(historyDate[0]+"\n"+historyDate[1]);
            Picasso.get().load("http://192.168.29.101/absensi_lokasi/images/" + valImg).placeholder(R.drawable.no_image).into(foto_selfie);
        }else{
            tvTgl.setText(strDate);
            statusAbsen.setText(statusIntent);
            capture_foto_selfie.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);
            tvLat.setText(sharePrefManager.getSPString(SharePrefManager.KEY_LAT));
            tvLong.setText(sharePrefManager.getSPString(SharePrefManager.KEY_LONG));
            tvAddress.setText(sharePrefManager.getSPString(SharePrefManager.KEY_ADRESS));
        }
    }

    public void actionView(View view) {
        switch(view.getId()){
            case R.id.foto_selfie: // statement
                viewDetailImage();
                break;
            case R.id.capture_foto_selfie: //statement
                checkPermissions();
                break;
            case R.id.save_status: //statement
                saveDataAbsen();
                break;

        }
    }

    private void saveDataAbsen() {


        if(statusCap == 0){
            new SweetAlertDialog(CheckAbsensi.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Photo Selfie not yet filled! ")
                    .show();
        }else{
            encodeBitmap(bitmap);
            String valTglAbsen = tvTgl.getText().toString();
            String valImage= encodedImage;
            String valLat = tvLat.getText().toString();
            String valLong = tvLong.getText().toString();
            String valAddress = tvAddress.getText().toString();
            String valStatus = statusAbsen.getText().toString();

            SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();

            api.tambahData(valTglAbsen, valLat,valLong,valAddress,valImage,valStatus).enqueue(new Callback<ResTambah>() {



                @Override
                public void onResponse(Call<ResTambah> call, Response<ResTambah> response) {
                    if (response.isSuccessful()) {
                        pDialog.dismissWithAnimation();
                        Toast.makeText(CheckAbsensi.this, "Success Disimpan " + response.message(), Toast.LENGTH_SHORT).show();

                        SweetAlertDialog sD = new SweetAlertDialog(CheckAbsensi.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("Data has been saved!")
                                .setConfirmButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Intent intent = new Intent(CheckAbsensi.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        startActivity(intent);
                                        sDialog.dismissWithAnimation();
                                    }
                                });

                        sD.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                sD.getButton(SweetAlertDialog.BUTTON_CANCEL).setEnabled(false);
                                sD.getButton(SweetAlertDialog.BUTTON_CANCEL).setVisibility(View.GONE);
                            }
                        });

//                        sD.setCancelable(false);
                        sD.show();


                    } else {
                        Toast.makeText(CheckAbsensi.this, "Gagal Disimpan", Toast.LENGTH_SHORT).show();
                        pDialog.dismissWithAnimation();
                    }

                }

                @Override
                public void onFailure(Call<ResTambah> call, Throwable t) {
                    pDialog.dismissWithAnimation();
                    Log.e("NAH", "onFailure:" + t.getLocalizedMessage());
                    Toast.makeText(CheckAbsensi.this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
                }
            });
        }




    }

    private void checkPermissions() {
        //Permissions we need
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        //Permissions that we will ask for
        ArrayList<String> needed_permissions = new ArrayList<>();

        //Check which is not granted yet
        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(CheckAbsensi.this, permission) !=
                    PackageManager.PERMISSION_GRANTED){
                needed_permissions.add(permission);
            }
        }

        //Ask for multiple not granted permissions
        if(!needed_permissions.isEmpty())
            ActivityCompat.requestPermissions(CheckAbsensi.this, needed_permissions.toArray(new String[needed_permissions.size()]), ALL_PERMISSIONS);
        else
            openCameraDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ALL_PERMISSIONS){
            if ((grantResults.length > 0) &&
                    (grantResults[0]
                            + grantResults[1]
                            + grantResults[2] == PackageManager.PERMISSION_GRANTED)){
                openCameraDialog();

            }else {
                Toast.makeText(CheckAbsensi.this, "All permissions need to be granted", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    private void openCameraDialog() {
        String mPath;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            mPath= this.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + File.separator + ".jpeg";
        }else{
            mPath= Environment.getExternalStorageDirectory().toString() + "/" + File.separator + ".jpeg";
        }

        CameraViewDialog cameraViewDialog = new CameraViewDialog();
        cameraViewDialog.init(CheckAbsensi.this, mPath);

//        cameraViewDialog.init(CheckAbsensi.this, Environment.getExternalStorageDirectory().getAbsolutePath()
//                + File.separator + "photos.jpg");

        cameraViewDialog.setOnCaptureDoneListener(new OnCaptureDone() {
            @Override
            public void onDone(String imagePath) {
                statusCap = 1;
                bitmap = flip(BitmapFactory.decodeFile((new File(imagePath)).getAbsolutePath()));
                foto_selfie.setImageBitmap(bitmap);
            }
        });


        cameraViewDialog.setOnCaptureCancelledListener(new OnCaptureCancelled() {
            @Override
            public void OnCancelled() {
                Toast.makeText(CheckAbsensi.this, "Cancelled", Toast.LENGTH_LONG)
                        .show();
            }
        });

        cameraViewDialog.setOnRecapturingListener(new OnRecapturing() {
            @Override
            public void onRecapturing(int times) {
                Toast.makeText(CheckAbsensi.this, String.valueOf(times), Toast.LENGTH_SHORT)
                        .show();
            }
        });

        cameraViewDialog.startDialog();

    }

    void viewDetailImage(){

        final DialogPlus dialog = DialogPlus.newDialog(CheckAbsensi.this)
                .setContentHolder(new ViewHolder(R.layout.dialog_view_photo))
                .setContentBackgroundResource(R.color.white)
                .setCancelable(false)
                .setGravity(Gravity.CENTER)
                .create();

        View v = dialog.getHolderView();

        ImageView ivDetail;
        Button bClose;

        ivDetail = v.findViewById(R.id.dialog_crime_image);
        bClose = v.findViewById(R.id.logout);

        if(statusCap == 1 ){
            ivDetail.setImageBitmap(bitmap);
        }else if(statusIntent.equalsIgnoreCase("1")){
            Picasso.get().load("http://192.168.29.101/absensi_lokasi/images/" + valImg).placeholder(R.drawable.no_image).into(ivDetail);
        }


        bClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static Bitmap flip(Bitmap src) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();

        matrix.preScale(-1.0f, 1.0f);

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }


    private void encodeBitmap(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,40,byteArrayOutputStream);

        byte[] byteofimages = byteArrayOutputStream.toByteArray();
        encodedImage = Base64.encodeToString(byteofimages,Base64.DEFAULT);

    }
}