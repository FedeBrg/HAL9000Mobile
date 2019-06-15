package hci.hal9000;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static int notification_time = 15;
    public static final String username =
            "hci.hal9000.extra.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //desde acá configuramos las notificaciones
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Intent in = new Intent(getApplicationContext(),CheckNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1234/*unique request code*/, in, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),60 * 1000 /*5 min in millis*/, pendingIntent);


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
