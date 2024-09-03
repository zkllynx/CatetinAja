package id.ac.polman.astra.kel10.catetinaja;

import java.util.Date;

public class LaporanEntity {
    private int tipe_kategori;
    private int nominal;
    private String nama_kategori;
    private String keterangan;
    private Date tanggal;

    public LaporanEntity(int tipe_kategori, int nominal, String nama_kategori, String keterangan, Date tanggal) {
        this.tipe_kategori = tipe_kategori;
        this.nominal = nominal;
        this.nama_kategori = nama_kategori;
        this.keterangan = keterangan;
        this.tanggal = tanggal;
    }

    public int getTipe_kategori() {
        return tipe_kategori;
    }

    public void setTipe_kategori(int tipe_kategori) {
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
}
