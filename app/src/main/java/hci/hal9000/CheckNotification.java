package hci.hal9000;

import hci.hal9000.R;
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
                            final ArrayList<String> desiredDevices = getActiveNotifications();
                            Api.getInstance(context).getDevice(event.deviceId, new Response.Listener<Device>() {
                                @Override
                                public void onResponse(Device response) {
                                    if(desiredDevices.contains(response.getTypeId())){
                                        String toSend = myContext.getString(R.string.notificationTitle);
                                        sendNotification(myContext, toSend, getMessage(response.getName(), event.event));
                                    }
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
                                    String toSend = myContext.getString(R.string.externNotification, response.getName());
                                    Toast.makeText(myContext, toSend, Toast.LENGTH_SHORT).show();

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {}
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
        String text = myContext.getString(R.string.connectionError);
        if (response != null)
            text += " " + response.getDescription().get(0);

    }

    private String getMessage(String device, String event){
        switch(event){
            case "statusChanged":
                return myContext.getString(R.string.statusChanged, device);
            case "temperatureChanged":
                return myContext.getString(R.string.temperatureChanged, device);
            case "heatChanged":
                return myContext.getString(R.string.heatChanged, device);
            case "grillChanged":
                return myContext.getString(R.string.grillChanged, device);
            case "convectionChanged":
                return myContext.getString(R.string.convectionChanged, device);
            case "colorChanged":
                return myContext.getString(R.string.colorChanged, device);
            case "brightnessChanged":
                return myContext.getString(R.string.brightnessChanged, device);
            case "modeChanged":
                return myContext.getString(R.string.modeChanged, device);
            case "verticalSwingChanged":
                return myContext.getString(R.string.verticalSwingChanged, device);
            case "horizontalSwingChanged":
                return myContext.getString(R.string.horizontalSwingChanged, device);
            case "fanSpeedChanged":
                return myContext.getString(R.string.fanSpeedChanged, device);
            case "lockChanged":
                return myContext.getString(R.string.lockChanged, device);
            case "freezerTemperatureChanged":
                return myContext.getString(R.string.freezerTemperatureChanged, device);
            default:
                return "";

        }
    }

    private ArrayList<String> getActiveNotifications(){
        SharedPreferences pref =  myContext.getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        ArrayList<String> ret = new ArrayList<>();
        if(!pref.getBoolean("switch_notif",true)){
            return ret;
        }
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
