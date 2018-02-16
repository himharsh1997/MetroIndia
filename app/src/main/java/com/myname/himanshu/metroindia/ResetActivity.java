package com.myname.himanshu.metroindia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by himanshu on 15/2/18.
 */

public class ResetActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn;
    EditText edt;
    private FirebaseAuth mAuth;
   public void onCreate(Bundle bu){
    super.onCreate(bu);
    setContentView(R.layout.reset_pass);

    edt = (EditText)findViewById(R.id.emailme);
    btn = (Button)findViewById(R.id.cli);
    mAuth  = FirebaseAuth.getInstance();
    btn.setOnClickListener(this);
}

    @Override
    public void onClick(View view) {
    String str = edt.getText().toString();

      if(TextUtils.isEmpty(str))
      {
          Toast.makeText(getApplicationContext(),"Enter your email",Toast.LENGTH_SHORT);
          return;
      }

      mAuth.sendPasswordResetEmail(str)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                   Toast.makeText(getApplicationContext(),"Check email to reset password",Toast.LENGTH_LONG);
                }
                else {Toast.makeText(getApplicationContext(),"Fail to send rest password email",Toast.LENGTH_LONG); }



            }
        });
        Intent inta =  new Intent(getApplicationContext(),MetroMActivity.class);

        startActivity(inta);

    }
}
