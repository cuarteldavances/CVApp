package cv.andevelopnica.com.crediveloz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CobrosActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etsaldoactual;
    private EditText etabono;
    private EditText etcuota;
    private EditText etmonto;
    private EditText etnombre;
    private EditText etdireccion;
    private EditText ettelefono;
    private EditText etultimoabono;
    private Button btnbbuscarnombre;
    private Button btnhistorial;
    private TextView tvIdCartera;
    private DatabaseAccess databaseAccess;
    private String SUCURSAL;
    private Boolean clicke;
    public String ICartera2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cobros);
        databaseAccess = DatabaseAccess.getInstance(this);
        etsaldoactual = (EditText) findViewById(R.id.edt_bsaldo);
        etabono = (EditText) findViewById(R.id.edt_babono);
        etcuota = (EditText) findViewById(R.id.edt_bcuota);
        etmonto = (EditText) findViewById(R.id.edt_bmonto);
        etnombre = (EditText) findViewById(R.id.edt_bnombre);
        btnbbuscarnombre = (Button) findViewById(R.id.btn_bbuscarnom);
        btnhistorial = (Button) findViewById(R.id.btn_historialpagos);
        etdireccion= (EditText) findViewById(R.id.edt_bdireccion);
        ettelefono = (EditText) findViewById(R.id.edt_btelefono);
        etultimoabono = (EditText) findViewById(R.id.edt_bultimoabono);
        tvIdCartera = (TextView) findViewById(R.id.tv_bidcartera);
        btnbbuscarnombre.setOnClickListener(this);
        btnhistorial.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        etnombre.setText(bundle.getString("nme"));
        //btnbbuscarnombre.performClick();
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
                databaseAccess.open();
                String Cliente;
                String ID_Cartera;
                String Monto;
                String Cuota;
                String Saldo;
                String Direccion;
                String Telefono;
                String UltimoAbono;
                ID_Cartera = databaseAccess.BuscarIdCartera(etnombre.getText().toString());
                tvIdCartera.setText("Cartera: "+ID_Cartera);
                Cliente = databaseAccess.BuscarCliente(etnombre.getText().toString());
                Monto = databaseAccess.BuscarMonto(ID_Cartera);
                etmonto.setText(Monto);
                Cuota = databaseAccess.BuscarCuota(ID_Cartera);
                etcuota.setText(Cuota);
                Saldo = databaseAccess.BuscarSaldoActual(ID_Cartera);
                etsaldoactual.setText(Saldo);
                Direccion = databaseAccess.ObtenerDireccionCiente(etnombre.getText().toString());
                etdireccion.setText(Direccion);
                Telefono = databaseAccess.ObtenerTelefonoCiente(etnombre.getText().toString());
                ettelefono.setText(Telefono);
                UltimoAbono = databaseAccess.UltimaFechaAbono(ID_Cartera);
                etultimoabono.setText(UltimoAbono);
                ICartera2 = ID_Cartera;
                //Toast toast = Toast.makeText(this, databaseAccess.aver(), Toast.LENGTH_SHORT);
                //toast.show();

                databaseAccess.close();
        }
        catch (Exception ex) {
            Toast toast = Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onClick(View view) {
//        try {

            if (view.getId()== R.id.btn_bbuscarnom){

                if(TextUtils.isEmpty(etabono.getText().toString())) {
                    etabono.setError("Este campo es requerido.");
                    return;
                }
                databaseAccess.open();
                String sAbonoCliente = databaseAccess.Comprobarcobro(Integer.parseInt(databaseAccess.BuscarIdCartera(etnombre.getText().toString())));
//                    Toast.makeText(getBaseContext(), ICartera2+String.valueOf(sAbonoCliente),
//                            Toast.LENGTH_LONG).show();
                databaseAccess.close();
                Boolean SihayAbono = true;
                if(sAbonoCliente == null){
                    SihayAbono = false;
                }

                if (SihayAbono == false ) {
                    Boolean clicke = true;
                    lanzardialogo();
                }

                if (SihayAbono == true ) {
                    databaseAccess.open();
                    String showid_sucursal = databaseAccess.ObtenerIdSucursal(SUCURSAL); //--PARAMETRO--//
                    //Comprobando salida
                    //Toast toast = Toast.makeText(this, "ID_Sucursal: "+showid_sucursal, Toast.LENGTH_SHORT);
                    //toast.show();
                    String showid_vendedor = databaseAccess.ObtenerVendedor(); //--PARAMETRO--//
                    //Comprobando salida
                    //Toast toast = Toast.makeText(this, "ID_Vendedor: "+showid_vendedor, Toast.LENGTH_SHORT);
                    //toast.show();
                    String showid_cartera = databaseAccess.BuscarIdCartera(etnombre.getText().toString()); //--PARAMETRO--//
                    //Comprobando salida
                    //Toast toast = Toast.makeText(this, "ID_Cartera: "+showid_cartera, Toast.LENGTH_SHORT);
                    //toast.show();
                    String showtarjeta = databaseAccess.ObtenerTarjeta(showid_cartera); //--PARAMETRO--//
                    //Comprobando salida
                    //Toast toast = Toast.makeText(this, "ID_Tarjeta: "+showtarjeta, Toast.LENGTH_SHORT);
                    //toast.show();
                    String showsaldoanterior = etsaldoactual.getText().toString(); //--PARAMETRO--//

                    String showabono = etabono.getText().toString(); //--PARAMETRO--//

                    Double showsaldoactual = Double.parseDouble(showsaldoanterior) - Double.parseDouble(showabono); //--PARAMETRO--//

                    String showcuota = etcuota.getText().toString(); //--PARAMETRO--//

                    String showid_cliente = databaseAccess.ObtenerIdCliente(showid_cartera);
                    databaseAccess.ActualizarCobro(Integer.parseInt(ICartera2),Integer.parseInt(etabono.getText().toString()));
                    databaseAccess.ActualizarCartera2(Integer.parseInt(showabono), Integer.parseInt(showid_cartera), Integer.parseInt(showid_cliente));
                    databaseAccess.close();
                    Toast.makeText(getBaseContext(), "Abono actualizado!",
                            Toast.LENGTH_LONG).show();
                }

            }
            if (view.getId()== R.id.btn_historialpagos) {
                Toast toastp = Toast.makeText(getApplicationContext(), "Procesando solicitud...", Toast.LENGTH_LONG);
                toastp.show();
                esperarYabrir(1000);
            }

//        } catch (Exception ex){
//            tvIdCartera.setText(ex.getMessage());
//            Toast toast = Toast.makeText(this, ex.getMessage()+ICartera2, Toast.LENGTH_SHORT);
//            toast.show();
//        }
    }

    public void esperarYabrir(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                Intent i = new Intent(getBaseContext(), HistorialPagosActivity.class );
                i.putExtra("idcartera", ICartera2);
                startActivity(i);
            }
        }, milisegundos);
    }

    public boolean lanzardialogo(){
        String sms_tipoabono = null;
        if (Double.parseDouble(etabono.getText().toString()) <= 0 ){
            Toast toast = Toast.makeText(getApplicationContext(), "NO PERMITIDO!", Toast.LENGTH_SHORT);
            toast.show();

        }
        else {
            sms_tipoabono = "Se abonaran C$"+etabono.getText().toString()+" a la cuenta actual.\n";

        if (clicke = true){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Confirmar")
                    .setMessage(sms_tipoabono+"Estás seguro?")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which){

//                            try {
                            //Aplicar abono
                            databaseAccess.open();
                            String showid_sucursal = databaseAccess.ObtenerIdSucursal(SUCURSAL); //--PARAMETRO--//
                            //Comprobando salida
                            //Toast toast = Toast.makeText(this, "ID_Sucursal: "+showid_sucursal, Toast.LENGTH_SHORT);
                            //toast.show();
                            String showid_vendedor = databaseAccess.ObtenerVendedor(); //--PARAMETRO--//
                            //Comprobando salida
                            //Toast toast = Toast.makeText(this, "ID_Vendedor: "+showid_vendedor, Toast.LENGTH_SHORT);
                            //toast.show();
                            String showid_cartera = databaseAccess.BuscarIdCartera(etnombre.getText().toString()); //--PARAMETRO--//
                            //Comprobando salida
                            //Toast toast = Toast.makeText(this, "ID_Cartera: "+showid_cartera, Toast.LENGTH_SHORT);
                            //toast.show();
                            String showtarjeta = databaseAccess.ObtenerTarjeta(showid_cartera); //--PARAMETRO--//
                            //Comprobando salida
                            //Toast toast = Toast.makeText(this, "ID_Tarjeta: "+showtarjeta, Toast.LENGTH_SHORT);
                            //toast.show();
                            String showsaldoanterior = etsaldoactual.getText().toString(); //--PARAMETRO--//

                            String showabono = etabono.getText().toString(); //--PARAMETRO--//

                            Double showsaldoactual = Double.parseDouble(showsaldoanterior) - Double.parseDouble(showabono); //--PARAMETRO--//

                                String showcuota = etcuota.getText().toString(); //--PARAMETRO--//
                                int Rcuota = (int) Math.round(Double.parseDouble(showcuota));

                            String showid_cliente = databaseAccess.ObtenerIdCliente(showid_cartera);

                            //Integer show_moracliente = databaseAccess.IngresarMora(showabono, String.valueOf(showcuota));

                            //operacion para calcular porcentual
                            double intabono = Double.parseDouble(etabono.getText().toString());
                            double intcuota = Double.parseDouble(etcuota.getText().toString());
                            double divi = intabono/intcuota;
                            double showporcentualD =  divi*100; //--PARAMETRO--//

//                            DecimalFormat format = new DecimalFormat();
//                            format.setDecimalSeparatorAlwaysShown(false);
//                                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
//                                symbols.setDecimalSeparator('.');
//                                symbols.setGroupingSeparator(' ');
//                                format.setDecimalFormatSymbols(symbols);
//                            //format.setGroupingUsed(false);
//                            String Nporcen = format.format(showporcentualD);
                            String Nporcen = String.valueOf(showporcentualD);

                            //Comprobando salida
                            //Toast toast = Toast.makeText(this, String.valueOf(showporcentual), Toast.LENGTH_SHORT);
                            //toast.show();
                            //operacion para calcular porcentual

                            //--FUNCION INGREDAR ABONO--//
                                Integer idcobroS = databaseAccess.ObtenerIdcobros();
                            databaseAccess.IngresarAbono(idcobroS, Integer.parseInt(showid_sucursal), Integer.parseInt(showid_cliente), Integer.parseInt(showid_cartera),
                                    Integer.parseInt(showtarjeta), Double.parseDouble(showsaldoanterior), Double.parseDouble(showabono), 0, showsaldoactual, Rcuota,
                                    Nporcen, Integer.parseInt(showid_vendedor));

                            databaseAccess.ActualizarCartera(Double.parseDouble(showabono), Integer.parseInt(showid_cartera), Integer.parseInt(showid_cliente));

                            databaseAccess.close();
                            LlenarDatoscliente();
                            Toast toast = Toast.makeText(getApplicationContext(), "Abono Exitoso!", Toast.LENGTH_SHORT);
                            toast.show();
                            etabono.setText("");
//                            }catch (Exception ex){
//                                tvIdCartera.setText(ex.getMessage());
//                                Toast toast = Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT);
//                                toast.show();
//                            }
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
}
