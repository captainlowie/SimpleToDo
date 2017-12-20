package com.app.captainlowie.simpletodo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by eduard on 30.11.17.
 */

public class AddTask_Activity extends AppCompatActivity {


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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_activity);

        taskEditText = findViewById(R.id.taskNameEditText);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Black.ttf");
        taskEditText.setTypeface(tf);

        notesEditText = findViewById(R.id.notesEditText);
        notesEditText.setTypeface(tf);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setDefaultDateTime();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void saveButtonClicked(View view) {
        taskEditText = findViewById(R.id.taskNameEditText);
        String taskName = taskEditText.getText().toString();
        notesEditText = findViewById(R.id.notesEditText);
        String notesText = notesEditText.getText().toString();

        if (taskName.equals("")) {
            Toast noInputToast = Toast.makeText(AddTask_Activity.this,
                    "Your task needs a name", Toast.LENGTH_SHORT);
            noInputToast.show();
        } else {
            Intent intent = new Intent();
            intent.putExtra(IntentConstants.NEW_TASK_NAME, taskName);
            intent.putExtra(IntentConstants.NEW_TASK_DESCRIPTION, notesText);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, AddTask_Activity.class);
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


        TimePickerDialog timePickerDialog = new TimePickerDialog(AddTask_Activity.this, R.style.addPicker, myTimeListener, hour, minute, true);
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
