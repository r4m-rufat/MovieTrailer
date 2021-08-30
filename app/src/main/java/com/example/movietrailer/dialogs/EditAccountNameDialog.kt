package com.example.movietrailer.dialogs

import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.movietrailer.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

fun editAccountBottomSheetDialog(
    context: Context,
    previousUsername: String,
    db: FirebaseFirestore,
    user: FirebaseUser,
    bottomSheetDialog: BottomSheetDialog,
    onCLick: () -> Unit
) {

    bottomSheetDialog.setContentView(R.layout.dialog_for_edit_account_name)

    val editAccount = bottomSheetDialog.findViewById<EditText>(R.id.edit_accountName)
    val saveButton = bottomSheetDialog.findViewById<Button>(R.id.saveEditAccountName)

    editAccount!!.setText(previousUsername)

    saveButton!!.setOnClickListener {

        var hashMap: HashMap<String, Any> = HashMap()
        hashMap["username"] = editAccount.text.toString()
        db.collection("users")
            .document(user.uid)
            .update(hashMap)
            .addOnSuccessListener {
                Toast.makeText(context, "Username successfully changed", Toast.LENGTH_SHORT).show()
                onCLick ()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

        bottomSheetDialog.dismiss()

    }

    bottomSheetDialog.show()

}