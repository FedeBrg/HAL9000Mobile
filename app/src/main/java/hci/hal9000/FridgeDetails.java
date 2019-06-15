package hci.hal9000;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


public class FridgeDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge_details);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Fridge");
        }


        View fridgeSeekV = findViewById(R.id.fridge_seek);
        SeekBar fridgeSB = (SeekBar) fridgeSeekV;
        fridgeSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int fridgeValue = progress;
                TextView fridgeTempTV = findViewById(R.id.fridge_temp);
                fridgeTempTV.setText(String.valueOf(fridgeValue+2));
                Spinner spinner = findViewById(R.id.fridge_mode_spinner);
                spinner.setSelection(3);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        View freezerSeekV = findViewById(R.id.freezer_seek);
        SeekBar freezerSB = (SeekBar) freezerSeekV;
        freezerSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int freezerValue = progress;
                TextView fridgeTempTV = findViewById(R.id.freezer_temp);
                fridgeTempTV.setText(String.valueOf(freezerValue-20));
                Spinner spinner = findViewById(R.id.fridge_mode_spinner);
                spinner.setSelection(3);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button done = findViewById(R.id.done_fridge);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FridgeDetails.this, HomeScreen.class);
                startActivity(intent);
                finish();

            }
        });

    }
}
