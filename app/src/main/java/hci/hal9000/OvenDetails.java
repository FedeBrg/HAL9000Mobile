package hci.hal9000;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OvenDetails extends AppCompatActivity {

    SeekBar ovenSB;
    TextView ovenTempTV;
    Switch onoff;
    Spinner heatSource;
    Spinner convetcionMode;
    Spinner grilMode;
    String id;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oven_details);



        onoff = findViewById(R.id.oven_switch);
        heatSource = findViewById(R.id.heat_source_spinner);
        convetcionMode = findViewById(R.id.convection_mode_spinner);
        grilMode = findViewById(R.id.grill_mode_spinner);
        ovenSB = findViewById(R.id.oven_seek);
        ovenTempTV = findViewById(R.id.oven_temp_text);
        id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        done=findViewById(R.id.done_oven);


        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(name);
        }

        onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setEnabled(isChecked);
            }
        });

        ovenSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int airValue = progress;
                ovenTempTV.setText(String.valueOf(airValue+90));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Api.getInstance(getApplicationContext()).getDeviceStatus(id, new Response.Listener<Map<String, String>>() {
            @Override
            public void onResponse(Map<String, String> response) {
                setSwitch(response.get("status"));
                setSeek(response.get("temperature"));
                setSpinners(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api.getInstance(getApplicationContext()).setDeviceStatusInteger(id, "setTemperature", Arrays.asList(ovenSB.getProgress()+90), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                Map<String,String> map = new HashMap<>();
                map.put("setHeat",heatSource.getSelectedItem().toString().toLowerCase());
                map.put("setConvection",convetcionMode.getSelectedItem().toString().toLowerCase());
                map.put("setGrill",grilMode.getSelectedItem().toString().toLowerCase());
                for(String key : map.keySet()){
                    Api.getInstance(getApplicationContext()).setDeviceStatusString(id, key, Arrays.asList(map.get(key)), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                }
                Api.getInstance(getApplicationContext()).setDeviceStatusBoolean(id, onoff.isChecked()? "turnOn" : "turnOff", new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                Intent intent = new Intent(OvenDetails.this, HomeScreen.class);
                startActivity(intent);
                finish();

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history:
                Intent intent = new Intent(OvenDetails.this,DeviceHistory.class);
                //startActivityForResult(intent,1);
                //Log.i("DeviceLogs","Menu")
                intent.putExtra("id",id);
                startActivity(intent);
                //finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setSwitch(String status) {
        if(status.compareTo("off") == 0){
            onoff.setChecked(false);
            setEnabled(false);
        }
        else{
            onoff.setChecked(true);
            setEnabled(true);
        }
    }

    private void setSeek(String temperature) {
        ovenTempTV.setText(temperature);
        ovenSB.setProgress(Integer.parseInt(temperature)-18);
    }

    private void setSpinners(Map<String, String> response) {
        String heat = response.get("heat");
        String grill = response.get("grill");
        String convection = response.get("convection");
        heatSource.setSelection((heat.compareTo("conventional")==0)?0:(heat.compareTo("bottom")==0)?1:2);
        grilMode.setSelection((grill.compareTo("large")==0)?0:(heat.compareTo("eco")==0)?1:2);
        convetcionMode.setSelection((convection.compareTo("normal")==0)?0:(heat.compareTo("eco")==0)?1:2);

    }

    public void setEnabled(boolean isChecked){
        heatSource.setEnabled(isChecked);
        grilMode.setEnabled(isChecked);
        convetcionMode.setEnabled(isChecked);
        ovenSB.setEnabled(isChecked);
    }
}
