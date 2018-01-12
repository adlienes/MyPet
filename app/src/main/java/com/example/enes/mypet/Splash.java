package com.example.enes.mypet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    private TextView textView,textView2;
    private ImageView ımageView;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textView= (TextView) findViewById(R.id.textView);
        textView2= (TextView) findViewById(R.id.textView2);
        ımageView= (ImageView) findViewById(R.id.imageView);

        Animation anim= AnimationUtils.loadAnimation(this,R.anim.splashanim);
        textView.startAnimation(anim);
        textView2.startAnimation(anim);
        ımageView.startAnimation(anim);

        final Intent i=new Intent(this,MainActivity.class);
        Thread timer=new Thread()
        {
            public void run()
            {
                try {
                    sleep(5000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();;
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}
