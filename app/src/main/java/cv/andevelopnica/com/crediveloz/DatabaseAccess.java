package cv.andevelopnica.com.crediveloz;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess extends Activity {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    public static EditText et1;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public List<String> getQuotes() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT fecha FROM gastos", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public String getPhoneName() {
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        String deviceName = myDevice.getName();
        return deviceName;
    }


    public String ObtenerIdSucurGasto(String lsucursal){
        Cursor cursor;
        cursor = database.rawQuery("select id_sucursal from sucursal where sucursal = '"+lsucursal+"'",null);

        cursor.moveToFirst();

        String foundidvendedor =cursor.getString(0);

        return  foundidvendedor;
    }


    public List<String> getAllLabels(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        Cursor cursor = database.rawQuery("select id_sucursal as _id, sucursal as Nombre from sucursal",null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();

        // returning lables
        return labels;
    }



    public void IsertarGasto(Integer idgasto, Integer sucursal, Integer vendedor, Integer gasolina, Integer otros, String detalle){
        database.execSQL("INSERT INTO gastos (id_gasto, id_sucursal, id_vendedor, fecha, efectivo, gasto, basediaria, prestamos, totald, mora, efectivoreal, " +
                "sobrante, faltante, depositar, refunds, procesa, trabajo, g_arrenda, g_sueldo, g_gasolina, g_telefono, g_personal, g_otros, g_detalle, " +
                "saldo, ctascobrar, creadoel, creadopor, creadoen) Values("+idgasto+", "+sucursal+", "+vendedor+", date('now', 'localtime'), 0, "+(gasolina+otros)+", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, date('now', 'localtime'), " +
                "0, 0, "+gasolina+", 0, 0, "+otros+",'"+detalle+"', 0, 0, datetime(), '"+getPhoneName()+"' ,'"+android.os.Build.MODEL+"');");
    }

    public String ComprobarGasto(){
        Cursor cursor;
        String foundgasto = null;
        cursor = database.rawQuery("select gasto from gastos where fecha = date('now', 'localtime')",null);
        if( cursor != null && cursor.moveToFirst() ){
        foundgasto =cursor.getString(0);
            cursor.close();
        }
        else {
            foundgasto = null;
        }
        return  foundgasto;
    }

    public String Comprobarcobro(Integer iDcartera){
        Cursor cursor;
        String foundcobro = null;
        cursor = database.rawQuery("select abono from cobros where id_cartera = "+iDcartera+" and operacion = date('now', 'localtime')",null);
        if( cursor != null && cursor.moveToFirst() ){
            foundcobro =cursor.getString(0);
            cursor.close();
        }
        else {
            foundcobro = null;
        }
        return  foundcobro;
    }

    public void ActualizarCobro(Integer iDcartera, Integer Aabono){
        database.execSQL("update cobros set saldo = anterior - "+Aabono+", abono = "+Aabono+" where id_cartera = "+iDcartera+" and operacion = date('now', 'localtime')");
    }

    public void ActualizarGasto(Integer gasolina, Integer otros){
        database.execSQL("update gastos set gasto = gasto + "+gasolina+" + "+otros+" , g_gasolina = g_gasolina + "+gasolina+", g_otros = g_otros + "+otros+" where fecha = date('now', 'localtime')");
    }



    public  String md5( String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return "";
        }
    }


    public String login(String etusername, String etpasswd){
        Cursor cursor;

        cursor = database.rawQuery("SELECT login FROM usuario where login = '" + etusername + "' and password = '" + etpasswd + "'",null);
        cursor.moveToFirst();

        String found =cursor.getString(0);

        return  found;
    }

    public String ObtenerGastoHoy(){
        Cursor cursor;
        cursor = database.rawQuery("SELECT SUM(gasto) as totalhoy FROM gastos WHERE fecha = date('now', 'localtime')",null);
        //return database.rawQuery("SELECT login FROM usuario where login = '" + etusername + "' and password = '" + etpasswd + "'",null).toString();
        cursor.moveToFirst();

        String foundhoy =cursor.getString(0);

        return  foundhoy;

    }


    public String BuscarIdCartera(String bnombre){
        Cursor cursor;
        cursor = database.rawQuery("SELECT MAX(id_cartera) AS Mayor_ID FROM cartera cart Inner Join cliente cli on cart.id_cliente = cli.id_cliente where nombre like '%"+bnombre+"%' and cart.incobrable = 0 and cart.id_estado = 1",null);

        cursor.moveToFirst();

        String foundIdCart =cursor.getString(0);

        return  foundIdCart;
    }


    public String BuscarCliente(String bnombre){
        Cursor cursor;
        cursor = database.rawQuery("select nombre from cliente where nombre like '%"+bnombre+"%'",null);

        cursor.moveToFirst();

        String foundnommbre =cursor.getString(0);

        return  foundnommbre;
    }

    public String ObtenerDireccionCiente(String bnombre){
        Cursor cursor;
        cursor = database.rawQuery("select domicilio from cliente where nombre like '%"+bnombre+"%'",null);

        cursor.moveToFirst();

        String founddir =cursor.getString(0);

        return  founddir;
    }

    public String ObtenerTelefonoCiente(String bnombre){
        Cursor cursor;
        cursor = database.rawQuery("select telefono from cliente where nombre like '%"+bnombre+"%'",null);

        cursor.moveToFirst();

        String foundtel =cursor.getString(0);

        return  foundtel;
    }

    public String BuscarMonto(String tv_monto){
        Cursor cursor;
        cursor = database.rawQuery("select montototal from cartera where id_cartera = "+tv_monto+"",null);

        cursor.moveToFirst();

        String foundmonto =cursor.getString(0);

        return  foundmonto;
    }

    public String BuscarCuota(String tv_idcartera){
        Cursor cursor;
        cursor = database.rawQuery("select cuota from cartera where id_cartera = "+tv_idcartera+"",null);

        cursor.moveToFirst();

        String foundcuota =cursor.getString(0);

        return  foundcuota;
    }

    public String BuscarSaldoActual(String tv_idcartera){
        Cursor cursor;
        cursor = database.rawQuery("select saldo from cartera where id_cartera = "+tv_idcartera+"",null);

        cursor.moveToFirst();

        String foundcuota =cursor.getString(0);

        return  foundcuota;
    }

    public Cursor ObtenerClientes(String lugar){

        return database.rawQuery("select cli.id_cliente AS _id, nombre as Nombre \n" +
                "From cliente cli Inner Join cartera cart on cli.id_cliente = cart.id_cliente \n" +
                "Where  cart.id_estado = 1 and cart.id_sucursal = "+lugar+" and cart.incobrable = 0 and cart.saldo > 0 ORDER BY nombre", null);

    }


    public Cursor ObtenerSucursal(){

        return database.rawQuery("select id_sucursal as _id, sucursal as Nombre from sucursal",null);

    }

    public String ObtenerIdSucursal(String bnombre){
        Cursor cursor;
        cursor = database.rawQuery("SELECT id_sucursal FROM sucursal where sucursal like '%"+bnombre+"%'",null);

        cursor.moveToFirst();

        String foundIdSucursal =cursor.getString(0);

        return  foundIdSucursal;
    }

    public void IngresarAbono(Integer idcobro, Integer idsucursal, Integer idcliente, Integer idcartera, Integer tarjeta, Double santerior, Double abono, Integer mora, Double saldo, Integer cuota, String porsentual, Integer idvendedor){

        database.execSQL("INSERT INTO cobros (id_cobro, id_sucursal, id_cliente, id_cartera, tarjeta, anterior, abono, mora, saldo, cuota, porcentual, operacion, verdadero, procesa, trabajo, operado, rutaimg, creadoel, creadopor, creadoen, modificadoel, modificadopor, modificadoen, id_vendedor)\n" +
                "Values ("+idcobro+", "+idsucursal+", "+idcliente+", "+idcartera+", "+tarjeta+", "+santerior+", "+abono+", "+mora+", "+saldo+", "+cuota+", "+porsentual+", date('now', 'localtime'), 0, 0, date('now', 'localtime'), '', 0,  datetime(),'"+getPhoneName()+"' ,'"+android.os.Build.MODEL+"', "+null+", '', '', "+idvendedor+");");

    }

    public void ActualizarCartera(Double abono, Integer idcartera, Integer idcliente){
        database.execSQL("UPDATE cartera SET abono = abono + "+abono+", saldo = saldo - "+abono+" WHERE id_cartera = "+idcartera+" and id_cliente= "+idcliente+"");

    }

    public void ActualizarCartera2(Integer abono, Integer idcartera, Integer idcliente){
        database.execSQL("UPDATE cartera SET saldo = (select saldo from cobros where id_cartera = "+idcartera+" and operacion = date('now', 'localtime')), abono = montototal - saldo WHERE id_cartera = "+idcartera+" and id_cliente= "+idcliente+"");

    }

    public String ObtenerTarjeta(String idcartera1){
        Cursor cursor;
        cursor = database.rawQuery("select autorizaok from cartera where id_cartera = "+idcartera1+"",null);
        cursor.moveToFirst();
        String foundtarjeta =cursor.getString(0);
        return foundtarjeta;

    }

    public String ObtenerIdCliente(String idcartera){
        Cursor cursor;
        cursor = database.rawQuery("select id_cliente from cartera where id_cartera = "+idcartera+"",null);
        cursor.moveToFirst();
        String foundidcliente =cursor.getString(0);
        return foundidcliente;

    }

    public String ObtenerIdClienteFromCliente(String nombreC){
        Cursor cursor;
        cursor = database.rawQuery("select id_cliente from cliente where nombre = '"+nombreC+"'",null);
        cursor.moveToFirst();
        String foundidcliente2 =cursor.getString(0);
        return foundidcliente2;

    }

    public String aver(){
        Cursor cursor;
        cursor = database.rawQuery("select abono from cartera where id_cliente = 14107 and id_cartera = 22220",null);
        cursor.moveToFirst();
        String foundidcliente =cursor.getString(0);
        return foundidcliente;
    }

    public String CobrosFinalHoy(){
        Cursor cursor;
        cursor = database.rawQuery("select sum(abono) from cobros where operacion  like date('now', 'localtime')",null);
        cursor.moveToFirst();
        String foundcobrohoy =cursor.getString(0);
        return foundcobrohoy;
    }

    public String GastosFinalHoy(){
        Cursor cursor;
        cursor = database.rawQuery("select sum(gasto) from gastos where fecha like date('now', 'localtime')",null);
        cursor.moveToFirst();
        String foundgastohoy =cursor.getString(0);
        return foundgastohoy;
    }

//    public String DesembolsoFinalHoy(){
//        Cursor cursor;
//        cursor = database.rawQuery("select sum(monto) from cartera where fecha like date('now', 'localtime')",null);
//        cursor.moveToFirst();
//        String foundgastohoy =cursor.getString(0);
//        return foundgastohoy;
//    }

    public String DesembolsoFinalHoy(){
        Cursor cursor;
        cursor = database.rawQuery("select sum(monto) from cartera where fecha like date('now', 'localtime')",null);
        cursor.moveToFirst();
        String foundgastohoy =cursor.getString(0);
        return foundgastohoy;
    }

    public Integer IngresarMora(String abono, String ccuota){

        Integer Moracliente = null;
        if (Integer.parseInt(abono) <= 0){
            Moracliente = Integer.parseInt(ccuota);
        }else if(Integer.parseInt(abono) > 0){
            Moracliente = 0;
        }

        return Moracliente;
    }

    public String UltimaFechaAbono(String idcartera){
        Cursor cursor;
        cursor = database.rawQuery("select trabajo from cobros where id_cobro =(select MAX(id_cobro) from cobros where id_cartera = "+idcartera+" and abono > 0)",null);
        cursor.moveToFirst();
        String foundultimaf =cursor.getString(0);
        return foundultimaf;
    }

    public ArrayList<String> BuscarTodosClientesActivos() {

        ArrayList<String> arrTblNames = new ArrayList<String>();
        Cursor c = database.rawQuery("select nombre as Nombre \n" +
                "From cliente cli Inner Join cartera cart on cli.id_cliente = cart.id_cliente \n" +
                "Where  cart.id_estado = 1 and cart.id_sucursal = 8 and cart.incobrable = 0 and cart.saldo > 0 ORDER BY nombre", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {

            arrTblNames.add(c.getString(c.getColumnIndex("Nombre")));
            c.moveToNext();
        }
        // make sure to close the cursor
        c.close();
        return arrTblNames;
    }



    public ArrayList<Customer> getAllClients(Integer idvendedor) {

        ArrayList<Customer> clienteList = null;
        try{
            clienteList = new ArrayList<Customer>();

            Cursor cursor = database.rawQuery("select cli.id_cliente as id, nombre as name \n" +
                    "From cliente cli Inner Join cartera cart on cli.id_cliente = cart.id_cliente \n" +
                    "Where  cart.id_estado = 1 and cart.id_vendedor = "+idvendedor+" and cart.incobrable = 0 and cart.saldo > 0 order by nombre", null);
            if(!cursor.isLast())
            {
                while (cursor.moveToNext())
                {
                    Customer cliente = new Customer();
                    cliente.setId(cursor.getLong(0));
                    cliente.setName(cursor.getString(1));
                    clienteList.add(cliente);
                }
            }

        }catch (Exception e){
            Log.e("error",e+"");
        }
        return clienteList;
    }



    public ArrayList<String> BuscarTodosClientes() {

        ArrayList<String> arrTblNames = new ArrayList<String>();
        Cursor c = database.rawQuery("select nombre as Nombre from cliente ORDER BY nombre", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {

            arrTblNames.add(c.getString(c.getColumnIndex("Nombre")));
            c.moveToNext();
        }
        // make sure to close the cursor
        c.close();
        return arrTblNames;
    }

    public String TarjetaMasAlta(){
        Cursor cursor;
        cursor = database.rawQuery("select max(autorizaok) from cartera",null);
        cursor.moveToFirst();
        String foundtarjeta =cursor.getString(0);
        return foundtarjeta;
    }

    public void DemebolsarCliente(Integer idcartera, Integer idsucursal, Integer idcliente, Integer idvendedor , Integer tarjeta, Integer NDdias, Double Cuota, Integer Monto, Double VInteres, Double Mtotal, Double Saldo, Double Tasa, Integer IDactividad){
        database.execSQL("insert into cartera (id_cartera, id_sucursal, id_cliente, id_vendedor, id_territorio, autorizaok,fecha, dias_cre, concepto, cuota, monto, valorinteres, montototal, abono, saldo, tasa, procesa, trabajo, tipo, quien, atraso, incobrable, id_actividad, proceso, creadoel, creadopor, creadoen, modificadoen, modificadopor, modificadoen, id_estado) " +
                " values("+idcartera+", "+idsucursal+", "+idcliente+", "+idvendedor+", 32, "+tarjeta+", date('now', 'localtime'), "+NDdias+", '', "+Cuota+", "+Monto+", "+VInteres+", "+Mtotal+", 0, "+Saldo+", "+Tasa+", 1, date('now', 'localtime'), 1, '', 0, 0, "+IDactividad+", 0, datetime(), 'AND', '"+android.os.Build.MODEL+"', '', '', '', 1)");

    }

//    // solo resitra desembolso para fines de entrega
//    public void DemebolsarCliente(Integer Monto){
//        database.execSQL("insert into carteracel (id_carteracel, monto,fecha) " +
//                " values(-1, "+Monto+", date('now', 'localtime'))");
//
//    }

    public String ObtenerIdCatividad(String nombreC){
        Cursor cursor;
        cursor = database.rawQuery("select id_actividad from cliente where nombre like '%"+nombreC+"%'",null);
        cursor.moveToFirst();
        String foundtarjeta =cursor.getString(0);
        return foundtarjeta;
    }

    public void IngresarLleva(Integer montollv){
        database.execSQL("insert into lleva(id_lleva, montolleva, fecha) values((select min(id_lleva) -1 from lleva), "+montollv+", date('now', 'localtime'))");
    }

    public String VerLleva(){
        String foundlleva = null;
        Cursor cursor;
        cursor = database.rawQuery("select montolleva from lleva where fecha = date('now', 'localtime')",null);
        if (cursor.moveToFirst()) {
            // retrieve values from the cursor
            foundlleva = cursor.getString(0);
        }

        return foundlleva;
    }

    public String ObtenerCajaInicial(){
        Cursor cursor;
        cursor = database.rawQuery("select depositar from sucurdet where id_detalle = (select MAX(id_detalle) from sucurdet)",null);
        cursor.moveToFirst();
        String foundcaja =cursor.getString(0);
        return foundcaja;
    }

    public String ObtenerIdSucur(){
        Cursor cursor;
        cursor = database.rawQuery("select id_sucursal from sucursal",null);
        cursor.moveToFirst();
        String founidsuc =cursor.getString(0);
        return founidsuc;
    }

    public void IngresarDetalleSucursal(Integer idsucursal, Double caja_inicial, Integer cobros, Integer gastos, Double basediaria, Integer prestamos, Double reembolso, Double cajaactual, Double efectivoreal, Double depositar){
        database.execSQL("INSERT INTO sucurdet (trabajo,id_sucursal,capital,cajainicial,cobros,mora,bcoentrada,bcosalida,gastos,basediaria,prestamos,aportes,reembolso,desembolso,cajactual,actual,procesa,trabajos,afectacash,ctascobrar,efectivoreal,efectivo,faltante,sobrante,depositar)\n" +
                "VALUES (date('now', 'localtime'),"+idsucursal+",0,"+caja_inicial+","+cobros+",0,0,0,"+gastos+","+basediaria+","+prestamos+",0,"+reembolso+",0,"+cajaactual+",300000,0,date('now', 'localtime'),0,0,"+efectivoreal+",0,0,0,"+depositar+")");
    }

    public String ComprobarSucurdet(){
        String founsucurdet = null;
        Cursor cursor;
        cursor = database.rawQuery("select trabajo from sucurdet where trabajo like date('now', 'localtime')",null);
        if (cursor.moveToFirst()) {
            // retrieve values from the cursor
            founsucurdet = cursor.getString(0);
        }
        return founsucurdet;
    }

    public Cursor Historial(String idcarterac) {
        Cursor mCursor;
        mCursor = database.rawQuery("select id_cobro, abono, operacion, saldo from cobros where id_cartera = "+idcarterac+"", null);
        return mCursor;
    }

    public void vendedortemporal(String username){
        database.execSQL("CREATE TABLE IF NOT EXISTS vendedoractual  ( id_vendedor integer not null )");
        database.execSQL("INSERT INTO vendedoractual (id_vendedor) SELECT (select id_vendedor from usuario where login = '"+username+"') WHERE NOT EXISTS (SELECT * FROM vendedoractual)");
    }

    public void Insertvendedortemporal(String username){
        database.execSQL("update vendedoractual set id_vendedor = (select id_vendedor from usuario where login = '"+username+"')");
    }

    public String ObtenerVendedor(){
        Cursor cursor;
        cursor = database.rawQuery("select max(id_vendedor) from vendedoractual",null);

        cursor.moveToFirst();

        String foundidvendedor =cursor.getString(0);

        return  foundidvendedor;
    }

    public String ObtenerRuta(){
        Cursor cursor;
        cursor = database.rawQuery("select nombre from usuario where id_vendedor = (select max(id_vendedor) from vendedoractual)",null);

        cursor.moveToFirst();

        String foundidvendedor =cursor.getString(0);

        return  foundidvendedor;
    }


    public Integer ObtenerIdcartera(){
        database.execSQL("update configuracion set id_mincartera = (select min(id_mincartera) -1 from configuracion )");
        Cursor cursor;
        cursor = database.rawQuery("select id_mincartera from configuracion",null);
        cursor.moveToFirst();
        String foundtarjeta =cursor.getString(0);
        return Integer.parseInt(foundtarjeta);

    }

    public Integer ObtenerIdgastos(){
        database.execSQL("update configuracion set id_mingastos = (select min(id_mingastos) -1 from configuracion )");
        Cursor cursor;
        cursor = database.rawQuery("select id_mingastos from configuracion",null);
        cursor.moveToFirst();
        String foundtarjeta =cursor.getString(0);
        return Integer.parseInt(foundtarjeta);

    }

    public Integer ObtenerIdcobros(){
        database.execSQL("update configuracion set id_mincobros = (select min(id_mincobros) -1 from configuracion )");
        Cursor cursor;
        cursor = database.rawQuery("select id_mincobros from configuracion",null);
        cursor.moveToFirst();
        String foundtarjeta =cursor.getString(0);
        return Integer.parseInt(foundtarjeta);

    }

    public Double ComprbarCartera(String idCliente){
        Cursor cursor;
        cursor = database.rawQuery("select sum(saldo) from cartera where id_cliente = "+idCliente+"",null);
        cursor.moveToFirst();
        String foundidCliente =cursor.getString(0);
        return Double.parseDouble(foundidCliente);

    }

}
