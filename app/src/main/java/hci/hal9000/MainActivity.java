package hci.hal9000;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String username =
            "hci.hal9000.extra.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){
            Intent intent = new Intent(this, HomeScreen.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.commit();
        }

    }

    public void goToMainMenu(View view){
//        EditText et = (EditText)findViewById(R.id.username);
//        String content = et.getText().toString();
//        if(content.equals("")){
//            Toast.makeText(getApplicationContext(),"You forgot to type your name!",Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Intent intent = new Intent(this,HomeScreen.class);
//
//            intent.putExtra(username,content);
//            startActivity(intent);
//        }

        Intent intent = new Intent(this,HomeScreen.class);
        startActivity(intent);
    }


}
