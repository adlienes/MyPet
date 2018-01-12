package com.example.enes.mypet;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetayActivity extends AppCompatActivity {

    TextView tvbaslik,tvaciklama,tvtel,tvadres,tvsehir;
    ImageView resim;
    Button paylas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detay);

        tvbaslik= (TextView) findViewById(R.id.DetayBaslik);
        tvaciklama= (TextView) findViewById(R.id.DetayAciklama);
        tvtel= (TextView) findViewById(R.id.DetayTel);
        tvadres= (TextView) findViewById(R.id.DetayAdres);
        tvsehir= (TextView) findViewById(R.id.DetaySehir);
        resim= (ImageView) findViewById(R.id.articleDetailImg);
        paylas= (Button) findViewById(R.id.paylas);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i=getIntent();

        final String baslik=i.getExtras().getString("BASLİK");
        final String aciklam=i.getExtras().getString("ACİKLAMA");
        final String tel=i.getExtras().getString("TEL");
        final String adres=i.getExtras().getString("ADRES");
        final String sehir=i.getExtras().getString("SEHİR");
        String resimyolu=i.getExtras().getString("RESİMYOLU");


        tvbaslik.setText(baslik);
        tvaciklama.setText(aciklam);
        tvtel.setText(tel);
        tvadres.setText(adres);
        tvsehir.setText(sehir);
        Glide.with(getApplicationContext()).load(resimyolu).into(resim);

        paylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence mesaj="Bu Bir Mypet İlanıdır.Bilgi Amaçlıdır."+"\n"+
                                "Başlık: "+baslik+"\n"+
                                "Açıklama: "+aciklam+"\n"+
                                "Tel: "+tel+"\n"+
                                "Adres: "+adres+"\n"+
                                "Sehir: "+sehir;
                paylas(mesaj);
            }
        });

    }

    private void paylas(CharSequence mesaj) {
        Intent paylas=new Intent(Intent.ACTION_SEND);
        paylas.setType("text/plain");
        paylas.putExtra(Intent.EXTRA_TEXT,mesaj);
        startActivity(Intent.createChooser(paylas,"İlan Paylas"));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            Intent intent=new Intent(getApplicationContext(),IlanPaylas_Ana.class);
            NavUtils.navigateUpTo(this,intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
