package com.example.googlesignin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private static final String TAG = "Register";
    EditText n,p,e,a;
    Button signUp,submit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        n=findViewById(R.id.name);
        p=findViewById(R.id.pass);
        a=findViewById(R.id.age);
        e=findViewById(R.id.mail);
        signUp=findViewById(R.id.signup);
        submit=findViewById(R.id.button3);
        mAuth = FirebaseAuth.getInstance();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEmpty();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
    }
    public void isEmpty(){
        String name,pass,age,mail;
        name=  n.getText().toString();
        pass=p.getText().toString();
        age=a.getText().toString();
        mail=e.getText().toString();

        if(name.isEmpty()||pass.isEmpty()||age.isEmpty()||mail.isEmpty()){
            Toast.makeText(this, "Every Field is compulsory", Toast.LENGTH_SHORT).show();
            return;
        }
        signUp(mail,pass);
    }
    private void signUp(String email,String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Register.this, "Registered", Toast.LENGTH_SHORT).show();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });

    }
    public void addData(){
        String name,pass,age,mail;
        name=  n.getText().toString();
        pass=p.getText().toString();
        age=a.getText().toString();
        mail=e.getText().toString();

        if(name.isEmpty()||pass.isEmpty()||age.isEmpty()||mail.isEmpty()){
            Toast.makeText(this, "Every Field is compulsory", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> user = new HashMap<>();
        user.put("Name", name);
        user.put("Email", mail);
        user.put("Age", age);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Register.this, "DocumentSnapshot added with ID: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Error adding document", Toast.LENGTH_SHORT).show();
                        //Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    private void updateUI(FirebaseUser user) {
        finish();
    }

}