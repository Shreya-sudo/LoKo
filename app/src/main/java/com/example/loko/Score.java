package com.example.loko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Score extends AppCompatActivity {
private TextView score;
private Button done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        score=findViewById(R.id.score);
        done=findViewById(R.id.done);
        String scoreStr=getIntent().getStringExtra("SCORE");
        score.setText(scoreStr);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Score.this,MainActivity.class);
                Score.this.startActivity(intent);
                Score.this.finish();
            }
        });

    }
}