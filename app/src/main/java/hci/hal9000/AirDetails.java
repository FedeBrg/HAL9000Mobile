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
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AirDetails extends AppCompatActivity {

    SeekBar airSB;
    TextView airTempTV;
    Switch onoff;
    Spinner mode_spinner;
    Spinner fan_spinner;
    Spinner horizontal_spinner;
    Spinner vertical_spinner;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_details);
        onoff = findViewById(R.id.air_switch);
        mode_spinner = findViewById(R.id.air_mode_spinner);
        fan_spinner = findViewById(R.id.fan_speed_spinner);
        horizontal_spinner = findViewById(R.id.horizontal_blades_spinner);
        vertical_spinner = findViewById(R.id.vertical_blades_spinner);
        airTempTV = findViewById(R.id.air_temp);
        id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");

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


        airSB = findViewById(R.id.air_seek);
        airSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int airValue = progress;
                airTempTV.setText(String.valueOf(airValue+18));
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

        Button done = findViewById(R.id.done_air);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api.getInstance(getApplicationContext()).setDeviceStatusInteger(id, "setTemperature", Arrays.asList(airSB.getProgress()+18), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                Map<String,String> map = new HashMap<>();
                map.put("setMode",mode_spinner.getSelectedItem().toString().toLowerCase());
                map.put("setVerticalSwing",vertical_spinner.getSelectedItem().toString().toLowerCase().replaceAll("°",""));
                map.put("setHorizontalSwing",horizontal_spinner.getSelectedItem().toString().toLowerCase().replaceAll("°",""));
                map.put("setFanSpeed",fan_spinner.getSelectedItem().toString().toLowerCase().replaceAll("%",""));
                //Log.i("TestAire",mode_spinner.getSelectedItem().toString());
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
                Intent intent = new Intent(AirDetails.this, HomeScreen.class);
                startActivity(intent);
                finish();

            }
        });


    }

    private void setSpinners(Map<String, String> response) {
        String mode = response.get("mode");
        String vertical = response.get("verticalSwing");
        String horizontal = response.get("horizontalSwing");
        String fan = response.get("fanSpeed");

        if(mode.compareTo("cool") == 0){
            mode_spinner.setSelection(2);
        }
        else if(mode.compareTo("heat") == 0){
            mode_spinner.setSelection(1);
        }
        else if(mode.compareTo("fan") == 0){
            mode_spinner.setSelection(0);
        }

        if(vertical.compareTo("auto") == 0){
            vertical_spinner.setSelection(0);
        }
        else if(vertical.compareTo("22") == 0){
            vertical_spinner.setSelection(1);
        }
        else if(vertical.compareTo("45") == 0){
            vertical_spinner.setSelection(2);
        }
        else if(vertical.compareTo("67") == 0){
            vertical_spinner.setSelection(3);
        }
        else if(vertical.compareTo("90") == 0){
            vertical_spinner.setSelection(4);
        }

        if(horizontal.compareTo("auto") == 0){
            horizontal_spinner.setSelection(0);
        }
        else if(horizontal.compareTo("-90") == 0){
            horizontal_spinner.setSelection(1);
        }
        else if(horizontal.compareTo("-45") == 0){
            horizontal_spinner.setSelection(2);
        }
        else if(horizontal.compareTo("0") == 0){
            horizontal_spinner.setSelection(3);
        }
        else if(horizontal.compareTo("45") == 0){
            horizontal_spinner.setSelection(4);
        }
        else if(horizontal.compareTo("90") == 0){
            horizontal_spinner.setSelection(5);
        }

        if(fan.compareTo("auto") == 0){
            fan_spinner.setSelection(0);
        }
        else if(fan.compareTo("25") == 0){
            fan_spinner.setSelection(1);
        }
        else if(fan.compareTo("50") == 0){
            fan_spinner.setSelection(2);
        }
        else if(fan.compareTo("75") == 0){
            fan_spinner.setSelection(3);
        }
        else if(fan.compareTo("100") == 0){
            fan_spinner.setSelection(4);
        }


    }

    private void setSeek(String temperature) {
        airTempTV.setText(temperature);
        airSB.setProgress(Integer.parseInt(temperature)-18);
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

    public void setEnabled(boolean isChecked){
        mode_spinner.setEnabled(isChecked);
        fan_spinner.setEnabled(isChecked);
        horizontal_spinner.setEnabled(isChecked);
        vertical_spinner.setEnabled(isChecked);
        airSB.setEnabled(isChecked);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history:
                Intent intent = new Intent(AirDetails.this,DeviceHistory.class);
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
