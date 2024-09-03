package id.ac.polman.astra.kel10.catetinaja.model;

import java.util.Date;

public class LaporanModel {

    private String tipe_kategori;
    private int nominal;
    private String nama_kategori;
    private String keterangan;
    private Date tanggal;
    private int total_pemasukan;
    private int total_pengeluaran;
    private int selisih;

    public LaporanModel(String tipe_kategori, int nominal, String nama_kategori, String keterangan, Date tanggal) {
        this.tipe_kategori = tipe_kategori;
        this.nominal = nominal;
        this.nama_kategori = nama_kategori;
        this.keterangan = keterangan;
        this.tanggal = tanggal;
    }

    public LaporanModel(int total_pemasukan, int total_pengeluaran, int selisih) {
        this.total_pemasukan = total_pemasukan;
        this.total_pengeluaran = total_pengeluaran;
        this.selisih = selisih;
    }

    public LaporanModel(){

    }

    public String getTipe_kategori() {
        return tipe_kategori;
    }

    public void setTipe_kategori(String tipe_kategori) {
        this.tipe_kategori = tipe_kategori;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
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

    public int getTotal_pemasukan() {
        return total_pemasukan;
    }

    public void setTotal_pemasukan(int total_pemasukan) {
        this.total_pemasukan = total_pemasukan;
    }

    public int getTotal_pengeluaran() {
        return total_pengeluaran;
    }

    public void setTotal_pengeluaran(int total_pengeluaran) {
        this.total_pengeluaran = total_pengeluaran;
    }

    public int getSelisih() {
        return selisih;
    }

    public void setSelisih(int selisih) {
        this.selisih = selisih;
    }
}

