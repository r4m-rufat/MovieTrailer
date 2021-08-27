package com.example.movietrailer.activities.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import com.example.movietrailer.R;
import com.example.movietrailer.activities.HomeActivity;
import com.example.movietrailer.models.authentication.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText username, password;
    private CircularProgressButton circularSignInButton;
    private String usernameValue;
    private String passwordValue = "";
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Context context;
    private TextView txt_signUp, forgot_password;
    private FirebaseFirestore db;
    private Button buttonSignIn;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 0;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;

        db = FirebaseFirestore.getInstance();

        initializeWidgets();
        circularSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTexts();
                clickedSignInButton();
            }
        });

        clickedForgotPassword();
        clickedSignUp();
        signInWithGoogle();
        clickedSignInWithGoogleButton();

    }

    private void clickedForgotPassword() {

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ResetActivity.class));
            }
        });

    }

    private void clickedSignUp() {

        txt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, RegisterActivity.class));
            }
        });

    }

    private void initializeWidgets() {

        username = findViewById(R.id.edit_username);
        password = findViewById(R.id.edit_password);
        circularSignInButton = findViewById(R.id.circular_button_sign_in);
        txt_signUp = findViewById(R.id.txt_signUp);
        buttonSignIn = findViewById(R.id.button_sign_in);
        forgot_password = findViewById(R.id.forgot_password);

    }

    private void getTexts() {

        passwordValue = password.getText().toString().trim();
        usernameValue = username.getText().toString().trim();

        if (TextUtils.isEmpty(usernameValue)) {
            username.setError("Write your email");
        } else {
            usernameValue = username.getText().toString().trim();
        }


    }

    private void clickedSignInButton() {

        if (passwordValue.length() >= 6 && !TextUtils.isEmpty(usernameValue)) {

            circularSignInButton.startAnimation();

            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();

            /**
             * checks username is email type or not if
             * it is email type then goes to database and is checked this username
             */
            if (!PatternsCompat.EMAIL_ADDRESS.matcher(usernameValue).matches()) {

                db.collection("users").whereEqualTo("username", usernameValue).whereEqualTo("password", passwordValue)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {

                                    String dbEmail = "";
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                        dbEmail = documentSnapshot.getString("email");
                                        Log.d(TAG, "onComplete: Email is " + dbEmail);
                                    }

                                    /**
                                     * maybe task is successful but there is no that username
                                     * in this situation email equals ""
                                     */
                                    if (!dbEmail.equals("")) {
                                        auth.signInWithEmailAndPassword(dbEmail, passwordValue)
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                                        try {
                                                            if (task.isSuccessful()) {
                                                                if (auth.getCurrentUser().isEmailVerified()) {
                                                                    Intent intent = new Intent(context, HomeActivity.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    startActivity(intent);
                                                                } else {
                                                                    Toast.makeText(context, "Email is not verified", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        } catch (NullPointerException e) {
                                                            Toast.makeText(context, "Authentication error. Please contact us", Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, "onComplete: Exception is " + e.getMessage());
                                                        }

                                                        circularSignInButton.revertAnimation();

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        Log.d(TAG, "onFailure: Authentication error. Reason is --- " + e.getMessage());
                                                        circularSignInButton.revertAnimation();
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(context, "Please write your account informations correctly.", Toast.LENGTH_SHORT).show();
                                        circularSignInButton.revertAnimation();
                                    }


                                } else {
                                    Toast.makeText(context, "Please write your account informations correctly.", Toast.LENGTH_SHORT).show();
                                    circularSignInButton.revertAnimation();
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Please write informations correctly.", Toast.LENGTH_SHORT).show();
                                circularSignInButton.revertAnimation();
                            }
                        });

            } else {

                /**
                 * otherwise username is email type and sign in
                 * account successfully
                 */
                auth.signInWithEmailAndPassword(usernameValue, passwordValue)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                FirebaseUser firebaseUser = auth.getCurrentUser();

                                try {
                                    if (firebaseUser.isEmailVerified()) {
                                        Intent intent = new Intent(context, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(context, "Email is not verified", Toast.LENGTH_SHORT).show();
                                    }

                                    circularSignInButton.revertAnimation();
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "There is no an account.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                circularSignInButton.revertAnimation();
                            }
                        });

            }

        }

    }

    private void signInWithGoogle() {

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("449450545795-nbmunjibppqqihd9f066habmif87bns0.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions);

    }

    private void signInIntent() {

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void clickedSignInWithGoogleButton() {

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInIntent();
            }
        });

    }

    private void handleSignInWithGoogleResult(Task<GoogleSignInAccount> googleSignInAccountTask) {

        try {
            GoogleSignInAccount account = googleSignInAccountTask.getResult(ApiException.class);
            Log.d(TAG, "handleSignInWithGoogleResult: Name: " + account.getDisplayName() + "\nEmail: " + account.getEmail());
            firebaseAuthWithGoogle(account);
            Toast.makeText(context, "successfully", Toast.LENGTH_SHORT).show();
        } catch (ApiException e) {
            e.printStackTrace();
            Log.d(TAG, "handleSignInWithGoogleResult: exception -> " + e.getMessage());
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            addNewUser(account.getEmail(), account.getDisplayName());
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {

            if (requestCode == RC_SIGN_IN && data != null) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInWithGoogleResult(task);
            }

        }

    }

    private void addNewUser(String email, String username) {

        String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        User user = new User(
                uID,
                email,
                "",
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
    }
}