package hci.hal9000;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChangeIP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_ip);

        Button confirm = (Button) findViewById(R.id.confirm_ip);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_ip = ((EditText)findViewById(R.id.new_ip)).getText().toString();
                Api.setIP(new_ip);
                finish();
            }
        });
    }
}
