package hci.hal9000;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Intent intent = getIntent();
        String name = intent.getStringExtra(MainActivity.username);

        TextView tv = (TextView) findViewById(R.id.whoAmI);

        String wel = "Welcome ";
        tv.setText(wel.concat(name).concat("!"));

    }
}
