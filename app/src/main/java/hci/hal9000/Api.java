package hci.hal9000;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Api {
    private static Api instance;
    private static RequestQueue requestQueue;
    private static String URL = "http://10.0.2.2:8080/api/";
    private static Context myContext;

    private Api(Context context){
        this.requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        SharedPreferences pref = context.getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(!pref.contains("ip")){
            SharedPreferences.Editor ed = pref.edit();
            ed.putString("ip",URL);
            ed.apply();
        }
        else{
            URL = pref.getString("ip",URL);
        }
    }

    public static void setIP(String ip){

        URL = "http://" + ip +"/api/";
    }
    
    public static synchronized Api getInstance(Context context){
        if(instance == null){
            instance = new Api(context);
            myContext = context;
        }
        SharedPreferences pref = context.getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        URL = pref.getString("ip",URL);
        Log.i("IP",URL);
        return instance;
    }

    public void addRoom(Room room, Response.Listener<Room> listener,Response.ErrorListener errorListener){
        String url = URL + "rooms";
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        GsonRequest<Room,Room> request = new GsonRequest<>(Request.Method.POST,url,room,"room",new TypeToken<Room>(){},headers,listener,errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        //return uuid;
    }

    public String getRooms(Response.Listener<ArrayList<Room>> listener, Response.ErrorListener errorListener){
        Log.i("TABLET",String.format("EL url es: %s",URL));
        String url = URL + "rooms/";
        GsonRequest<Object,ArrayList<Room>> request = new GsonRequest<>(Request.Method.GET,url,null,"rooms",new TypeToken<ArrayList<Room>>(){}, null, listener,errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String addDevice(Device device, Response.Listener<Device> listener,Response.ErrorListener errorListener){
        String url = URL + "devices";
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        GsonRequest<Device,Device> request = new GsonRequest<>(Request.Method.POST,url,device,"device",new TypeToken<Device>(){},headers,listener,errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String getDevices(Response.Listener<ArrayList<Device>> listener,Response.ErrorListener errorListener){
        String url = URL + "devices/";
        GsonRequest<Object,ArrayList<Device>> request = new GsonRequest<>(Request.Method.GET,url,null,"devices",new TypeToken<ArrayList<Device>>(){},null,listener,errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String getEvents(Response.Listener<ArrayList<Event>> listener, Response.ErrorListener errorListener){
        String url = URL + "devices/events";
        String uuid = UUID.randomUUID().toString();
        GetEvents request = new GetEvents(Request.Method.GET, url, listener, errorListener);
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String getDeviceEvent(String id,Response.Listener<ArrayList<Event>> listener, Response.ErrorListener errorListener){
        String url = URL + "devices/" +id + "/events";
        String uuid = UUID.randomUUID().toString();
        GetEvents request = new GetEvents(Request.Method.GET, url, listener, errorListener);
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String getDeviceStatus(String id,Response.Listener<Map<String,String>> listener,Response.ErrorListener errorListener){
        String url = URL + "devices/" + id +"/getState";
        Log.i("Test API",String.format("URL: %s ",url));
        GsonRequest<Object, Map<String,String>> request = new GsonRequest<Object, Map<String,String>>(Request.Method.PUT, url, null, "result", new TypeToken<Map<String,String>>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String setDeviceStatusBoolean(String id, String action, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices/"+id+"/"+action;
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        GsonRequest<Object,Boolean> request = new GsonRequest<>(Request.Method.PUT,url,null,"result",new TypeToken<Boolean>(){},headers,listener,errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        String toSend = myContext.getString(R.string.deviceUpdated);
        Toast.makeText(myContext, toSend, Toast.LENGTH_LONG).show();
        return uuid;
    }

    public String setDeviceStatusInteger(String id, String action, List<Integer> params, Response.Listener<String> listener, Response.ErrorListener errorListener){
        String url = URL + "devices/"+id+"/"+action;
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        GsonRequest<List<Integer>,String> request = new GsonRequest<>(Request.Method.PUT,url,params,"result",new TypeToken<String>(){},headers,listener,errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        String toSend = myContext.getString(R.string.deviceUpdated);
        Toast.makeText(myContext, toSend, Toast.LENGTH_LONG).show();
        return uuid;
    }

    public String setDeviceStatusString(String id, String action, List<String> params, Response.Listener<String> listener, Response.ErrorListener errorListener){
        String url = URL + "devices/"+id+"/"+action;
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        GsonRequest<List<String>,String> request = new GsonRequest<>(Request.Method.PUT,url,params,"result",new TypeToken<String>(){},headers,listener,errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        String toSend = myContext.getString(R.string.deviceUpdated);
        Toast.makeText(myContext, toSend, Toast.LENGTH_LONG).show();
        return uuid;
    }

    public String getRoutines(Response.Listener<ArrayList<Routine>> listener, Response.ErrorListener errorListener){
        String url = URL + "routines";
        GsonRequest<Object,ArrayList<Routine>> request = new GsonRequest<>(Request.Method.GET,url,null,"routines",new TypeToken<ArrayList<Routine>>(){},null,listener,errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String executeRoutine(String id, Response.Listener<ArrayList<Boolean>> listener, Response.ErrorListener errorListener){
        String url = URL+"routines/" + id +"/execute";
        GsonRequest<Object,ArrayList<Boolean>> request = new GsonRequest<>(Request.Method.PUT,url,null,"result",new TypeToken<ArrayList<Boolean>>(){},null,listener,errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }


    public String getDevice(String id,Response.Listener<Device> listener,Response.ErrorListener errorListener){
        String url = URL + "devices/"+id;
        GsonRequest<Object,Device> request = new GsonRequest<>(Request.Method.GET,url,null,"device",new TypeToken<Device>(){},null,listener,errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String getDeviceLogs(String id,Response.Listener<ArrayList<DeviceLog>> listener,Response.ErrorListener errorListener){
        String url = URL + "devices/"+id+"/logs/limit/10/offset/{offset}";
        GsonRequest<Object,ArrayList<DeviceLog>> request = new GsonRequest<>(Request.Method.GET,url,null,"deviceLogs",new TypeToken<ArrayList<DeviceLog>>(){},null,listener,errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

}

