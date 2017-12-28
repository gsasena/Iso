package com.example.asus.iso;

/**
 * Created by Asus on 19/12/2017.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class ManagUserActivity extends Activity{
    EditText nama,username,password,email; //declarasi EditText
    Spinner tipeUser;String mode="",idUser="0";
    Button btnSimpan;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                    return true;
            }
        }
        return false;
    }
    private static  String EMAIL_REGEX = "^[\\w+]*[\\w]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manag_user);
        nama =(EditText) findViewById(R.id.txtNama);
        username =(EditText) findViewById(R.id.txtUsername);
        password =(EditText) findViewById(R.id.txtPassword);
        email =(EditText) findViewById(R.id.txtEmail);
        tipeUser =(Spinner) findViewById(R.id.spinner1);
        btnSimpan=(Button) findViewById(R.id.btnSimpan);
        //Cek mode hasil parameter yang dikirim oleh class MainActivity.java
        Intent in = this.getIntent();
        mode= in.getStringExtra("mode");
        if(mode.equals("Edit")){
            idUser =  in.getStringExtra("id_user");
            if(idUser.equals("1")){
                username.setEnabled(false);
                password.setEnabled(false);
            }else{
                username.setEnabled(true);
                password.setEnabled(true);
            }
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            ModelUser dataUser = db.getUserById(idUser);
            nama.setText(dataUser.getNama());
            username.setText(dataUser.getUsername());
            password.setText(dataUser.getPassword());
            email.setText(dataUser.getEmail());
            tipeUser.setSelection(getSelection(dataUser.getTipeUser()));
        }
    }

    // handle tombol Simpan. pada XML layout tombol sudah di beri properti onClick dan di beri nama "Simpan"

    public View Simpan(View v){
        if(nama.getText().toString().isEmpty()){
            nama.setError("Form Kosong");
        }else if(username.getText().toString().isEmpty()){
            username.setError("Form Kosong");
        }else if(password.getText().toString().isEmpty()){
            password.setError("Form Kosong");
        }else if(email.getText().toString().isEmpty()){
            email.setError("Form Kosong");
        }else if(!email.getText().toString().matches(EMAIL_REGEX)){
            email.setError("Format Email Salah");
        }else if(tipeUser.getSelectedItemPosition()==0){
            Toast.makeText(ManagUserActivity.this, "Silahkan Tentukan Tipe User",Toast.LENGTH_LONG).show();
        }else{
            ModelUser mdUser = new ModelUser();
            mdUser.setNama(nama.getText().toString());
            mdUser.setUsername(username.getText().toString());
            mdUser.setPassword(password.getText().toString());
            mdUser.setEmail(email.getText().toString());
            mdUser.setTipeUser(tipeUser.getSelectedItem().toString());
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            if(mode.equals("Edit")){
                mdUser.setIdUser(idUser);
                int status = db.prosesUpdate(mdUser);
                if(status   >0){
                    Toast.makeText(ManagUserActivity.this, "Data Berhasil Diubah ",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ManagUserActivity.this, "Data Gagal Diubah ",Toast.LENGTH_LONG).show();
                }
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }else{
                db.prosesTambahUser(mdUser);
                Toast.makeText(ManagUserActivity.this, "Proses Berhasil ",Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        }
        return v;
    }
    public View Kembali(View v){
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
        return v;
    }

    private int getSelection(final String tipeUser){

        int val= 0;
        if(tipeUser.equals("SUPER ADMIN")){
            val= 1;
        }else if(tipeUser.equals("USER")){
            val= 2;
        }
        return val;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manag_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_logouts) {

            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}




