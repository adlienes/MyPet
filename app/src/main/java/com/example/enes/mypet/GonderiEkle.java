package com.example.enes.mypet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;

public class GonderiEkle extends AppCompatActivity {

    private EditText baslik,Aciklama,tel, address, shir;
    private Button kaydet;
    private ImageButton resimsec;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(getApplicationContext(),IlanPaylas_Ana.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent2 = new Intent(getApplicationContext(),GonderiEkle.class);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                    return true;
                case R.id.navigation_notifications:
                    Intent intent3 = new Intent(getApplicationContext(),ProfileActivity.class);
                    startActivity(intent3);
                    overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gonderi_ekle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(1).setChecked(true);

        baslik= (EditText) findViewById(R.id.getBaslik);
        Aciklama= (EditText) findViewById(R.id.getAciklama);
        tel= (EditText) findViewById(R.id.getTelefon);
        address = (EditText) findViewById(R.id.getAdres);
        shir = (EditText) findViewById(R.id.getSehir);
        kaydet= (Button) findViewById(R.id.gbtKaydet);
        resimsec= (ImageButton) findViewById(R.id.imageButton);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Gonderiler");
        storageReference= FirebaseStorage.getInstance().getReference("GonderiResim");

        progressDialog=new ProgressDialog(this);

       kaydet.setOnClickListener(new View.OnClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
           @Override
           public void onClick(View v) {
              // GonderiPaylas();
               ResimYukle();
           }
       });
       resimsec.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               chooseImage();
           }
       });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Resim Seç"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                resimsec.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void GonderiPaylas()
    {
        String baslık=baslik.getText().toString().trim();
        String aciklama=Aciklama.getText().toString().trim();
        String telefon=tel.getText().toString().trim();
        String adress=address.getText().toString().trim();
        String sehir=shir.getText().toString().trim();



        if(!TextUtils.isEmpty(baslık) && !TextUtils.isEmpty(aciklama) && !TextUtils.isEmpty(telefon) && !TextUtils.isEmpty(adress) && !TextUtils.isEmpty(sehir))
        {
            FirebaseUser user=firebaseAuth.getCurrentUser();
            String idkullanici=user.getUid();
            String id=databaseReference.push().getKey();
            String resimyolu=storageReference.getPath();
            String resimyolu2=resimyolu+"/"+id;


            GonderiBilgileri gonderiBilgileri=new GonderiBilgileri(id,baslık,aciklama,telefon,adress,sehir,null, new Date(System.currentTimeMillis()));
            databaseReference.child(idkullanici).child(id).setValue(gonderiBilgileri);

            Toast.makeText(this, "Gönderi Başarılı Bir Şekilde Paylaşıldı.", Toast.LENGTH_SHORT).show();
           // startActivity(new Intent(getApplicationContext(),IlanPaylas_Ana.class));

        }
        else
        {
            Toast.makeText(this, "Lütfen Verileri Tam giriniz", Toast.LENGTH_SHORT).show();
        }
    }

    private void ResimYukle() {

        final String baslık = baslik.getText().toString().trim();
        final String aciklama = Aciklama.getText().toString().trim();
        final String telefon = tel.getText().toString().trim();
        final String adress = address.getText().toString().trim();
        final String sehir = shir.getText().toString().trim();


        if (!TextUtils.isEmpty(baslık) && !TextUtils.isEmpty(aciklama) && !TextUtils.isEmpty(telefon) && !TextUtils.isEmpty(adress) && !TextUtils.isEmpty(sehir) && filePath != null) {

            FirebaseUser user=firebaseAuth.getCurrentUser();
            final String idkullanici=user.getUid();
            final String id=databaseReference.push().getKey();

            progressDialog.setMessage("İlan Paylaşılıyor....Lütfen Bekleyin!!..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

                storageReference.child(id).putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        GonderiBilgileri gonderiBilgileri = new GonderiBilgileri(id, baslık, aciklama, telefon, adress, sehir, taskSnapshot.getDownloadUrl().toString(), new Date(System.currentTimeMillis()));
                        databaseReference.child(idkullanici).child(id).setValue(gonderiBilgileri);
                        Toast.makeText(GonderiEkle.this, "Gönderi Başarılı Şekilde Paylaşıldı.", Toast.LENGTH_SHORT).show();
                        getBildirim();
                        startActivity(new Intent(getApplicationContext(),IlanPaylas_Ana.class));
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        }
        else {
            Toast.makeText(this, "Lütfen Verileri Tam Giriniz.", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void getBildirim()
    {
        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification bildirim= new Notification.Builder(this)
                .setSmallIcon(R.drawable.add)
                .setContentTitle("Yeni Gönderi Paylaşıldı.")
                .setContentText("Bildirimm")
                .build();

        notificationManager.notify(0,bildirim);
    }

}
