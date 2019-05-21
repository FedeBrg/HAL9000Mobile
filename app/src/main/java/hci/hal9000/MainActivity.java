package hci.hal9000;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String username =
            "hci.hal9000.extra.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void goToMainMenu(View view){
        EditText et = (EditText)findViewById(R.id.username);
        String content = et.getText().toString();
        if(content.equals("")){
            Toast.makeText(getApplicationContext(),"You forgot to type your name!",Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(this,HomeScreen.class);

            intent.putExtra(username,content);
            startActivity(intent);
        }
    }
}
