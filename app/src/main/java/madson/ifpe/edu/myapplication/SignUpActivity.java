package madson.ifpe.edu.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText edEmail, edPassword, edNome;
    private Button btnCadastrar;
    private FirebaseAuth fbAuth;
    private FirebaseAuthListener authListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edEmail = findViewById(R.id.edit_email);
        edPassword = findViewById(R.id.edit_password);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        edNome = findViewById(R.id.edit_name);

        this.fbAuth = FirebaseAuth.getInstance();
        this.authListener = new FirebaseAuthListener(this);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSignUpClick(v);
            }
        });

        this.fbAuth = FirebaseAuth.getInstance();
        this.authListener = new FirebaseAuthListener(this);
    }

    public void buttonSignUpClick(View view) {
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    String msg = task.isSuccessful() ? "SIGN UP OK!" :
                            "SIGN UP ERROR!";
                    Toast.makeText(SignUpActivity.this, msg,
                            Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        fbAuth.addAuthStateListener(authListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        fbAuth.removeAuthStateListener(authListener);
    }
}