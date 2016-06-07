package cv.andevelopnica.com.crediveloz;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LobbyActivity extends AppCompatActivity implements View.OnClickListener{
    private Button ir_gastos;
    private Button ir_ruta;
    private Button ir_clientes;
    private Button ir_reportefinal;
    private Button ir_desembolso;
    private DatabaseAccess databaseAccess ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        //inicializando botones
        ir_gastos = (Button) findViewById(R.id.btn_gastos);
        ir_ruta = (Button) findViewById(R.id.btn_sucursal);
        ir_clientes = (Button) findViewById(R.id.btn_clientes);
        ir_reportefinal = (Button) findViewById(R.id.btn_reportef);
        ir_desembolso = (Button) findViewById(R.id.btn_desembolso);

        //Asignando Listeners
        ir_gastos.setOnClickListener(this);
        ir_ruta.setOnClickListener(this);
        ir_clientes.setOnClickListener(this);
        ir_reportefinal.setOnClickListener(this);
        ir_desembolso.setOnClickListener(this);

        ComprobarLleva();

    }

    @Override
    public void onClick(View v) {
        //Condicionando botones
        if (v.getId()==R.id.btn_gastos){
            Intent i_gastos = new Intent(this, GastosActivity.class );
            startActivity(i_gastos);
        }

        if (v.getId()==R.id.btn_clientes){
            Intent i_pagos = new Intent(this, ProgramacionActvity.class );
            startActivity(i_pagos);
        }

        if (v.getId()==R.id.btn_sucursal){
            Intent i_ruta = new Intent(this, CobroSucursalActivity.class );
            startActivity(i_ruta);
        }

        if (v.getId()==R.id.btn_reportef){
            Intent i_ruta = new Intent(this, DetalleFinalActivity.class );
            startActivity(i_ruta);
        }

        if (v.getId()==R.id.btn_desembolso){
            Intent i_ruta = new Intent(this, DesembolsoSucursalActivity.class );
            startActivity(i_ruta);
        }

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
}
