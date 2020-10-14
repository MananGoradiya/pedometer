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

public class regestration extends AppCompatActivity {

   TextView phno,email,password,name;
   FirebaseFirestore fstorer;
   FirebaseAuth fauthr;
   Button register,login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regestration);
        phno=findViewById(R.id.phno);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        name=findViewById(R.id.name);
        fauthr=FirebaseAuth.getInstance();
        fstorer=FirebaseFirestore.getInstance();
        register=findViewById(R.id.button);
        login=findViewById(R.id.button3);




        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fmail,fpassword,fname,fphno;

                fmail=email.getText().toString();
                fpassword=password.getText().toString();
                fname=name.getText().toString();
                fphno=phno.getText().toString();
                fauthr.createUserWithEmailAndPassword(fmail,fpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            Map<String,Object> objr=new HashMap<>();
                            String uid=fauthr.getUid();
                            objr.put("User-id",uid);
                            objr.put("Name",fname);
                            objr.put("phone no",fphno);
                            objr.put("E-mail",fmail);
                            objr.put("password",fpassword);



                            fstorer.collection("Users").document(uid).set(objr).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"Successfull_user created",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"unable to create user",Toast.LENGTH_SHORT).show();

                                    //  fstore1.collection("details").document("id").set(obj1);

                                }
                            });



                            Toast.makeText(getApplicationContext(),"user created",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


login.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(regestration.this,login.class);
        startActivity(intent);
    }
});

    }
}