package hci.hal9000;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.home_menu:
                        Toast.makeText(HomeScreen.this, "home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.devices_menu:
                        Toast.makeText(HomeScreen.this, "devices", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.voice_menu:
                        Toast.makeText(HomeScreen.this, "voice", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.routines_menu:
                        Toast.makeText(HomeScreen.this, "routines", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rooms_menu:
                        Toast.makeText(HomeScreen.this, "rooms", Toast.LENGTH_SHORT).show();
                        RoomsFragment rf = new RoomsFragment();
                        fragmentTransaction.replace(R.id.container, rf);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                }
                return true;
            }
        });


    }

    public void createNewRoom(View v){
        Intent intent = new Intent(this,CreateRoom.class);
        startActivity(intent);
    }

}
