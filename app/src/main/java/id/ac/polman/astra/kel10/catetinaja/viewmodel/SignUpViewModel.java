package id.ac.polman.astra.kel10.catetinaja.viewmodel;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.ac.polman.astra.kel10.catetinaja.FirebaseAnalyticsHelper;
import id.ac.polman.astra.kel10.catetinaja.FirebaseAuthHelper;
import id.ac.polman.astra.kel10.catetinaja.Preference;
import id.ac.polman.astra.kel10.catetinaja.UserEntity;
import id.ac.polman.astra.kel10.catetinaja.Validation;
import id.ac.polman.astra.kel10.catetinaja.model.ResponseModel;
import id.ac.polman.astra.kel10.catetinaja.model.SignUpModel;


public class SignUpViewModel extends ViewModel {
    private final static String TAG = SignUpViewModel.class.getSimpleName();
    private FirebaseAuth fbAuth;

    public LiveData<ResponseModel> signUp(Activity activity, SignUpModel signUpModel) {
        MutableLiveData<ResponseModel> signUpLiveData = new MutableLiveData<>();

        FirebaseAuthHelper.getInstance();

        FirebaseAnalyticsHelper analytics = new FirebaseAnalyticsHelper(activity);
        analytics.logEventUserLogin(signUpModel.getEmail());

        if (signUpModel.getUsername().isEmpty() && signUpModel.getEmail().isEmpty()
                && signUpModel.getPassword().isEmpty() &&signUpModel.getRePassword().isEmpty())
            signUpLiveData.postValue(new ResponseModel(false, "Data tidak boleh kosong"));
        else if (signUpModel.getUsername().isEmpty())
            signUpLiveData.postValue(new ResponseModel(false, "Nama Pengguna kosong"));
        else if (signUpModel.getEmail().isEmpty())
            signUpLiveData.postValue(new ResponseModel(false, "Email kosong"));
        else if (signUpModel.getPassword().isEmpty())
            signUpLiveData.postValue(new ResponseModel(false, "Kata Sandi kosong"));
        else if (signUpModel.getRePassword().isEmpty())
            signUpLiveData.postValue(new ResponseModel(false, "Konfirmasi Kata Sandi kosong"));
        else if (!Validation.matchEmail(signUpModel.getEmail()))
            signUpLiveData.postValue(new ResponseModel(false, "Email tidak cocok"));
        else if (!signUpModel.getRePassword().equals(signUpModel.getPassword()))
            signUpLiveData.postValue(new ResponseModel(false, "Kata Sandi tidak sama"));
        else {
            Log.e("Running", "running");
            FirebaseAuthHelper.signUp(activity, signUpModel.getEmail(), signUpModel.getPassword()).observe((LifecycleOwner) activity, responseModel -> {
                Log.e(TAG, "signUp: "+ "response" );
                if (responseModel.isSuccess()) {
                    Log.e(TAG, "signUp: "+ "success" );
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Map<String, Object> user = new HashMap<>();
                    user.put("Email", signUpModel.getEmail());
                    user.put("Username", signUpModel.getUsername());
                    List<String> pemasukan = new ArrayList<>();
                    pemasukan.add("gaji");
                    List<String> pengeluaran = new ArrayList<>();
                    pengeluaran.add("Makan");
                    pengeluaran.add("Transportasi");
                    user.put("pemasukan", pemasukan);
                    user.put("pengeluaran", pengeluaran);
                    user.put("saldo", 0);

                    db.collection("user").document(signUpModel.getEmail())
                            .set(user).addOnSuccessListener(doc -> {
                                new Preference(activity).setUser(new UserEntity(
                                        signUpModel.getEmail(),
                                        signUpModel.getPassword(),
                                        signUpModel.getUsername(),
                                        pemasukan,
                                        pengeluaran,
                                        0
                                ));
                                analytics.logUser(signUpModel.getEmail());
                                signUpLiveData.postValue(new ResponseModel(true, "Success"));
                            }).addOnFailureListener(e -> {
                                signUpLiveData.postValue(new ResponseModel(false, e.getLocalizedMessage()));
                            });
                }
                else
                    signUpLiveData.postValue(responseModel);
            });
        }
        return signUpLiveData;
    }

    public LiveData<ResponseModel> editProfile(Activity activity, SignUpModel signUpModel) {
        MutableLiveData<ResponseModel> editProfileLiveData = new MutableLiveData<>();

        fbAuth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = fbAuth.getCurrentUser();
        FirebaseAuthHelper.getInstance();

        id.ac.polman.astra.kel10.catetinaja.FirebaseAnalyticsHelper analytics = new id.ac.polman.astra.kel10.catetinaja.FirebaseAnalyticsHelper(activity);
        analytics.logEventUserLogin(signUpModel.getEmail());

        if (signUpModel.getUsername().isEmpty())
            editProfileLiveData.postValue(new ResponseModel(false, "Username is Empty"));
        else if (signUpModel.getEmail().isEmpty())
            editProfileLiveData.postValue(new ResponseModel(false, "Email is Empty"));
        else {
            Log.e("Running", "running");
            Log.e(TAG, "signUp: "+ "response" );
            Log.e(TAG, "signUp: "+ "success" );
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("user")
                    .document(fbUser.getEmail()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            Map<String, Object> user = new HashMap<>();

                            String username = signUpModel.getUsername();
                            String email = signUpModel.getEmail();

                            Log.e("CEKKKKKKKKKKKKK", "username: "+ username +"email: "+email);

                            //edit data
                            user.put("username", username);
                            user.put("email", email);

                            db.collection("users").document(fbUser.getEmail())
                                    .update(user).addOnSuccessListener(doc -> {
                                        new Preference(activity).setUser(new UserEntity(
                                                username,
                                                email
                                        ));
                                        editProfileLiveData.postValue(new ResponseModel(true, "Success"));
                                    }).addOnFailureListener(e -> {
                                        editProfileLiveData.postValue(new ResponseModel(false, e.getLocalizedMessage()));
                                        Log.e(TAG, "ERROR"+ e.getLocalizedMessage());
                                    });
                        }
                    });
        }
        return editProfileLiveData;
    }
}

