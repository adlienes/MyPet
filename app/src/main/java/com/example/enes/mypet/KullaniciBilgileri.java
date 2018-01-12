package com.example.enes.mypet;

/**
 * Created by ENES on 24.10.2017.
 */

public class KullaniciBilgileri {

    public String id;
    public String isim;
    public String tel;
    public String adres;
    public String resimyolu;

    public String getId() {
        return id;
    }

    public String getIsim() {
        return isim;
    }

    public String getTel() {
        return tel;
    }

    public String getAdres() {
        return adres;
    }

    public String getResimyolu() {
        return resimyolu;
    }

    public KullaniciBilgileri()
    {

    }

    public KullaniciBilgileri(String id,String isim, String tel, String adres) {
        this.id=id;
        this.isim = isim;
        this.tel = tel;
        this.adres = adres;
        this.resimyolu=resimyolu;
    }

    public KullaniciBilgileri(String id,String isim, String tel, String adres,String resimyolu) {
        this.id=id;
        this.isim = isim;
        this.tel = tel;
        this.adres = adres;
        this.resimyolu=resimyolu;
    }

}
