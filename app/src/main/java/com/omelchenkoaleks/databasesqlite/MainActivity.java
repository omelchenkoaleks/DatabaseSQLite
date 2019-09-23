package com.omelchenkoaleks.databasesqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.omelchenkoaleks.databasesqlite._001_sql_lite_intro.SqlLiteIntroActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            // кнопка назад
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.sql_lite_intro_button:
                Intent sqlLiteIntroIntent = new Intent(this, SqlLiteIntroActivity.class);
                startActivity(sqlLiteIntroIntent);
                break;
        }
    }
}
