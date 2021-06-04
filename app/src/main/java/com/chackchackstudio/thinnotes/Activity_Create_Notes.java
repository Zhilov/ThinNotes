package com.chackchackstudio.thinnotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Activity_Create_Notes extends AppCompatActivity {

    EditText editTitle;
    EditText editBody;
    DBHelper dbHelper;
    SQLiteDatabase db;
    SimpleDateFormat sdf;
    String currentDateAndTime;
    String titleNull;
    NoteModel noteModel;
    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__create__notes);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        editTitle = findViewById(R.id.editTitle);
        editBody = findViewById(R.id.editBody);


        noteModel = getIntent().getParcelableExtra("NoteModel");
        if (noteModel != null) {
            editTitle.setText(noteModel.getTitle());
            editBody.setText(noteModel.getBody());
        }

        dbHelper = new DBHelper(this, "DBNotes", null, 1);
        db = dbHelper.getWritableDatabase();

        sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        currentDateAndTime = sdf.format(new Date());

    }

    private void checkAndPush() {
        if (editTitle.getText().toString().isEmpty() || editTitle.getText().toString().equals("") || editTitle.getText().toString().equals(" ")) {
            titleNull = " ";
        } else {
            titleNull = editTitle.getText().toString();
        }
        if (!editBody.getText().toString().isEmpty()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("NOTE_TITLE", titleNull);
            contentValues.put("NOTE_DATE", currentDateAndTime);
            contentValues.put("NOTE_BODY", editBody.getText().toString());
            if (noteModel != null) {
                db.update("Notes", contentValues, "_id=" + "'" + noteModel.getId() + "'", null);
            } else {
                db.insert("Notes", null, contentValues);
            }
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            checkAndPush();
        }

        if (item.getItemId() == R.id.action_delete) {
            db.delete("Notes", "_id=" + "'" + noteModel.getId() + "'", null);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Заполнение меню; элементы действий добавляются на панель приложения
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        menuItem = menu.findItem(R.id.action_delete);
        menuItem.setVisible(noteModel != null);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        checkAndPush();
    }
}