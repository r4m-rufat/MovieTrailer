package com.example.movietrailer.activities.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movietrailer.R;
import com.example.movietrailer.models.authentication.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextInputEditText email, password, username;
    private Button signupButton;
    private Context context;
    private static final String TAG = "RegisterActivity";
    private String emailText, passwordText, usernameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;

        firebaseAuth = FirebaseAuth.getInstance();

        initializeWidgets();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                emailText = email.getText().toString().trim();
//                passwordText = password.getText().toString().trim();
//                usernameText = username.getText().toString().trim();
                showProgress();
//                createUser(firebaseAuth);
            }
        });

    }

    private void initializeWidgets() {

        username = findViewById(R.id.edit_username);
        password = findViewById(R.id.edit_password);
        email = findViewById(R.id.edit_email);
        signupButton = findViewById(R.id.button_signup);

    }

    private void createUser(FirebaseAuth auth) {

        auth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            sendEmailVerification();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void sendEmailVerification(){

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            firebaseUser.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                addNewUser(emailText, usernameText, passwordText);
                                Toast.makeText(context, "The email verification is sent your email", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(context, LoginActivity.class));
                            }else{
                                Toast.makeText(context, "couldn't send verification email", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }

    }

    private void addNewUser(String email, String username, String password){

        String uID = firebaseAuth.getCurrentUser().getUid();

        User user = new User(
                uID,
                email,
                password,
                username
        );

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .document(uID)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: User informations added to database");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Something went wrong");
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){

            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);

        }

    }

    private void showProgress(){

        KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait...")
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
                .show();

    }

}