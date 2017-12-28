package com.example.asus.iso;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends Activity {

    private List   <ModelUser> newsList = new ArrayList   <ModelUser>();
    private ListUserAdapter adapter;
    ListUserAdapter custum = new ListUserAdapter(null, newsList);
    ListView listView;
    public String[] idUserArray;

	/*
	 Author : www.theheran.com | info@theheran.com
	 PIN BBM: 571078A7
	*/

    private long lastPressedTime;
    private static final int PERIOD = 2000;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime<PERIOD) {

                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "Tekan 2 kali untuk keluar",Toast.LENGTH_SHORT).show();
                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView= (ListView)findViewById(R.id.listView);
        adapter = new ListUserAdapter(MainActivity.this, newsList);
        listView.setAdapter(adapter);
        reloadData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView   <?> parent, View view,final int position, long id) {

                if(idUserArray[position].equals("1")){
                    PopupMenu popup = new PopupMenu(MainActivity.this, view);
                    popup.getMenuInflater().inflate(R.menu.popup_super_admin, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            if(item.getTitle().equals("Edit Data")){

                                Intent i = new Intent(getApplicationContext(),ManagUserActivity.class);
                                i.putExtra("mode", "Edit");
                                i.putExtra("id_user",idUserArray[position]);
                                startActivity(i);
                                finish();

                            }
                            return true;
                        }});popup.show();

                }else{

                    PopupMenu popup = new PopupMenu(MainActivity.this, view);
                    popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            if(item.getTitle().equals("Edit Data")){

                                Intent i = new Intent(getApplicationContext(),ManagUserActivity.class);
                                i.putExtra("mode", "Edit");
                                i.putExtra("id_user",idUserArray[position]);
                                startActivity(i);
                                finish();


                            }else{

                                Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                alertDialog.setTitle("Hapus Data");
                                alertDialog.setMessage("Kamu Yakin Mau Hapus Data ini?");
                                alertDialog.setPositiveButton("YAKIN BANGET",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {

                                                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                                int status = db.prosesHapus(idUserArray[position]);
                                                if(status>0){
                                                    Toast.makeText(MainActivity.this, "Data Berhasil Dihapus ",Toast.LENGTH_LONG).show();
                                                }else{
                                                    Toast.makeText(MainActivity.this, "Data Gagal Dihapus ",Toast.LENGTH_LONG).show();
                                                }
                                                newsList.clear();
                                                reloadData();

                                            }
                                        });

                                alertDialog.setNegativeButton("ENGGAK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {

                                            }
                                        });
                                alertDialog.show();
                            }
                            return true;
                        }});popup.show();
                }




            }
        });

    }

    public View Tambah(View v){
        Intent i = new Intent(getApplicationContext(),ManagUserActivity.class);
        i.putExtra("mode", "Tambah");
        startActivity(i);
        finish();
        return v;
    }

    public View Reload(View v){
        reloadData();
        return v;
    }

    private void reloadData(){
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Cursor cursor=db.prosesAmbilSemuaDatauser();

        if (cursor.moveToFirst()) {

            idUserArray=new String[cursor.getCount()];
            int i=0,No=1;
            newsList.clear();
            do {
                ModelUser mdUser = new ModelUser();

                mdUser.setIdUser(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID_USER)));
                mdUser.setNama(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAMA)));
                mdUser.setUsername(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_USERNAME)));
                mdUser.setPassword(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_PASSWORD)));
                mdUser.setEmail(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_EMAIL)));
                mdUser.setTipeUser(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_TIPE_USER)));
                mdUser.setNo(String.valueOf(No)+".");

                idUserArray[i] = mdUser.getIdUser();

                No++;
                i++;
                newsList.add(mdUser);
            } while (cursor.moveToNext());

            // Tutup Koneksi
            cursor.close();
            db.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_reset) {

            Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Reset Data");
            alertDialog.setMessage("Kamu Yakin Mau Reset Data?");
            alertDialog.setPositiveButton("YAKIN BANGET",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                            db.prosesResetData();
                            reloadData();
                        }
                    });

            alertDialog.setNegativeButton("ENGGAK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
            alertDialog.show();

            return true;
        }else{
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
