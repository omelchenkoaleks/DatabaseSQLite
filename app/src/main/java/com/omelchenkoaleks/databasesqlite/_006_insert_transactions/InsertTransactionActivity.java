package com.omelchenkoaleks.databasesqlite._006_insert_transactions;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.omelchenkoaleks.databasesqlite.R;

public class InsertTransactionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DB_NAME = "transactionDB";
    private static final String TABLE_NAME = "insertDB";
    private SQLiteDatabase mDatabase;

    private TextView mTimeTextView;
    private Button mInsertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_006_insert_transaction);

        mTimeTextView = findViewById(R.id.time_text_view);
        mInsertButton = findViewById(R.id.insert_transactions_button);
        mInsertButton.setOnClickListener(this);

        initDB();
    }

    private void initDB() {
        mDatabase = this.openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        mDatabase.execSQL( "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(FirstNumber INT, SecondNumber INT, Result INT);" );
        mDatabase.delete(TABLE_NAME, null, null);
    }

    @Override
    public void onClick(View v) {
        mDatabase.delete(TABLE_NAME, null, null);
        long startTime = System.currentTimeMillis();
        insertRecords();
        long diff = System.currentTimeMillis() - startTime;
        mTimeTextView.setText(getString(R.string.time) + Long.toString(diff) + "ms");
    }

    private void insertRecords() {
//        for (int i = 0; i < 1000; i++) {
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("FirstNumber", i);
//            contentValues.put("SecondNumber", i);
//            contentValues.put("Result", i * i);
//            mDatabase.insert(TABLE_NAME, null, contentValues);
//        }

        // используем транзакции - это ускорит выполнение кода в несколько раз!!!
//        mDatabase.beginTransaction();
//        try {
//            for (int i = 0; i < 1000; i++) {
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("FirstNumber", i);
//                contentValues.put("SecondNumber", i);
//                contentValues.put("Result", i * i);
//                mDatabase.insert(TABLE_NAME, null, contentValues);
//            }
//            mDatabase.setTransactionSuccessful();
//        } finally {
//            mDatabase.endTransaction();
//        }

        // еще быстрее выполнится, если использовать SQLiteStatement (множественная вставка) !!!
        String sql = "INSERT INTO " + TABLE_NAME + " VALUES(?,?,?);";
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        try {
            for (int i = 0; i < 1000; i++) {
                statement.clearBindings();
                statement.bindLong(1, i);
                statement.bindLong(2, i);
                statement.bindLong(3, i * i);
                statement.execute();
            }
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.close();
    }
}
