package com.example.enes.mypet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etEmail,etSifre,etUserName,etUserPhone,etUserAdress;
    private Button buttonKayıt;
    private TextView tvGiris;

    private ProgressDialog progressDialog,progressDialog2;
    InternetKontrol ınternetKontrol=new InternetKontrol(this);

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog2=new ProgressDialog(this);

        if(!ınternetKontrol.isConnected(getApplicationContext()))
        {
            progressDialog2.setMessage("Sisteme Giriş Yapabilmeniz için İnternet Bağlantısına İhtiyacınız var.Lütfen İnternet Bağlantınızı Kontrol Ederek Tekrar Deneyiniz...!");
            progressDialog2.setTitle("İnternet Bağlantısı Sorunu");
            progressDialog2.show();
            progressDialog2.setCanceledOnTouchOutside(false);
        }

        mAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("KullaniciBilgileri");

        if(mAuth.getCurrentUser()!=null)
        {
            //profil activiy burada
            finish();
            startActivity(new Intent(getApplicationContext(),IlanPaylas_Ana.class));
        }

        progressDialog=new ProgressDialog(this);

        etEmail= (EditText) findViewById(R.id.etEmail);
        etSifre= (EditText) findViewById(R.id.etSifre);
        buttonKayıt=(Button) findViewById(R.id.buttonKayit);
        tvGiris=(TextView) findViewById(R.id.tvgiris);
        etUserName= (EditText) findViewById(R.id.etUserName);
        etUserPhone= (EditText) findViewById(R.id.etUserPhone);
        etUserAdress= (EditText) findViewById(R.id.etUserAdress);

        buttonKayıt.setOnClickListener(this);
        tvGiris.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if(v==buttonKayıt)
        {
            KayitOl();
        }

        if(v==tvGiris)
        {
            //giris actvity açılacak
            startActivity(new Intent(this,LoginActivity.class));

        }
    }

    private void KayitOl() {

        String Email=etEmail.getText().toString().trim();
        String Sifre=etSifre.getText().toString().trim();

        if(TextUtils.isEmpty(Email) || TextUtils.isEmpty(Sifre))
        {
            //email şifre boş
            Toast.makeText(this, "Lütfen E-Mail Adresinizi veya Şifre Giriniz!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Üye Kaydı Gerçekleştiriliyor.Lüffen Bekleyiniz..!!!");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(Email,Sifre)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            //başarıolı kayıt
                            //profile gider
                            finish();
                            KullaniciBilgiKaydet();
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Kayıt İşlemi Başarısız Oldu!!!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.cancel();
                    }
                });
    }

    private void KullaniciBilgiKaydet()
    {
        String isim=etUserName.getText().toString().trim();
        String tel=etUserPhone.getText().toString().trim();
        String adres=etUserAdress.getText().toString().trim();

        if(!TextUtils.isEmpty(isim) || TextUtils.isEmpty(tel))
        {
            FirebaseUser user=mAuth.getCurrentUser();
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
}
