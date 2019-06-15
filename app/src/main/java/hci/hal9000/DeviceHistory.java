package hci.hal9000;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

public class DeviceHistory extends AppCompatActivity {
    String id;
    TextView tv;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_history);
        list = new ArrayList<>();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Device history");
        }


        id = getIntent().getStringExtra("id");

        Api.getInstance(getApplicationContext()).getDeviceLogs(id, new Response.Listener<ArrayList<DeviceLog>>() {
            @Override
            public void onResponse(ArrayList<DeviceLog> response) {
                for(DeviceLog dv : response){
                    if(dv.getAction().compareTo("getState")!= 0){
                        list.add(dv.getAction());
                    }
                }
                if(list.size() == 0){
                    list.add("There where no events!");
                }

                DeviceLogAdapter adapter = new DeviceLogAdapter(getApplicationContext(),list);
                ListView lv = findViewById(R.id.logs_list);
                lv.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("DeviceLogs","Error");

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
