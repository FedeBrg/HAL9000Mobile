package hci.hal9000;

import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class HomeScreen extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment newFragment = new HomeFragment();
        fragmentTransaction.replace(R.id.container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();




        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment newFragment = new HomeFragment();
                switch (item.getItemId()) {
                    case R.id.home_menu:
                        Toast.makeText(HomeScreen.this, "home", Toast.LENGTH_SHORT).show();
                        newFragment = new HomeFragment();
                        break;
                    case R.id.devices_menu:
                        Toast.makeText(HomeScreen.this, "devices", Toast.LENGTH_SHORT).show();
                        newFragment = new DevicesFragment();
                        break;
                    case R.id.voice_menu:
                        Toast.makeText(HomeScreen.this, "voice", Toast.LENGTH_SHORT).show();
                        newFragment = new VoiceFragment();
                        break;
                    case R.id.routines_menu:
                        Toast.makeText(HomeScreen.this, "routines", Toast.LENGTH_SHORT).show();
                        newFragment = new RoutinesFragment();
                        break;
                    case R.id.rooms_menu:
                        Toast.makeText(HomeScreen.this, "rooms", Toast.LENGTH_SHORT).show();
                        newFragment = new RoomsFragment();
                        break;

                }
                fragmentTransaction.replace(R.id.container, newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                return true;
            }
        });
    }



}
