package cv.andevelopnica.com.crediveloz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;

public class DesembolsoActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, AdapterView.OnItemSelectedListener{
    private EditText etd_monto;
    private EditText etd_montototald;
    private EditText etd_dinteres;
    private EditText etd_cuota;
    private EditText etnombre;
    private Button btnd_desembolsar;
    private String ndias;
    private Spinner spinner_dias;
    private DatabaseAccess databaseAccess;
    private String SUCURSAL;
    private Boolean clicke;
    private Button cobros;
    private Button pagos;
    private Button clientes;
    private Button entrega;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desembolso);

        spinner_dias = (Spinner) findViewById(R.id.edt_dias);
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource( this, R.array.dias , android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_dias.setAdapter(spinner_adapter);
        spinner_dias.setOnItemSelectedListener(this);

        etnombre = (EditText) findViewById(R.id.edt_dnombre);
        btnd_desembolsar = (Button) findViewById(R.id.btn_desmbolsar);
        etd_monto = (EditText) findViewById(R.id.edt_dmonto);
        etd_dinteres = (EditText) findViewById(R.id.edt_interes);
        etd_cuota = (EditText) findViewById(R.id.edt_dcuota);
        etd_montototald = (EditText) findViewById(R.id.edt_dmtotal);

        //Obteniendo una instancia del boton show_pet_button
        cobros = (Button)findViewById(R.id.cobros);
        pagos = (Button)findViewById(R.id.pagos);
        clientes = (Button)findViewById(R.id.programacion);
        entrega = (Button)findViewById(R.id.entrega);

        //Registrando la escucha sobre la actividad Main
        cobros.setOnClickListener(this);
        pagos.setOnClickListener(this);
        clientes.setOnClickListener(this);
        entrega.setOnClickListener(this);

        etd_monto.addTextChangedListener(this);

        btnd_desembolsar.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        etnombre.setText(bundle.getString("nme"));

        Bundle bundle2 = getIntent().getExtras();
        SUCURSAL = bundle2.getString("sucur");
        LlenarDatoscliente();
    }

    public void LlenarDatoscliente(){
        if(TextUtils.isEmpty(etnombre.getText().toString())) {
            etnombre.setError("Este campo es requerido.");
            return;
        }
        try {

        }
        catch (Exception ex) {
            Toast toast = Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.cobros){
            finish();
            //Iniciando la actividad Visor
            Intent intent = new Intent(this,CobroSucursalActivity.class);
            startActivity(intent);
        }

        if (view.getId()==R.id.desembolso){
            finish();
            //Iniciando la actividad Visor
            Intent intent2 = new Intent(this,DesembolsoSucursalActivity.class);
            startActivity(intent2);
        }

        if (view.getId()==R.id.programacion){
            finish();
            //Iniciando la actividad Visor
            Intent intent3 = new Intent(this,ProgramacionActvity.class);
            startActivity(intent3);
        }

        if (view.getId()==R.id.entrega){
            Toast toastp = Toast.makeText(getApplicationContext(), "Procesando solicitud...", Toast.LENGTH_LONG);
            toastp.show();
            esperarYEntregar(1000);

        }

        if (view.getId()==R.id.btn_desmbolsar){
            //Desncadena funcion desembolso
            try {

                if (view.getId()== R.id.btn_desmbolsar){

                    if(TextUtils.isEmpty(etd_monto.getText().toString())) {
                        etd_monto.setError("Este campo es requerido.");
                        return;
                    }

                    Boolean clicke = true;
                    lanzardialogo();

                }

            } catch (Exception ex){
                //tvIdCartera.setText(ex.getMessage());
                Toast toast = Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }

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

    public boolean lanzardialogo(){

        if (clicke = true){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Confirmar")
                    .setMessage("Estás seguro?")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which){

                            try {

                            }catch (Exception ex){
                                //tvIdCartera.setText(ex.getMessage());
                                Toast toast = Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT);
                                toast.show();
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
        }
        // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Salir")
                    .setMessage("Estás seguro que deceas salir?")
                    .setNegativeButton(android.R.string.cancel, null)//sin listener
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            //Salir
                            finish();
                        }
                    })
                    .show();

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
//para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        if(TextUtils.isEmpty(etd_monto.getText().toString())) {
            etd_monto.setError("Este campo es requerido.");
            return;
        }
        // Asignando valor a lugar de sucursal
        ndias = parent.getItemAtPosition(position).toString();
        if (ndias.equals("23")){
            double interes = (Integer.parseInt(etd_monto.getText().toString()) * 15) /100;
            double montototal = Integer.parseInt(etd_monto.getText().toString()) + interes;
            double cuota = montototal / Integer.parseInt(ndias);
            DecimalFormat df = new DecimalFormat("0.00");
            etd_dinteres.setText(String.valueOf(df.format(interes)));
            etd_cuota.setText(String.valueOf(df.format(cuota)));
            etd_montototald.setText(String.valueOf(montototal));
        }

        if (ndias.equals("30")){
            double interes = (Integer.parseInt(etd_monto.getText().toString()) * 15) /100;
            double montototal = Integer.parseInt(etd_monto.getText().toString()) + interes;
            double cuota = montototal / Integer.parseInt(ndias);
            DecimalFormat df = new DecimalFormat("0.00");
            etd_dinteres.setText(String.valueOf(df.format(interes)));
            etd_cuota.setText(String.valueOf(df.format(cuota)));
            etd_montototald.setText(String.valueOf(montototal));
        }

        if (ndias.equals("50")){
            double interes = (Integer.parseInt(etd_monto.getText().toString()) * 25) /100;
            double montototal = Integer.parseInt(etd_monto.getText().toString()) + interes;
            double cuota = montototal / Integer.parseInt(ndias);
            DecimalFormat df = new DecimalFormat("0.00");
            etd_dinteres.setText(String.valueOf(df.format(interes)));
            etd_cuota.setText(String.valueOf(df.format(cuota)));
            etd_montototald.setText(String.valueOf(montototal));
        }

        // Comprobando que obtnemos el lugar de sucursal
        //Toast.makeText(parent.getContext(), "You selected: " + ndias,
        //Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}