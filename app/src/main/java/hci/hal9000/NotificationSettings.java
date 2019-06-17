package hci.hal9000;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotificationSettings extends AppCompatActivity {

    CheckBox lights;
    CheckBox oven;
    CheckBox fridge;
    CheckBox air;
    CheckBox door;
    CheckBox curtains;
    Switch onoff;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Notification Settings");
        }

        done=findViewById(R.id.done_notification);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        lights = findViewById(R.id.checkbox_light);
        lights.setChecked(pref.getBoolean("light_notif",true));

        oven = findViewById(R.id.checkbox_oven);
        oven.setChecked(pref.getBoolean("oven_notif",true));

        fridge = findViewById(R.id.checkbox_fridge);
        fridge.setChecked(pref.getBoolean("fridge_notif",true));

        air = findViewById(R.id.checkbox_air);
        air.setChecked(pref.getBoolean("air_notif",true));

        door = findViewById(R.id.checkbox_door);
        door.setChecked(pref.getBoolean("door_notif",true));

        curtains = findViewById(R.id.checkbox_curtains);
        curtains.setChecked(pref.getBoolean("curtains_notif",true));

        onoff = findViewById(R.id.notification_switch);
        onoff.setChecked(pref.getBoolean("switch_notif",true));

        setEnabled(onoff.isChecked());

        onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = pref.edit();
                ed.putBoolean("switch_notif",isChecked);
                ed.apply();
                setEnabled(isChecked);
            }
        });



    }

    private void setEnabled(boolean isChecked) {
        lights.setEnabled(isChecked);
        oven.setEnabled(isChecked);
        fridge.setEnabled(isChecked);
        air.setEnabled(isChecked);
        door.setEnabled(isChecked);
        curtains.setEnabled(isChecked);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        switch(view.getId()) {
            case R.id.checkbox_light:
                ed.putBoolean("light_notif",checked);
                break;
            case R.id.checkbox_oven:
                ed.putBoolean("oven_notif",checked);
                break;
            case R.id.checkbox_air:
                ed.putBoolean("air_notif",checked);
                break;
            case R.id.checkbox_curtains:
                ed.putBoolean("curtains_notif",checked);
                break;
            case R.id.checkbox_door:
                ed.putBoolean("door_notif",checked);
                break;
            case R.id.checkbox_fridge:
                ed.putBoolean("fridge_notif",checked);
                break;

        }
        ed.apply();
    }
}
