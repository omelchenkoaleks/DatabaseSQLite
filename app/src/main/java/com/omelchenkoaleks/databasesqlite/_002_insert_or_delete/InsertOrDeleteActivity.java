package com.omelchenkoaleks.databasesqlite._002_insert_or_delete;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.omelchenkoaleks.databasesqlite.R;

// КАК ОБНОВИТЬ И УДАЛИТЬ КОНКРЕТНУЮ ЗАПИСЬ
public class InsertOrDeleteActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "InsertOrDeleteActivity";

    private EditText mIdEditText;
    private EditText mNameEditText;
    private EditText mEmailEditText;

    private TextView mAddTextView;
    private TextView mReadTextView;
    private TextView mClearTextView;
    private TextView mUpdateTextView;
    private TextView mDeleteTextView;

    private DBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_002_insert_or_delete);

        mIdEditText = findViewById(R.id.result_id_edit_text);
        mNameEditText = findViewById(R.id.name_intro_edit_text);
        mEmailEditText = findViewById(R.id.email_intro_edit_text);

        mUpdateTextView = findViewById(R.id.update_intro_button);
        mUpdateTextView.setOnClickListener(this);

        mDeleteTextView = findViewById(R.id.delete_intro_button);
        mDeleteTextView.setOnClickListener(this);

        mAddTextView = findViewById(R.id.add_intro_button);
        mAddTextView.setOnClickListener(this);

        mReadTextView = findViewById(R.id.read_intro_button);
        mReadTextView.setOnClickListener(this);

        mClearTextView = findViewById(R.id.clear_intro_button);
        mClearTextView.setOnClickListener(this);

        mDBHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {
        // создаем объект для данных - этот класс используется для указания полей и значений,
        // которые мы в эти поля будем вставлять
        ContentValues contentValues = new ContentValues();

        // получаем данные из полей ввода
        String id = mIdEditText.getText().toString().trim();
        String name = mNameEditText.getText().toString().trim();
        String email = mEmailEditText.getText().toString().trim();

        // подключаеся к базе данных - получаем объект SQLiteDatabase, который позволит работать с db
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        switch (v.getId()) {

            // КАК ОБНОВИТЬ КОНКРЕТНУЮ ЗАПИСЬ
            case R.id.update_intro_button:
                // проверяем, что id не пустое
                if (id.equalsIgnoreCase("")) {
                    break;
                }

                Log.d(TAG, "--- Update myTable ---");

                // подготовим значения для обновления
                contentValues.put("name", name);
                contentValues.put("email", email);

                // обновляем по id
                int updCount = db.update(
                        "myTable", contentValues, "id = ?", new String[] {id});
                Log.d(TAG, "Updated rows count = " + updCount);
                break;


            // КАК УДАЛИТЬ КОНКРЕТНУЮ ЗАПИСЬ
            case R.id.delete_intro_button:
                if (id.equalsIgnoreCase("")) {
                    break;
                }

                Log.d(TAG, "--- Delete from myTable ---");

                // удаляем по id
                int delCount = db.delete("myTable", "id = " + id, null);
                Log.d(TAG, "Deleted rows count = " + delCount);
                break;


            // КАК ВСТАВИТЬ ЗАПИСЬ
            case R.id.add_intro_button:
                Log.d(TAG, "Insert in myTable: ");

                // подготовим данные для вставки в виде пар: наименование столбца - значение
                contentValues.put("name", name);
                contentValues.put("email", email);

                // вставляем запись и получаем ее id
                long rowID = db.insert("myTable", null, contentValues);
                Log.d(TAG, "row inserted, ID = " + rowID); // смотрим id полученной строки с данными
                break;

            // КАК СЧИТАТЬ ВСЕ ЗАПИСИ
            case R.id.read_intro_button:
                Log.d(TAG, "--- Rows in myTable ---");

                // делаем запрос всех данных таблицы myTable, получаем Cursor
                Cursor cursor = db.query("myTable", null, null, null, null, null, null);

                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (cursor.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    int idColumnIndex = cursor.getColumnIndex("id");
                    int nameColumnIndex = cursor.getColumnIndex("name");
                    int emailColumnIndex = cursor.getColumnIndex("email");

                    do {
                        // получаем значения по номера столбцов и пишем все в лог
                        Log.d(TAG,
                                "ID = " + cursor.getInt(idColumnIndex) +
                                        ", name: " + cursor.getString(nameColumnIndex) +
                                        ", email: " + cursor.getString(emailColumnIndex));

                        // переход на следующую строку, а следующей нет false - выход из цикла
                    } while (cursor.moveToNext());

                } else {
                    Log.d(TAG, "0 rows");
                    cursor.close();
                    break;
                }
                break;

            // КАК ОЧИСТИТЬ ВСЮ ТАБЛИЦУ
            case R.id.clear_intro_button:
                Log.d(TAG, "--- Clear myTable ---");

                // удаляем все записи
                int clearCount = db.delete("myTable", null, null);
                Log.d(TAG, "Deleted rows count = " + clearCount);
                break;

        }


        // закрываем подключение к db
        mDBHelper.close();

    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(@Nullable Context context) {
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "--- onCreate database ---");

            // создаем таблицу с полями
            db.execSQL("CREATE TABLE myTable ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT,"
                    + "email TEXT" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
