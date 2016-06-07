package cv.andevelopnica.com.crediveloz;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetalleFinalActivity extends AppCompatActivity implements View.OnClickListener{
    private DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
    private TextView cobroshoy;
    private TextView gastoshoy;
    private TextView desembolsohoy;
    private TextView llevahoy;
    private TextView efectivohoy;
    private String Choy;
    private String LLvhoy;
    private String Ghoy;
    private String Phoy;
    private Button aplicar_sucurdet;
    private Double CajaInicial;
    private Double MovimientosHoy;
    private Integer IdSucur;
    private Double EfectivoReal;
    private Button cobros;
    private Button desembolso;
    private Button programacion;
    private Button pagos;
    //private Button syncdb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_final);
        databaseAccess.open();
        cobroshoy = (TextView) findViewById(R.id.tv_cobroshoy);
        gastoshoy = (TextView) findViewById(R.id.tv_gastoshoy);
        desembolsohoy = (TextView) findViewById(R.id.tv_desembolsohoy);
        llevahoy = (TextView) findViewById(R.id.tv_basehoy);
        aplicar_sucurdet = (Button) findViewById(R.id.btn_sucurdet);
        efectivohoy = (TextView) findViewById(R.id.tv_totalefectivohoy);


        //Obteniendo una instancia del boton show_pet_button
        cobros = (Button)findViewById(R.id.cobros);
        desembolso = (Button)findViewById(R.id.desembolso);
        programacion = (Button)findViewById(R.id.programacion);
        pagos = (Button)findViewById(R.id.pagos);
        //syncdb = (Button)findViewById(R.id.btn_sync);

        //Registrando la escucha sobre la actividad Main
        cobros.setOnClickListener(this);
        desembolso.setOnClickListener(this);
        programacion.setOnClickListener(this);
        pagos.setOnClickListener(this);


        //syncdb.setOnClickListener(this);
        aplicar_sucurdet.setOnClickListener(this);


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
        String pretamos = databaseAccess.DesembolsoFinalHoy();
        if(pretamos == null){
            Phoy = "0";
        }
        if(pretamos != null){
            Phoy = databaseAccess.DesembolsoFinalHoy();
        }
//          solo si se necesita registrar en sucurdet por cada vendedor
//        CajaInicial = Double.parseDouble(databaseAccess.ObtenerCajaInicial());
//
        MovimientosHoy = Double.parseDouble(LLvhoy) + Double.parseDouble(Choy) - Double.parseDouble(Ghoy) - Double.parseDouble(Phoy);
//
//        IdSucur = Integer.parseInt(databaseAccess.ObtenerIdSucur());
//
//        EfectivoReal = CajaInicial + MovimientosHoy;

        //Toast.makeText(this, "Caja INi: "+String.valueOf(CajaInicial)+", Movimientos: "+String.valueOf(MovimientosHoy)+", Sucursal ID: "+String.valueOf(IdSucur), Toast.LENGTH_LONG).show();


        cobroshoy.setText("C$ "+Choy);
        gastoshoy.setText("C$ "+Ghoy);
        desembolsohoy.setText("C$ "+Phoy);
        llevahoy.setText("C$ "+LLvhoy);
        efectivohoy.setText("C$ "+MovimientosHoy.toString());
        databaseAccess.close();
    }


    public void backupdDatabase(){

        try {
            File sd = Environment.getExternalStorageDirectory();
//            File data = Environment.getDataDirectory();
//            String packageName  = "cv.andevelopnica.com.crediveloz";
//            String sourceDBName = "cv.db";
            String targetDBName = "respaldo-";
            if (sd.canWrite()) {
                Date now = new Date();
                String currentDBPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/database/cv.db";
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm");

                String backupDBPath = targetDBName + dateFormat.format(now)+ ".db";

                File currentDB = new File(currentDBPath);
                File backupDB = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/respaldodb/", backupDBPath);

                Log.i("backup","backupDB=" + backupDBPath);
                Log.i("backup","sourceDB=" + currentDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast toastp2 = Toast.makeText(getApplicationContext(), "Respaldo Completo!", Toast.LENGTH_LONG);
                toastp2.show();
            }
        } catch (Exception e) {
            Log.i("Backup", e.toString());
        }
    }


//    public static void Eliminar(String pArchivo) {
//        try {
//            File fichero = new File(pArchivo);
//            if (fichero.delete())
//                System.out.println("El fichero ha sido borrado satisfactoriamente");
//            else
//                System.out.println("El fichero no puede ser borrado");
//        } catch (Exception e) {
//            System.out.println(e);
//        } // end try
//    } // end Eliminar


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

        if (view.getId()==R.id.pagos){
            //Iniciando la actividad Visor
            finish();
            Intent intent3 = new Intent(this,GastosActivity.class);
            startActivity(intent3);

        }
        if (view.getId()==R.id.btn_sucurdet){
            Toast toastp = Toast.makeText(getApplicationContext(), "Respaldando base de datos...", Toast.LENGTH_SHORT);
            toastp.show();
            try {
                backupdDatabase();

                //Eliminar("/data/data/cv.andevelopnica.com.crediveloz/databases/cv.db");
//                databaseAccess.open();
//                String versucurdet;
//                versucurdet = databaseAccess.ComprobarSucurdet();
//
//                if (versucurdet==null ){
//                    databaseAccess.IngresarDetalleSucursal(IdSucur, CajaInicial, Integer.parseInt(Choy), Integer.parseInt(Ghoy), CajaInicial, Integer.parseInt(Phoy), EfectivoReal, EfectivoReal, EfectivoReal, EfectivoReal );
//                    Toast.makeText(this, "Datos guardados con exito!", Toast.LENGTH_LONG).show();
//                }
//
//                if (versucurdet!=null ){
//                    Toast.makeText(this, "Ya se guardo registro.", Toast.LENGTH_LONG).show();
//                }
            }catch (Exception ex) {
                Toast toast = Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
//        if (view.getId()==R.id.btn_sync){
//            String sFichero =  Environment.getExternalStorageDirectory().getPath()+"/nueva.db";
//            File fichero = new File(sFichero);
//
//            if (fichero.exists()){
//                System.out.println("El fichero " + sFichero + " existe");
//            }
//
//            else
//                System.out.println("Pues va a ser que no");
//        }
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
