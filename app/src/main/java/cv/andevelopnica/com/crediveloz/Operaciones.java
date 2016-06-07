package cv.andevelopnica.com.crediveloz;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Javier Qosmio on 23/05/2016.
 */
public class Operaciones {


    public String Obtenerfecha() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy"); //"yyyy/MM/dd_HHmmss" o segun el formato que querras bro.
        String currentDateandTime = sdf.format(new Date());

        return currentDateandTime;
    }
}
