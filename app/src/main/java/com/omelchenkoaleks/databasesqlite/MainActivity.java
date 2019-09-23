package com.omelchenkoaleks.databasesqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.omelchenkoaleks.databasesqlite._001_sql_lite_intro.SqlLiteIntroActivity;
import com.omelchenkoaleks.databasesqlite._002_insert_or_delete.InsertOrDeleteActivity;
import com.omelchenkoaleks.databasesqlite._003_selecting.SelectingActivity;
import com.omelchenkoaleks.databasesqlite._004_sql_joins.SqlJoinsActivity;

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

            case R.id.insert_or_delete_button:
                Intent insertOrDeleteIntent = new Intent(this, InsertOrDeleteActivity.class);
                startActivity(insertOrDeleteIntent);
                break;

            case R.id.selection_button:
                Intent selectingIntent = new Intent(this, SelectingActivity.class);
                startActivity(selectingIntent);
                break;

            case R.id.sql_joins_button:
                Intent joinsIntent = new Intent(this, SqlJoinsActivity.class);
                startActivity(joinsIntent);
                break;
        }
    }
}
