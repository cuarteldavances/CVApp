package cv.andevelopnica.com.crediveloz;

/**
 * Created by Javier Qosmio on 16/05/2016.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MyDialog extends DialogFragment{

    LayoutInflater inflater;
    View v;
    EditText montollv;
    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.dialog_lleva,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v).setPositiveButton("Aplicar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                montollv = (EditText) v.findViewById(R.id.edt_montolleva);
                String llevotanto = montollv.getText().toString();
                if(TextUtils.isEmpty(montollv.getText().toString())) {
                    montollv.setError("Este campo es requerido.");
                    restartActivity(v);
                    return;
                }else {
                    databaseAccess.open();
                    databaseAccess.IngresarLleva(Integer.parseInt(llevotanto));
                    databaseAccess.close();
                    Toast toast = Toast.makeText(getActivity(), "Operacion Exitosa!", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        return builder.create();
    }

    public void restartActivity(View view)
    {
        // do your work Here
        Intent intent= new Intent(getContext(), GastosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
