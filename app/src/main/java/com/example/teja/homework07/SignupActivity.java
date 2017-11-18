package com.example.teja.homework07;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    public DatePickerDialog fromDatePickerDialog;
    private DatabaseReference dataref;
    EditText firstName, lastName, email, birthdayText, passwordData, conPass;
    private FirebaseAuth mAuth;
    public int ageCalculation;
    private GoogleApiClient mGoogleApiClient;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        firstName = (EditText) findViewById(R.id.fName);
        firstName.setFocusedByDefault(true);
        lastName = (EditText) findViewById(R.id.lastname);
        email = (EditText) findViewById(R.id.emailID);
        passwordData = (EditText) findViewById(R.id.pWord);
        conPass = (EditText) findViewById(R.id.confirmpWord);
        birthdayText = (EditText) findViewById(R.id.date);
        birthdayText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    setDateFields();
                } else {

                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setDateFields(){
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                ageCalculation = Calendar.getInstance().get(Calendar.YEAR) - year;
                birthdayText.setText(monthOfYear+"/"+dayOfMonth+"/"+year);
                birthdayText.clearFocus();
                passwordData.requestFocus();

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }
    public void registerUser(View view) {
       // mAuth = FirebaseAuth.getInstance();
//        mAuth.signOut();
//        Intent i = new Intent(SignupActivity.this, MainActivity.class);
//        startActivity(i);
        final String emailText = email.getText().toString().trim();
        final String passwordText = passwordData.getText().toString().trim();
        final String conpasswordText = conPass.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        mAuth = FirebaseAuth.getInstance();
        if (emailText.matches(emailPattern) && passwordText.equals(conpasswordText) && !birthdayText.equals("") && ageCalculation > 13 && !passwordText.equals("") && !firstName.equals("") && !lastName.equals("")) {
            createUser(emailText, passwordText);
        } else {
            Toast.makeText(SignupActivity.this, "Make sure Email ID is valid, password texts are same and age is above 13", Toast.LENGTH_LONG).show();
        }
    }
    public void createUser(final String emailText1, String conpasswordText1){
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(emailText1, conpasswordText1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User newUser = new User();
                    newUser.setFirstName(firstName.getText().toString().trim());
                    newUser.setLastName(lastName.getText().toString().trim());
                    newUser.setEmailId(emailText1);
                    newUser.setDateofBirth(birthdayText.getText().toString().trim());
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser() ;
                    newUser.setUserId(currentUser.getUid().toString());
                    //Add data
                    dataref = FirebaseDatabase.getInstance().getReference().child("Users");
                    dataref.push().setValue(newUser);
                    Toast.makeText(SignupActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(SignupActivity.this, PostsActivity.class);
                    startActivity(i);
                } else {
                    Log.d("The exception", "is" + task.getException());
                    Toast.makeText(SignupActivity.this, "Failed to register the user", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
