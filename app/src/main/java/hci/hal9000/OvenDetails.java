package hci.hal9000;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;
import yuku.ambilwarna.AmbilWarnaDialog;

public class OvenDetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oven_details);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Oven");
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> heatSource = new ArrayAdapter<>(OvenDetails.this ,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.heatSource));
        heatSource.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(heatSource);

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> grillMode = new ArrayAdapter<>(OvenDetails.this ,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.grillMode));
        grillMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(grillMode);

        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<String> convectionMode = new ArrayAdapter<>(OvenDetails.this ,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.convectionMode));
        grillMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(convectionMode);

        Button done = (Button) findViewById(R.id.done_oven);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OvenDetails.this,HomeScreen.class);
                startActivity(intent);
            }
        });


    }

}
