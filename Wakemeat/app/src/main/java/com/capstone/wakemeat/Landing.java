package com.capstone.wakemeat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class Landing extends AppCompatActivity {
    DatabaseHelper dbHelper;
    Button AddAlarmButton;
    TextView DisplayAlarms;
    EditText IdToDelete;
    Button DeleteAlarmButton;
    Button toggleButton;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DisplayAlarms = findViewById(R.id.DisplayAlarms);
        DisplayAlarms.setText(showAlarms());

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();

                        if(resultCode==102)
                        {
                            String alarmMessage=data.getStringExtra("activity_result");
                            Toast.makeText(Landing.this, alarmMessage, Toast.LENGTH_SHORT).show();
                            DisplayAlarms.setText(showAlarms());
                        }
                        else {
                            Toast.makeText(Landing.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        AddAlarmButton = findViewById(R.id.AddAlarmButton);
        AddAlarmButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Landing.this, AddAlarm.class);
                        activityResultLauncher.launch(i);
                    }
                }
        );

        IdToDelete = findViewById(R.id.IdToDelete);
        DeleteAlarmButton = findViewById(R.id.DeleteAlarmButton);
        DeleteAlarmButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(IdToDelete.getText().toString().isEmpty()){
                            Toast.makeText(Landing.this, "Please enter an Alarm Id", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Integer deletedRows = deleteAlarm(Integer.parseInt(IdToDelete.getText().toString()));
                            if(deletedRows > 0){
                                Toast.makeText(Landing.this, "Alarm deleted successfully", Toast.LENGTH_SHORT).show();
                                DisplayAlarms.setText(showAlarms());
                            } else {
                                Toast.makeText(Landing.this, "No alarm found with the given Id, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );

        toggleButton = findViewById(R.id.toggleButton);

    }

    public void OnToggleClicked(View v) {
        long time;
        if (((ToggleButton) v).isChecked()) {
            Toast.makeText(Landing.this, "ALARM ON", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();

            String HourMinute = returnLatestAlarmTime();

            if(HourMinute.isEmpty()){
                return;
            }

            int Hour = Integer.parseInt(HourMinute.split(":")[0]);
            int Minute = Integer.parseInt(HourMinute.split(":")[1]);

            // calendar is called to get current time in hour and minute
            calendar.set(Calendar.HOUR_OF_DAY, Hour);
            calendar.set(Calendar.MINUTE, Minute);

            // using intent i have class AlarmReceiver class which inherits
            // BroadcastReceiver
            Intent intent = new Intent(Landing.this, AlarmReceiver.class);

            // we call broadcast using pendingIntent
            pendingIntent = PendingIntent.getBroadcast(Landing.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, alarmManager.INTERVAL_DAY, pendingIntent);

            if (System.currentTimeMillis() > time) {

                // setting time as AM and PM
                if (Calendar.AM_PM == 0)
                    time = time + (1000 * 60 * 60 * 12);
                else
                    time = time + (1000 * 60 * 60 * 24);
            }
            // Alarm rings continuously until toggle button is turned off
            //alarmManager.setA(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);
            //Toast.makeText(Landing.this, "time=" + time, Toast.LENGTH_SHORT).show();
            //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time * 1000), pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(Landing.this, "ALARM OFF", Toast.LENGTH_SHORT).show();
        }
    }

    public String showAlarms() {
        dbHelper = new DatabaseHelper(this);
        Cursor res = dbHelper.listAllAlarms();
        if(res.getCount() == 0){

            return "No alarms found, try adding a new one";
        } else {
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("Alarm Id: "+res.getString(0)+"\n");
                buffer.append("Alarm Name: "+res.getString(1)+"\n");
                buffer.append("Alarm Initial Time: "+res.getString(2)+"\n");
                buffer.append("Alarm Limit Time: "+res.getString(3)+"\n");
                buffer.append("Alarm Location Latitude: "+res.getString(4)+"\n");
                buffer.append("Alarm Location Longitude: "+res.getString(5)+"\n");
                buffer.append("_____________________________________\n");
            }
            return buffer.toString();
        }
    }

    public Integer deleteAlarm(Integer alarmId){
        dbHelper = new DatabaseHelper(this);
        return dbHelper.deleteAlarmbyId(alarmId);
    }

    public String returnLatestAlarmTime(){
        dbHelper = new DatabaseHelper(this);
        Cursor res = dbHelper.listLastAlarm();
        if(res.getCount() == 0){
            return null;
        } else {
            String hour = "";
            while (res.moveToNext()) {
                hour = res.getString(0);
            }
            return hour;
        }
    }

}