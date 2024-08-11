package com.capstone.wakemeat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Landing extends AppCompatActivity {
    DatabaseHelper dbHelper;
    Button AddAlarmButton;
    TextView DisplayAlarms;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        //DisplayAlarms.setText(message);
        //showAlarms();
    }

    public String showAlarms() {
        dbHelper = new DatabaseHelper(this);
        Cursor res = dbHelper.listAllAlarms();
        if(res.getCount() == 0){
             //showMessage("Error","No alarms found, try adding a new one");
            return "No alarms found, try adding a new one";
        } else {
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("Alarm Name: "+res.getString(1)+"\n");
                buffer.append("Alarm Initial Time: "+res.getString(2)+"\n");
                buffer.append("Alarm Limit Time: "+res.getString(3)+"\n");
                buffer.append("Alarm Location Latitude: "+res.getString(4)+"\n");
                buffer.append("Alarm Location Longitude: "+res.getString(5)+"\n");
                buffer.append("_____________________________________\n");
            }

             //showMessage("data", buffer.toString());
            return buffer.toString();
        }
    }

    public void showMessage(String title, String mesage){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(mesage);
        builder.show();
    }
}