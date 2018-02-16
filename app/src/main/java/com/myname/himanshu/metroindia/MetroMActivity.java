package com.myname.himanshu.metroindia;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MetroMActivity extends TabActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;
    public static final String TAG = MetroMActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_m);
        TabHost tabhost= getTabHost();
        Resources res = getResources();
        Intent intent= new Intent(MetroMActivity.this,SignActivity.class);

        TabHost.TabSpec spec;
        spec= tabhost.newTabSpec("firstsign");
        spec.setIndicator("Signup",res.getDrawable(R.drawable.signup));
        spec.setContent(intent);
        tabhost.addTab(spec);

        Intent intent2= new Intent(MetroMActivity.this,LoginActivity.class);

        spec= tabhost.newTabSpec("secondlogin");
        spec.setIndicator("Login",res.getDrawable(R.drawable.signin));
        spec.setContent(intent2);
        tabhost.addTab(spec);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent inew = new Intent(MetroMActivity.this,UserMetroActivity.class);
                     startActivity(inew);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Toast.makeText(getApplicationContext(),"Welcome new user",Toast.LENGTH_SHORT);
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser!=null){
        String email = currentUser.getEmail().toString();
        Intent im = new Intent(MetroMActivity.this,UserMetroActivity.class);
         Bundle bun = new Bundle();
          bun.putString("email",email);
          im.putExtras(bun);
        startActivity(im);}

    }


}

