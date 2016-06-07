package cv.andevelopnica.com.crediveloz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayAdapter extends BaseAdapter {
    private Context mContext;
    //list fields to be displayed
    private ArrayList<String> cid;
    private ArrayList<String> abono;
    private ArrayList<String> operacion;


    public DisplayAdapter(Context c, ArrayList<String> cid, ArrayList<String> abono, ArrayList<String> operacion) {
        this.mContext = c;
//transfer content from database to temporary memory
        this.cid = cid;
        this.abono = abono;
        this.operacion = operacion;
    }

    public int getCount() {
// TODO Auto-generated method stub
        return cid.size();
    }

    public Object getItem(int position) {
// TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
// TODO Auto-generated method stub
        return 0;
    }

    public View getView(int pos, View child, ViewGroup parent) {
        Holder mHolder;
        LayoutInflater layoutInflater;
        if (child == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.listcell, null);
            mHolder = new Holder();

//link to TextView
            mHolder.txtcid = (TextView) child.findViewById(R.id.txtcid);
            mHolder.txtabono = (TextView) child.findViewById(R.id.txtabono);
            mHolder.txtoperacion = (TextView) child.findViewById(R.id.txtoperacion);
            child.setTag(mHolder);
        } else {
            mHolder = (Holder) child.getTag();
        }
//transfer to TextView in screen
        mHolder.txtcid.setText(cid.get(pos));
        mHolder.txtabono.setText(abono.get(pos));
        mHolder.txtoperacion.setText(operacion.get(pos));


        return child;
    }

    public class Holder {
        TextView txtcid;
        TextView txtabono;
        TextView txtoperacion;
    }

}
