package id.ac.polman.astra.kel10.catetinaja.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import id.ac.polman.astra.kel10.catetinaja.model.HomeModel;

public class HomeListFragment extends Fragment {

    private TextView mKategoriTextView;
    private TextView mTotalTextView;
    private TextView mTanggalTextView;
    private TextView mKeteranganTextView;
    private TextView mTipeKategoriTextView;
    List<HomeModel> listHome = new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    FirebaseFirestore db;
    LaporanAdapter mAdapter;
    FloatingActionButton btnTambah;


    Calendar calendar = Calendar.getInstance();
    Locale id = new Locale("in","ID");
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", id);
    Date currentDate = calendar.getTime();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewTambah = inflater.inflate(R.layout.fragment_home_list, container, false);

        mRecyclerView = viewTambah.findViewById(R.id.dashboard_recycler_view);

        db = FirebaseFirestore.getInstance();

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        btnTambah = viewTambah.findViewById(R.id.transaksi_tambah);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TambahDataFragment dialogFragment = new TambahDataFragment();
                dialogFragment.show(getFragmentManager(), "Form");
            }
        });

        showData();

        return viewTambah;
    }


    private void showData() {
        Log.e("BUTTON CARI", " MASUK DATE DATA");
        FirebaseAuth fbAuth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = fbAuth.getCurrentUser();
        db.collection("users").document(fbUser.getEmail())
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
                                HomeModel homeModel = new HomeModel();
                                homeModel.setTipe_kategori(document.getString("Tipe Kategori"));
                                homeModel.setNominal((document.getLong("Nominal").intValue()));
                                homeModel.setNama_kategori(document.getString("Nama Kategori"));
                                homeModel.setTanggal(document.getDate("Tanggal"));
                                homeModel.setKeterangan(document.getString("Keterangan"));
                                listHome.add(homeModel);

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
                        }
                        mAdapter = new HomeListFragment.LaporanAdapter(listHome);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", e.getMessage());
                    }
                });
    }


    private class LaporanHolder extends RecyclerView.ViewHolder {

        private HomeModel mHomeModel;

        public LaporanHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.home_item_fragment, parent, false));

            mKategoriTextView = (TextView) itemView.findViewById(R.id.nama_kategori);
            mTotalTextView = (TextView) itemView.findViewById(R.id.total_kategori);
            mTanggalTextView = (TextView) itemView.findViewById(R.id.tanggal);
            mKeteranganTextView = (TextView) itemView.findViewById(R.id.keterangan_kategori);
            mTipeKategoriTextView = (TextView) itemView.findViewById(R.id.tipe_kategori);

//            if(mTipeKategoriTextView.getText() == "Pemasukan"){
//                mTipeKategoriTextView.setTextColor(Color.BLUE);
//                mKategoriTextView.setTextColor(Color.BLUE);
//                mTanggalTextView.setTextColor(Color.BLUE);
//                mKeteranganTextView.setTextColor(Color.BLUE);
//                mTotalTextView.setTextColor(Color.BLUE);
//            }else{
//                mTipeKategoriTextView.setTextColor(Color.RED);
//                mKategoriTextView.setTextColor(Color.RED);
//                mTanggalTextView.setTextColor(Color.RED);
//                mKeteranganTextView.setTextColor(Color.RED);
//                mTotalTextView.setTextColor(Color.RED);
//            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void bind(HomeModel homeModel) {
            mHomeModel = homeModel;

            mKategoriTextView.setText(mHomeModel.getNama_kategori());
            mTotalTextView.setText(formatRupiah(Double.parseDouble(String.valueOf(mHomeModel.getNominal()))));
            mTanggalTextView.setText(mSimpleDateFormat.format(homeModel.getTanggal()));
            mKeteranganTextView.setText(mHomeModel.getKeterangan());
            mTipeKategoriTextView.setText(mHomeModel.getTipe_kategori());
        }
    }

    private class LaporanAdapter extends RecyclerView.Adapter<LaporanHolder>{
        private List<HomeModel> mHomeModels;

        public LaporanAdapter(List<HomeModel> homeModels) {mHomeModels = homeModels;}

        @NonNull
        @Override
        public LaporanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new LaporanHolder(layoutInflater, parent);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onBindViewHolder(@NonNull LaporanHolder holder, int position) {
            HomeModel homeModel = mHomeModels.get(position);
            holder.bind(homeModel);
        }

        @Override
        public int getItemCount() {
            return mHomeModels.size();
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
