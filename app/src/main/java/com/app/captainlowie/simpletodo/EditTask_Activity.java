package com.app.captainlowie.simpletodo;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditTask_Activity extends AppCompatActivity {

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TaskDbHelper mHelper;
    EditText taskEditText;
    FloatingActionButton addTaskFab;
    TextView clockTextView;
    TextView dateTextView;
    EditText notesEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task_);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setDefaultDateTime();

        taskEditText = findViewById(R.id.taskEditText);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Black.ttf");
        taskEditText.setTypeface(tf);

        notesEditText = findViewById(R.id.notesEditText);
        notesEditText.setTypeface(tf);

        String taskOld = getIntent().getStringExtra("EXTRA_OLD_TEXT");
        Log.v("Oldtask", taskOld);
        taskEditText.setText(taskOld);
    }

    public void saveButtonClicked(View v) {

        String taskOld = getIntent().getStringExtra("EXTRA_OLD_TEXT");
        String taskNew = taskEditText.getText().toString();

        if (taskEditText.equals("")) {
            Toast toast = Toast.makeText(EditTask_Activity.this, "You have to enter a task, idiot", Toast.LENGTH_SHORT);
            toast.show();
        } else {

            Intent intent = new Intent();
            intent.putExtra(IntentConstants.EDIT_TASK_NAME, taskNew);
            intent.putExtra(IntentConstants.OLD_TASK_NAME, taskOld);
            Log.v("taskNew", taskNew);
            setResult(Activity.RESULT_FIRST_USER, intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void openTimePicker(View view) {
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(1000);
        view.startAnimation(animation1);

        clockTextView = findViewById(R.id.clockTextView);
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    mcurrentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mcurrentTime.set(Calendar.MINUTE, minute);

                    String sMinute = minute + "";

                    if (minute == 0) {
                        sMinute = "00";
                        clockTextView.setText(hourOfDay + ":" + sMinute);
                    } else if (minute <10) {
                        sMinute = "0"+minute;
                        clockTextView.setText(hourOfDay + ":" + sMinute);
                    } else {
                        clockTextView.setText(hourOfDay + ":" + minute);
                    }
                }
            }

        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(EditTask_Activity.this, R.style.editPicker, myTimeListener, hour, minute, true);
        timePickerDialog.show();

    }

    public void openDatePicker(View view) {
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(1000);
        view.startAnimation(animation1);

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(),"Date Picker");
    }

    private void setDefaultDateTime() {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        month++;
        int day = c.get(Calendar.DAY_OF_MONTH);

        dateTextView = findViewById(R.id.dateTextView);
        clockTextView = findViewById(R.id.clockTextView);

        dateTextView.setText(day+"."+month+"."+year);
        Log.v("DATE",day+"."+month+"."+year);


        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        String sMinute = minute + "";

        if (minute == 0) {
            sMinute = "00";
            clockTextView.setText(hour + ":" + sMinute);
        } else if (minute <10) {
            sMinute = "0"+minute;
            clockTextView.setText(hour + ":" + sMinute);
        } else {
            clockTextView.setText(hour + ":" + minute);
        }
    }
}
