package id.ac.polman.astra.kel10.catetinaja.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import id.ac.polman.astra.kel10.catetinaja.R;
import id.ac.polman.astra.kel10.catetinaja.model.TambahDataModel;

public class TambahDataAdapter extends RecyclerView.Adapter<TambahDataAdapter.TambahDataHolder> {

    private List<TambahDataModel> list;
    private Activity act;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    public TambahDataAdapter(List<TambahDataModel> list, Activity act){
        this.list =list;
        this.act = act;
    }
    @NonNull
    @Override
    public TambahDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.home_item_fragment, parent, false);
        return new TambahDataHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull TambahDataHolder holder, int position) {
        final TambahDataModel tData = list.get(position);
        holder.tanggal.setText("Tanggal : " + tData.getTanggal());
        holder.nama.setText("Nama Kategori : " + tData.getNamaKategori());
        holder.keterangan.setText("Keterangan : " +tData.getKeterangan());
        holder.total.setText("Total : Rp " + tData.getNominal());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TambahDataHolder extends RecyclerView.ViewHolder {
        private TextView tanggal, nama, keterangan, total;
        private CardView cv_kategori_laporan;

        public TambahDataHolder(@NonNull View itemView) {
            super(itemView);

            tanggal = itemView.findViewById(R.id.tanggal);
            nama = itemView.findViewById(R.id.nama_kategori);
            keterangan = itemView.findViewById(R.id.keterangan_kategori);
            total = itemView.findViewById(R.id.total_kategori);
            cv_kategori_laporan = itemView.findViewById(R.id.cv_kategori_laporan);

        }
    }
}
