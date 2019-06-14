package hci.hal9000;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class CreateDevice extends AppCompatActivity {
    private String deviceName;
    private String deviceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_device);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);

            actionBar.setTitle("Create new device");
        }


        Button done = findViewById(R.id.create_new_device);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceName = ((EditText) findViewById(R.id.device_name)).getText().toString();
                deviceType = ((Spinner) findViewById(R.id.device_type)).getSelectedItem().toString().toLowerCase().replaceAll(" ","_");
                Device device = new Device();
                device.setMeta(deviceType);
                device.setName(deviceName);
                device.setTypeId(getTypeID(deviceType));
                Api.getInstance(getApplicationContext()).addDevice(device, new Response.Listener<Device>() {
                    @Override
                    public void onResponse(Device response) {
                        Intent intent = new Intent(CreateDevice.this,HomeScreen.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                });

            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
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

        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    String getTypeID(String device){
        if(device.compareTo("light") == 0){
            return "go46xmbqeomjrsjr";
        }
        else if(device.compareTo("curtains") == 0){
            return "eu0v2xgprrhhg41g";
        }
        else if(device.compareTo("air_conditioner") == 0){
            return "li6cbv5sdlatti0j";
        }
        else if(device.compareTo("oven") == 0){
            return "im77xxyulpegfmv8";
        }
        else if(device.compareTo("timer") == 0){
            return "ofglvd9gqX8yfl3l";
        }
        else if(device.compareTo("alarm") == 0){
            return "mxztsyjzsrq7iaqc";
        }
        else if(device.compareTo("door") == 0){
            return "lsf78ly0eqrjbz91";
        }
        else if(device.compareTo("fridge") == 0){
            return "rnizejqr2di0okho";
        }
        else{
            return null;
        }
    }

}
