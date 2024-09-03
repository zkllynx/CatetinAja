package id.ac.polman.astra.kel10.catetinaja.viewmodel;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.ac.polman.astra.kel10.catetinaja.fragment.HomeFragment;
import id.ac.polman.astra.kel10.catetinaja.model.HomeModel;
import id.ac.polman.astra.kel10.catetinaja.model.ResponseModel;
import id.ac.polman.astra.kel10.catetinaja.model.TambahDataModel;

public class TambahDataViewModel extends ViewModel {
    private final static String TAG = TambahDataViewModel.class.getSimpleName();
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private HomeFragment mHomeFragment;

    public LiveData<ResponseModel> saveTransaksi(Activity activity, TambahDataModel tambahdataModel){
        MutableLiveData<ResponseModel> saveTransaksiLiveData = new MutableLiveData<>();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (tambahdataModel == null){
            saveTransaksiLiveData.postValue(new ResponseModel(false, "Transaksi is Empty"));
        } else {
            if (tambahdataModel.getNamaKategori().isEmpty()){
                saveTransaksiLiveData.postValue(new ResponseModel(false, "Kategori is Empty"));
            } else if (tambahdataModel.getNominal() == 0) {
                saveTransaksiLiveData.postValue(new ResponseModel(false, "Jumlah cannot be 0"));
            } else if (tambahdataModel.getTipeKategori() == 0){
                saveTransaksiLiveData.postValue(new ResponseModel(false, "jenis kategori is empty"));
            } else if (tambahdataModel.getKeterangan().isEmpty()) {
                saveTransaksiLiveData.postValue(new ResponseModel(false, "Keterangan is Empty"));
            } else {
                Map<String, Object> transaksi = new HashMap<>();
                transaksi.put("Tipe Kategori", tambahdataModel.getTipeKategori());
                transaksi.put("Nominal", tambahdataModel.getNominal());
                transaksi.put("Nama Kategori", tambahdataModel.getNamaKategori());
                transaksi.put("Keterangan", tambahdataModel.getKeterangan());
                transaksi.put("Tanggal", new Date());

                db.collection("users").document(firebaseUser.getEmail())
                        .collection("Laporan").add(transaksi)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                saveTransaksiLiveData.postValue(new ResponseModel(true, "Success"));

                                //////
                                db.collection("user")
                                        .document(firebaseUser.getEmail()).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                DocumentSnapshot document = task.getResult();
                                                Map<String, Object> user = new HashMap<>();

                                                HomeModel homeModel = new HomeModel();
                                                homeModel.setSaldo(document.getLong("Saldo").intValue());

                                                Log.e("TRANSAKSI SALDO", "SALDO AWAL : " + homeModel.getSaldo());
                                                updateSaldo(tambahdataModel.getTipeKategori(), tambahdataModel.getNominal(), homeModel.getNominal());
                                                Log.e("UPDATE SALDO", String.valueOf(tambahdataModel.getTipeKategori()) + " " + String.valueOf(tambahdataModel.getNominal()) + " " + String.valueOf(homeModel.getSaldo()));
                                            }
                                        })
                                        .addOnFailureListener(e -> {

                                        });
                                /////
                                //updateSaldo(transaksiModel.getJenis_kategori(), transaksiModel.getJumlah(), dashboardModel.getSaldo());
                            }
                        })
///////////////
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                saveTransaksiLiveData.postValue(new ResponseModel(true, e.getMessage()));
                            }
                        });
            }
        }
        return saveTransaksiLiveData;
    }

    public void updateSaldo(int tipe, int total, int saldoSekarang){
        int saldo;
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (tipe == 1){
            saldo = saldoSekarang + total;
        } else {
            saldo = saldoSekarang - total;
        }

        db.collection("users").document(firebaseUser.getEmail())
                .update("Saldo", saldo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.e("SALDO BERHASIL", String.valueOf(saldo));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", e.getMessage());
                    }
                });
    }
}
