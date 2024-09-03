package id.ac.polman.astra.kel10.catetinaja.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.ac.polman.astra.kel10.catetinaja.R;

public class TambahDataFragment extends DialogFragment{
    private EditText nominal, keterangan;
    private Spinner tipe, nama;
    private Button btnSimpan;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    FirebaseUser fbUser = fbAuth.getCurrentUser();
    private View viewTambah;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewTambah = inflater.inflate(R.layout.fragment_tambah_data, container, false);

        tipe = viewTambah.findViewById(R.id.ddl_jenis);
        nama = viewTambah.findViewById(R.id.ddl_kategori);
        nominal = viewTambah.findViewById(R.id.txt_jumlah);
        keterangan = viewTambah.findViewById(R.id.txt_keterangan);
        btnSimpan = viewTambah.findViewById(R.id.btn_simpan);

//        List<String> subjects = new ArrayList<>();
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, subjects);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        nama.setAdapter(adapter);
//        tipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                if(position == 1){
//                    String tipekategori = "Pemasukan";
//                    int tipe_kategori = 1;
//                } else{
//                    String tipekategori = "Pengeluaran";
//                    int tipe_kategori = 1;
//                }
//                db.collection("users").document(fbUser.getEmail())
//                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                if(task.isSuccessful()){
//                                    subjects.clear();
//                                    DocumentSnapshot snapshot = task.getResult();
//                                    List<String> list = new ArrayList<>();
//                                    for(int i=1;i<list.size();i++){
//                                        subjects.add(i, list.get(i));
//                                    }
//                                }
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(keterangan.getText().toString().length()>0 && tipe.getSelectedItem().toString().length()>0 && nama.getSelectedItem().toString().length()>0){
                    saveTransaksi(String.valueOf(tipe.getSelectedItem().toString()), nama.getSelectedItem().toString(), Integer.parseInt(nominal.getText().toString()), keterangan.getText().toString());
                    clear();
                }
            }
        });
        return viewTambah;
    }

    private void saveTransaksi(String tipe, String nama, int nominal, String keterangan){
        Map<String, Object> data = new HashMap<>();
        data.put("Tipe Kategori", tipe);
        data.put("Nama Kategori", nama);
        data.put("Nominal", nominal);
        data.put("Keterangan", keterangan);
        data.put("Tanggal", new Date());

        ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setTitle("Loading");
        loading.setMessage("Menyimpan data...");
        loading.show();

        db.collection("users")
                .document(fbUser.getEmail())
                .collection("Laporan")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Berhasil menyimpan data!", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }

    private void clear(){
        nominal.getText().clear();
        keterangan.getText().clear();
    }
}