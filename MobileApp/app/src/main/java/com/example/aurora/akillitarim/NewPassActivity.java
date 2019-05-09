package com.example.aurora.akillitarim;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @author Sinan Elveren, Gebze Technical University
 */
public class NewPassActivity  extends AppCompatActivity {
    EditText email;
    Button btnNewPass;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO : fix main_activitiy to new_Pass_activity
        setContentView(R.layout.password_activity);
        email = (EditText) findViewById(R.id.uyeEmail);
        btnNewPass = (Button) findViewById(R.id.yeniParolaGonder);

        firebaseAuth = FirebaseAuth.getInstance();

        btnNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText)findViewById(R.id.uyeEmail)).toString();
            //    String email = (EditText) email.getText().toString();
            //    String email;
            //    email = ((EditText)findViewById(R.id.uyeEmail).getText()).toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Lutfen e-postanızı giriniz",Toast.LENGTH_SHORT).show();
                    return;
                }

                //TODO: check reset password
                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Yeni parola e-posta adresinize gönderildi",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"e-posta gönderilemedi",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }
}
