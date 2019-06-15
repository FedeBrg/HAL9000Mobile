package hci.hal9000;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import android.content.Context;

public class VolleySingleton {
    private static VolleySingleton instance;
    private  RequestQueue requestQueue;

    private VolleySingleton(Context context){
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized VolleySingleton getInstance(Context context){
        if(instance == null){
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue(){
        return requestQueue;
    }

}
