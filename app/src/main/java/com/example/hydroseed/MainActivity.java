package com.example.hydroseed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private FrameLayout fragmentContainer;
    private Button calculate, history, button0, button1, button2, button3, button4, button5, button6,
            button7, button8, button9, button10, buttonC;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new CalculateFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_calculate);
        }
    }

    //Shows all options for navigation that are in the drawer each will fetch the layout for that fragment
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Quick Link URL source:
        String calTransURL = "https://dot.ca.gov/";
        //String stormWaterContactURL = "https://www.waterboards.ca.gov/water_issues/programs/stormwater/contact.html";
        String weatherURL = "https://weather.com/weather/today/l/3e10208cb1c3765b5072191becf9220634b5a329cb7f75865facb1e55b24f4b6";
        //Check navigation item selected from drawer
        switch (item.getItemId()) {
            case R.id.nav_calculate:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CalculateFragment()).commit();
                break;
            case R.id.nav_files:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FilesFragment()).commit();
                break;
            case R.id.nav_caltrans:
                //Toast.makeText(this, "Open Web Page: Cal Trans", Toast.LENGTH_SHORT).show();
                openLink(calTransURL);
                break;
            case R.id.nav_weather:
                //Toast.makeText(this, "Open Web Page: Weather", Toast.LENGTH_SHORT).show();
                openLink(weatherURL);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //When back is pressed, sidebar closes first then app
    @Override
    public void onBackPressed() {
        //Close Drawer only if back is pressed
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {//Close Activity as usual if pressed and drawer is closed
            super.onBackPressed();
        }
    }

    //Open Link given a URL String
    public void openLink(String URL){
        Intent openURL = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        openURL.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        openURL.setPackage("com.android.chrome"); //choose google chrome by default
        try {
            this.startActivity(openURL);
        } catch (ActivityNotFoundException ex) {
            // Chrome not installed so allow user to choose instead
            openURL.setPackage(null);
            this.startActivity(openURL);
        }
    }

    public void historyButton(View v) {
        Intent toHistoryPage = new Intent(this, History.class);

        startActivity(toHistoryPage);
    }


}