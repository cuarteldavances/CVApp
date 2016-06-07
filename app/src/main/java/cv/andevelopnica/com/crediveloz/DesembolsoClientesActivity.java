package cv.andevelopnica.com.crediveloz;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DesembolsoClientesActivity extends AppCompatActivity {
    private TextView tvsucur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desembolso_clientes);

        tvsucur = (TextView) findViewById(R.id.tv_sucursal);

        try {

            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
            databaseAccess.open();
            Bundle bundle = getIntent().getExtras();
            tvsucur.setText(bundle.getString("nme"));

            String ID_Sucursal;
            ID_Sucursal = databaseAccess.ObtenerIdSucursal(tvsucur.getText().toString());
            databaseAccess.ObtenerClientes(ID_Sucursal);




            ListView listView = (ListView) findViewById(R.id.listView);



            Cursor cursor = databaseAccess.ObtenerClientes(ID_Sucursal);
            startManagingCursor(cursor);

            String[] from = new String[]{"Nombre"};
            int[] to = new int[]{R.id.text};

            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.row, cursor, from, to);

            listView.setAdapter(cursorAdapter);

            databaseAccess.close();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = ((TextView)view).getText().toString();
                    String LSucursal = tvsucur.getText().toString();
                    Intent i = new Intent(getApplicationContext(), DesembolsoActivity.class );
                    i.putExtra("nme", item);
                    i.putExtra("sucur", LSucursal);
                    startActivity(i);

                    Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
                    finish();

                }
            });


        }
        catch (Exception ex) {
            Toast toast = Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
