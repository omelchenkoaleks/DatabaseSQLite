package com.omelchenkoaleks.databasesqlite._004_sql_joins;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.omelchenkoaleks.databasesqlite.R;

public class SqlJoinsActivity extends AppCompatActivity {
    private static final String TAG = "SqlJoinsActivity";

    // данные для таблицы должностей
    int[] position_id = {1, 2, 3, 4};
    String[] position_name = {"Директор", "Программер", "Бухгалтер", "Охранник"};
    int[] position_salary = {15000, 13000, 10000, 8000};

    // данные для таблицы людей
    String[] people_name = {"Иван", "Марья", "Петр", "Антон", "Даша", "Борис", "Костя", "Игорь"};
    int[] people_position_id = {2, 3, 2, 2, 3, 1, 2, 4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_004_joins);

        // подключаемся к Database
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // описание курсора
        Cursor cursor;


        // выводим в лог данные по должностям
        Log.d(TAG, "--- Table position ---");

        cursor = database.query("position", null, null,
                null, null, null, null);
        logCursor(cursor);
        cursor.close();
        Log.d(TAG, "--- ---");


        // выводим в лог данные по людям
        Log.d(TAG, "--- Table people ---");

        cursor = database.query("people", null, null,
                null, null, null, null);
        logCursor(cursor);
        cursor.close();
        Log.d(TAG, "--- ---");


        // выводим результат ОБЪЕДИНЕНИЯ - испоьзуем rawQuery
        Log.d(TAG, "--- INNER JOIN with rawQuery ---");

        String sqlQuery = "SELECT PL.name AS Name, PS.name AS Position, salary AS Salary "
                + "FROM people AS PL "
                + "INNER JOIN position AS PS "
                + "ON PL.position_id = PS.id "
                + "WHERE salary > ?";

        cursor = database.rawQuery(sqlQuery, new String[] { "12000" });
        logCursor(cursor);
        cursor.close();
        Log.d(TAG, "--- ---");


        // выводим результат ОБЪЕДИНЕНИЯ - испоьзуем query
        Log.d(TAG, "--- INNER JOIN with query ---");

        String table = "people AS PL INNER JOIN position AS PS ON PL.position_id = PS.id";
        String[] columns = { "PL.name AS Name", "PS.name AS Position", "salary AS Salary", };
        String selection = "salary < ?";
        String[] selectionArgs = { "12000" };

        cursor = database.query(table, columns, selection, selectionArgs,
                null, null, null);
        logCursor(cursor);
        cursor.close();
        Log.d(TAG, "--- ---");

        // закрываем database
        dbHelper.close();
    }

    // вывод в лог данных из курсора
    void logCursor(Cursor cursor) {
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : cursor.getColumnNames()) {
                        str = str.concat(cn + " = " + cursor.getString(cursor.getColumnIndex(cn))) + ";";
                    }
                    Log.d(TAG, str);
                } while (cursor.moveToNext());
            }
        } else {
            Log.d(TAG, "Cursor is null");
        }
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "joinsDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "--- onCreate database ---");

            ContentValues contentValues = new ContentValues();


            // создаем таблицу должностей
            db.execSQL("CREATE TABLE position ("
                    + "id INTEGER PRIMARY KEY,"
                    + "name TEXT,"
                    + "salary INTEGER" + ");");

            // заполняем ее
            for (int i = 0; i < position_id.length; i++) {
                contentValues.clear();
                contentValues.put("id", position_id[i]);
                contentValues.put("name", position_name[i]);
                contentValues.put("salary", position_salary[i]);
                db.insert("position", null, contentValues);
            }


            // создаем таблицу людей
            db.execSQL("CREATE TABLE people ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT,"
                    + "position_id INTEGER" + ");");

            // заполняем ее
            for (int i = 0; i < people_name.length; i++) {
                contentValues.clear();
                contentValues.put("name", people_name[i]);
                contentValues.put("position_id", people_position_id[i]);
                db.insert("people", null, contentValues);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
