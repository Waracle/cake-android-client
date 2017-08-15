package com.waracle.androidtest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.waracle.androidtest.frags.ListFrag;
import com.waracle.androidtest.logic.DataTask;
import com.waracle.androidtest.logic.ImageLoader;
import com.waracle.androidtest.models.Cake;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static String JSON_URL = "https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/" +
            "raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json";

    private ArrayList<Cake> loadedCakes = null;
    private String          LIST_KEY    = "CAKE_LIST";

    private DataTask taskRef = null;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if( savedInstanceState != null ) {
            loadedCakes = savedInstanceState.getParcelableArrayList(LIST_KEY);
        }
    }


    @Override
    protected void onResume() {
        if( loadedCakes != null ) {
            showList();
        } else {
            DataTask.CakeCallback cakeCallback = new DataTask.CakeCallback() {
                @Override
                public void cakesLoaded( @NonNull List<Cake> cakes ) {
                    loadedCakes = new ArrayList<>(cakes);
                    showList();
                }
            };

            taskRef = new DataTask(cakeCallback);
            taskRef.execute(JSON_URL);
        }

        super.onResume();
    }

    @Override
    protected void onStop() {
        if( taskRef != null ) {
            taskRef.cancel(true); // Cancel the call
            taskRef = null; // Possible help for GC
        }

        // Also stop any image retrieval (This can of course be vastly improved in a million ways.)
        ImageLoader.getInstance().stopAndClear();

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState( Bundle outState ) {
        outState.putParcelableArrayList(LIST_KEY, loadedCakes);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if( id == R.id.action_refresh ) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showList() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.container, ListFrag.newInstance(loadedCakes))
                .commit();
    }
}
