package com.myname.himanshu.metroindia;

import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by himanshu on 10/2/18.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Resources res;
    ImageView imgvg,imgfa;

     ProgressBar progress;
    EditText email,password;
    TextView txt;
    private FirebaseAuth mAuth;
    private static final String TAG1 = "GoogleActivity";
    private CallbackManager mCallbackManager;
    public static final int RC_SIGN_IN = 9001;

    /* Data from the authenticated user */
  /* A reference to the Firebase */

    private FirebaseAuth.AuthStateListener mAuthListener;

    public static final String TAG = LoginActivity.class.getSimpleName();
    Button butt;
    private GoogleSignInClient mGoogleSignInClient;
    public void onCreate(Bundle bin)
    {

           super.onCreate(bin);

           setContentView(R.layout.login);

           res = getResources();

           email = (EditText) findViewById(R.id.usernameel);
           password = (EditText) findViewById(R.id.passwordel);
           txt = (TextView) findViewById(R.id.textforgetl);
           imgvg = (ImageView) findViewById(R.id.googlel);

           progress = (ProgressBar) findViewById(R.id.progressBar);
           butt = (Button) findViewById(R.id.clicklog);
           GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                   .requestIdToken(getString(R.string.default_web_client_id))
                   .requestEmail()
                   .build();

           mCallbackManager = CallbackManager.Factory.create();

           mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
           mAuth = FirebaseAuth.getInstance();
           txt.setOnClickListener(this);
           butt.setOnClickListener(this);

           imgvg.setOnClickListener(this);



       }






    // [END auth_with_facebook

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.clicklog:
                signIn(email.getText().toString(), password.getText().toString());
                break;
            case R.id.textforgetl:
                sendEmailVerifications();
                break;
            case R.id.googlel:
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

    @Override
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

    private void googlelogAuth() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

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
                            updateUI(user);
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






    private void sendEmailVerifications() {

        Intent inta = new Intent(LoginActivity.this,ResetActivity.class);
        startActivity(inta);
    }

    public void signIn(String email, String password) {

            Log.d(TAG, "signIn:" + email);
            if (!validateForm()) {
              return;
            }

            progress.setVisibility(View.VISIBLE);
        // [START sign_in_with_email]
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(LoginActivity.this, "Sigin successfully.",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();

                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // [START_EXCLUDE]

                            progress.setVisibility(View.GONE);
                            // [END_EXCLUDE]
                        }


                    });
            // [END sign_in_with_email]

        }


    private void updateUI(FirebaseUser user) {
        progress.setVisibility(View.GONE);
        if(user!=null)
         {
            Intent innewuser = new Intent(LoginActivity.this,UserMetroActivity.class);
            Bundle bun = new Bundle();
            bun.putString("email",user.getEmail()+"");
            innewuser.putExtras(bun);
            startActivity(innewuser);
            bun = null;
          }
          else{Toast.makeText(LoginActivity.this, " Authentication failed.",
                Toast.LENGTH_SHORT).show();}
       }

    private boolean validateForm() {

        boolean valid = true;

        String emails = email.getText().toString();


        String passwords = password.getText().toString();


        if (TextUtils.isEmpty(emails)) {
            email.setError("Required");
            valid = false;
        }
        else {
            email.setError(null);
        }

        if (TextUtils.isEmpty(passwords))
        {
            password.setError("Required.");
            valid = false;
        }
        else {
            password.setError(null);
        }
           return valid;

        }



}






