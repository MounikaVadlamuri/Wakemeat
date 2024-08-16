package com.capstone.wakemeat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.model.LatLng;

public class AddAlarm extends AppCompatActivity {
    DatabaseHelper DbHelper;
    Button SelectLocationButton;
    Button SaveAlarmButton  ;
    EditText AlarmNameEditText ;
    TimePicker TimeInitialEditText;
    TimePicker TimeLimitEditText;
    String selectedlatLng = null;
    TextView LatlongText;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DbHelper = new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_alarm);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LatlongText = findViewById(R.id.LatlongText);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();

                        LatlongText = findViewById(R.id.LatlongText);
                        if(resultCode==101)
                        {
                            String latLong=data.getStringExtra("activity_result");
                            LatlongText.setText(latLong.replace("lat/lng: (","").replace(")",""));
                            selectedlatLng = latLong;
                        }
                    }
                });


        SaveAlarmButton = findViewById(R.id.SaveAlarmButton);
        AlarmNameEditText = findViewById(R.id.AlarmName);
        TimeInitialEditText = findViewById(R.id.TimeInitial);
        TimeLimitEditText = findViewById(R.id.TimeLimit);
        SaveAlarmButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String alarmName = AlarmNameEditText.getText().toString();
                        String initialHourResult;
                        String initialMinuteResult;
                        String limitHourResult;
                        String limitMinuteResult;
                        int initialHour = TimeInitialEditText.getHour();
                        int initialMinute = TimeInitialEditText.getMinute();
                        int limitHour = TimeLimitEditText.getHour();
                        int limitMinute = TimeLimitEditText.getMinute();

                        if (initialHour < 10)
                            initialHourResult = String.valueOf("0" + initialHour);
                        else
                            initialHourResult = String.valueOf(initialHour);

                        if (initialMinute < 10)
                            initialMinuteResult = String.valueOf("0" + initialMinute);
                        else
                            initialMinuteResult = String.valueOf(initialMinute);

                        if (limitHour < 10)
                            limitHourResult = String.valueOf("0" + limitHour);
                        else
                            limitHourResult = String.valueOf(limitHour);

                        if (limitMinute < 10)
                            limitMinuteResult = String.valueOf("0" + limitMinute);
                        else
                            limitMinuteResult = String.valueOf(limitMinute);

                        String timeInitial = initialHourResult + ":" + initialMinuteResult;
                        String timeLimit = limitHourResult + ":" + limitMinuteResult;

                        if (alarmName.isEmpty() || timeInitial.isEmpty()|| timeLimit.isEmpty()) {
                            Toast.makeText(AddAlarm.this, "Please enter both Alarm Name and Time Interval", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (selectedlatLng == null) {
                            Toast.makeText(AddAlarm.this, "Please select a location using the Select Location Button", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Double latitude = Double.parseDouble(selectedlatLng.replace("lat/lng: (","").replace(")","").split(",")[0]);
                            Double longitude = Double.parseDouble(selectedlatLng.replace("lat/lng: (","").replace(")","").split(",")[1]);

                            boolean isInserted = DbHelper.insertAlarm(alarmName, timeInitial, timeLimit, latitude, longitude);

                            if (isInserted) {
                                Intent landing = new Intent();
                                landing.putExtra("activity_result","Alarm registered with success!");
                                setResult(102, landing);
                                finish();
                            } else {
                                Toast.makeText(AddAlarm.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );

        SelectLocationButton = findViewById(R.id.SelectLocationButton);

        SelectLocationButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(AddAlarm.this, MapsInteraction.class);
                        activityResultLauncher.launch(i);
                    }
                }
        );
    }
}