package com.example.enes.mypet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class IlanPaylas_Ana extends AppCompatActivity {

    private static final int DIALOG_HAKKINDA=1;

    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private RecyclerView recyclerView;
    private List<GonderiBilgileri> result;
    private List<GonderiBilgileri> result2;
    private RecyclerView.Adapter adapter;

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
        setContentView(R.layout.activity_ilan_paylas__ana);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setChecked(true);


        result=new ArrayList<>();
        result2=new ArrayList<>();

        recyclerView= (RecyclerView) findViewById(R.id.gonderi_list);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(IlanPaylas_Ana.this));

        progressDialog = new ProgressDialog(IlanPaylas_Ana.this);
        progressDialog.setMessage("Veriler Yüklenirken Lütfen Bekleyin..");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        databaseReference=FirebaseDatabase.getInstance().getReference("Gonderiler");
        firebaseAuth=FirebaseAuth.getInstance();

        databaseReference.orderByChild("date").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    GonderiBilgileri gonderiBilgileri=dataSnapshot1.getValue(GonderiBilgileri.class);
                    result.add(gonderiBilgileri);
                }

               /*
                for (int i=result.size()-1;i>=0;i--)
                {
                    result2.add(result.get(i));
                }*/
                adapter=new GonderiAdapter(IlanPaylas_Ana.this,result);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();

                // adapter=new GonderiAdapter(IlanPaylas_Ana.this,result);
                // recyclerView.setAdapter(adapter);
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //geri tuşuna basılma durumunu yakalıyoruz
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    // finish ile activity'i sonlandırıyoruz.
                    finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
