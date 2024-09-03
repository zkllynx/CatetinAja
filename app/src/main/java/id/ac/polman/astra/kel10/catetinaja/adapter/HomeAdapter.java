package id.ac.polman.astra.kel10.catetinaja.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.ac.polman.astra.kel10.catetinaja.model.HomeModel;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    public HomeAdapter(List<HomeModel> listHome) {
    }

    @NonNull
    @Override
    public HomeAdapter.HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.HomeHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class HomeHolder extends RecyclerView.ViewHolder {
        public HomeHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
