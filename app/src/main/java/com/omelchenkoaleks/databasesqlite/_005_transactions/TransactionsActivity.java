package com.omelchenkoaleks.databasesqlite._005_transactions;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.omelchenkoaleks.databasesqlite.R;

public class TransactionsActivity extends AppCompatActivity {
    private static final String TAG = "TransactionsActivity";

    public static final String TABLE_NAME = "transactions";
    public static final String DATABASE_NAME = "transactionsDB";

    private DBHelper mDBHelper;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_005_transactions);

        Log.d(TAG, "onCreate: Activity");

        mDBHelper = new DBHelper(this);
        myActions();
    }

    private void myActions() {
//        mDatabase = mDBHelper.getWritableDatabase();
//        delete(mDatabase, TABLE_NAME);
//        insert(mDatabase, TABLE_NAME, "value_1");
//        read(mDatabase, TABLE_NAME);
//        mDBHelper.close();

//        mDatabase = mDBHelper.getWritableDatabase();
//        delete(mDatabase, TABLE_NAME);
//        mDatabase.beginTransaction();
//        insert(mDatabase, TABLE_NAME, "value_1");
//        mDatabase.setTransactionSuccessful();
//        insert(mDatabase, TABLE_NAME, "value_2");
//        mDatabase.endTransaction();
//        insert(mDatabase, TABLE_NAME, "value_3");
//        read(mDatabase, TABLE_NAME);
//        mDBHelper.close();


        /*
            finally нам гарантирует закрытие транзакции
         */
        mDatabase = mDBHelper.getWritableDatabase();
        delete(mDatabase, TABLE_NAME);
        mDatabase.beginTransaction();
        try {
            insert(mDatabase, TABLE_NAME, "value_1");
            insert(mDatabase, TABLE_NAME, "value_2");
            insert(mDatabase, TABLE_NAME, "value_3");
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
    }

    private void insert(SQLiteDatabase database, String table, String value) {
        Log.d(TAG, "Insert in table " + table + " value = " + value);

        ContentValues contentValues = new ContentValues();
        contentValues.put("value", value);
        mDatabase.insert(table, null, contentValues);
    }

    private void read(SQLiteDatabase database, String table) {
        Log.d(TAG, "Read table " + table);

        Cursor cursor = mDatabase.query(table, null, null,
                null, null, null, null);
        if (cursor != null) {
            Log.d(TAG, "Records count = " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    Log.d(TAG, cursor.getString(cursor.getColumnIndex("value")));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    private void delete(SQLiteDatabase database, String table) {
        Log.d(TAG, "Delete all from table " + table);

        mDatabase.delete(table, null, null);
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "--- onCreate Database ---");

            db.execSQL("CREATE TABLE transactions ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "value TEXT" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
