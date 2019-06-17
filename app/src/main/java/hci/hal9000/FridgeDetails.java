package hci.hal9000;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    SeekBar fridgeSB;
    SeekBar freezerSB;
    String id;
    TextView fridgeTempTV;
    TextView freezerTempTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge_details);


        String name = getIntent().getStringExtra("name");


        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(name);

        }

        id = getIntent().getStringExtra("id");
        freezerSB = findViewById(R.id.freezer_seek);
        fridgeSB = findViewById(R.id.fridge_seek);
        fridgeTempTV = findViewById(R.id.fridge_temp);
        freezerTempTV = findViewById(R.id.freezer_temp);
        spinner = findViewById(R.id.fridge_mode_spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    freezerSB.setProgress(-13+20);
                    fridgeSB.setProgress(4-2);

                    freezerTempTV.setText(String.format("%d",-13));
                    fridgeTempTV.setText(String.format("%d",4));
                }
                else if(position ==1){
                    freezerSB.setProgress(-8+20);
                    fridgeSB.setProgress(7-2);

                    freezerTempTV.setText(String.format("%d",-8));
                    fridgeTempTV.setText(String.format("%d",7));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        fridgeSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int fridgeValue = progress;
                fridgeTempTV.setText(String.valueOf(fridgeValue+2));
                spinner.setSelection(2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        freezerSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int freezerValue = progress;
                freezerTempTV.setText(String.valueOf(freezerValue-20));
                spinner.setSelection(2);
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
                freezerSB.setProgress(Integer.parseInt(response.get("freezerTemperature"))+20);
                fridgeSB.setProgress(Integer.parseInt(response.get("temperature"))-2);
                String temp=response.get("mode");
                freezerTempTV.setText(response.get("freezerTemperature"));
                fridgeTempTV.setText(response.get("temperature"));


                spinner.setSelection((temp.compareTo("default")==0)?2:((temp.compareTo("party")==0)?0:1));
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

                ArrayList<Integer> arr = new ArrayList<>();
                arr.add(freezerSB.getProgress()-20);


                Api.getInstance(getApplicationContext()).setDeviceStatusInteger(id,"setFreezerTemperature",arr, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<Integer> arr = new ArrayList<>();
                        arr.add(fridgeSB.getProgress()+2);
                        Api.getInstance(getApplicationContext()).setDeviceStatusInteger(id, "setTemperature",arr, new Response.Listener<String>() {
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history:
                Intent intent = new Intent(FridgeDetails.this,DeviceHistory.class);
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

