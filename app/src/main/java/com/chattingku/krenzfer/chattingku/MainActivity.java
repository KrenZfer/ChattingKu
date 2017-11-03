package com.chattingku.krenzfer.chattingku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Toast.makeText(getApplicationContext(),
                    "Welcome "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + " MainActivity",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void authenticateUser(){
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivityForResult(
                    AuthUI
                            .getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .build(),
                    Constants.SIGN_IN_REQUEST_CODE
            );
        }
//        else{
//            Toast.makeText(getApplicationContext(),
//                    "Welcome "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
//                    Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.SIGN_IN_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "succesfully Signed In, Welcome!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
                finish();
            }
        }else{
            Toast.makeText(this, "We couldn't Sign You In, Please Try Again Later, MainActivity", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.loginBtn):
                authenticateUser();
                break;
        }
    }
}
