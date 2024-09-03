package id.ac.polman.astra.kel10.catetinaja.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

import id.ac.polman.astra.kel10.catetinaja.R;


public class LoginActivity extends AppCompatActivity {
    private EditText emailLog, passLog;
    private Button btnLog;
    private TextView btnReg;
    private ProgressDialog loading;
    private FirebaseAuth mAuth;
    private ImageView btnShow;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize
        emailLog = (EditText) findViewById(R.id.emailLog);
        passLog = (EditText) findViewById(R.id.logPassword);
        btnLog = (Button)  findViewById(R.id.login_button);
        btnReg = (TextView) findViewById(R.id.register_button);
        btnShow = (ImageView) findViewById(R.id.show_pass_btn);

        mAuth = FirebaseAuth.getInstance();

        //Loading
        loading = new ProgressDialog(LoginActivity.this);
        loading.setTitle("Loading");
        loading.setMessage("Mencoba masuk...");

        //Login Button Function
        btnLog.setOnClickListener(v -> {
            if(emailLog.getText().length()>0 && passLog.getText().length()>0){
                login(emailLog.getText().toString(), passLog.getText().toString());
            }else{
                Toast.makeText(this, "Isi semua data!!", Toast.LENGTH_SHORT).show();
            }
        });

        //SignUP Button Function
        btnReg.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

        //View pass button
        btnShow.setOnClickListener(v ->{
            if(passLog.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(v)).setImageResource(R.drawable.ic_baseline_remove_red_eye_24);

                //Show Password
                passLog.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                ((ImageView)(v)).setImageResource(R.drawable.ic_baseline_remove_red_eye_24);

                //Hide Password
                passLog.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }

    //Login Authentication
    private void login(String emailLog, String passLog){
        mAuth.signInWithEmailAndPassword(emailLog, passLog).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() && task.getResult()!=null){
                    if(task.getResult().getUser()!=null){
                        Toast.makeText(LoginActivity.this, "Berhasil Masuk!!", Toast.LENGTH_SHORT).show();
                        SharedPreferences sp = getSharedPreferences("users", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("email", emailLog);
                        editor.putString("password", passLog);
                        editor.apply();
                        reload();
                    }else{
                        Toast.makeText(LoginActivity.this, "Proses Masuk Gagal!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Proses Masuk Gagal!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Start new Activity
    private void reload(){
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
    }

    //Read user data from firebase
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    //Clear all field in login form
    private void loginClear(){
        emailLog.getText().clear();
        passLog.getText().clear();
    }
}
