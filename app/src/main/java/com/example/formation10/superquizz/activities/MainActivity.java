package com.example.formation10.superquizz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.formation10.superquizz.fragments.CreationFragment;
import com.example.formation10.superquizz.model.Question;
import com.example.formation10.superquizz.fragments.QuestionListFragment;
import com.example.formation10.superquizz.R;
import com.example.formation10.superquizz.fragments.ScoreFragment;
import com.example.formation10.superquizz.fragments.SettingsFragment;
import com.example.formation10.superquizz.api.APIClient;
import com.example.formation10.superquizz.database.QuestionsDatabaseHelper;

import java.io.IOException;
import java.util.List;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, QuestionListFragment.OnListFragmentInteractionListener, CreationFragment.CreationFragmentListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (savedInstanceState == null) {
            QuestionListFragment fragment = new QuestionListFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }

        

        APIClient.getInstance().getQuestions(new APIClient.APIResult<List<Question>>() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void OnSuccess(List<Question> object) {
                for (Question question : object){
                    QuestionsDatabaseHelper.getInstance(MainActivity.this).addQuestion(question);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_play) {
            Bundle arguments = new Bundle();
            QuestionListFragment fragment = new QuestionListFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

        } else if (id == R.id.nav_score) {
            Bundle arguments = new Bundle();
            ScoreFragment fragment = new ScoreFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

        } else if (id == R.id.nav_info) {

        } else if (id == R.id.nav_param) {
            Bundle arguments = new Bundle();
            SettingsFragment fragment = new SettingsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

        } else if (id == R.id.nav_add_question) {
            Bundle arguments = new Bundle();
            CreationFragment fragment = new CreationFragment();
            fragment.listener=this;
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onListFragmentInteraction(Question item) {

        Intent intent = new Intent(this,QuestionActivity.class);
        intent.putExtra("Question", item);
        this.startActivity(intent);
    }

    @Override
    public void createQuestion(Question q) {
        QuestionListFragment fragment = new QuestionListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        QuestionsDatabaseHelper databaseHelper = QuestionsDatabaseHelper.getInstance(this);
        databaseHelper.addQuestion(q);
    }
}
