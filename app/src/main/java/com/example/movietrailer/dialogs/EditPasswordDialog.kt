package com.example.movietrailer.dialogs

import android.content.Context
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.movietrailer.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

fun changePasswordBottomSheetDialog(
    context: Context,
    password: String,
    db: FirebaseFirestore,
    user: FirebaseUser,
    bottomSheetDialog: BottomSheetDialog,
){

    bottomSheetDialog.setContentView(R.layout.dialog_for_change_password)

    val currentPassword = bottomSheetDialog.findViewById<EditText>(R.id.edit_currentPassword)
    val newPassword = bottomSheetDialog.findViewById<EditText>(R.id.edit_newPassword)
    val confirmPassword = bottomSheetDialog.findViewById<EditText>(R.id.edit_confirmPassword)
    val saveButton = bottomSheetDialog.findViewById<Button>(R.id.savePassword)

    saveButton!!.setOnClickListener {

        if (currentPassword!!.text.trim().toString() != password && !TextUtils.isEmpty(currentPassword.text)){
            Toast.makeText(context, "current password is false", Toast.LENGTH_SHORT).show()
        }

        if (TextUtils.isEmpty(currentPassword.text)){
            currentPassword.error = "write a password"
        }

        if (TextUtils.isEmpty(newPassword!!.text)){
            newPassword.error = "write a password"
        }

        if (TextUtils.isEmpty(confirmPassword!!.text)){
            confirmPassword.error = "write a password"
        }

        if (newPassword.text.length < 6 ){
            newPassword.error = "password must be more than 6"
        }

        if (confirmPassword.text.length < 6 ){
            confirmPassword.error = "password must be more than 6"
        }

        if (newPassword.text.length >= 6
            && confirmPassword.text.length >= 6
            && !confirmPassword.text.equals(newPassword.text)
            && currentPassword.text.equals(password)){

            Toast.makeText(context, "passwords don't match", Toast.LENGTH_SHORT).show()

        }

        if (newPassword.text.length >= 6
            && confirmPassword.text.length >= 6
            && confirmPassword.text.trim().toString() == newPassword.text.trim().toString()
            && currentPassword.text.trim().toString() == password){

            var hashMap: HashMap<String, Any> = HashMap()
            hashMap["password"] = newPassword.text.trim().toString()
            db.collection("users")
                .document(user.uid)
                .update(hashMap)
                .addOnSuccessListener {
                    Toast.makeText(context, "Password successfully changed", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }

            bottomSheetDialog.dismiss()

        }

    }

    bottomSheetDialog.show()

}