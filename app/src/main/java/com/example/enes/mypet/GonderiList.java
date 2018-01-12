package com.example.enes.mypet;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ENES on 22.11.2017.
 */

public class GonderiList extends ArrayAdapter<GonderiBilgileri> {

    private Activity context;
    private List<GonderiBilgileri> gonderibilgilerilist;

    public GonderiList(Activity context,List<GonderiBilgileri> gonderibilgilerilist)
    {
        super(context,R.layout.list_layout,gonderibilgilerilist);
        this.context=context;
        this.gonderibilgilerilist=gonderibilgilerilist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();

        View listviewitem=inflater.inflate(R.layout.list_layout,null,true);

        TextView baslik= (TextView) listviewitem.findViewById(R.id.Baslik);
        TextView aciklama= (TextView) listviewitem.findViewById(R.id.Aciklama);
        TextView sehir= (TextView) listviewitem.findViewById(R.id.Sehir);

        GonderiBilgileri bilgi=gonderibilgilerilist.get(position);

        baslik.setText(bilgi.getBaslik());
        aciklama.setText(bilgi.getAciklama());
        sehir.setText(bilgi.getSehir());

        return listviewitem;
    }
}
