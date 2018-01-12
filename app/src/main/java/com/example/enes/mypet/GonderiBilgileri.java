package com.example.enes.mypet;

import android.util.Log;

import java.util.Date;

/**
 * Created by ENES on 29.10.2017.
 */

public class GonderiBilgileri {
    private String gonderiid;
    private String baslik;
    private String aciklama;
    private String tel;
    private String adress;
    private String sehir;
    private String resimyolu;
    private Date date;


    public GonderiBilgileri() {

    }


    public GonderiBilgileri(String gonderiid, String baslik, String aciklama, String tel, String adress, String sehir, Date date) {
        this.gonderiid = gonderiid;
        this.baslik = baslik;
        this.aciklama = aciklama;
        this.tel = tel;
        this.adress = adress;
        this.sehir = sehir;
    }

    public GonderiBilgileri(String gonderiid, String baslik, String aciklama, String tel, String adress, String sehir, String resimyolu, Date date) {
        this.gonderiid = gonderiid;
        this.baslik = baslik;
        this.aciklama = aciklama;
        this.tel = tel;
        this.adress = adress;
        this.sehir = sehir;
        this.resimyolu = resimyolu;
        this.date = date;
        Log.i("Date:", String.valueOf(date.getTime()));
    }

    public String getGonderiid() {
        return gonderiid;
    }

    public String getBaslik() {
        return baslik;
    }

    public String getAciklama() {
        return aciklama;
    }

    public String getTel() {
        return tel;
    }

    public String getAdress() {
        return adress;
    }

    public String getSehir() {
        return sehir;
    }

    public Date getDate() { return date;}

    public void setDate(Date date) {this.date = date;}

    public String getResimyolu() {
        return resimyolu;
    }

    public void setGonderiid(String gonderiid) {
        this.gonderiid = gonderiid;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setSehir(String sehir) {
        this.sehir = sehir;
    }

    public void setResimyolu(String resimyolu) {
        this.resimyolu = resimyolu;
    }
}
