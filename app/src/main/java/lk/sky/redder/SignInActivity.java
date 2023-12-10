package lk.sky.redder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
setContentView(R.layout.activity_sign_in);


        findViewById(R.id.signinSignUpbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        EditText email = findViewById(R.id.signInEmail);
        EditText password = findViewById(R.id.signInPassword);
        findViewById(R.id.signInbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//         Toast.makeText(SignInActivity.this, "Test Toast", Toast.LENGTH_SHORT).show();
                String stringEmail = email.getText().toString();
                String stringPassword = password.getText().toString();

                if (stringEmail.isEmpty()) {
                    email.setError("Please Enter your Email");
                    email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(stringEmail).matches()) {
                    email.setError("Please Enter Valid Email");
                    email.requestFocus();
                } else if (stringPassword.isEmpty()) {
                    password.setError("Please Enter your Password");
                    password.requestFocus();
                } else {
                    signInProcess(stringEmail, stringPassword);
                }
            }
        });
    }

    private void signInProcess(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if (currentUser.isEmailVerified()) {
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(SignInActivity.this, "Please Check your Inbox for the Verification Email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "Invalid Details", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignInActivity.this, "Invalid Sign In Details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}