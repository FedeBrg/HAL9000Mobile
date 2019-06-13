package hci.hal9000;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Map;

public class AirDetails extends AppCompatActivity {

    SeekBar airSB;
    TextView airTempTV;
    Switch onoff;
    Spinner mode_spinner;
    Spinner fan_spinner;
    Spinner horizontal_spinner;
    Spinner vertical_spinner;

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
        String id = getIntent().getStringExtra("id");

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

        Button done = findViewById(R.id.done_air);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AirDetails.this, HomeScreen.class);
                startActivity(intent);
                finish();

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


    }

    private void setSpinners(Map<String, String> response) {
        //Hacer todos los ifs
    }

    private void setSeek(String temperature) {
        airTempTV.setText(temperature);
        airSB.setProgress(Integer.parseInt(temperature)-18);
    }

    private void setSwitch(String status) {
        if(status.compareTo("off") == 0){
            onoff.setChecked(false);
        }
        else{
            onoff.setChecked(true);
        }
    }


}
