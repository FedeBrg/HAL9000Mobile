package hci.hal9000;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class CreateRoom extends AppCompatActivity {
    public static final String roomname = "hci.hal9000.extra.ROOMNAME";
    private String content;
    private String roomName;
    private String roomType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Create new room");
        }


    }




    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void createNewRoom(View view){
        EditText et = (EditText)findViewById(R.id.room_name);
        content = et.getText().toString();

        roomName = ((EditText) findViewById(R.id.room_name)).getText().toString();
        roomType = ((Spinner) findViewById(R.id.room_type)).getSelectedItem().toString().toLowerCase().replaceAll(" ","_");
        Room room = new Room();
        room.setMeta(roomType);
        room.setName(roomName);
        Api.getInstance(getApplicationContext()).addRoom(room, new Response.Listener<Room>() {
            @Override
            public void onResponse(Room response) {
                Intent intent = new Intent(CreateRoom.this,HomeScreen.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        });
        Intent intent = new Intent(CreateRoom.this, HomeScreen.class);
        //intent.putExtra(roomname, content);
        startActivity(intent);
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

        Log.e("Testing", error.toString());
        //String text = getResources().getString(R.string.error_message);
        String text = "Connection error."; //Parametrizar en Strings
        if (response != null)
            text += " " + response.getDescription().get(0);

        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
