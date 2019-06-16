package hci.hal9000;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class CheckNotification extends BroadcastReceiver {
    final static String GROUP_DEVICES = "group_devices";
    int NOTIFICATION_ID = 1;
    Context myContext;
    String deviceName;
    Event event;
    @Override
    public void onReceive(final Context context, Intent intent) {
        myContext = context;
        Api.getInstance(context).getEvents(new Response.Listener<ArrayList<Event>>() {
            @Override
            public void onResponse(ArrayList<Event> response) {
                if(response.size() != 0) {
                    if(!HomeScreen.isAppRunning){
                        Log.i("RESPONSE-SIZE", String.format("%d",response.size()));
                        for(int i = 0; i < response.size(); i++){
                            event = response.get(i);
                            Api.getInstance(context).getDevice(event.deviceId, new Response.Listener<Device>() {
                                @Override
                                public void onResponse(Device response) {
                                    sendNotification(myContext, "Noticias en tu casa!", getMessage(response.getName(), event.event));

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });

                        }
                    }
                    else{
                        Event event;
                        Log.i("RESPONSE-SIZE", String.format("%d",response.size()));
                        for(int i = 0; i < response.size(); i++){
                            event = response.get(i);
                            Api.getInstance(context).getDevice(event.deviceId, new Response.Listener<Device>() {
                                @Override
                                public void onResponse(Device response) {
                                    Toast.makeText(myContext, String.format("Device %s has been modified!", response.getName()), Toast.LENGTH_SHORT).show();

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });

                        }
                    }
                }
                else{
                    Log.i("EVENTO", "no encontre nada");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        });
    }


    public void sendNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "default")
                .setContentTitle(title)
                .setContentText(message)
                .setGroup(GROUP_DEVICES)
                .setSmallIcon(R.mipmap.ic_launcher_round);

        notificationManager.notify(NOTIFICATION_ID, notification.build());
        NOTIFICATION_ID++;
    }

    private void handleError(VolleyError error) {
        Error response = null;

        NetworkResponse networkResponse = error.networkResponse;
        if ((networkResponse != null) && (error.networkResponse.data != null)) {
            try {
                String json = new String(
                        error.networkResponse.data,
                        HttpHeaderParser.parseCharset(networkResponse.headers));

                JSONObject jsonObject = new JSONObject(json);
                json = jsonObject.getJSONObject("error").toString();

                Gson gson = new Gson();
                response = gson.fromJson(json, Error.class);
            } catch (JSONException e) {
            } catch (UnsupportedEncodingException e) {
            }
        }

        Log.e("Testing", error.toString());
        //String text = getResources().getString(R.string.error_message);
        String text = "Connection error."; //Parametrizar en Strings
        if (response != null)
            text += " " + response.getDescription().get(0);

    }

    //CHEQUEAR SI ESE DEVICE QUE TENGO EN EL ONRESPONSE ESTA RETORNANDO BIEN,
    //EN LA APP LLEGA CON UN NULL
    private void getDeviceName(Context context, String deviceId){
        Api.getInstance(context).getDevice(deviceId, new Response.Listener<Device>() {
            @Override
            public void onResponse(Device response) {
                Log.i("DEVICE", response.getName());
            }

    }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        });
    }

    private String getMessage(String device, String event){
        switch(event){
            case "statusChanged":
                return String.format("%s status has been modified", device);
            case "temperatureChanged":
                return String.format("%s temperature has been modified", device);
            case "heatChanged":
                return String.format("%s heat has been modified", device);
            case "grillChanged":
                return String.format("%s grill has been modified", device);
            case "convectionChanged":
                return String.format("%s grill has been modified", device);
            case "colorChanged":
                return String.format("The color of %s has been modified", device);
            case "brightnessChanged":
                return String.format("The brightness of %s has been modified", device);
            case "modeChanged":
                return String.format("The mode of %s has been modified", device);
            case "verticalSwingChanged":
                return String.format("The vertical swing of %s has been modified", device);
            case "horizontalSwingChanged":
                return String.format("The horizontal swing of %s has been modified", device);
            case "fanSpeedChanged":
                return String.format("The fan speed of %s has been modified", device);
            case "lockChanged":
                return String.format("%s lock has been modified", device);
            case "freezerTemperatureChanged":
                return String.format("%s freezer temperature has been modified", device);
            default:
                return "";

        }
    }

    private ArrayList<String> getActiveNotifications(){
        SharedPreferences pref =  myContext.getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        ArrayList<String> ret = new ArrayList<>();
        if(pref.getBoolean("light_notif",true)){
            ret.add("go46xmbqeomjrsjr");
        }
        if(pref.getBoolean("oven_notif",true)){
            ret.add("im77xxyulpegfmv8");
        }
        if(pref.getBoolean("fridge_notif",true)){
            ret.add("rnizejqr2di0okho");
        }
        if(pref.getBoolean("air_notif",true)){
            ret.add("li6cbv5sdlatti0j");
        }
        if(pref.getBoolean("door_notif",true)){
            ret.add("lsf78ly0eqrjbz91");

        }
        if(pref.getBoolean("curtains_notif",true)){
            ret.add("eu0v2xgprrhhg41g");

        }

        return ret;
    }
}
