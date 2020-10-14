package com.example.try2_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
TextView email,password;
FirebaseFirestore fstorel;
FirebaseAuth fauthl;
Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.button2);
        fstorel=FirebaseFirestore.getInstance();
        fauthl=FirebaseAuth.getInstance();



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fmail,fpassword;
                fmail=email.getText().toString();
                fpassword=password.getText().toString();


                fauthl.signInWithEmailAndPassword(fmail,fpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "login successfull", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(login.this,MainActivity.class);
                            startActivity(intent);



                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });





                Map<String,Object> obj1=new HashMap<>();
                String uid=fauthl.getUid();
                obj1.put("reviewerid",uid);
                obj1.put("name","manan");

                fstorel.collection("details").document(uid).set(obj1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Successfull",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed,Trynig again",Toast.LENGTH_SHORT).show();

                        //  fstore1.collection("details").document("id").set(obj1);

                    }
                });



            }
        });


    }
}