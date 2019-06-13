package hci.hal9000;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import yuku.ambilwarna.AmbilWarnaDialog;

public class LightDetails extends AppCompatActivity {
    int color =0xFFFFFF;
    SeekBar sb;
    Button color_btn;
    Switch onoff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_details);
        final String id = getIntent().getStringExtra("id");
        Log.i("Test API",String.format("Recibi ID: %s",id));
        sb = findViewById(R.id.light_sb);
        onoff = findViewById(R.id.light_toggle);

        Api.getInstance(getApplicationContext()).getDeviceStatus(id, new Response.Listener<Map<String,String>>() {
            @Override
            public void onResponse(Map<String,String> response) {
                Log.i("Light API",String.format("Recibi %s %s %s: ",response.get("color"),response.get("status"),response.get("brightness")));
                change_background( Integer.parseInt(response.get("color"),16));
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


        color_btn = (Button) findViewById(R.id.color_button);
        color_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AmbilWarnaDialog dialog = new AmbilWarnaDialog(v.getContext(), 000000, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        // color is the color selected by the user.
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
                //String id = getIntent().getStringExtra("id");
                //onoff = findViewById(R.id.light_toggle);

                if(onoff.isChecked()){
                    Log.i("Lights","Mande un on");

                    Api.getInstance(getApplicationContext()).setDeviceStatusBoolean(id,"turnOn", new Response.Listener<Boolean>() {
                        @Override
                        public void onResponse(Boolean response) {
                            Api.getInstance(getApplicationContext()).setDeviceStatusString(id,"setColor", Arrays.asList("ffff11"), new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            });
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Lights","On Error");

                        }
                    });

                }
                else{
                    Log.i("Lights","Mande un off");

                    Api.getInstance(getApplicationContext()).setDeviceStatusBoolean(id,"turnOff", new Response.Listener<Boolean>() {
                        @Override
                        public void onResponse(Boolean response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Lights","Off Error");

                        }
                    });

                }

                Intent intent = new Intent(LightDetails.this,HomeScreen.class);
                startActivity(intent);
                finish();

            }
        });




    }

    void change_background(int color){
        this.getWindow().findViewById(R.id.color_button).setBackgroundColor(color);
    }

//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return true;
//    }


}
