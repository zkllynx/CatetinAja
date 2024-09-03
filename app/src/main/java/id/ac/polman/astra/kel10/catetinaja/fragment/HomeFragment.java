package id.ac.polman.astra.kel10.catetinaja.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import id.ac.polman.astra.kel10.catetinaja.R;
import id.ac.polman.astra.kel10.catetinaja.model.HomeModel;

public class HomeFragment extends Fragment {
    private TextView mSaldo, nominal_pemasukan, nominal_pengeluaran, tanggal;
    private View viewHome;
    HomeModel homeModel = new HomeModel();

    Calendar calendar = Calendar.getInstance();
    Locale id = new Locale("in","ID");
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("EEE, dd/MM/yyyy", id);
    Date currentDate = calendar.getTime();
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewHome = inflater.inflate(R.layout.fragment_home, container, false);

        mSaldo = viewHome.findViewById(R.id.saldo);
        nominal_pemasukan = viewHome.findViewById(R.id.nominal_pemasukan);
        nominal_pengeluaran = viewHome.findViewById(R.id.nominal_pengeluaran);
        tanggal = viewHome.findViewById(R.id.tanggal);

//        showTotalData();
        showDatabyDate();
        return viewHome;
    }

    private void showDatabyDate() {
        Log.e("BUTTON CARI", " MASUK DATE DATA");
        FirebaseAuth fbAuth = FirebaseAuth.getInstance();
        FirebaseUser fbuser = fbAuth.getCurrentUser();
        db.collection("users").document(fbuser.getEmail())
                .collection("Laporan")
                .orderBy("Tanggal", Query.Direction.ASCENDING)
                .startAt(yesterday()).endAt(currentDate)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //show data
                            int listjumlahtam = 0;
                            int listjumtam = 0;
                            int listjumlahkur = 0;
                            int listjumkur = 0;
                            String listkategori = "";

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("BUTTON CARI", " selesai FOR");

                                //getTotalPemasukan&Pengeluaran
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
                            homeModel.setPemasukan(listjumtam);
                            String totalpem = String.valueOf(homeModel.getPemasukan());
                            Log.e("LAPORAN", "Total Pemasukan :" + totalpem);

                            nominal_pemasukan.setText(formatRupiah(Double.parseDouble(totalpem)));

                            homeModel.setPengeluaran(listjumkur);
                            String totalpen = String.valueOf(homeModel.getPengeluaran());
                            Log.e("LAPORAN", "Total Pengeluaran :" + totalpen);
                            Log.e("LAPORAN", "Total Pemasukan di pengeluaran :" + String.valueOf(homeModel.getPengeluaran()));

                            nominal_pengeluaran.setText(formatRupiah(Double.parseDouble(totalpen)));

                            //SaldoHariIni
                            int totpem = Integer.parseInt(totalpem);
                            int totpen = Integer.parseInt(totalpen);
                            int totsel = totpem - totpen;
                            Log.e("SALDO", "Saldo :" + String.valueOf(totsel));
                            mSaldo.setText(formatRupiah(Double.parseDouble(String.valueOf(totsel))));

                            //TodayDate
                            tanggal = (TextView) viewHome.findViewById(R.id.tanggal);
                            tanggal.setText(mSimpleDateFormat.format(currentDate));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", e.getMessage());
                    }
                });

//        db.collection("users").document(fbuser.getEmail()).get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @RequiresApi(api = Build.VERSION_CODES.N)
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()){
//                            DocumentSnapshot doc = task.getResult();
//                            int saldo = doc.getLong("Saldo").intValue();
//                            homeModel.setSaldo(saldo);
//                            mSaldo.setText(formatRupiah(Double.parseDouble(Integer.toString(homeModel.getSaldo()))));
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e("TAG", e.getMessage());
//                    }
//                });
    }

//    private void showTotalData(){
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//
//        //getTotalPemasukan
//        db.collection("users").document(firebaseUser.getEmail())
//                .collection("Laporan").whereEqualTo("Tipe Kategori", "Pemasukan")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
//                        homeModel.setPemasukan(listjum);
//                        String totalpem = String.valueOf(homeModel.getPemasukan());
//                        Log.e("LAPORAN", "Total Pemasukan :" + totalpem);
//
//                        nominal_pemasukan = viewHome.findViewById(R.id.nominal_pemasukan);
//                        nominal_pemasukan.setText(formatRupiah(Double.parseDouble(totalpem)));
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e("TAG", e.getMessage());
//                    }
//                });
//
//        //getTotalPengeluaran
//        db.collection("users").document(firebaseUser.getEmail())
//                .collection("Laporan").whereEqualTo("Tipe Kategori", "Pengeluaran")
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
//                        homeModel.setPengeluaran(listjum);
//                        String totalpen = String.valueOf(homeModel.getPengeluaran());
//
//                        nominal_pengeluaran = viewHome.findViewById(R.id.nominal_pengeluaran);
//                        nominal_pengeluaran.setText(formatRupiah(Double.parseDouble(totalpen)));
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
//                                            mSaldo = viewHome.findViewById(R.id.saldo);
//                                            mSaldo.setText(formatRupiah(Double.parseDouble(Integer.toString(homeModel.getSaldo()))));
//                                        }
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.e("TAG", e.getMessage());
//                                    }
//                                });
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e("TAG", e.getMessage());
//                    }
//                });
//
//    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        Fragment childFragment = new HomeListFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, childFragment).commit();
    }

    public HomeModel getHomeModel(){
        return this.homeModel;
    }

    private class HomeHolder extends RecyclerView.ViewHolder {
        private TextView nominal_pemasukan, nominal_pengeluaran;

        private HomeModel mHomeModel;

        public HomeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_home, parent, false));

            nominal_pemasukan =  itemView.findViewById(R.id.nominal_pemasukan);
            nominal_pengeluaran =  itemView.findViewById(R.id.nominal_pengeluaran);
        }

        public void bind(HomeModel homeModel1){
            mHomeModel = homeModel1;

            nominal_pengeluaran.setText(homeModel1.getPengeluaran());
            nominal_pemasukan.setText(homeModel1.getPemasukan());
        }
    }

    private class HomeAdapter extends RecyclerView.Adapter<HomeHolder> {
        private HomeModel mHomeModel;

        public HomeAdapter (HomeModel homeModel2) {
            mHomeModel = homeModel2;
        }

        @NonNull
        @Override
        public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new HomeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
            holder.bind(mHomeModel);
        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

}
