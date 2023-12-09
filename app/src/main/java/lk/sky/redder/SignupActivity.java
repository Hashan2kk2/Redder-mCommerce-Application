package lk.sky.redder;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import lk.sky.redder.model.User;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fireStoreDatabase;

    private SignInClient signInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sign_up);

        fireStoreDatabase = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        EditText fName = findViewById(R.id.fName);
        EditText lName = findViewById(R.id.lName);

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);

        findViewById(R.id.signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, SignInActivity.class));
            }
        });


        signInClient = Identity.getSignInClient(getApplicationContext());
        findViewById(R.id.signUpWithGoogleBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInWithGoogle();
            }
        });

        findViewById(R.id.signInBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fnameValue = fName.getText().toString();
                String lnameValue = lName.getText().toString();
                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();

                if (fnameValue.isEmpty()) {
                    fName.setError("Please Enter your First Name");
                    fName.requestFocus();
                } else if (lnameValue.isEmpty()) {
                    lName.setError("Please Enter your Last Name");
                    lName.requestFocus();
                } else if (emailValue.isEmpty()) {
                    email.setError("Please Enter your Email Address");
                    email.requestFocus();
                } else if (passwordValue.isEmpty()) {
                    password.setError("Please Enter your Password");
                    password.requestFocus();
                } else {
                    signUpProcess(fnameValue, lnameValue, emailValue, passwordValue);
                }

//                Toast.makeText(SignupActivity.this, "test", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void signUpProcess(String fname, String lname, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    User user = new User();
                    user.setFname(fname);
                    user.setLname(lname);

                    user.setEmail(currentUser.getEmail());

//                    user.setEmail(email);
//                    user.setPassword(password);
                    currentUser.sendEmailVerification();
                    Toast.makeText(SignupActivity.this, "Email Sent Successfully.", Toast.LENGTH_SHORT).show();

                    fireStoreDatabase.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            startActivity(new Intent(SignupActivity.this, SignInActivity.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignupActivity.this, "Error! Please Try Again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(SignupActivity.this, "Already Registered on this Account", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SignInWithGoogle() {
        GetSignInIntentRequest signInIntentRequest = GetSignInIntentRequest.builder().setServerClientId(getString(R.string.web_client_id)).build();

        Task<PendingIntent> signInIntent = signInClient.getSignInIntent(signInIntentRequest);
        signInIntent.addOnSuccessListener(new OnSuccessListener<PendingIntent>() {
            @Override
            public void onSuccess(PendingIntent pendingIntent) {
                IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(pendingIntent).build();
                signInLauncher.launch(intentSenderRequest);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final ActivityResultLauncher<IntentSenderRequest> signInLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            handleSignInResult(result.getData());
        }
    });

    private void handleSignInResult(Intent intent) {
        try {

            SignInCredential signInCredential = signInClient.getSignInCredentialFromIntent(intent);
            String idToken = signInCredential.getGoogleIdToken();
            firebaseAuthWithGoogle(idToken);

        } catch (ApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken, null);
        Task<AuthResult> authResultTask = firebaseAuth.signInWithCredential(authCredential);
        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this, "Error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}