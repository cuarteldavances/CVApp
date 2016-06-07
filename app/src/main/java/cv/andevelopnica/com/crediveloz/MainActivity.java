package cv.andevelopnica.com.crediveloz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Declarando Controles
    private EditText usuario;
    private EditText passwd;
    private Button entrarbtn;
    private Operaciones funciones;
    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        TextView tv = (TextView) findViewById(R.id.copyrightyear);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy"); //"yyyy/MM/dd_HHmmss" o segun el formato que querras bro.
        String currentDateandTime = sdf.format(new Date());
        tv.setText("Todos Los Derechos Reservados " +currentDateandTime);
        TextView tv2 = (TextView) findViewById(R.id.txtfecha);
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy"); //"yyyy/MM/dd_HHmmss" o segun el formato que querras bro.
        String currentDateandTime2 = sdf2.format(new Date());
        tv2.setText(currentDateandTime2);

        //Inicializando Controles
        usuario =  (EditText) findViewById(R.id.edt_usuario);
        passwd = (EditText) findViewById(R.id.edt_passwd);
        entrarbtn = (Button) findViewById(R.id.btn_login);

        //definiendo listener al boton login
        entrarbtn.setOnClickListener(this);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});
    }


    @Override
    public void onClick(View v) {

            //Validando campos vacios y mostrando error con TextUtils
        if(TextUtils.isEmpty(usuario.getText().toString())) {
            usuario.setError("Este campo es requerido.");
            return;
        }
        if(TextUtils.isEmpty(passwd.getText().toString())) {
            passwd.setError("Este campo es requerido.");
            return;
        }
            //Usando try catch para controlar los errores
        try {

            //Instanciando la clase DatabaseAccess
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        String sqlogin;
            String Crypt;
            Crypt = databaseAccess.md5(passwd.getText().toString());
        sqlogin = databaseAccess.login(usuario.getText().toString(),Crypt);
            databaseAccess.vendedortemporal(usuario.getText().toString());
            databaseAccess.Insertvendedortemporal(usuario.getText().toString());


        databaseAccess.close();
            //condicionando si existen los datos ingresados
        if (sqlogin!=usuario.getText().toString()){
            //Lanza el segundo activity

            Intent i = new Intent(this, GastosActivity.class );
            startActivity(i);
            //Limpiando los EdtitText
            usuario.setText("");
            passwd.setText("");
            //Muestra Toast de Bienvenida.
            Toast toast = Toast.makeText(this, "Bienvenido!", Toast.LENGTH_SHORT);
            toast.show();
        }

        }
        catch (Exception ex) {
            //Toast toast = Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT);
            //toast.show();
            //Limpiando los EdtitText
            usuario.setText("");
            passwd.setText("");
            //Muestra Toast de error.
            Toast toast = Toast.makeText(this, "Datos no validos!", Toast.LENGTH_SHORT);
            toast.show();

        }

    }

    @Override
    public void onBackPressed(){
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
            super.onBackPressed();
            finish();
            return;
        }else {
            Toast.makeText(this, "Vuelve a presionar para salir", Toast.LENGTH_SHORT).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();
    }

//    public void guardarIdvendedor(String nombre){
//        SharedPreferences preferencias=getSharedPreferences("vendedoractual", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor=preferencias.edit();
//        editor.putString("loginvendedor", nombre);
//        editor.commit();
//    }
}
