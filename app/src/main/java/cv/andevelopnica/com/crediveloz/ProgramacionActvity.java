package cv.andevelopnica.com.crediveloz;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import cv.andevelopnica.com.crediveloz.utilities.DBhelper;
import cv.andevelopnica.com.crediveloz.utilities.SQLControlador;

public class ProgramacionActvity extends AppCompatActivity implements View.OnClickListener {

    Button btnAgregarMiembro;
    ListView lista;
    SQLControlador dbconeccion;
    TextView tv_miemID, tv_miemNombre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacion);

        dbconeccion = new SQLControlador(this);
        dbconeccion.abrirBaseDeDatos();
        btnAgregarMiembro = (Button) findViewById(R.id.btnAgregarMiembro);
        lista = (ListView) findViewById(R.id.listViewMiembros);



        //acción del boton agregar miembro
        btnAgregarMiembro.setOnClickListener(this);



        // Tomar los datos desde la base de datos para poner en el curso y después en el adapter
        Cursor cursor = dbconeccion.leerDatos();

        String[] from = new String[] {
                DBhelper.MIEMBRO_ID,
                DBhelper.MIEMBRO_NOMBRE
        };
        int[] to = new int[] {
                R.id.miembro_id,
                R.id.miembro_nombre
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                ProgramacionActvity.this, R.layout.formato_fila, cursor, from, to);

        adapter.notifyDataSetChanged();
        lista.setAdapter(adapter);

        // acción cuando hacemos click en item para poder modificarlo o eliminarlo
        lista.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                tv_miemID = (TextView) view.findViewById(R.id.miembro_id);
                tv_miemNombre = (TextView) view.findViewById(R.id.miembro_nombre);

                String aux_miembroId = tv_miemID.getText().toString();
                String aux_miembroNombre = tv_miemNombre.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(), ModificarProgramacion.class);
                modify_intent.putExtra("miembroId", aux_miembroId);
                modify_intent.putExtra("miembroNombre", aux_miembroNombre);
                startActivity(modify_intent);
            }
        });
    }  //termina el onCreate

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
    public void onClick(View v) {
            Intent iagregar = new Intent(ProgramacionActvity.this, AgregarProgramacion.class);
            startActivity(iagregar);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        Intent i = new Intent(ProgramacionActvity.this, GastosActivity.class);
        startActivity(i);

//para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

} //termina clase
