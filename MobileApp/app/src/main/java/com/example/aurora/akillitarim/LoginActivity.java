package com.example.aurora.akillitarim;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


/**
 * @author Sinan Elveren, Gebze Technical University
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    EditText loginEmail, loginPassword;
    Button loginButton, registerButton, newPassButton;
    private static final int RC_SIGN_IN = 9001;
    private SignInButton signInButton;

    FirebaseAuth firebaseAuth;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        loginEmail = (EditText) findViewById(R.id.girisEmail);
        loginPassword = (EditText) findViewById(R.id.girisParola);
        loginButton = (Button) findViewById(R.id.girisButton);
        registerButton = (Button) findViewById(R.id.uyeOlButton);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        newPassButton = (Button) findViewById(R.id.yeniSifreButton);


        firebaseAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Bağlanıyor",Toast.LENGTH_LONG).show();
                signIn();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Login",Toast.LENGTH_LONG);

                final String mail = loginEmail.getText().toString();
                final String pass = loginPassword.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(mail, pass)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("LOGINBUTTON", "signInWithEmail:success");

                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("LOGINBUTTON", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Hatalı giriş", Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });
            }

        });

/*
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Bağlanıyor: giriş yap",Toast.LENGTH_LONG).show();


                firebaseAuth.signinWithEmailAndPassword("test", "test2");
             //   startActivityForResult(signIntent,RC_SIGN_IN);
            }
        });
*/
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Bağlanıyor: kayıt ol",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                finish();
                //go back;
            }
        });

        newPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Şifremi unuttum",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),NewPassActivity.class));
                finish();
                //signIn();
            }
        });

        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

    }

    private void signIn() {
        Toast.makeText(getApplicationContext()," >> sign in",Toast.LENGTH_LONG).show();

        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getApplicationContext(),"onActiveResult",Toast.LENGTH_LONG).show();

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                Toast.makeText(getApplicationContext(),"Signed succesfully",Toast.LENGTH_SHORT).show();
                GoogleSignInAccount account = result.getSignInAccount();
                authWithGoogle(account);
            }else {
                Toast.makeText(getApplicationContext(),"Sign unsucces",Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void authWithGoogle(GoogleSignInAccount account) {
        Toast.makeText(getApplicationContext(),"authWithGoogle",Toast.LENGTH_LONG).show();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(getApplicationContext(),"Auth g",Toast.LENGTH_SHORT).show();

                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"succes TASK",Toast.LENGTH_LONG).show();

                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Auth Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),"connectio failed",Toast.LENGTH_LONG).show();

        System.out.println("Connection failed");
    }
}