package hci.hal9000;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class CurtainDetails extends AppCompatActivity {
    Switch openClose;
    ProgressBar progressBar;
    TextView progressText;
    int progressValue;
    String id;
    int auxValue;
    Thread updater;
    Handler progressBarHandler = new Handler();
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curtain_details);

        progressText=findViewById(R.id.curtain_percent);
        openClose = findViewById(R.id.curtain_switch);
        id = getIntent().getStringExtra("id");
        progressBar = findViewById(R.id.curtain_progress);
        name = getIntent().getStringExtra("name");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(name);
        }

        Button done = findViewById(R.id.done_curtain);

        Api.getInstance(getApplicationContext()).getDeviceStatus(id, new Response.Listener<Map<String, String>>() {
            @Override
            public void onResponse(Map<String, String> response) {
                setCurtainSwitch(response.get("status"));
                setProgressBar(Integer.parseInt(response.get("level")));
                launchBarThread(Integer.parseInt(response.get("level")));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Api.getInstance(getApplicationContext()).setDeviceStatusBoolean(id, openClose.isChecked() ? "close" : "open", new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                });
                if(updater.isAlive()){
                updater.interrupt();
                }
                Intent intent = new Intent(CurtainDetails.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void setProgressBar(int status){
        progressBar.setProgress(status);
        progressText.setText(String.valueOf(status));
        progressValue=status;
    }




    private void setCurtainSwitch(String status)     {
        if((status.compareTo("closed")== 0)|| (status.compareTo("closing")==0)){
            openClose.setChecked(true);
        }
        else{
            openClose.setChecked(false);
        }
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

    private void launchBarThread(int value){
        progressValue=value;
        updater = new Thread(new Runnable() {
            public void run() {
                while (progressValue < 100 && progressValue > 0) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getStatus();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Updating the progress bar
                    progressBarHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressValue);
                        }
                    });
                }
                // performing operation if file is progress is cmpleted,
                if (progressValue == 100 || progressValue== 0) {
                    // sleeping for 1 second after operation completed
                    updater.interrupt();
                }
            }
        });
        updater.start();
    }

    void getStatus(){
        Api.getInstance(getApplicationContext()).getDeviceStatus(id, new Response.Listener<Map<String, String>>() {
            @Override
            public void onResponse(Map<String, String> response) {
                setProgressBar(Integer.parseInt(response.get("level")));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                Intent intent = new Intent(CurtainDetails.this,DeviceHistory.class);
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

