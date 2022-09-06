package org.tu.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, PatientFragment.class, null)
                    .commit();

            Snackbar.make(findViewById(R.id.fragment_container_view), "Successfully loaded", Snackbar.LENGTH_LONG)
                    .show();
        }
    }
}