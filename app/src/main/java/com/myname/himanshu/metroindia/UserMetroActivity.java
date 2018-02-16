package com.myname.himanshu.metroindia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by himanshu on 13/2/18.
 */

public class UserMetroActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.user_exist);
        mAuth = FirebaseAuth.getInstance();
        Intent imm = getIntent();
        Bundle bun= imm.getExtras();
        TextView tv = (TextView)findViewById(R.id.name);
        tv.setText("Hello 8 -) "+bun.getString("email").toString());
        btn = (Button)findViewById(R.id.logout);
        btn.setOnClickListener(this);

    }
   public void onClick(View vv){
       signOut();
   }
    public void signOut(){
        mAuth.signOut();
        updateUI(null);
    }
    void updateUI(FirebaseUser user){

        if(user == null)
        {
            Intent innn = new Intent(UserMetroActivity.this,MetroMActivity.class);
            //FirebaseAuth.getInstance().signOut();
            startActivity(innn);
        }

    }

}
