package hci.hal9000;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Map;

public class CurtainDetails extends AppCompatActivity {
    Switch openClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curtain_details);

        openClose = findViewById(R.id.curtain_switch);
        String id = getIntent().getStringExtra("id");


        Button done = findViewById(R.id.done_curtain);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurtainDetails.this, HomeScreen.class);
                startActivity(intent);
                finish();

            }
        });

        Api.getInstance(getApplicationContext()).getDeviceStatus(id, new Response.Listener<Map<String, String>>() {
            @Override
            public void onResponse(Map<String, String> response) {
                setCurtainSwitch(response.get("status"));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });



    }

    private void setCurtainSwitch(String status) {
        if(status.compareTo("off") == 0){
            openClose.setChecked(false);
        }
        else{
            openClose.setChecked(true);
        }
    }
}