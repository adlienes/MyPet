package com.example.enes.mypet;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;

public class IlanlarimDetay extends AppCompatActivity  {

    EditText IlanlarımDetayBaslik,IlanlarımDetayAciklama,IlanlarımDetayTel,IlanlarımDetayAdres,IlanlarımDetaySehir;
    Button button_sil,button_kaydet;
    private ImageButton resimsec;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;


    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilanlarim_detay);

        IlanlarımDetayBaslik= (EditText) findViewById(R.id.IlanlarımDetayBaslik);
        IlanlarımDetayAciklama= (EditText) findViewById(R.id.IlanlarımDetayAciklama);
        IlanlarımDetayTel= (EditText) findViewById(R.id.IlanlarımDetayTel);
        IlanlarımDetayAdres= (EditText) findViewById(R.id.IlanlarımDetayAdres);
        IlanlarımDetaySehir= (EditText) findViewById(R.id.IlanlarımDetaySehir);
        resimsec= (ImageButton) findViewById(R.id.ilan_resim);
        button_sil= (Button) findViewById(R.id.button_ilansil);
        button_kaydet= (Button) findViewById(R.id.button_ilankaydet);

        databaseReference=FirebaseDatabase.getInstance().getReference("Gonderiler");
        storageReference= FirebaseStorage.getInstance().getReference("GonderiResim");
        firebaseAuth=FirebaseAuth.getInstance();

        button_kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IlanDüzenle();
            }
        });
        button_sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(IlanlarimDetay.this);
                builder.setTitle("MyPet");
                builder.setMessage("İlanı Silmek İstiyormusunuz!!!");
                builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        //İptal butonuna basılınca yapılacaklar.Sadece kapanması isteniyorsa boş bırakılaca
                    }
                });
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    Intent i=getIntent();
                    String idd=i.getExtras().getString("ID");
                    databaseReference.child(firebaseAuth.getUid()).child(idd).removeValue();
                    storageReference.child(idd).delete();
                    startActivity(new Intent(getApplicationContext(),IlanPaylas_Ana.class));

                    }
                });
                builder.show();
            }
        });

        resimsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i=getIntent();

        String baslik=i.getExtras().getString("BASLİK");
        String aciklam=i.getExtras().getString("ACİKLAMA");
        String tel=i.getExtras().getString("TEL");
        String adres=i.getExtras().getString("ADRES");
        String sehir=i.getExtras().getString("SEHİR");
        String resimyolu=i.getExtras().getString("RESİMYOLU");

        IlanlarımDetayBaslik.setText(baslik);
        IlanlarımDetayAciklama.setText(aciklam);
        IlanlarımDetayTel.setText(tel);
        IlanlarımDetayAdres.setText(adres);
        IlanlarımDetaySehir.setText(sehir);
        Glide.with(getApplicationContext()).load(resimyolu).into(resimsec);

    }

    private void IlanDüzenle() {

        Intent i=getIntent();

        final String baslik=IlanlarımDetayBaslik.getText().toString().trim();
        final String aciklam=IlanlarımDetayAciklama.getText().toString().trim();
        final String tel=IlanlarımDetayTel.getText().toString().trim();
        final String adres=IlanlarımDetayAdres.getText().toString().trim();
        final String sehir=IlanlarımDetaySehir.getText().toString().trim();
        final String id=i.getExtras().getString("ID");
        final String resimyolu=i.getExtras().getString("RESİMYOLU");

        if(!TextUtils.isEmpty(baslik) && !TextUtils.isEmpty(aciklam) &&!TextUtils.isEmpty(tel) &&!TextUtils.isEmpty(adres) &&!TextUtils.isEmpty(sehir))
        {
            FirebaseUser user=firebaseAuth.getCurrentUser();
            final String idkullanici=user.getUid();

            if(filePath!=null)
            {
                storageReference.child(id).putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        GonderiBilgileri gonderiBilgileri = new GonderiBilgileri(id, baslik, aciklam, tel, adres, sehir, taskSnapshot.getDownloadUrl().toString(), new Date(System.currentTimeMillis()));
                        databaseReference.child(idkullanici).child(id).setValue(gonderiBilgileri);
                        Toast.makeText(getApplicationContext(), "Gönderi Başarılı Bir Şekilde Paylaşıldı.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),IlanPaylas_Ana.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
            else
            {
                GonderiBilgileri gonderiBilgileri = new GonderiBilgileri(id, baslik, aciklam, tel, adres, sehir,resimyolu, new Date(System.currentTimeMillis()));
                databaseReference.child(idkullanici).child(id).setValue(gonderiBilgileri);
                Toast.makeText(getApplicationContext(), "Gönderi Başarılı Bir Şekilde Paylaşıldı.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),IlanPaylas_Ana.class));            }
        }
        else {
            Toast.makeText(this, "Lütfen Verileri Tam giriniz", Toast.LENGTH_SHORT).show();
            }


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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            Intent intent=new Intent(getApplicationContext(),Profil_Ilanlarim.class);
            NavUtils.navigateUpTo(this,intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
