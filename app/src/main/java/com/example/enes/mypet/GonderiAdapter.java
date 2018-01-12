package com.example.enes.mypet;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by ENES on 29.11.2017.
 */

public class GonderiAdapter extends RecyclerView.Adapter<GonderiAdapter.GonderiViewHolder> {

    private List<GonderiBilgileri> list;
    Context context;

    public GonderiAdapter(Context context,List<GonderiBilgileri> list) {
        this.list = list;
        this.context=context;
    }

    @Override
    public GonderiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.list_layout,parent,false);
        GonderiViewHolder gonderiViewHolder=new GonderiViewHolder(view);

        return  gonderiViewHolder;
        //GonderiViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(GonderiViewHolder holder, int position) {

        final GonderiBilgileri gonderiBilgileri=list.get(position);

        holder.aciklama.setText(gonderiBilgileri.getAciklama());
        holder.baslik.setText(gonderiBilgileri.getBaslik());
        holder.sehir.setText(gonderiBilgileri.getSehir());
        Glide.with(context).load(gonderiBilgileri.getResimyolu()).into(holder.resim);


        holder.setItemClickListener(new IitemClicklistener() {
            @Override
            public void onItemClick(int pos) {
                Log.i("Context:", context.getClass().getSimpleName());

                if(context.getClass().getSimpleName().matches("IlanPaylas_Ana"))
                {
                    openDetayActivity(gonderiBilgileri.getBaslik(), gonderiBilgileri.getAciklama(), gonderiBilgileri.getTel(), gonderiBilgileri.getAdress(), gonderiBilgileri.getSehir(),gonderiBilgileri.getResimyolu());
                }
                else if(context.getClass().getSimpleName().matches("Profil_Ilanlarim"))
                {
                    openIlanlarımActivity(gonderiBilgileri.getGonderiid(),gonderiBilgileri.getBaslik(), gonderiBilgileri.getAciklama(), gonderiBilgileri.getTel(), gonderiBilgileri.getAdress(), gonderiBilgileri.getSehir(),gonderiBilgileri.getResimyolu());
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void openDetayActivity(String... details)
    {
        Intent i=new Intent(context,DetayActivity.class);

        i.putExtra("BASLİK",details[0]);
        i.putExtra("ACİKLAMA",details[1]);
        i.putExtra("TEL",details[2]);
        i.putExtra("ADRES",details[3]);
        i.putExtra("SEHİR",details[4]);
        i.putExtra("RESİMYOLU",details[5]);

        context.startActivity(i);
    }

    public void openIlanlarımActivity(String... details)
    {
        Intent i2=new Intent(context,IlanlarimDetay.class);

        i2.putExtra("ID",details[0]);
        i2.putExtra("BASLİK",details[1]);
        i2.putExtra("ACİKLAMA",details[2]);
        i2.putExtra("TEL",details[3]);
        i2.putExtra("ADRES",details[4]);
        i2.putExtra("SEHİR",details[5]);
        i2.putExtra("RESİMYOLU",details[6]);


        context.startActivity(i2);
    }

    class GonderiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView baslik,aciklama,sehir;
        ImageView resim;
        IitemClicklistener ıitemClicklistener;

        public GonderiViewHolder(View itemView) {
            super(itemView);

            baslik= (TextView) itemView.findViewById(R.id.Baslik);
            aciklama= (TextView) itemView.findViewById(R.id.Aciklama);
            sehir= (TextView) itemView.findViewById(R.id.Sehir);
            resim= (ImageView) itemView.findViewById(R.id.resim);


            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(IitemClicklistener itemClickListener)
        {
            this.ıitemClicklistener=itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.ıitemClicklistener.onItemClick(this.getLayoutPosition());
        }
    }
}
