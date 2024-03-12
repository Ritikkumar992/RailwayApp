package com.example.trainapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trainapi.Activities.TrainActivity;

public class MainActivity extends AppCompatActivity {
    EditText train_no_name;
    Button search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        train_no_name = findViewById(R.id.train_no_name);
        search = findViewById(R.id.btnId);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trainNoName = train_no_name.getText().toString().trim();
                Intent intent = new Intent(MainActivity.this, TrainActivity.class);
                intent.putExtra("Train_no_name", trainNoName);
                startActivity(intent);
            }
        });
    }
}