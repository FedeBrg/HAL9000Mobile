package hci.hal9000;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.v4.content.FileProvider.getUriForFile;

public class CreateDevice extends AppCompatActivity {
    private String deviceName;
    private String deviceType;
    private Uri photoUri;
    private static final String TAKE_PHOTO_TAG = "Take Photo"; //getString(R.string.takephoto);
    private static final int REQUEST_TAKE_PHOTO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_device);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.createnewroom));
        }


        Button done = findViewById(R.id.create_new_device);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceName = ((EditText) findViewById(R.id.device_name)).getText().toString();
                deviceType = ((Spinner) findViewById(R.id.device_type)).getSelectedItem().toString().toLowerCase().replaceAll(" ","_");

                Device device = new Device();
                String[] toPut = getTypeID(deviceType);
                device.setMeta(toPut[1]);
                device.setTypeId(toPut[0]);
                device.setName(deviceName);

                Api.getInstance(getApplicationContext()).addDevice(device, new Response.Listener<Device>() {
                    @Override
                    public void onResponse(Device response) {
//                        Intent intent = new Intent(CreateDevice.this,HomeScreen.class);
//                        startActivity(intent);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                });
            finish();

            }

        });


    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.qr_menu:
                Intent intent = new Intent(CreateDevice.this,QRReader.class);
                startActivityForResult(intent,1);

                //finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == 1) {
                    finish();
                }
                break;
            }
        }
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.qr_menu,menu);
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
        String text = getString(R.string.connectionError);
        if (response != null)
            text += " " + response.getDescription().get(0);

        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    String[] getTypeID(String device){
        String[] toReturn = new String[2];
        if(device.compareTo("light") == 0 || device.compareTo("luz") == 0){
            toReturn[0] = "go46xmbqeomjrsjr";
            toReturn[1] = "light";
            return toReturn;
        }

        else if(device.compareTo("oven") == 0 || device.compareTo("horno") == 0){
            toReturn[0] = "im77xxyulpegfmv8";
            toReturn[1] = "oven";
            return toReturn;
        }
        else if(device.compareTo("curtains") == 0){
            toReturn[0] = "eu0v2xgprrhhg41g";
            toReturn[1] = "curtains";
            return toReturn;
        }

        else if(device.compareTo("air_conditioner") == 0 || device.compareTo("aire_acondicionado") == 0){
            toReturn[0] = "li6cbv5sdlatti0j";
            toReturn[1] = "air_conditioner";
            return toReturn;
        }
        else if(device.compareTo("timer") == 0 || device.compareTo("temporizador") == 0){
            toReturn[0] = "ofglvd9gqX8yfl3l";
            toReturn[1] = "timer";
            return toReturn;
        }
        else if(device.compareTo("alarm") == 0 || device.compareTo("alarma") == 0){
            toReturn[0] = "mxztsyjzsrq7iaqc";
            toReturn[1] = "alarm";
            return toReturn;
        }
        else if(device.compareTo("door") == 0 || device.compareTo("puerta") == 0){
            toReturn[0] = "lsf78ly0eqrjbz91";
            toReturn[1] = "door";
            return toReturn;
        }
        else if(device.compareTo("fridge") == 0 || device.compareTo("heladera") == 0){
            toReturn[0] = "rnizejqr2di0okho";
            toReturn[1] = "fridge";
            return toReturn;
        }
        else{
            return null;
        }
    }



}
