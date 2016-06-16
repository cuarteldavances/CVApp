package cv.andevelopnica.com.crediveloz;

import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class GastosActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Button sgasto;
    private EditText etgasolina;
    private EditText etotros;
    private EditText etdetalle;
    private TextView gastohoy;
    private Spinner spinsucur;
    private String lugar;
    private DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
    private boolean siclick;
    private String Choy;
    private String LLvhoy;
    private String Ghoy;
    private String Phoy;
    public static int MILISEGUNDOS_ESPERA = 1000;
    private Button cobros;
    private Button desembolso;
    private Button programacion;
    private Button entrega;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);
        //inicializando controles
        sgasto = (Button) findViewById(R.id.btn_gastos);
        etgasolina = (EditText) findViewById(R.id.edt_gasolina);
        etotros = (EditText) findViewById(R.id.edt_otros);
        etdetalle = (EditText) findViewById(R.id.edt_detalle);
        gastohoy = (TextView) findViewById(R.id.tv_ghoy);
        spinsucur = (Spinner) findViewById(R.id.snplsucursal);

        //Obteniendo una instancia del boton show_pet_button
        cobros = (Button)findViewById(R.id.cobros);
        desembolso = (Button)findViewById(R.id.desembolso);
        programacion = (Button)findViewById(R.id.programacion);
        entrega = (Button)findViewById(R.id.entrega);

        //Registrando la escucha sobre la actividad Main
        cobros.setOnClickListener(this);
        desembolso.setOnClickListener(this);
        programacion.setOnClickListener(this);
        entrega.setOnClickListener(this);

        spinsucur.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerData();

        ComprobarLleva();

        //asignando listener a boton
        sgasto.setOnClickListener(this);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        String gasttodehoy;
        gasttodehoy = databaseAccess.ObtenerGastoHoy();
        if (gasttodehoy == null){
            gastohoy.setText("0C$");
        }
        if (gasttodehoy != null){
            gastohoy.setText(" "+gasttodehoy+"C$");
        }

        databaseAccess.close();
    }

    private void loadSpinnerData() {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        List<String> lables = databaseAccess.getAllLabels();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinsucur.setAdapter(dataAdapter);
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

        if (v.getId()==R.id.desembolso){
            finish();
            //Iniciando la actividad Visor
            Intent intent2 = new Intent(this,DesembolsoSucursalActivity.class);
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
            esperarYEntregar(MILISEGUNDOS_ESPERA);

        }
        if (v.getId()==R.id.btn_gastos){
            //Iniciando la actividad Visor
            Toast toastp = Toast.makeText(getApplicationContext(), "Procesando solicitud...", Toast.LENGTH_LONG);
            toastp.show();

            esperarYCerrar(MILISEGUNDOS_ESPERA);
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

    public void esperarYCerrar(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                comprobando();
            }
        }, milisegundos);
    }

    public void comprobando() {
        if(TextUtils.isEmpty(etgasolina.getText().toString())) {
            etgasolina.setError("Este campo es requerido.");
            return;
        }
        if(TextUtils.isEmpty(etotros.getText().toString())) {
            etotros.setError("Este campo es requerido.");
            return;
        }

        databaseAccess.open();

        String cobroshoy;
        cobroshoy = databaseAccess.CobrosFinalHoy();
        if(cobroshoy == null){
            Choy = "0";
        }
        if(cobroshoy != null){
            Choy = databaseAccess.CobrosFinalHoy();
        }
        String llevahoy;
        llevahoy = databaseAccess.VerLleva();
        if(llevahoy == null){
            LLvhoy = "0";
        }
        if(llevahoy != null){
            LLvhoy = databaseAccess.VerLleva();
        }
        String gastoshoy;
        gastoshoy = databaseAccess.GastosFinalHoy();
        if(gastoshoy == null){
            Ghoy = "0";
        }
        if(gastoshoy != null){
            Ghoy = databaseAccess.GastosFinalHoy();
        }
        String desembolsohoy;
        desembolsohoy = databaseAccess.DesembolsoFinalHoy();
        if(desembolsohoy == null){
            Phoy = "0";
        }
        if(desembolsohoy != null){
            Phoy = databaseAccess.DesembolsoFinalHoy();
        }
        double dineroacumlado;
        dineroacumlado = Double.parseDouble(Choy) + Double.parseDouble(LLvhoy) - Double.parseDouble(Ghoy) - Double.parseDouble(Phoy);

        databaseAccess.close();

        if (dineroacumlado < Double.parseDouble(etgasolina.getText().toString()) + Double.parseDouble(etotros.getText().toString())){
            Toast.makeText(this, "Fondo insuficiente.", Toast.LENGTH_LONG).show();
        }
        if (dineroacumlado > Double.parseDouble(etotros.getText().toString()) + Double.parseDouble(etgasolina.getText().toString())){
            siclick = true;
            lanzardialogo();
        }
    }

    public boolean lanzardialogo(){
        Double sumag = Double.parseDouble(etotros.getText().toString())+Double.parseDouble(etgasolina.getText().toString());
        String sms_chkdetalles = null;
        sms_chkdetalles = "Total de gastos a ingresar C$"+sumag.toString()+".\n";
        if(Integer.parseInt(etotros.getText().toString()) > 0 & TextUtils.isEmpty(etdetalle.getText().toString()) ){
            sms_chkdetalles = "No has detallado el gasto.\n";
        }

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirmar")
                .setMessage(sms_chkdetalles+"Estás seguro?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                    @Override
                    public void onClick(DialogInterface dialog, int which){

                        databaseAccess.open();
                        String Cgasto = databaseAccess.ComprobarGasto();
                        databaseAccess.close();

                        if (Cgasto==null){
                            try {
                                if(Integer.parseInt(etgasolina.getText().toString()) + Integer.parseInt(etotros.getText().toString()) <= 0){
                                    Toast toast = Toast.makeText(getApplicationContext(), "No puedes guardar valores menores o igual a 0!", Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                                else if(Integer.parseInt(etgasolina.getText().toString()) + Integer.parseInt(etotros.getText().toString()) > 0){
                                    try {
                                        //Instancioando Clase DatabaseAccess
                                        //DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                                        databaseAccess.open();
                                        String Ovendedor;
                                        Ovendedor = databaseAccess.ObtenerVendedor();

                                        //String lugar = null;

                                        String IDSucursalGasto;
                                        IDSucursalGasto = databaseAccess.ObtenerIdSucurGasto(lugar);

                                        Integer idgastos = databaseAccess.ObtenerIdgastos();
                                        databaseAccess.IsertarGasto(idgastos,Integer.parseInt(IDSucursalGasto),Integer.parseInt(Ovendedor),Integer.parseInt(etgasolina.getText().toString()), Integer.parseInt(etotros.getText().toString()), etdetalle.getText().toString());
                                        String gasttodehoy;
                                        gasttodehoy = databaseAccess.ObtenerGastoHoy();
//                                        Toast toast1 = Toast.makeText(getApplicationContext(), Ovendedor, Toast.LENGTH_SHORT);
//                                        toast1.show();
                                        gastohoy.setText(" "+gasttodehoy+"C$");

                                        databaseAccess.close();
                                        Toast toast = Toast.makeText(getApplicationContext(), "Datos guardados correctamente!", Toast.LENGTH_SHORT);
                                        toast.show();
                                        //comprobando que obtenermos el id de sucursal
                                        //Toast toast = Toast.makeText(this, IDSucursalGasto, Toast.LENGTH_SHORT);
                                        //toast.show();
                                        etgasolina.setText("0");
                                        etotros.setText("0");
                                        etdetalle.setText("");

                                    }
                                    catch(Exception e){
                                        // here you can catch all the exceptions
                                        System.err.println("Error en la consulta SQL!");
                                        Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                }
                            }catch (Exception ex){

                                Toast toast = Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }

                        if (Cgasto != null){

                            try {
                                if(Integer.parseInt(etgasolina.getText().toString()) + Integer.parseInt(etotros.getText().toString()) <= 0){
                                    Toast toast = Toast.makeText(getApplicationContext(), "No puedes guardar valores menores o igual a 0!", Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                                else if(Integer.parseInt(etgasolina.getText().toString()) + Integer.parseInt(etotros.getText().toString()) > 0){
                                    try {
                                        //Instancioando Clase DatabaseAccess
                                        //DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                                        databaseAccess.open();
                                        String Ovendedor;
                                        Ovendedor = databaseAccess.ObtenerVendedor();

                                        //String lugar = null;

                                        String IDSucursalGasto;
                                        IDSucursalGasto = databaseAccess.ObtenerIdSucurGasto(lugar);

                                        Integer idgastos = databaseAccess.ObtenerIdgastos();
                                        //databaseAccess.IsertarGasto(idgastos,Integer.parseInt(IDSucursalGasto),Integer.parseInt(Ovendedor),Integer.parseInt(etgasolina.getText().toString()), Integer.parseInt(etotros.getText().toString()), etdetalle.getText().toString());

                                        databaseAccess.ActualizarGasto(Integer.parseInt(etgasolina.getText().toString()),Integer.parseInt(etotros.getText().toString()));

                                        String gasttodehoy;
                                        gasttodehoy = databaseAccess.ObtenerGastoHoy();
//                                        Toast toast1 = Toast.makeText(getApplicationContext(), Ovendedor, Toast.LENGTH_SHORT);
//                                        toast1.show();
                                        gastohoy.setText(" "+gasttodehoy+"C$");

                                        databaseAccess.close();
                                        Toast toast = Toast.makeText(getApplicationContext(), "Datos guardados correctamente!", Toast.LENGTH_SHORT);
                                        toast.show();
                                        //comprobando que obtenermos el id de sucursal
                                        //Toast toast = Toast.makeText(this, IDSucursalGasto, Toast.LENGTH_SHORT);
                                        //toast.show();
                                        etgasolina.setText("0");
                                        etotros.setText("0");
                                        etdetalle.setText("");

                                    }
                                    catch(Exception e){
                                        // here you can catch all the exceptions
                                        System.err.println("Error en la consulta SQL!");
                                        Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                }
                            }catch (Exception ex){

                                Toast toast = Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
                                toast.show();
                            }

                        }

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Toast toast = Toast.makeText(getApplicationContext(), "Operacion Cancelada!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .show();


        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // Asignando valor a lugar de sucursal
        lugar = parent.getItemAtPosition(position).toString();

        // Comprobando que obtnemos el lugar de sucursal
        //Toast.makeText(parent.getContext(), "You selected: " + label,
                //Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void ComprobarLleva(){
        try {
            databaseAccess = DatabaseAccess.getInstance(this);
            databaseAccess.open();
            String LLevaHoy = databaseAccess.VerLleva();
            databaseAccess.close();

            if (LLevaHoy == null){

                MyDialog dialog = new MyDialog();
                dialog.show(getSupportFragmentManager() ,"my_dialog");

            }
        }catch (Exception ex) {
            Toast toast = Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Salir")
                    .setMessage("Estás seguro?")
                    .setNegativeButton(android.R.string.cancel, null)//sin listener
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            //Salir
                            finish();
                            System.exit(0);
                        }
                    })
                    .show();

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
//para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }


}
