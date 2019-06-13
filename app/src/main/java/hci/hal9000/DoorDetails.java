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

public class DoorDetails extends AppCompatActivity {

    SeekBar airSB;
    Switch openClose;
    Switch lockUnlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_details);

        openClose = findViewById(R.id.door_switch);
        lockUnlock = findViewById(R.id.lock_switch);
        //mode_spinner = findViewById(R.id.air_mode_spinner);
        //fan_spinner = findViewById(R.id.fan_speed_spinner);
        //horizontal_spinner = findViewById(R.id.horizontal_blades_spinner);
        //vertical_spinner = findViewById(R.id.vertical_blades_spinner);
        //airTempTV = findViewById(R.id.air_temp);
        String id = getIntent().getStringExtra("id");


        Button done = findViewById(R.id.done_door);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoorDetails.this, HomeScreen.class);
                startActivity(intent);
                finish();

            }
        });

        Api.getInstance(getApplicationContext()).getDeviceStatus(id, new Response.Listener<Map<String, String>>() {
            @Override
            public void onResponse(Map<String, String> response) {
                setDoorSwitch(response.get("status"));
                setLockSwitch(response.get("lock"));
                //ESTO ESTA MAL
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });



    }

    private void setDoorSwitch(String status) {
        if(status.compareTo("off") == 0){
            openClose.setChecked(false);
        }
        else{
            openClose.setChecked(true);
        }
    }

    private void setLockSwitch(String status) {
        if(status.compareTo("off") == 0){
            lockUnlock.setChecked(false);
        }
        else{
            lockUnlock.setChecked(true);
        }
    }
}
