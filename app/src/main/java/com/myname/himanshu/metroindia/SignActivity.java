package com.myname.himanshu.metroindia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by himanshu on 10/2/18.
 */

public class SignActivity extends AppCompatActivity  implements View.OnClickListener{



    private FirebaseAuth.AuthStateListener mAuthListener;
    public static final String TAG = SignActivity.class.getSimpleName();
    public EditText emails,passwords;
    Button  signinme;
    ProgressBar progress;
    ImageView imggo;
    private FirebaseAuth mAuth;
    private static final String TAG1 = "GoogleActivity";
    private CallbackManager mCallbackManager;
    public static final int RC_SIGN_IN = 9001;
    public static final Pattern EMAIL =
            Pattern.compile("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$", Pattern.CASE_INSENSITIVE);
    public static final Pattern PASSWORD = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",Pattern.CASE_INSENSITIVE);


    private GoogleSignInClient mGoogleSignInClient;
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.signup);
                      // facebook auth picture take
        emails = (EditText)findViewById(R.id.usernamees);             // Email edittext  take
        passwords = (EditText)findViewById(R.id.passwordes);          // Password edittext take
        signinme = (Button)findViewById(R.id.clicksign);
        progress = (ProgressBar)findViewById(R.id.progressBar);

        imggo = (ImageView)findViewById(R.id.googles);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mCallbackManager = CallbackManager.Factory.create();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

         imggo.setOnClickListener(this);

         signinme.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.clicksign:
                createAccount(emails.getText().toString(),passwords.getText().toString());
              break;
            case R.id.googles:
                mGoogleSignInClient.signOut().addOnCompleteListener(this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                googlelogAuth();
            break;

        }


    }

    private void googlelogAuth() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account  = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                Toast.makeText(getApplicationContext(),"Authentication failed",Toast.LENGTH_LONG);
                // [END_EXCLUDE]
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        progress.setVisibility(View.VISIBLE);
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI2(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"Authentication failed",Toast.LENGTH_LONG);

                        }

                        // [START_EXCLUDE]
                        progress.setVisibility(View.GONE);
                        // [END_EXCLUDE]
                    }
                });
    }
    private void createAccount(String ema, String pass) {
        Log.d(TAG, "createAccount:" + ema);
        if (!validateForm())
        {
            return;
        }
        progress.setVisibility(View.VISIBLE);

        // [START create_user_with_email]
         mAuth.createUserWithEmailAndPassword(ema, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            mAuth = null;
                            Toast.makeText(SignActivity.this, "Account created.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        progress.setVisibility(View.GONE);
                        // [END_EXCLUDE]
                    }


                });


    }


    // [END create_user_with_email]
    private void updateUI(FirebaseUser user) {
        progress.setVisibility(View.GONE);
      if(user!=null)
      { mAuth = null;
      Toast.makeText(getApplicationContext(),"Account is succesfully formed",Toast.LENGTH_LONG);
      }
      else{Toast.makeText(getApplicationContext(),"Account not created",Toast.LENGTH_LONG);}


    }
    private void updateUI2(FirebaseUser user) {
        progress.setVisibility(View.GONE);
        if(user!=null)
        { mAuth = null;
            Intent innewuser = new Intent(SignActivity.this,UserMetroActivity.class);
            Bundle bun = new Bundle();
            bun.putString("email",user.getEmail()+"");
            innewuser.putExtras(bun);
            startActivity(innewuser);
            bun = null;
        }
        else{Toast.makeText(getApplicationContext(),"Account not created",Toast.LENGTH_LONG);}


    }

    private boolean validateForm() {
        Matcher matcheremail = EMAIL.matcher(emails.getText().toString());
        Matcher matcherpassword = PASSWORD.matcher(passwords.getText().toString());
                boolean valid = true;
        String email = emails.getText().toString();
        String password = passwords.getText().toString();
                if(matcheremail.find()==false)
                 {
                   emails.setError("Wrong format");
                     valid = false;
                 }
                 else{emails.setError(null);}

                 if(matcherpassword.find()==false)
                 {passwords.setError("Wrong format");
                  valid =false;
                 }
                 else{passwords.setError(null);}


                if (TextUtils.isEmpty(email)) {
                   emails.setError("Required.");
                   valid = false;
                }
                else {
                      emails.setError(null);
                     }


                if (TextUtils.isEmpty(password))
                {
                  passwords.setError("Required.");
                  valid = false;
                }
                else {
                       passwords.setError(null);
                     }

                return valid;

                 }

    }
