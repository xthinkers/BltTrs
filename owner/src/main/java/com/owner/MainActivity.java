package com.owner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 业主端APk
 * 供业主使用
 */
public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("ownerinfo", MODE_PRIVATE);
        boolean isEdited = sharedPreferences.getBoolean("isEdited", false);

        Intent intent = new Intent();
        intent.setClass(this, isEdited ? GuestListActivity.class : OwnerInfoInputActivity.class);
        startActivity(intent);
        finish();

        setContentView(R.layout.content_main);
    }
}
