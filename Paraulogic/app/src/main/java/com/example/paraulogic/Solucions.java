package com.example.paraulogic;
/*
 * @author Pau Rosado Muñoz
 */
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import android.text.Html;

public class Solucions extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solucions);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView text = (TextView) findViewById(R.id.textView);
        text.setText(Html.fromHtml(message));
    }
}
