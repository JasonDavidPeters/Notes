package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.util.ArrayList;

public class NotePage extends AppCompatActivity implements Serializable {

   private String note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_page);

        Button addNotesBtn = (Button) this.findViewById(R.id.addNoteBtn);
        TextInputEditText textField = (TextInputEditText) findViewById(R.id.textField);

        addNotesBtn.setOnClickListener(e -> {
            note = textField.getText().toString();
            //Log.d("test",text);

            Intent switchToHome = new Intent(this, MainActivity.class);
            if ((note.trim()).length() > 0)
            switchToHome.putExtra("note",note);
            startActivity(switchToHome);


        });

    }
}