package com.app.captainlowie.simpletodo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private int close;
    private TaskDbHelper mHelper;
    private ListView taskView;
    private FloatingActionButton btnAdd;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        btnAdd = findViewById(R.id.btnAdd);
        mHelper = new TaskDbHelper(this);
        taskView = findViewById(R.id.taskList);
        deleteTask();
        updateList();
        editTask();


    }

    @Override
    public void onBackPressed() {
        if (close == 0) {
            Toast exit = Toast.makeText(MainActivity.this, "Click again to close the app", Toast.LENGTH_SHORT);
            exit.show();
            close++;
        } else {
            super.onBackPressed();
        }
    }

    public void openAddDialog(View v) {
        Intent intent = AddTask_Activity.makeIntent(this);
        startActivityForResult(intent, IntentConstants.INTENT_REQUEST_NEW);
        updateList();
    }

    private void updateList() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.task_title,
                    taskList);
            taskView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    public void deleteTask() {

        taskView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, int i, long l) {

                Log.v("Longclick", "pos " + i);

                TextView taskTextView = view.findViewById(R.id.task_title);
                String clickedItem = taskTextView.getText().toString();
                Log.v("clickedItem", clickedItem);


                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete task")
                        .setMessage("Are you sure?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView taskTextView = view.findViewById(R.id.task_title);
                                String task = String.valueOf(taskTextView.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                db.delete(TaskContract.TaskEntry.TABLE,
                                        TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                                        new String[]{task});
                                db.close();
                                updateList();

                            }
                        })

                        .create();
                dialog.show();


                return true;
            }


        });

    }

    public void editTask() {
        taskView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                Log.v("Shortclick", "pos " + i);

                TextView taskTextView = view.findViewById(R.id.task_title);
                String clickedItem = taskTextView.getText().toString();
                Log.v("clickedItem", clickedItem);

                Intent intent = new Intent(getBaseContext(), EditTask_Activity.class);
                intent.putExtra("EXTRA_OLD_TEXT", clickedItem);
                startActivityForResult(intent, IntentConstants.INTENT_REQUEST_EDIT);
                updateList();
            }
        });
    }


    //get Result from Intent and handles it by its request/result code
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        close = 0;

        switch (requestCode) {
            //if intent was to make a new task -> add task to arraylist
            case IntentConstants.INTENT_REQUEST_NEW:
                if (resultCode == Activity.RESULT_OK) {
                    //get Taskname
                    String taskName = data.getStringExtra(IntentConstants.NEW_TASK_NAME);

                    //save taskName to DB
                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(TaskContract.TaskEntry.COL_TASK_TITLE, taskName);
                    db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                            null,
                            values,
                            SQLiteDatabase.CONFLICT_REPLACE);
                    db.close();
                    updateList();
                    break;

                }
                //error handling -> makes a toast with an error message
                else {
                    //Toast t = Toast.makeText(MainActivity.this, "oops, something went wrong!", Toast.LENGTH_LONG);
                    //t.show();
                    break;
                }

            case IntentConstants.INTENT_REQUEST_EDIT:
                if (resultCode == Activity.RESULT_FIRST_USER) {
                    String taskOld = data.getStringExtra(IntentConstants.OLD_TASK_NAME);
                    String taskNew = data.getStringExtra(IntentConstants.EDIT_TASK_NAME);

                    SQLiteDatabase db = mHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    db.delete(TaskContract.TaskEntry.TABLE,
                            TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                            new String[]{taskOld});
                    values.put(TaskContract.TaskEntry.COL_TASK_TITLE, taskNew);
                    db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                            null,
                            values,
                            SQLiteDatabase.CONFLICT_REPLACE);
                    db.close();
                    updateList();
                    break;
                } else {

                    break;
                }

        }
    }
}