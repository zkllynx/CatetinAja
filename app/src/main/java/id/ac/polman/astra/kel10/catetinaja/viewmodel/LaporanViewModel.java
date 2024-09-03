package id.ac.polman.astra.kel10.catetinaja.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import id.ac.polman.astra.kel10.catetinaja.model.LaporanModel;

public class LaporanViewModel extends ViewModel {
    private final String TAG = LaporanViewModel.class.getSimpleName();
    private List<LaporanModel> mLaporanModels;
    private FirebaseAuth firebaseAuth;

    public LaporanViewModel() {}

    public List<LaporanModel> getLaporanModels(){
        if (mLaporanModels == null)
        {
            mLaporanModels = new ArrayList<>();
            loadLaporanModels();
        }
        Log.e(TAG, "getHomeModels: "+ mLaporanModels.size());
        return mLaporanModels;
    }

    private void loadLaporanModels(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Log.e("Login", firebaseUser.getEmail() + "Home");

        if (!firebaseUser.getEmail().isEmpty())
        {
            db.collection("users").document(firebaseUser.getEmail())
                    .collection("Laporan").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult())
                                {
                                    LaporanModel laporanModel = new LaporanModel();
                                    laporanModel.setTipe_kategori(document.getString("Tipe Kategori"));
                                    Log.e("Login", "Tipe Kategori :" + laporanModel.getTipe_kategori());

                                    laporanModel.setNominal((document.getLong("Nominal").intValue()));
                                    Log.e("Login", "Nominal :" + laporanModel.getNominal());

                                    laporanModel.setNama_kategori(document.getString("Nama Kategori"));
                                    Log.e("Login", "Nama Kategori :" + laporanModel.getNama_kategori());

                                    laporanModel.setTanggal(document.getDate("Tanggal"));
                                    Log.e("Login", "Tanggal :" + laporanModel.getTanggal());

                                    mLaporanModels.add(laporanModel);
                                    Log.e(TAG, "onComplete: " + mLaporanModels.size() );
                                }
                            }
                            else
                                Log.e(TAG, "Error getting document ", task.getException() );
                        }
                    });
        }
    }

}
