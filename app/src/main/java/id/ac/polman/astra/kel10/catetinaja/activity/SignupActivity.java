package id.ac.polman.astra.kel10.catetinaja.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import id.ac.polman.astra.kel10.catetinaja.R;


public class SignupActivity extends AppCompatActivity {
    private EditText mEmail, mPass, mUsername, mConfPass;
    private Button btnSignup;
    private TextView btnLogin;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog loading;
    private FirebaseAuth fbAuth;
    private ImageView btnShow1, btnShow2;
    private String txemail, txpass, txusername;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //initialize
        mEmail = findViewById(R.id.emailReg);
        mPass = findViewById(R.id.regPassword);
        mConfPass = findViewById(R.id.confPassword);
        mUsername = findViewById(R.id.usernameReg);
        btnSignup = (Button) findViewById(R.id.signup_button);
        btnLogin = (TextView) findViewById(R.id.logButton);
        btnShow1 = (ImageView) findViewById(R.id.show_pass_btn);
        btnShow2 = (ImageView) findViewById(R.id.show_confPass_btn);

        fbAuth = FirebaseAuth.getInstance();
        if(fbAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }

        //Loading ProgressDialog
        loading = new ProgressDialog(SignupActivity.this);
        loading.setTitle("Loading");
        loading.setMessage("Menyimpan data akun...");

        //SignUp Button Function
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEmail.getText().toString().length()>0 && mUsername.getText().toString().length()>0 && mPass.getText().toString().length()>0 && mConfPass.getText().toString().length()>0){
                    if(mPass.getText().toString().equals(mConfPass.getText().toString())){
                        signUp(mEmail.getText().toString(), mUsername.getText().toString(), mPass.getText().toString());
                    }else {
                        Toast.makeText(SignupActivity.this, "Kata Sandi Tidak Sama!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignupActivity.this, "Isi Semua Data!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Login Button Function
        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });

        //View Pass Function
        btnShow1.setOnClickListener(v ->{
            if(mPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(v)).setImageResource(R.drawable.ic_baseline_remove_red_eye_24);

                //Show Password
                mPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                ((ImageView)(v)).setImageResource(R.drawable.ic_baseline_remove_red_eye_24);

                //Hide Password
                mPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        //View Conf. Pass Function
        btnShow2.setOnClickListener(v->{
            if(mConfPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(v)).setImageResource(R.drawable.ic_baseline_remove_red_eye_24);

                //Show Password
                mConfPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                mConfPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }

    //Save signup data to database
    private void signUp(String txemail, String txusername, String txpass){
        String email = mEmail.getText().toString().trim();
        String pass = mPass.getText().toString().trim();

        fbAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loading.show();
                    Toast.makeText(SignupActivity.this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();
                    SharedPreferences sp = getSharedPreferences("users", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("username", txusername);
                    editor.putString("email", txemail);
                    editor.putString("password", txpass);
                    editor.apply();
                    reload();
                }else{
                    Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Start Home Acvtivity from SignUp Activity
    private void reload(){
        startActivity(new Intent(SignupActivity.this, HomeActivity.class));
    }

    //Read user data from firebase authentication
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = fbAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    //Clear all field in signup form
    private void regisClear(){
        mEmail.getText().clear();
        mPass.getText().clear();
        mConfPass.getText().clear();
    }
}
