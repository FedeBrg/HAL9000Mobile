package hci.hal9000;

import hci.hal9000.R;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
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
import yuku.ambilwarna.AmbilWarnaDialog;

public class LightDetails extends AppCompatActivity {
    int color =0xFFFFFF;
    SeekBar sb;
    Button color_btn;
    Switch onoff;
    int oldColor;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_details);
        id = getIntent().getStringExtra("id");
        Log.i("Test API",String.format("Recibi ID: %s",id));
        sb = findViewById(R.id.light_sb);
        onoff = findViewById(R.id.light_toggle);
        color_btn = findViewById(R.id.color_button);

        onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    sb.setEnabled(isChecked);
                    color_btn.setEnabled(isChecked);
            }
        });

        Api.getInstance(getApplicationContext()).getDeviceStatus(id, new Response.Listener<Map<String,String>>() {
            @Override
            public void onResponse(Map<String,String> response) {
                Log.i("Light API",String.format("Recibi %s %s %s: ",response.get("color"),response.get("status"),response.get("brightness")));
                //change_background(Integer.parseInt("ff" + String.format("%x",Integer.parseInt(response.get("color")),16),16));
                //change_background(Integer.parseInt("ff"+response.get("color"),16));
                //BigInteger bg = new BigInteger(Integer.parseInt(response.get("color"),16) + 427819008*10);
                oldColor = Integer.parseInt(response.get("color"),16);
                change_background(Integer.parseInt(response.get("color"),16)+427819008*10);
                //Log.i("color",String.format("%d",Integer.parseInt(response.get("color"),16)));

                sb.setProgress(Integer.parseInt(response.get("brightness")));
                if(response.get("status").compareTo("off") == 0){
                    // Log.i("Lights","Estan apagadas");
                    onoff.setChecked(false);
                }
                else{
                    onoff.setChecked(true);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("Test API",String.format("Recibi: "));
            }
        });




        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getIntent().getCharSequenceExtra("name"));
        }


        color_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(v.getContext(), 000000, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        // color is the color selected by the user.
                        Log.i("TOAST", getString(R.string.notif, "XD"));
                        Log.i("color",String.format("%x",color));
                        LightDetails.this.color = color;
                        change_background(color);
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // cancel was selected by the user
                    }
                });
                dialog.show();
            }
        });

        Button done = (Button) findViewById(R.id.done_light);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api.getInstance(getApplicationContext()).setDeviceStatusString(id,"setColor", Arrays.asList(String.format("%x",color).substring(2)), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Lights","Error de color");
                        handleError(error);
                    }
                });
                Log.i("Lights",String.format("%d",sb.getProgress()));
                ArrayList<Integer> ar = new ArrayList<>();
                ar.add(sb.getProgress());
                Api.getInstance(getApplicationContext()).setDeviceStatusInteger(id, "setBrightness",ar , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Lights","Bright Error");
                        handleError(error);

                    }
                });

                Api.getInstance(getApplicationContext()).setDeviceStatusBoolean(id,onoff.isChecked()?"turnOn":"turnOff", new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        Intent intent = new Intent(LightDetails.this,HomeScreen.class);
                        startActivity(intent);
                        finish();
                    }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Lights","On Error");

                        }
                    });
            }
        });
    }

    void change_background(int color){
        this.getWindow().findViewById(R.id.color_button).setBackgroundColor(color);
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
        String text ;//= "Connection error."; //Parametrizar en Strings
        if (response != null)
            text = response.getDescription().get(0);

        //Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        Log.i("Light",error.toString());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history:
                Intent intent = new Intent(LightDetails.this,DeviceHistory.class);
                //startActivityForResult(intent,1);
                //Log.i("DeviceLogs","Menu")
                intent.putExtra("id",id);
                startActivity(intent);
                //finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
