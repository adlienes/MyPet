package com.example.enes.mypet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener{

    private EditText etEmail,etSifre;
    private Button buttonGiris;
    private TextView tvKayit;

    private ProgressDialog progressDialog,progressDialog2;

    private FirebaseAuth firebaseAuth;

    InternetKontrol ınternetKontrol=new InternetKontrol(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog2=new ProgressDialog(this);

        if(!ınternetKontrol.isConnected(getApplicationContext())){
            progressDialog2.setMessage("Sisteme Giriş Yapabilmeniz için İnternet Bağlantısına İhtiyacınız var.Lütfen İnternet Bağlantınızı Kontrol Ederek Tekrar Deneyiniz...!");
            progressDialog2.setTitle("İnternet Bağlantısı Sorunu");
            progressDialog2.show();
            progressDialog2.setCanceledOnTouchOutside(false);
        }

        etEmail=(EditText) findViewById(R.id.etEmail);
        etSifre=(EditText) findViewById(R.id.etSifre);
        buttonGiris=(Button) findViewById(R.id.buttonGiris);
        tvKayit=(TextView) findViewById(R.id.tvkayit);

        buttonGiris.setOnClickListener(this);
        tvKayit.setOnClickListener(this);


        progressDialog=new ProgressDialog(this);

        firebaseAuth=FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null)
        {
            //profil activiy burada
            finish();
            Intent intent=new Intent(getApplicationContext(),IlanPaylas_Ana.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {

        if(v==buttonGiris)
        {
            UserLogin();
        }
        if(v==tvKayit)
        {
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }

    }

    private void UserLogin() {

        String Email=etEmail.getText().toString().trim();
        String Sifre=etSifre.getText().toString().trim();

        if(TextUtils.isEmpty(Email) && TextUtils.isEmpty(Sifre))
        {
            Toast.makeText(this, "Lütfen E-Mail yada Şifrenizi Tam Giriniz", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Giriş Yapılıyor...Lütfen Bekleyin!!..");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(Email,Sifre)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful())
                        {
                                //profil activity burda başlar
                                finish();
                                startActivity(new Intent(getApplicationContext(),IlanPaylas_Ana.class));
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Üye Girişi Başarısız oldu.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
