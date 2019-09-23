package com.omelchenkoaleks.databasesqlite._003_selecting;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.omelchenkoaleks.databasesqlite.R;

public class SelectingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SelectingActivity";

    String name[] = {"Китай", "США", "Бразилия", "Россия", "Япония",
            "Германия", "Египет", "Италия", "Франция", "Канада"};
    int people[] = {1400, 311, 195, 142, 128, 82, 80, 60, 66, 35};
    String region[] = {"Азия", "Америка", "Америка", "Европа", "Азия",
            "Европа", "Африка", "Европа", "Европа", "Америка"};

    private Button mAllCountriesButton;
    private Button mFunctionButton;
    private Button mPeopleButton;
    private Button mSortButton;
    private Button mGroupButton;
    private Button mHavingButton;

    private EditText mFunctionEditText;
    private EditText mPeopleEditText;
    private EditText mRegionEditText;

    private RadioGroup mSortRadioGroup;

    private SQLiteDatabase mDatabase;
    private DBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_003_selecting);

        mAllCountriesButton = findViewById(R.id.all_notes_button);
        mAllCountriesButton.setOnClickListener(this);

        mFunctionButton = findViewById(R.id.function_button);
        mFunctionButton.setOnClickListener(this);

        mPeopleButton = findViewById(R.id.people_button);
        mPeopleButton.setOnClickListener(this);

        mGroupButton = findViewById(R.id.population_by_region_button);
        mGroupButton.setOnClickListener(this);

        mHavingButton = findViewById(R.id.having_button);
        mHavingButton.setOnClickListener(this);

        mSortButton = findViewById(R.id.sort_button);
        mSortButton.setOnClickListener(this);

        mFunctionEditText = findViewById(R.id.function_edit_text);
        mPeopleEditText = findViewById(R.id.people_edit_text);
        mRegionEditText = findViewById(R.id.population_by_region_edit_text);

        mSortRadioGroup = findViewById(R.id.sort_radio_group);


        mDBHelper = new DBHelper(this);
        // подключаемся к database
        mDatabase = mDBHelper.getWritableDatabase();

        // проверка существования записей
        Cursor cursor = mDatabase.query("counties", null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            ContentValues contentValues = new ContentValues();

            // заполняем таблицу
            for (int i = 0; i < 10; i++) {
                contentValues.put("name", name[i]);
                contentValues.put("people", people[i]);
                contentValues.put("region", region[i]);

                Log.d(TAG, "id = " + mDatabase.insert("counties", null, contentValues));
            }
        }

        cursor.close();
        mDBHelper.close();

        // эмулируем нажатие кнопки mAllCountriesButton
        onClick(mAllCountriesButton);
    }

    @Override
    public void onClick(View v) {

        // подключаемся к базе данных
        mDatabase = mDBHelper.getWritableDatabase();

        // данные с экрана
        String dataFunctionEditText = mFunctionEditText.getText().toString().trim();
        String dataPeopleEditText = mPeopleEditText.getText().toString().trim();
        String dataRegionEditText = mRegionEditText.getText().toString().trim();

        // переменные для query
        String[] columns = null;
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;

        // курсор
        Cursor cursor = null;

        switch (v.getId()) {

            // ВСЕ ЗАПИСИ
            case R.id.all_notes_button:
                Log.d(TAG, "--- All notes ---");

                cursor = mDatabase.query("counties", null, null, null, null, null, null);
                break;

            // ФУНКЦИЯ
            case R.id.function_button:
                Log.d(TAG, "--- Function ---");

                columns = new String[]{dataFunctionEditText};
                cursor = mDatabase.query("counties", columns, null, null, null, null, null);
                break;

            // НАСЕЛЕНИЕ БОЛЬШЕ, ЧЕМ
            case R.id.people_button:
                Log.d(TAG, "--- Population more " + dataPeopleEditText + " ---");

                selection = "people > ?";
                selectionArgs = new String[]{dataPeopleEditText};
                cursor = mDatabase.query("counties", null, selection, selectionArgs, null, null, null);
                break;

            // НАСЕЛЕНИЕ БОЛЬШЕ ПО РЕГИОНУ
            case R.id.population_by_region_button:
                Log.d(TAG, "--- Population in region ---");

                columns = new String[]{"region", "sum(people) as people"};
                groupBy = "region";
                cursor = mDatabase.query("counties", columns, null, null, groupBy, null, null);
                break;

            // НАСЕЛЕНИЕ ПО РЕГИОНУ БОЛЬШЕ, ЧЕМ
            case R.id.having_button:
                Log.d(TAG, "--- Regions with population more " + dataRegionEditText + " ----");

                columns = new String[] {"region", "sum(people) as people"};
                groupBy = "region";
                having = "sum(people) > " + dataRegionEditText;
                cursor = mDatabase.query("counties", columns, null, null, groupBy, having, null, null);
                break;

            // СОРТИРОВКА
            case R.id.sort_button:

                // сортировка по
                switch (mSortRadioGroup.getCheckedRadioButtonId()) {

                    // НАИМЕНОВАНИЕ
                    case R.id.name_radio_button:
                        Log.d(TAG, "--- Sort by name ---");

                        orderBy = "name";
                        break;

                    // НАСЕЛЕНИЕ
                    case R.id.people_radio_button:
                        Log.d(TAG, "--- Sort by population ---");

                        orderBy = "people";
                        break;

                    // РЕГИОН
                    case R.id.region_radio_button:
                        Log.d(TAG, "Sort by region ---");

                        orderBy = "region";
                        break;

                }

                cursor = mDatabase.query("counties", null, null, null, null, null, orderBy);
                break;
        }

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : cursor.getColumnNames()) {
                        str = str.concat(cn + " = " + cursor.getString(cursor.getColumnIndex(cn)) + "; ");
                    }
                    Log.d(TAG, str);

                } while (cursor.moveToNext());
            }
            cursor.close();

        } else {
            Log.d(TAG, "Cursor is null");
        }

        mDBHelper.close();
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(@Nullable Context context) {
            super(context, "DB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "--- onCreate database ---");

            // создаем таблицу с полями
            db.execSQL("CREATE TABLE counties ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT,"
                    + "people INTEGER,"
                    + "region TEXT" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
