package com.example.enes.mypet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Profil_Ilanlarim extends AppCompatActivity {

    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private RecyclerView recyclerView;
    private List<GonderiBilgileri> result;
    private List<GonderiBilgileri> result2;
    private RecyclerView.Adapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil__ilanlarim);

        result=new ArrayList<>();
        result2=new ArrayList<>();

        recyclerView= (RecyclerView) findViewById(R.id.gonderi_list2);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(Profil_Ilanlarim.this));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        databaseReference= FirebaseDatabase.getInstance().getReference("Gonderiler");
        firebaseAuth=FirebaseAuth.getInstance();

        databaseReference.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    GonderiBilgileri gonderiBilgileri=dataSnapshot1.getValue(GonderiBilgileri.class);
                    result.add(gonderiBilgileri);
                }

                for (int i=result.size()-1;i>=0;i--)
                {
                    result2.add(result.get(i));
                }
                adapter=new GonderiAdapter(Profil_Ilanlarim.this,result2);
                recyclerView.setAdapter(adapter);

                // adapter=new GonderiAdapter(IlanPaylas_Ana.this,result);
                // recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
            NavUtils.navigateUpTo(this,intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
