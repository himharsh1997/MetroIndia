package com.myname.himanshu.metroindia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
/**
 * Created by himanshu on 15/2/18.
 */

public class GAOActivity extends AppCompatActivity  implements View.OnClickListener{
    Button btn;
    Intent imm;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.user_exist);
        mAuth = FirebaseAuth.getInstance();

        imm = getIntent();
        Bundle bun= imm.getExtras();
        TextView tv = (TextView)findViewById(R.id.name);
        tv.setText("Hello   "+bun.getString("email").toString());
        btn = (Button)findViewById(R.id.logout);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Button btn = (Button)findViewById(R.id.logout);
        btn.setOnClickListener(this);

    }
    public void onClick(View vv){
        signOut();
    }
    public void signOut(){
    mAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });


    }
    void updateUI(FirebaseUser user){

        if(user == null)
        {
            Intent innn = new Intent(GAOActivity.this,MetroMActivity.class);
            //FirebaseAuth.getInstance().signOut();
            startActivity(innn);
        }

    }
}
