package hci.hal9000;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FridgeDetails extends AppCompatActivity {

    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge_details);

        final String id = getIntent().getStringExtra("id");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Fridge");
        }



        View fridgeSeekV = findViewById(R.id.fridge_seek);
        final SeekBar fridgeSB = (SeekBar) fridgeSeekV;
        fridgeSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int fridgeValue = progress;
                TextView fridgeTempTV = findViewById(R.id.fridge_temp);
                fridgeTempTV.setText(String.valueOf(fridgeValue+2));
                spinner = findViewById(R.id.fridge_mode_spinner);
                spinner.setSelection(3);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        View freezerSeekV = findViewById(R.id.freezer_seek);
        final SeekBar freezerSB = (SeekBar) freezerSeekV;
        freezerSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int freezerValue = progress;
                TextView fridgeTempTV = findViewById(R.id.freezer_temp);
                fridgeTempTV.setText(String.valueOf(freezerValue-20));
                spinner = findViewById(R.id.fridge_mode_spinner);
                spinner.setSelection(3);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        Api.getInstance(getApplicationContext()).getDeviceStatus(id, new Response.Listener<Map<String,String>>() {
            @Override
            public void onResponse(Map<String,String> response) {
                freezerSB.setProgress(Integer.parseInt(response.get("freezerTemperature")));
                fridgeSB.setProgress(Integer.parseInt(response.get("temperature")));
                String temp=response.get("mode");

                spinner.setSelection((temp.compareTo("default")==0)?3:((temp.compareTo("Party")==0)?1:2));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("Test API",String.format("Recibi: "));
            }
        });



        Button done = findViewById(R.id.done_fridge);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Api.getInstance(getApplicationContext()).setDeviceStatusInteger(id,"setFreezerTemperature",new ArrayList<Integer>(freezerSB.getProgress()), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Api.getInstance(getApplicationContext()).setDeviceStatusInteger(id, "setTemperature",new ArrayList<Integer>(fridgeSB.getProgress()), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                List<String> lista=new ArrayList<String>();
                                lista.add(spinner.getSelectedItem().toString());
                                Api.getInstance(getApplicationContext()).setDeviceStatusString(id, "setMode",lista, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

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
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);

                    }
                });
                Intent intent = new Intent(FridgeDetails.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
        });
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

