package id.ac.polman.astra.kel10.catetinaja.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import id.ac.polman.astra.kel10.catetinaja.R;
import id.ac.polman.astra.kel10.catetinaja.fragment.HomeFragment;
import id.ac.polman.astra.kel10.catetinaja.fragment.LaporanFragment;
import id.ac.polman.astra.kel10.catetinaja.fragment.ProfileFragment;


public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    public HomeActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

//        getSupportFragmentManager().beginTransaction().replace(R.id.child_fragment_container,
//                new HomeListFragment()).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(firebaseUser.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        int id = item.getItemId();
//                        if (id == R.id.beranda){
//                            Fragment selectedFragment = null;
//                            selectedFragment = new HomeFragment();
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                    selectedFragment).commit();
//                        }
//                        else if (id == R.id.laporan){
//                            Fragment selectedFragment = null;
//
//                            selectedFragment = new PieChartFragment();
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                    selectedFragment).commit();
//                        }
                        if (id == R.id.profile){
                            Fragment selectedFragment = null;

                            selectedFragment = new ProfileFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                        }
                        else if (id == R.id.riwayat){
                            Fragment selectedFragment = null;

                            selectedFragment = new LaporanFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    selectedFragment).commit();

                        }
                    }
                });
        return true;
    }

//    private void signOutUser(){
//        Intent ma = new Intent(HomeActivity.this, LoginActivity.class);
//        ma.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(ma);
//        finish();
//
//    }
}