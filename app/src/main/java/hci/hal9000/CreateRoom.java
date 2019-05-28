package hci.hal9000;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class CreateRoom extends AppCompatActivity {
    public static final String roomname = "hci.hal9000.extra.ROOMNAME";
    private String content;

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

//        Button button = (Button) findViewById(R.id.create_room);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Me tocaste!",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(CreateRoom.this, HomeScreen.class);
//                intent.putExtra(roomname, content);
//                startActivity(intent);
//            }
//        });

        //Toast.makeText(getApplicationContext(),"Me tocaste!",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CreateRoom.this, HomeScreen.class);
        intent.putExtra(roomname, content);
        startActivity(intent);
    }
}
