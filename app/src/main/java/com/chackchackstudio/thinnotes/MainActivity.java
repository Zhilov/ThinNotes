package com.chackchackstudio.thinnotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textEmptyList;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    List<NoteModel> list;

    SQLiteDatabase db;
    DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textEmptyList = findViewById(R.id.textEmptyList);
        recyclerView = findViewById(R.id.recycler_main);

        dbHelper = new DBHelper(this, "DBNotes", null, 1);
        db = dbHelper.getWritableDatabase();

        list = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(this, list);
        StaggeredGridLayoutManager staggeredgridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredgridLayoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(2, 30, true));
        recyclerView.setAdapter(recyclerAdapter);

        updateRecyclerView();

        recyclerAdapter.setClickListener((view, position) -> {
            Intent intent = new Intent(MainActivity.this, Activity_Create_Notes.class);
            intent.putExtra("NoteModel", list.get(position));
            startActivityForResult(intent, 1);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Заполнение меню; элементы действий добавляются на панель приложения
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivityForResult(new Intent(MainActivity.this, Activity_Create_Notes.class), 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        updateRecyclerView();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateRecyclerView() {
        list.clear();
        Cursor cursor = db.query("Notes", new String[]{"_id", "NOTE_TITLE", "NOTE_DATE", "NOTE_BODY"}, null, null, null, null, null);
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            list.add(new NoteModel(cursor.getString(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("NOTE_TITLE")),
                    cursor.getString(cursor.getColumnIndex("NOTE_DATE")),
                    cursor.getString(cursor.getColumnIndex("NOTE_BODY"))
            ));
        }
        cursor.close();
        checkVisibility();
        recyclerAdapter.notifyDataSetChanged();
    }

    private void checkVisibility() {
        if (list.isEmpty()) {
            textEmptyList.setVisibility(View.VISIBLE);
        } else {
            textEmptyList.setVisibility(View.INVISIBLE);
        }
    }
}