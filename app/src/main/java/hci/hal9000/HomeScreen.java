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

        final Fragment home = new HomeFragment();
        final Fragment devices = new DevicesFragment();
        final Fragment rooms = new RoomsFragment();
        final Fragment voice = new VoiceFragment();
        final Fragment routines = new RoutinesFragment();




        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment newFragment = new HomeFragment();
                switch (item.getItemId()) {
                    case R.id.home_menu:
                        Toast.makeText(HomeScreen.this, "home", Toast.LENGTH_SHORT).show();
                        newFragment = home;
                        break;
                    case R.id.devices_menu:
                        Toast.makeText(HomeScreen.this, "devices", Toast.LENGTH_SHORT).show();
                        newFragment = devices;
                        break;
                    case R.id.voice_menu:
                        Toast.makeText(HomeScreen.this, "voice", Toast.LENGTH_SHORT).show();
                        newFragment = voice;
                        break;
                    case R.id.routines_menu:
                        Toast.makeText(HomeScreen.this, "routines", Toast.LENGTH_SHORT).show();
                        newFragment = routines;
                        break;
                    case R.id.rooms_menu:
                        Toast.makeText(HomeScreen.this, "rooms", Toast.LENGTH_SHORT).show();
                        newFragment = rooms;
                        break;

                }
                fragmentTransaction.replace(R.id.container, newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                //invalidateOptionsMenu();
                return true;
            }

        });

    }



}
