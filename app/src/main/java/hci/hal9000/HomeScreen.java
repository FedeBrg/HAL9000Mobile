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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public class HomeScreen extends AppCompatActivity {

    final Fragment homeFragment = new HomeFragment();
    final Fragment devicesFragment = new DevicesFragment();
    final Fragment roomsFragment = new RoomsFragment();
    final Fragment voiceFragment = new VoiceFragment();
    final Fragment routinesFragment = new RoutinesFragment();
    Fragment currentFragment = null;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    BottomNavigationView bottomNavigationView;
    NavController navController;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        bottomNavigationView = findViewById(R.id.bottom_navigation);

        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        //NavigationUI.setupActionBarWithNavController(this, navController);

//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.my_nav_host_fragment);
//        NavigationUI.setupWithNavController(bottomNavigationView,navHostFragment.getNavController());
//

//        fragmentManager.beginTransaction().add(R.id.container, roomsFragment).hide(roomsFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.container, routinesFragment).hide(routinesFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.container, voiceFragment).hide(voiceFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.container, devicesFragment).hide(devicesFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.container, homeFragment).commit();
//        currentFragment = homeFragment;
        //loadFragment(currentFragment);
    }

//    private boolean loadFragment(Fragment fragment) {
//        if (fragment != null) {
//            fragmentManager.beginTransaction()
//                    .hide(currentFragment)
//                    .show(fragment)
//                    .addToBackStack(null)
//                    .commit();
//
//
//            currentFragment = fragment;
//            for (int i = 0; i<fragmentManager.getBackStackEntryCount();i++){
//                Log.i("Fragments",String.format("%d",fragmentManager.getBackStackEntryAt(i).getId()));
//            }
//            return true;
//        }
//
//        return false;
//    }
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//        Fragment fragment = null;
//
//        switch (item.getItemId()) {
//            case R.id.home_menu:
//                //Log.v("Fragments", "Entre a load home");
//                navController.navigate(R.id.action_voiceFragment_to_homeFragment);
//                //Toast.makeText(HomeScreen.this, "home", Toast.LENGTH_SHORT).show();
//                //fragment = homeFragment;
//                break;
//            case R.id.devices_menu:
//                Toast.makeText(HomeScreen.this, "devices", Toast.LENGTH_SHORT).show();
//                fragment = devicesFragment;
//                break;
//            case R.id.voice_menu:
////                Toast.makeText(HomeScreen.this, "voice", Toast.LENGTH_SHORT).show();
////                fragment = voiceFragment;
//                navController.navigate(R.id.action_homeFragment_to_voiceFragment);
//                break;
//            case R.id.routines_menu:
//                Toast.makeText(HomeScreen.this, "routines", Toast.LENGTH_SHORT).show();
//                fragment = routinesFragment;
//                break;
//            case R.id.rooms_menu:
//                Toast.makeText(HomeScreen.this, "rooms", Toast.LENGTH_SHORT).show();
//                fragment = roomsFragment;
//                break;
//        }
//
//        return true; //loadFragment(fragment);
//    }

//    @Override
//    public void onBackPressed() {
//        int count = fragmentManager.getBackStackEntryCount();
//        if (count == 0) {
//            super.onBackPressed();
//        } else {
//            int index = count - 1;
//            fragmentManager.popBackStack();
//            FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
//            int stackId = backEntry.getId();
//            bottomNavigationView.getMenu().getItem(stackId).setChecked(true);
//
//            for (int i = 0; i<fragmentManager.getBackStackEntryCount();i++){
//                Log.i("Fragments",String.format("%d",fragmentManager.getBackStackEntryAt(i).getId()));
//            }
//        }
//    }

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


