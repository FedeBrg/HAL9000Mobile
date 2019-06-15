package hci.hal9000;

import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetEvents extends Request<ArrayList<Event>> {
    private Response.Listener<ArrayList<Event>> listener;

    public GetEvents(int method, String url, Response.Listener<ArrayList<Event>> listener,@Nullable Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
    }

    @Override
    protected Response<ArrayList<Event>> parseNetworkResponse(NetworkResponse response) {
        byte[] data = response.data;
        Log.i("DATA", String.format("%d",data.length));
        String dataString = new String(data);
        String line;
        ArrayList<Event> events = new ArrayList<>();
        Scanner scanner = new Scanner(dataString);

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if(line.contains("deviceId")) {
                Pattern pattern = Pattern.compile("data: \"deviceId\": \"(.*?)\"");
                Matcher matcher = pattern.matcher(line);
                if(matcher.find()){
                    String deviceId = matcher.group(1);
                    line = scanner.nextLine();
                    pattern = Pattern.compile("data: \"event\": \"(.*?)\"");
                    matcher = pattern.matcher(line);
                    if(matcher.find()){
                        String eventName = matcher.group(1);
                        Event event = new Event(deviceId, eventName);
                        events.add(event);
                        Log.i("GET-EVENT", eventName);
                    }
                }
            }
        }

        scanner.close();
        return Response.success(events, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(ArrayList<Event> response) {
        listener.onResponse(response);
    }
}
