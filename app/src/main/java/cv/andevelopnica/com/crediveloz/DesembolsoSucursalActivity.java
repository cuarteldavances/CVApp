package cv.andevelopnica.com.crediveloz;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DesembolsoSucursalActivity extends AppCompatActivity implements View.OnClickListener{
    private Button cobros;
    private Button pagos;
    private Button programacion;
    private Button entrega;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desembolso_sucursal);

        //Obteniendo una instancia del boton show_pet_button
        cobros = (Button)findViewById(R.id.cobros);
        pagos = (Button)findViewById(R.id.pagos);
        programacion = (Button)findViewById(R.id.programacion);
        entrega = (Button)findViewById(R.id.entrega);

        //Registrando la escucha sobre la actividad Main
        cobros.setOnClickListener(this);
        pagos.setOnClickListener(this);
        programacion.setOnClickListener(this);
        entrega.setOnClickListener(this);

        try {

            final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
            databaseAccess.open();
            //databaseAccess.ObtenerClientes();

            ListView listView = (ListView) findViewById(R.id.listv_sucurdesembolso);



            Cursor cursor = databaseAccess.ObtenerSucursal();
            //startManagingCursor(cursor);

            String[] from = new String[]{"Nombre"};
            int[] to = new int[]{R.id.text};

            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.rowruta, cursor, from, to);

            listView.setAdapter(cursorAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView item = (TextView) findViewById(R.id.text);


                    String id_SucurSal = databaseAccess.ObtenerIdSucursal(item.getText().toString());

                    Intent i = new Intent(getApplicationContext(), BusquedaDesembolsoActivity.class );
                    i.putExtra("nme", item.getText().toString());
                    i.putExtra("idsucur", id_SucurSal);
                    startActivity(i);

                    //Toast.makeText(getBaseContext(), "IDENTIFY: "+item+id_SucurSal, Toast.LENGTH_LONG).show();
                    finish();
                    databaseAccess.close();

                }
            });


        }
        catch (Exception ex) {
            Toast toast = Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    //programando accion de boton
    @Override
    public void onClick(View v) {


        if (v.getId()==R.id.cobros){
            finish();
            //Iniciando la actividad Visor
            Intent intent = new Intent(this,CobroSucursalActivity.class);
            startActivity(intent);
        }

        if (v.getId()==R.id.pagos){
            finish();
            //Iniciando la actividad Visor
            Intent intent2 = new Intent(this,GastosActivity.class);
            startActivity(intent2);
        }

        if (v.getId()==R.id.programacion){
            finish();
            //Iniciando la actividad Visor
            Intent intent3 = new Intent(this,ProgramacionActvity.class);
            startActivity(intent3);
        }

        if (v.getId()==R.id.entrega){
            Toast toastp = Toast.makeText(getApplicationContext(), "Procesando solicitud...", Toast.LENGTH_LONG);
            toastp.show();
            esperarYEntregar(1000);

        }

    }

    public void esperarYEntregar(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                finish();
                //Iniciando la actividad Visor
                Intent intent4 = new Intent(getBaseContext(),DetalleFinalActivity.class);
                startActivity(intent4);
            }
        }, milisegundos);
    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //finish();
//            Toast toast = Toast.makeText(this, "Atras...", Toast.LENGTH_SHORT);
//            toast.show();
//            Intent intent3 = new Intent(this,CobroSucursalActivity.class);
//            startActivity(intent3);

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
//para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }
}
