package cv.andevelopnica.com.crediveloz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class BusquedaDesembolsoActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private EditText etd_monto;
    private EditText etd_montototald;
    private EditText etd_dinteres;
    private EditText etd_cuota;
    private Button btnd_desembolsar;
    private String ndias;
    private Spinner spinner_dias;
    private AutoCompleteTextView etnombre;
    private Button btnbuscarnombre;
    private TextView tvIdCartera;
    private ArrayList<String> NOMBRES ;
    private DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
    private String SUCURSAL;
    private Boolean clicke;
    private Double Tvinteres;
    private double interes;
    private double montototal;
    private double cuota;
    private String Choy;
    private String LLvhoy;
    private String Ghoy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_desembolso);
        databaseAccess.open();

        spinner_dias = (Spinner) findViewById(R.id.edt_dias);
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource( this, R.array.dias , android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_dias.setAdapter(spinner_adapter);
        spinner_dias.setOnItemSelectedListener(this);

        etnombre = (AutoCompleteTextView) findViewById(R.id.edt_bdnombre);
        btnd_desembolsar = (Button) findViewById(R.id.btn_desmbolsar);
        etd_monto = (EditText) findViewById(R.id.edt_bdmonto);
        etd_dinteres = (EditText) findViewById(R.id.edt_interes);
        etd_cuota = (EditText) findViewById(R.id.edt_bdcuota);
        etd_montototald = (EditText) findViewById(R.id.edt_bdmtotal);

        etd_monto.addTextChangedListener(this);

        btnd_desembolsar.setOnClickListener(this);

        NOMBRES = databaseAccess.BuscarTodosClientes() ;

        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.edt_bdnombre);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, NOMBRES);
        textView.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();
        //etnombre.setText(bundle.getString("nme"));

        Bundle bundle2 = getIntent().getExtras();
        SUCURSAL = bundle2.getString("idsucur");
        //LlenarDatoscliente();
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
                            finish();
//            Toast toast = Toast.makeText(this, "Atras...", Toast.LENGTH_SHORT);
//            toast.show();
                            Intent intent3 = new Intent(getBaseContext(),GastosActivity.class);
                            startActivity(intent3);
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
            interes = (Integer.parseInt(etd_monto.getText().toString()) * 15) /100;
            montototal = Integer.parseInt(etd_monto.getText().toString()) + interes;
            cuota = montototal / Integer.parseInt(ndias);
            DecimalFormat df = new DecimalFormat("0.00");
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator('.');
            symbols.setGroupingSeparator(' ');
            df.setDecimalFormatSymbols(symbols);
            Tvinteres = 0.15;
            etd_dinteres.setText(String.valueOf(df.format(interes)));
            etd_cuota.setText(String.valueOf(df.format(cuota)));
            etd_montototald.setText(String.valueOf(montototal));
        }

        if (ndias.equals("30")){
            interes = (Integer.parseInt(etd_monto.getText().toString()) * 15) /100;
            montototal = Integer.parseInt(etd_monto.getText().toString()) + interes;
            cuota = montototal / Integer.parseInt(ndias);
            DecimalFormat df = new DecimalFormat("0.00");
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator('.');
            symbols.setGroupingSeparator(' ');
            df.setDecimalFormatSymbols(symbols);
            Tvinteres = 0.15;
            etd_dinteres.setText(String.valueOf(df.format(interes)));
            etd_cuota.setText(String.valueOf(df.format(cuota)));
            etd_montototald.setText(String.valueOf(montototal));
        }

        if (ndias.equals("50")){
            interes = (Integer.parseInt(etd_monto.getText().toString()) * 25) /100;
            montototal = Integer.parseInt(etd_monto.getText().toString()) + interes;
            cuota = montototal / Integer.parseInt(ndias);
            DecimalFormat df = new DecimalFormat("0.00");
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator('.');
            symbols.setGroupingSeparator(' ');
            df.setDecimalFormatSymbols(symbols);
            Tvinteres = 0.25;
            etd_dinteres.setText(String.valueOf(df.format(interes)));
            etd_cuota.setText(String.valueOf(df.format(cuota)));
            etd_montototald.setText(String.valueOf(montototal));
        }



        //Comprobando
        //Toast.makeText(parent.getContext(), "Caja Inicial: " + String.valueOf(cajahoy),
        //Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        btnd_desembolsar.setEnabled(false);

        if(spinner_dias.getSelectedItem().toString().trim().equals("Seleccionar dias...")) {
            Toast.makeText(v.getContext(), "Seleccione Dias.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        esperarproceso(1000);
        esperarYdesembolsar(3000);

    }

    public void esperarproceso(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                Toast.makeText(getBaseContext(), "Procesando solicitud...",
                        Toast.LENGTH_SHORT).show();
            }
        }, milisegundos);
    }

    public void esperarYdesembolsar(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                if(TextUtils.isEmpty(etnombre.getText().toString())) {
                    etnombre.setError("Este campo es requerido.");
                    return;
                }
                if(TextUtils.isEmpty(etd_monto.getText().toString())) {
                    etd_monto.setError("Este campo es requerido.");
                    return;
                }

                databaseAccess.open();
                String cobros = databaseAccess.CobrosFinalHoy();
                if(cobros == null){
                    Choy = "0";
                }
                if(cobros != null){
                    Choy = databaseAccess.CobrosFinalHoy();
                }
                String lleva = databaseAccess.VerLleva();
                if(lleva == null){
                    LLvhoy = "0";
                }
                if(lleva != null){
                    LLvhoy = databaseAccess.VerLleva();
                }
                String gastos = databaseAccess.GastosFinalHoy();
                if(gastos == null){
                    Ghoy = "0";
                }
                if(gastos != null){
                    Ghoy = databaseAccess.GastosFinalHoy();
                }

                Double cajahoy = Double.parseDouble(LLvhoy) + Double.parseDouble(Choy) - Double.parseDouble(Ghoy);

                databaseAccess.close();

                if(cajahoy < Double.parseDouble(etd_monto.getText().toString())){
                    Toast.makeText(getBaseContext(), "Fondo insuficiente.",
                            Toast.LENGTH_LONG).show();
                }

                if(cajahoy >= Double.parseDouble(etd_monto.getText().toString())){
                    //databaseAccess.ObtenerIdClienteFromCliente(etnombre.getText().toString());
                    databaseAccess.open();
                    Double sAldoCliente = databaseAccess.ComprbarCartera(databaseAccess.ObtenerIdClienteFromCliente(etnombre.getText().toString()));
//                    Toast.makeText(getBaseContext(), String.valueOf(sAldoCliente),
//                            Toast.LENGTH_LONG).show();
                    databaseAccess.close();
                    if (sAldoCliente > 0 ) {
                        Toast.makeText(getBaseContext(), "Este cliente ya tiene un prestamo activo!",
                                Toast.LENGTH_LONG).show();
                        btnd_desembolsar.setEnabled(true);
                    }
                    if(Double.parseDouble(etd_monto.getText().toString()) > 30000){
                        Toast.makeText(getBaseContext(), "No autorizado!",
                                Toast.LENGTH_LONG).show();
                        btnd_desembolsar.setEnabled(true);
                    }
                    if(Double.parseDouble(etd_monto.getText().toString()) <= 30000){
                        if (sAldoCliente == 0 ) {
                            clicke = true;
                            lanzardialogo();
                        }
                    }



//            Toast.makeText(v.getContext(), "Aqui ta tu pishto!",
//                    Toast.LENGTH_LONG).show();
                }

            }
        }, milisegundos);
    }


    public boolean lanzardialogo(){

        String sms_dembolso = "Se desembolsara C$"+etd_monto.getText().toString()+" al cliente actual..\n";

        if (clicke = true){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Confirmar")
                    .setMessage(sms_dembolso+"Estás seguro?")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which){

                            try {
                                databaseAccess.open();
                                String show_idcliente;
                                show_idcliente = databaseAccess.ObtenerIdClienteFromCliente(etnombre.getText().toString());
                                String show_idvendedor;
                                show_idvendedor = databaseAccess.ObtenerVendedor();
                                Integer Ntargeta;
                                Ntargeta = Integer.parseInt(databaseAccess.TarjetaMasAlta()) + 1;
                                String IDACtividad = databaseAccess.ObtenerIdCatividad(etnombre.getText().toString());
                                Integer idcarteraS = databaseAccess.ObtenerIdcartera();

                                databaseAccess.DemebolsarCliente(idcarteraS, Integer.parseInt(SUCURSAL), Integer.parseInt(show_idcliente), Integer.parseInt(show_idvendedor), Ntargeta, Integer.parseInt(ndias), cuota, Integer.parseInt(etd_monto.getText().toString()), Double.parseDouble(etd_dinteres.getText().toString()), montototal, montototal, Tvinteres, Integer.parseInt(IDACtividad));
                                //para fines de enrega
//                                databaseAccess.DemebolsarCliente(Integer.parseInt(etd_monto.getText().toString()));
                                databaseAccess.close();
                                btnd_desembolsar.setEnabled(true);
                                Toast toast = Toast.makeText(getApplicationContext(), "Desembolso registrado!", Toast.LENGTH_SHORT);
                                toast.show();

                            }
                            catch (Exception ex) {
                                Toast toast = Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            btnd_desembolsar.setEnabled(true);
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
