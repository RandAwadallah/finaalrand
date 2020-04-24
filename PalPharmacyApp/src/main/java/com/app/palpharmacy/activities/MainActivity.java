package com.app.palpharmacy.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.app.palpharmacy.R;
import com.app.palpharmacy.adapters.RecyclerViewAdapter;
import com.app.palpharmacy.model.Pharmacy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static int splashtime = 4000;
    private final String JSON_URL = "http://www.palpharmacy.com/index.php/getPharmacies";
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<Pharmacy> lstPharmacy;
    private RecyclerView recyclerView;
    ImageButton imgbut1, imgbut2, imgbut3, imgbut4, imgbut5, imgbut6, imgbut7, imgbut8;
    private DrawerLayout drawer;
    RecyclerViewAdapter myadapter;
    EditText searchinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchinput = findViewById(R.id.edittext);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        drawer = findViewById(R.id.draw_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
  /*      imgbut1= findViewById(R.id.imgbutton1);
        imgbut2= findViewById(R.id.imgbutton2);
        imgbut3= findViewById(R.id.imgbutton3);
        imgbut4= findViewById(R.id.imgbutton4);
        imgbut5= findViewById(R.id.imgbutton5);
        imgbut6= findViewById(R.id.imgbutton6);
        imgbut7= findViewById(R.id.imgbutton7);
        imgbut1= findViewById(R.id.imgbutton8);
*/

        lstPharmacy = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerviewid);
        jsonrequest();


    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void jsonrequest() {

        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {


                    try {
                        jsonObject = response.getJSONObject(i);
                        Pharmacy pharmacy = new Pharmacy();
                        pharmacy.setName(jsonObject.getString("name_en"));
                        pharmacy.setDescription(jsonObject.getString("address"));
//                        pharmacy.setRating(jsonObject.getString("Rating"));
//                        pharmacy.setCategorie(jsonObject.getString("categorie"));
//                        pharmacy.setNb_episode(jsonObject.getInt("episode"));
//                        pharmacy.setStudio(jsonObject.getString("studio"));
                        pharmacy.setImage_url(jsonObject.getString("image"));
                        lstPharmacy.add(pharmacy);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                setuprecyclerview(lstPharmacy);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        searchinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                myadapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);


    }

    private void setuprecyclerview(List<Pharmacy> lstPharmacy) {


        myadapter = new RecyclerViewAdapter(this, lstPharmacy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myadapter);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_aboutus:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new aboutusfragment()).commit();
                break;
            case R.id.nav_language:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Languagefragment()).commit();
                break;
            case R.id.nav_notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new notificationfragment()).commit();
                break;
            case R.id.nav_location:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new locationfragment()).commit();
                break;


        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
