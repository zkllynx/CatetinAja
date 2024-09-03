package id.ac.polman.astra.kel10.catetinaja.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import id.ac.polman.astra.kel10.catetinaja.R;
import id.ac.polman.astra.kel10.catetinaja.activity.LoginActivity;
import id.ac.polman.astra.kel10.catetinaja.viewmodel.SignUpViewModel;

public class ProfileFragment extends Fragment {

    private TextView mEmail, mPass, mUsername;
    private Button signout;
    private String email, password, username;
    private Image profilePict;
    FirebaseStorage fbStorage = FirebaseStorage.getInstance();


    public ProfileFragment(String email, String password) {
        this.email = email;
        this.password = password;
    }

    ArrayAdapter<CharSequence> adapter;
    SignUpViewModel viewModel = new SignUpViewModel();

    public ProfileFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewProfile = inflater.inflate(R.layout.fragment_profile, container, false);

        //initialisasi
        mUsername = viewProfile.findViewById(R.id.edit_username);
        mEmail = viewProfile.findViewById(R.id.edit_email);
        mPass = viewProfile.findViewById(R.id.edit_pass);
        signout = viewProfile.findViewById(R.id.signOutbtn);

        SharedPreferences sp = getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);

        //get profile data dari firebaseauth
        username = sp.getString("username", "");
        email = sp.getString("email", "");
        password = sp.getString("password", "");
        mUsername.setText(username);
        mEmail.setText(email);
        mPass.setText(password);

//        StorageReference storageRef = fbStorage.getReference("/profile");

        signout.setOnClickListener(v -> {
            FirebaseAuth fbAuth = FirebaseAuth.getInstance();
            fbAuth.signOut();
            signOutUser();
        });
        return viewProfile;
    }

    private void signOutUser() {
        Intent ma = new Intent(getActivity(), LoginActivity.class);
        ma.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(ma);
    }
}
