package com.example.projectmobile;


import android.content.ClipData;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.security.acl.Group;

public class Home_navigation extends AppCompatActivity {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_navigation);

        Group nav_massage = findViewById(R.id.nav_message);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                try {
                    if (id == R.id.nav_message) {
                        Log.d("id selected", String.valueOf(id));
//                    Toast.makeText(Home_navigation.this, "my account",Toast.LENGTH_LONG).show();
                        String token = getSharedPreferences("AUTH_TOKEN",0).getString("TOKEN",null);
                        Log.d("Token in main activity",token);
                        return true;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    return true;
                }
            }
        } );

        //test
//        String token = getPreferences(MODE_PRIVATE).getString("TOKEN",null);
//        Log.d("Token in main activity",token);
    }
}