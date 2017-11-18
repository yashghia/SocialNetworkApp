package com.example.teja.homework07;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity{
    SignInButton signInButton;
    public static Button loginButton;
    EditText uName, passwordEdit;
    private GoogleSignInAccount mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;
    public static int REQ_CODE = 1000;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(i, REQ_CODE);
            }
        });
        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("Sign in with Google");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uName = (EditText) findViewById(R.id.username);
                passwordEdit = (EditText) findViewById(R.id.pWord);
                String emailName = uName.getText().toString().trim();
                String passwordText = passwordEdit.getText().toString().trim();
                mAuth.signInWithEmailAndPassword(emailName, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Signin Successful", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(MainActivity.this, PostsActivity.class);
                            startActivity(i);
                        }else {
                            Toast.makeText(MainActivity.this , "Please enter valid credentials", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    public void signUpPage(View view) {
        Intent i = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount newaccount = result.getSignInAccount();
                firebaseAuthwithGoogle(newaccount);
                Intent i = new Intent(MainActivity.this, PostsActivity.class);
                startActivity(i);
            }else {
                Toast.makeText(this, "Error please try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthwithGoogle(GoogleSignInAccount newaccount) {
        AuthCredential credential = GoogleAuthProvider.getCredential(newaccount.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    Log.d("The email","is"+user.getEmail());
                    Log.d("The name","is"+user.getDisplayName());
                }
            }
        });
    }

}
