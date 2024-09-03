package id.ac.polman.astra.kel10.catetinaja.fragment;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.ac.polman.astra.kel10.catetinaja.R;
import id.ac.polman.astra.kel10.catetinaja.Validation;
import id.ac.polman.astra.kel10.catetinaja.adapter.LaporanAdapter;
import id.ac.polman.astra.kel10.catetinaja.model.HomeModel;
import id.ac.polman.astra.kel10.catetinaja.model.LaporanModel;

public class LaporanFragment extends Fragment {
    private View viewLaporan;

    List<LaporanModel> listLaporan = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore db;
    LaporanAdapter mLaporanAdapter;
    LaporanModel laporanModel = new LaporanModel();
    Validation valid;

    RecyclerView rv_view_laporan;
    CardView cv_kategori_laporan;
    TextView total_pemasukan;
    TextView total_pengeluaran;
    TextView selisih;

    EditText input_minimal;
    Button btn_minimal;
    EditText input_maximal;
    Button btn_maximal;
    Button btn_cari;

    Calendar calendar = Calendar.getInstance();
    Locale id = new Locale("in","ID");
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", id);
    HomeModel homeModel = new HomeModel();

    Date date_minimal;
    Date date_maximal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewLaporan = inflater.inflate(R.layout.fragment_laporan_list, container, false);

        rv_view_laporan = viewLaporan.findViewById(R.id.rv_view_laporan);
        cv_kategori_laporan = viewLaporan.findViewById(R.id.cv_kategori_laporan);

        total_pemasukan = viewLaporan.findViewById(R.id.total_pemasukan);
        total_pengeluaran = viewLaporan.findViewById(R.id.total_pengeluaran);
        selisih = viewLaporan.findViewById(R.id.selisih);

        input_minimal = viewLaporan.findViewById(R.id.input_minimal);
        input_maximal = viewLaporan.findViewById(R.id.input_maximal);
        btn_minimal = viewLaporan.findViewById(R.id.btn_minimal);
        btn_maximal = viewLaporan.findViewById(R.id.btn_maximal);
        btn_cari = viewLaporan.findViewById(R.id.cari);

        db = FirebaseFirestore.getInstance();

//        showTotalData();

        rv_view_laporan.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rv_view_laporan.setLayoutManager(layoutManager);
        //rv_view.setItemAnimator(new DefaultItemAnimator());

//        showData();

        btn_minimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        input_minimal.setText(mSimpleDateFormat.format(calendar.getTime()));
                        //date today
                        calendar.add(Calendar.DATE, -1);
                        date_minimal = calendar.getTime();

                        String input1 = input_minimal.getText().toString();
                        String input2 = input_maximal.getText().toString();

                        if(input1.isEmpty()&&input2.isEmpty()){
                            btn_cari.setEnabled(false);
                        }else{
                            btn_cari.setEnabled(true);
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btn_maximal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        input_maximal.setText(mSimpleDateFormat.format(calendar.getTime()));
                        date_maximal = calendar.getTime();

                        String input1 = input_minimal.getText().toString();
                        String input2 = input_maximal.getText().toString();

                        if(input1.isEmpty()&&input2.isEmpty()){
                            btn_cari.setEnabled(false);
                        }else{
                            btn_cari.setEnabled(true);
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btn_cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateData();
            }
        });

        return viewLaporan;
    }

    private void showDateData() {
        Log.e("BUTTON CARI", " MASUK DATE DATA");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String email = firebaseUser.getEmail();
        db.collection("users").document(email)
            .collection("Laporan")
            .orderBy("Tanggal", Query.Direction.ASCENDING)
            .startAt(date_minimal).endAt(date_maximal)
            .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //showListener(task);
                        if (task.isSuccessful()) {
                            Log.e("BUTTON CARI", "isSuccessfull :" +date_minimal +"after" +date_maximal);
                            listLaporan.clear();

                            //show data
                            int listjumlahtam = 0;
                            int listjumtam = 0;
                            int listjumlahkur = 0;
                            int listjumkur = 0;
                            String listkategori = "";

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                LaporanModel laporanModel = new LaporanModel();
                                laporanModel.setTipe_kategori(document.getString("Tipe Kategori"));
                                laporanModel.setNominal((document.getLong("Nominal").intValue()));
                                laporanModel.setKeterangan(document.getString("Keterangan"));
                                laporanModel.setNama_kategori(document.getString("Nama Kategori"));
                                laporanModel.setTanggal(document.getDate("Tanggal"));
                                listLaporan.add(laporanModel);

                                Log.e("BUTTON CARI", " selesai FOR");

                                //getTotalPemasukan
                                listkategori = (String) document.getString("Tipe Kategori");

                                if(listkategori.equals("Pemasukan")){
                                    listjumlahtam = (int) document.getLong("Nominal").intValue();
                                    Log.e("LAPORAN TAMBAH", "listjumlah :" + listjumlahtam);

                                    listjumtam = listjumtam + listjumlahtam;
                                    Log.e("LAPORAN LIST TAMBAH", "listjum :" + listjumtam);
                                }else if (listkategori.equals("Pengeluaran")){
                                    listjumlahkur = (int) document.getLong("Nominal").intValue();
                                    Log.e("LAPORAN KURANG", "listjumlah :" + listjumlahkur);

                                    listjumkur = listjumkur + listjumlahkur;
                                    Log.e("LAPORAN LIST KURANG", "listjum :" + listjumkur);
                                }

                            }
                            laporanModel.setTotal_pemasukan(listjumtam);
                            String totalpem = String.valueOf(laporanModel.getTotal_pemasukan());
                            Log.e("LAPORAN", "Total Pemasukan :" + totalpem);

                            total_pemasukan = viewLaporan.findViewById(R.id.total_pemasukan);
                            total_pemasukan.setText(formatRupiah(Double.parseDouble(totalpem)));

                            laporanModel.setTotal_pengeluaran(listjumkur);
                            String totalpen = String.valueOf(laporanModel.getTotal_pengeluaran());
                            Log.e("LAPORAN", "Total Pengeluaran :" + totalpen);
                            Log.e("LAPORAN", "Total Pemasukan di pengeluaran :" + String.valueOf(laporanModel.getTotal_pemasukan()));

                            total_pengeluaran = viewLaporan.findViewById(R.id.total_pengeluaran);
                            total_pengeluaran.setText(formatRupiah(Double.parseDouble(totalpen)));

                            //SELISIH
                            int totpem = Integer.parseInt(totalpem);
                            int totpen = Integer.parseInt(totalpen);
                            int totsel = totpem - totpen;
                            Log.e("SELISIH", "Selisih :" + String.valueOf(totsel));
                            selisih = viewLaporan.findViewById(R.id.selisih);
                            selisih.setText(formatRupiah(Double.parseDouble(String.valueOf(totsel))));
                        }
                        mLaporanAdapter = new LaporanAdapter(LaporanFragment.this, listLaporan);
                        rv_view_laporan.setAdapter(mLaporanAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", e.getMessage());
                    }
                });
    }

    private void showData() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String email = firebaseUser.getEmail();

        db.collection("users").document(email)
                .collection("Laporan")
                .orderBy("Tanggal", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                LaporanModel laporanModel = new LaporanModel();
                                laporanModel.setTipe_kategori(document.getString("Tipe Kategori"));
                                laporanModel.setNominal((document.getLong("Nominal").intValue()));
                                laporanModel.setNama_kategori(document.getString("Nama Kategori"));
                                laporanModel.setKeterangan(document.getString("Keterangan"));
                                laporanModel.setTanggal(document.getDate("Tanggal"));
                                listLaporan.add(laporanModel);
                            }
                        }
                        mLaporanAdapter = new LaporanAdapter(LaporanFragment.this, listLaporan);
                        rv_view_laporan.setAdapter(mLaporanAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", e.getMessage());
                    }
                });
    }

//    private void showTotalData(){
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//
//        //getTotalPemasukan
//        db.collection("users").document(firebaseUser.getEmail())
//            .collection("Laporan").whereEqualTo("Tipe Kategori", "Pemasukan")
//            .get()
//            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @RequiresApi(api = Build.VERSION_CODES.N)
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    //show data
//                    int listjumlah = 0;
//                    int listjum = 0;
//
//                    for (QueryDocumentSnapshot doc : task.getResult()) {
//                        listjumlah = (int) doc.getLong("Nominal").intValue();
//                        Log.e("LAPORAN", "listjumlah :" + listjumlah);
//
//                        listjum = listjum + listjumlah;
//                        Log.e("LAPORAN LIST", "listjum :" + listjum);
//                    }
//
//                    laporanModel.setTotal_pemasukan(listjum);
//                    String totalpem = String.valueOf(laporanModel.getTotal_pemasukan());
//                    Log.e("LAPORAN", "Total Pemasukan :" + totalpem);
//
//                    total_pemasukan = viewLaporan.findViewById(R.id.total_pemasukan);
//                    total_pemasukan.setText(formatRupiah(Double.parseDouble(totalpem)));
//                }
//            })
//            .addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.e("TAG", e.getMessage());
//                }
//            });
//
//        //getTotalPengeluaran
//        db.collection("users").document(firebaseUser.getEmail())
//            .collection("Laporan").whereEqualTo("Tipe Kategori", "Pengeluaran")
//            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @RequiresApi(api = Build.VERSION_CODES.N)
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        //show data
//                        int listjumlah = 0;
//                        int listjum = 0;
//
//                        for (QueryDocumentSnapshot doc : task.getResult()) {
//                            listjumlah = (int) doc.getLong("Nominal").intValue();
//                            Log.e("LAPORAN", "listjumlah :" + listjumlah);
//
//                            listjum = listjum + listjumlah;
//                            Log.e("LAPORAN LIST", "listjum :" + listjum);
//                        }
//
//                        laporanModel.setTotal_pengeluaran(listjum);
//                        String totalpen = String.valueOf(laporanModel.getTotal_pengeluaran());
//
//                        total_pengeluaran = viewLaporan.findViewById(R.id.total_pengeluaran);
//                        total_pengeluaran.setText(formatRupiah(Double.parseDouble(totalpen)));
//
//                        db.collection("users").document(firebaseUser.getEmail()).get()
//                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                    @RequiresApi(api = Build.VERSION_CODES.N)
//                                    @Override
//                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                        if (task.isSuccessful()){
//                                            DocumentSnapshot doc = task.getResult();
////                                            int saldo = doc.getLong("Saldo").intValue();
////                                            homeModel.setSaldo(saldo);
//
//
//                                            selisih = viewLaporan.findViewById(R.id.selisih);
//                                            selisih.setText(formatRupiah(Double.parseDouble(Integer.toString(homeModel.getSaldo()))));
//                                        }
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.e("TAG", e.getMessage());
//                            }
//                        });
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e("TAG", e.getMessage());
//                    }
//                });
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    private void showListener(DataSnapshot snapshot) {
        listLaporan.clear();
        for (DataSnapshot item : snapshot.getChildren()) {
            LaporanModel laporanModel = item.getValue(LaporanModel.class);
            listLaporan.add(laporanModel);
        }
        mLaporanAdapter = new LaporanAdapter(LaporanFragment.this, listLaporan);
        rv_view_laporan.setAdapter(mLaporanAdapter);
    }
}
