package cv.andevelopnica.com.crediveloz;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HistorialPagosActivity extends Activity {
    private String Cartera;

    private DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

    //variables to hold staff records
    private ArrayList<String> cid = new ArrayList<String>();
    private ArrayList<String> abono = new ArrayList<String>();
    private ArrayList<String> operacion = new ArrayList<String>();

    private ListView userList;
    private AlertDialog.Builder build;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pagos);

        userList = (ListView) findViewById(R.id.List);
        Bundle bundle3 = getIntent().getExtras();
        Cartera = bundle3.getString("idcartera");

    }

    @Override
    protected void onResume() {
//refresh data for screen is invoked/displayed
        displayData();
        super.onResume();
    }

    /**
     * displays data from SQLite
     */
    private void displayData() {

        try {
            //the SQL command to fetched all records from the table
            databaseAccess.open();

            Cursor mCursor = databaseAccess.Historial(String.valueOf(Cartera));

//reset variables
            cid.clear();
            abono.clear();
            operacion.clear();

//fetch each record
            if (mCursor.moveToFirst()) {
                do {
//get data from field

                    cid.add(mCursor.getString(mCursor.getColumnIndex("saldo")));
                    abono.add(mCursor.getString(mCursor.getColumnIndex("abono")));
                    operacion.add(mCursor.getString(mCursor.getColumnIndex("operacion")).replace("00:00:00",""));

                } while (mCursor.moveToNext());
//do above till data exhausted
            }

//display to screen
            DisplayAdapter disadpt = new DisplayAdapter(HistorialPagosActivity.this, cid, abono, operacion);
            userList.setAdapter(disadpt);
            mCursor.close();
            databaseAccess.close();
        }catch(Exception e){
            // here you can catch all the exceptions
            System.err.println("Error en la consulta SQL!");
            Toast toast3 = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            toast3.show();
        }

    }//end displayData


}
