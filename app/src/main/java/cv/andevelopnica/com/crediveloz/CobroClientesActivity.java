package cv.andevelopnica.com.crediveloz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cv.andevelopnica.com.crediveloz.listener.OnCustomerListChangedListener;
import cv.andevelopnica.com.crediveloz.listener.OnStartDragListener;
import cv.andevelopnica.com.crediveloz.utilities.SampleData;
import cv.andevelopnica.com.crediveloz.utilities.SimpleItemTouchHelperCallback;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CobroClientesActivity extends AppCompatActivity
        implements OnCustomerListChangedListener,
        OnStartDragListener{

    private Toolbar mToolbar;
    private TextView tvsucur;
    private RecyclerView mRecyclerView;
    private CustomerListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ItemTouchHelper mItemTouchHelper;
    private List<Customer> mCustomers;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    public static final String LIST_OF_SORTED_DATA_ID = "json_list_sorted_data_id";
    public final static String PREFERENCE_FILE = "preference_file";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cobro_clientes);

        //setSupportActionBar(mToolbar);
        tvsucur = (TextView) findViewById(R.id.tv_sucursal);
        Bundle bundle = getIntent().getExtras();
        tvsucur.setText(bundle.getString("nme"));

        mSharedPreferences = this.getApplicationContext()
                .getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        try{
            setupRecyclerView();
        }
        catch (Exception ex) {
            Toast toast = Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }

    }


    private void setupRecyclerView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.note_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mCustomers = getSampleData();


        //setup the adapter with empty list
        mAdapter = new CustomerListAdapter(mCustomers, this, this, this);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.colorPrimaryDark)
                .size(2)
                .build());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView item = (TextView) view.findViewById(R.id.text_view_customer_name);
                        String LSucursal = tvsucur.getText().toString();
                        Intent intent = new Intent(getApplicationContext(), CobrosActivity.class);
                        //Comproando Nombre y posicion seleccionada
                        //Toast.makeText(getBaseContext(), item.getText().toString()+String.valueOf(position), Toast.LENGTH_LONG).show();

                        intent.putExtra("nme", item.getText().toString());
                        intent.putExtra("sucur", LSucursal);
                        startActivity(intent);


                    }
                })
        );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNoteListChanged(List<Customer> customers) {
        //after drag and drop operation, the new list of Customers is passed in here

        //create a List of Long to hold the Ids of the
        //Customers in the List
        List<Long> listOfSortedCustomerId = new ArrayList<Long>();

        for (Customer customer: customers){
            listOfSortedCustomerId.add(customer.getId());
        }

        //convert the List of Longs to a JSON string
        Gson gson = new Gson();
        String jsonListOfSortedCustomerIds = gson.toJson(listOfSortedCustomerId);


        //save to SharedPreference
        mEditor.putString(LIST_OF_SORTED_DATA_ID, jsonListOfSortedCustomerIds).commit();
        mEditor.commit();
//        String json = new Gson().toJson(mCustomers);
//
//        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
//        databaseAccess.open();
//        String Test = new Gson().toJson(databaseAccess.getAllClients());
//
//        Toast toast = Toast.makeText(this, Test, Toast.LENGTH_SHORT);
//        toast.show();

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);

    }

    private List<Customer> getSampleData(){
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        Integer vendedorS = Integer.parseInt(databaseAccess.ObtenerVendedor());
        //String Test = new Gson().toJson(databaseAccess.getAllClients());

        //Get the sample data
        List<Customer> customerList = databaseAccess.getAllClients(vendedorS);

        //create an empty array to hold the list of sorted Customers
        List<Customer> sortedCustomers = new ArrayList<Customer>();

        //get the JSON array of the ordered of sorted customers
        String jsonListOfSortedCustomerId = mSharedPreferences.getString(LIST_OF_SORTED_DATA_ID, "");


        //check for null
        if (!jsonListOfSortedCustomerId.isEmpty()){

            //convert JSON array into a List<Long>
            Gson gson = new Gson();
            List<Long> listOfSortedCustomersId = gson.fromJson(jsonListOfSortedCustomerId, new TypeToken<List<Long>>(){}.getType());

            //build sorted list
            if (listOfSortedCustomersId != null && listOfSortedCustomersId.size() > 0){
                for (Long id: listOfSortedCustomersId){
                    for (Customer customer: customerList){
                        if (customer.getId().equals(id)){
                            sortedCustomers.add(customer);
                            customerList.remove(customer);
                            break;
                        }
                    }
                }
            }

            //if there are still customers that were not in the sorted list
            //maybe they were added after the last drag and drop
            //add them to the sorted list
            if (customerList.size() > 0){
                sortedCustomers.addAll(customerList);
            }

            return sortedCustomers;
        }else {
            return customerList;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
//            Toast toast = Toast.makeText(this, "Atras...", Toast.LENGTH_SHORT);
//            toast.show();
            Intent intent3 = new Intent(this,GastosActivity.class);
            startActivity(intent3);

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
//para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }



}