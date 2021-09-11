package com.example.movietrailer.activities.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import com.example.movietrailer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;

public class ResetActivity extends AppCompatActivity {

    private EditText editEmail, editNewPassword, editConfirmPassword;
    private Button nextButton, resetButton;
    private ImageView ic_back;
    private Context context;
    private FirebaseFirestore db;
    private KProgressHUD progressDialog;
    private String uID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        context = ResetActivity.this;
        db = FirebaseFirestore.getInstance();
        initializeWidgets();
        setupProgress();
        clickedNextButton();
        clickedResetButton();
        clickedIcBack();

    }

    private void initializeWidgets() {

        editEmail = findViewById(R.id.edit_email);
        editNewPassword = findViewById(R.id.edit_new_password);
        editConfirmPassword = findViewById(R.id.edit_confirm_password);
        nextButton = findViewById(R.id.button_next);
        resetButton = findViewById(R.id.button_reset);
        ic_back = findViewById(R.id.ic_backFromReset);

    }
    
    private void clickedResetButton(){
        
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonEvent();
            }
        });
        
    }

    private void clickedIcBack(){
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LoginActivity.class));
                finish();
            }
        });
    }

    private void clickedNextButton(){

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailField();
            }
        });

    }

    private void checkEmailField(){

        String getEmail = editEmail.getText().toString().trim();

        if (!TextUtils.isEmpty(getEmail)){
            if (isValidEmail()){
                progressDialog.show();
                db.collection("users").whereEqualTo("email", getEmail)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    String password = null;
                                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                        uID = documentSnapshot.getString("uID");
                                        password = documentSnapshot.getString("password");
                                    }

                                    if (password != null){
                                        if (password.equals("")) {
                                            Toast.makeText(context, "You can't change password. Because you signed in with Google", Toast.LENGTH_LONG).show();
                                        }else {
                                            editEmail.setVisibility(View.GONE);
                                            nextButton.setVisibility(View.GONE);
                                            editNewPassword.setVisibility(View.VISIBLE);
                                            editConfirmPassword.setVisibility(View.VISIBLE);
                                            resetButton.setVisibility(View.VISIBLE);
                                        }
                                    }else{
                                        Toast.makeText(context, "There is not account with this email", Toast.LENGTH_LONG).show();
                                    }

                                    progressDialog.dismiss(); // when gmail is found in database then progress dialog is dismiss

                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "There is not account with this email", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });

            }else{
                editEmail.setError("It is not valid email");
            }
        }else{
            editEmail.setError("Please enter your email");
        }

    }
    
    private void resetButtonEvent(){
        String newPasswordValue = editNewPassword.getText().toString().trim();
        String confirmPasswordValue = editConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(newPasswordValue) && TextUtils.isEmpty(confirmPasswordValue)){
            editNewPassword.setError("Please enter a password");
            editConfirmPassword.setError("Please enter a password");
        }else if (TextUtils.isEmpty(newPasswordValue) && !TextUtils.isEmpty(confirmPasswordValue)){
            editNewPassword.setError("Please enter a password");
        }else if (!TextUtils.isEmpty(newPasswordValue) && TextUtils.isEmpty(confirmPasswordValue)){
            editConfirmPassword.setError("Please enter a password");
        }else{
            if (newPasswordValue.equals(confirmPasswordValue)){
                setNewPassword(newPasswordValue);
            }else{
                Toast.makeText(context, "Passwords don't match.", Toast.LENGTH_SHORT).show();
            }
        }
        
    }

    private void setNewPassword(String newPassword){


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("password", newPassword);

        db.collection("users").document(uID).update(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "changed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isValidEmail(){

        return PatternsCompat.EMAIL_ADDRESS.matcher(editEmail.getText().toString().trim()).matches();

    }

    private void setupProgress(){

        progressDialog = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Checking your email. Please wait...")
                .setCancellable(true)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);

    }

}