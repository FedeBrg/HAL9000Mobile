package hci.hal9000;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curtain_details);

        openClose = findViewById(R.id.curtain_switch);
        final String id = getIntent().getStringExtra("id");
        progressBar = findViewById(R.id.curtain_progress);
        progressBar.setMax(100);

        Button done = findViewById(R.id.done_curtain);

        Api.getInstance(getApplicationContext()).getDeviceStatus(id, new Response.Listener<Map<String, String>>() {
            @Override
            public void onResponse(Map<String, String> response) {
                setCurtainSwitch(response.get("status"));
                setProgressBar(response.get("level"));
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
                Intent intent = new Intent(CurtainDetails.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void setProgressBar(String status){
        progressBar.setProgress(Integer.parseInt(status));
    }

    private void setCurtainSwitch(String status)     {
        if((status.compareTo("closed")== 0)|| (status.compareTo("closing")==0)){
            openClose.setChecked(false);
        }
        else{
            openClose.setChecked(true);
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
}