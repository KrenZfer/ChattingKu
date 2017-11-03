package com.chattingku.krenzfer.chattingku;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chattingku.krenzfer.chattingku.Model.ChatMessage;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    Button SendBtn, chooseImage;
    ListView listChat;
    private FirebaseListAdapter<ChatMessage> adapterChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SendBtn = (Button) findViewById(R.id.sendBtn);
        SendBtn.setOnClickListener(this);

        chooseImage = (Button) findViewById(R.id.chooseImage);
        chooseImage.setOnClickListener(this);

        listChat = (ListView) findViewById(R.id.listChat);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(ChatActivity.this, MainActivity.class);
            startActivity(intent);
        }else{
            displayMessage();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.sendBtn):
                EditText inputMessage = (EditText) findViewById(R.id.inputText);

                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(new ChatMessage(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                                inputMessage.getText().toString()
                                )
                        );
                inputMessage.setText("");
                break;
            case (R.id.chooseImage):
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, Constants.CHOOSE_IMAGE_ACTIVITY);
                break;
        }
    }

    public void displayMessage(){
        adapterChat = new FirebaseListAdapter<ChatMessage>(this,
                ChatMessage.class,
                R.layout.message,
                FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView username = (TextView) v.findViewById(R.id.username);
                TextView messagetText = (TextView) v.findViewById(R.id.messageText);
                TextView dateTime = (TextView) v.findViewById(R.id.dateTime);
                if(username==null){
                    Toast.makeText(ChatActivity.this, "username is null", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d("Model", "USERNAME : "+model.getUsername());
                Log.d("Model", "MESSAGE TEXT : "+model.getMessageText());
                Log.d("Model", "DATE : "+new SimpleDateFormat("dd-MM-yyyy (HH:mm:ss)").format(new Date(model.getMessageTime())));
                username.setText(model.getUsername());
                messagetText.setText(model.getMessageText());
                dateTime.setText(new SimpleDateFormat("dd-MM-yyyy (HH:mm:ss)").format(new Date(model.getMessageTime())));
            }
        };
        listChat.setAdapter(adapterChat);
    }

    public void displayMessage(String imagePath){

    }

    public String loadImage(Intent data){
        Uri selectedImage = data.getData();
        String filepathColumn[] = {MediaStore.Images.Media.DATA};
        String imagePath = "";
        Cursor cursor = getContentResolver().query(selectedImage, filepathColumn, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
            int ColumnIndex = cursor.getColumnIndex(filepathColumn[0]);
            imagePath = cursor.getString(ColumnIndex);
            cursor.close();
        }else{

        }
        return imagePath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if(requestCode == Constants.SIGN_IN_REQUEST_CODE){
//            if(resultCode == RESULT_OK){
//                Toast.makeText(this, "succesfully Signed In, Welcome!", Toast.LENGTH_LONG).show();
//                displayMessage();
//            }
//        }else{
//            Toast.makeText(this, "We couldn't Sign You In, Please Try Again Later ChatActivity", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(ChatActivity.this, MainActivity.class);
//            startActivity(intent);
//        }

        if(requestCode == Constants.CHOOSE_IMAGE_ACTIVITY){
            if(resultCode == RESULT_OK && data != null){
                Toast.makeText(this, "succesfully Load Image!", Toast.LENGTH_LONG).show();
                displayMessage(loadImage(data));
            }
        }else{
            Toast.makeText(this, "Error Load Image, Please Try Again Later", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out){
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ChatActivity.this, "You Have been Sign Out", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        }
        return true;
    }
}