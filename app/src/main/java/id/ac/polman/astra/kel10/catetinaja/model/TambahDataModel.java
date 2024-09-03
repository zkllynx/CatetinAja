package id.ac.polman.astra.kel10.catetinaja.model;

import java.util.Date;

public class TambahDataModel {
    int tipeKategori;
    String namaKategori, keterangan;
    int nominal;
    Date tanggal;

    public TambahDataModel(int tipeKategori, String namaKategori, int nominal, String keterangan) {
        this.tipeKategori = tipeKategori;
        this.namaKategori = namaKategori;
        this.nominal = nominal;
        this.keterangan = keterangan;
        this.tanggal = tanggal;
    }

    public TambahDataModel() {
    }

    public int getTipeKategori() {
        return tipeKategori;
    }

    public void setTipeKategori(int tipeKategori) {
        this.tipeKategori = tipeKategori;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }
}
