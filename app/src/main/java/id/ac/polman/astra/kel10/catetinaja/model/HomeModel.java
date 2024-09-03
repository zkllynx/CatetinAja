package id.ac.polman.astra.kel10.catetinaja.model;

import java.util.Date;

public class HomeModel {

    private String tipe_kategori;
    private String nama_kategori;
    private int nominal;
    private Date tanggal;
    private int pemasukan;
    private int pengeluaran;
    private int saldo;
    private String keterangan;

    public HomeModel(String tipe_kategori, String nama_kategori, int nominal, Date tanggal, int pemasukan, int pengeluaran, int saldo, String keterangan) {
        this.tipe_kategori = tipe_kategori;
        this.nama_kategori = nama_kategori;
        this.nominal = nominal;
        this.tanggal = tanggal;
        this.pemasukan = pemasukan;
        this.pengeluaran = pengeluaran;
        this.saldo = saldo;
        this.keterangan = keterangan;
    }


    public HomeModel() {
    }

    public String getTipe_kategori() {
        return tipe_kategori;
    }

    public void setTipe_kategori(String tipe_kategori) {
        this.tipe_kategori = tipe_kategori;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public int getPemasukan() {
        return pemasukan;
    }

    public void setPemasukan(int pemasukan) {
        this.pemasukan = pemasukan;
    }

    public int getPengeluaran() {
        return pengeluaran;
    }

    public void setPengeluaran(int pengeluaran) {
        this.pengeluaran = pengeluaran;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}