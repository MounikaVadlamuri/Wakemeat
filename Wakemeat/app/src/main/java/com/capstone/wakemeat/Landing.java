package com.capstone.wakemeat;

import android.content.Intent;
import android.database.Cursor;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Landing extends AppCompatActivity {
    DatabaseHelper dbHelper;
    Button AddAlarmButton;
    TextView DisplayAlarms;
    EditText IdToDelete;
    Button DeleteAlarmButton;
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
}