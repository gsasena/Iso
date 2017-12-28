package com.example.asus.iso;

/**
 * Created by Asus on 19/12/2017.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity{
    EditText username,password;

    private long lastPressedTime;
    private static final int PERIOD = 2000;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime             < PERIOD) { finish(); }else { Toast.makeText(getApplicationContext(), "Tekan 2 kali untuk keluar",Toast.LENGTH_SHORT).show(); lastPressedTime = event.getEventTime(); } return true; } } return false; } public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); setContentView(R.layout.activity_login); if (android.os.Build.VERSION.SDK_INT > 9) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


        username = (EditText) findViewById(R.id.text_username);
        password = (EditText) findViewById(R.id.text_pass);
        username.setText("admin");
        password.setText("admin");


    }

    public void Login (View v){
        if(username.getText().toString().isEmpty()){
            username.setError("Kosong");
        }else if(password.getText().toString().isEmpty()){
            password.setError("Kosong");
        }else{
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            ModelUser mdUser = db.getLogin(username.getText().toString(), password.getText().toString());

            if(mdUser.getIsLogin()){
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }else{
                Toast.makeText(LoginActivity.this, "Periksa Username atau Password",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void Batal (View v){
        finish();
    }
}
