package com.example.enes.mypet;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{


    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private ImageButton profil_resim;


    private TextView tvName,tvTel;
    private Button buttonCikis;

    private EditText etUserName,etUserPhone,etUserAdress;
    private Button buttonSave;

    private ProgressDialog progressDialog;

    public String resimyolu;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(getApplicationContext(),IlanPaylas_Ana.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_out, R.anim.anim_in);
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
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(2).setChecked(true);

        firebaseAuth=FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()==null)
        {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        progressDialog=new ProgressDialog(this);

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        String id=firebaseUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("KullaniciBilgileri");
        storageReference= FirebaseStorage.getInstance().getReference("KullaniciResim");

        etUserName=(EditText) findViewById(R.id.etUserName);
        etUserPhone=(EditText) findViewById(R.id.etUserPhone);
        etUserAdress=(EditText) findViewById(R.id.etUserAdress);
        buttonSave=(Button) findViewById(R.id.buttonSave);
        tvName= (TextView) findViewById(R.id.tvName);
        tvTel= (TextView) findViewById(R.id.tvTel);
        profil_resim= (ImageButton) findViewById(R.id.user_profile_photo);

        buttonSave.setOnClickListener(this);
        profil_resim.setOnClickListener(this);

       // tvName.setText(firebaseUser.getEmail());






    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                KullaniciBilgileri kb=dataSnapshot.getValue(KullaniciBilgileri.class);

                resimyolu=kb.getResimyolu();

                tvName.setText(kb.getIsim());
                tvTel.setText(kb.getTel());
                etUserAdress.setText(kb.getAdres());
                etUserName.setText(kb.getIsim());
                etUserPhone.setText(kb.getTel());
                Glide.with(getApplicationContext()).load(kb.getResimyolu()).into(profil_resim);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void KullaniciBilgiKaydet()
    {
        String isim=etUserName.getText().toString().trim();
        String tel=etUserPhone.getText().toString().trim();
        String adres=etUserAdress.getText().toString().trim();


        if(!TextUtils.isEmpty(isim) && !TextUtils.isEmpty(tel) && !TextUtils.isEmpty(adres) )
        {
            FirebaseUser user=firebaseAuth.getCurrentUser();
            String idkullanici=user.getUid();


            KullaniciBilgileri kullaniciBilgileri=new KullaniciBilgileri(idkullanici,isim,tel,adres,null);

            databaseReference.child(idkullanici).setValue(kullaniciBilgileri);
            Toast.makeText(this, "Bilgiler Kaydedildi.!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Lüsten Bilgileri Tam giriniz ", Toast.LENGTH_SHORT).show();
        }

    }

    private void ResimYukle()
    {
        final String isim=etUserName.getText().toString().trim();
        final String tel=etUserPhone.getText().toString().trim();
        final String adres=etUserAdress.getText().toString().trim();

        if(!TextUtils.isEmpty(isim) && !TextUtils.isEmpty(tel) && !TextUtils.isEmpty(adres))
        {
            FirebaseUser user=firebaseAuth.getCurrentUser();
            final String idkullanici=user.getUid();

            progressDialog.setMessage("Bilgiler Güncelleniyor....Lütfen Bekleyin!!..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            if(filePath!=null)
            {
                storageReference.child(idkullanici).putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        KullaniciBilgileri kullaniciBilgileri=new KullaniciBilgileri(idkullanici,isim,tel,adres,taskSnapshot.getDownloadUrl().toString());

                        databaseReference.child(idkullanici).setValue(kullaniciBilgileri);
                        Toast.makeText(getApplicationContext(), "Bilgiler Kaydedildi.!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        progressDialog.dismiss();

                    }
                });
            }
            else
            {
                    KullaniciBilgileri kullaniciBilgileri=new KullaniciBilgileri(idkullanici,isim,tel,adres,resimyolu);

                    databaseReference.child(idkullanici).setValue(kullaniciBilgileri);
                    Toast.makeText(getApplicationContext(), "Bilgiler Kaydedildi.!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                    progressDialog.dismiss();

            }
        }
        else
        {
            Toast.makeText(this, "Lütfen Bilgileri Tam giriniz ", Toast.LENGTH_SHORT).show();
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
                profil_resim.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View v) {

        if(v==buttonSave)
        {
            ResimYukle();
            //KullaniciBilgiKaydet();
            //Snackbar.make(v, "Bilgiler Kaydedildi.!!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        if(v==profil_resim)
        {
            chooseImage();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profil_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        switch (id)
        {
            case R.id.ilanlarım:
                startActivity(new Intent(this,Profil_Ilanlarim.class));
                break;
            case R.id.cıkıs:
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("MyPet");
                builder.setMessage("Çıkış Yapmak İstiyormusunuz!!!");
                builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        //İptal butonuna basılınca yapılacaklar.Sadece kapanması isteniyorsa boş bırakılaca
                    }
                });
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Tamam butonuna basılınca yapılacaklar
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    }
                });
                builder.show();

        }

        return super.onOptionsItemSelected(item);
    }
}
