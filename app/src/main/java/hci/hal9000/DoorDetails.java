package hci.hal9000;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class DoorDetails extends AppCompatActivity {
    Switch openClose;
    Switch lockUnlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_details);

        openClose = findViewById(R.id.door_switch);
        lockUnlock = findViewById(R.id.lock_switch);
        final String id = getIntent().getStringExtra("id");


        Button done = findViewById(R.id.done_door);

        Api.getInstance(getApplicationContext()).getDeviceStatus(id, new Response.Listener<Map<String, String>>() {
            @Override
            public void onResponse(Map<String, String> response) {
                setDoorSwitch(response.get("status"));
                setLockSwitch(response.get("lock"));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Api.getInstance(getApplicationContext()).setDeviceStatusBoolean(id, openClose.isChecked() ? "close" : "open", new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        Api.getInstance(getApplicationContext()).setDeviceStatusBoolean(id, lockUnlock.isChecked() ? "lock" : "unlock", new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                handleError(error);
                            }
                        });
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);

                    }
                });
                Intent intent = new Intent(DoorDetails.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setDoorSwitch(String status) {
        if(status.compareTo("closed") == 0){
            openClose.setChecked(true);
        }
        else{
            openClose.setChecked(false);
        }
    }

    private void setLockSwitch(String status) {
        if(status.compareTo("locked") == 0){
            lockUnlock.setChecked(true);
        }
        else{
            lockUnlock.setChecked(false);
        }
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
        String text ;
        if (response != null)
            text = response.getDescription().get(0);
    }
}
