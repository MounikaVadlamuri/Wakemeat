package com.capstone.wakemeat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    EditText TimeInitialEditText;
    EditText TimeLimitEditText;
    String selectedlatLng = null;
    TextView LatlongText;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                            Toast.makeText(AddAlarm.this, "Recieved Location: " + latLong, Toast.LENGTH_SHORT).show();
                            LatlongText.setText(latLong);
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
                        String timeInitial = TimeInitialEditText.getText().toString();
                        String timeLimit = TimeLimitEditText.getText().toString();

                        if (alarmName.isEmpty() || timeInitial.isEmpty()|| timeLimit.isEmpty()) {
                            Toast.makeText(AddAlarm.this, "Please enter both Alarm Name and Time Interval", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (selectedlatLng == null) {
                            Toast.makeText(AddAlarm.this, "Please select a location using the Select Location Button", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(AddAlarm.this, "Alarm Saved!", Toast.LENGTH_SHORT).show();
                        Intent landing = new Intent();
                        landing.putExtra("activity_result","Alarm " + alarmName + " saved with success!");
                        setResult(102, landing);
                        finish();
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