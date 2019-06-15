package hci.hal9000;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.util.Calendar;

public class HomeScreen extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    NavController navController;
    static boolean isAppRunning = true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        bottomNavigationView = findViewById(R.id.bottom_navigation);

        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }

    @Override
    public void onStop(){
        super.onStop();
         isAppRunning = false;
    }

    @Override
    public void onResume(){
        super.onResume();
        isAppRunning = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings_overflow:
                Intent intent = new Intent(HomeScreen.this,ChangeIP.class);
                startActivity(intent);
                return true;

            case R.id.notification_overflow:
                return true;

            default:
                super.onOptionsItemSelected(item);

        }
        return true;

    }


}


