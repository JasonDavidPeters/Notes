package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Serializable {

    private ArrayList<String> notes = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addNotesBtn = (Button) this.findViewById(R.id.addNotesBtn);
        addNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchToNotePage = new Intent(getApplicationContext(),NotePage.class);
                if (notes != null && notes.size() >0) {
                    switchToNotePage.putExtra("notes",notes);
                }
                startActivity(switchToNotePage);
            }
        });
        redraw();

    }

    protected void redraw() {
        readFromFile("Notes.txt");
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linLayout);
        String note = (String) getIntent().getStringExtra("note");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(Resources.getSystem().getDisplayMetrics().widthPixels-150, LinearLayout.LayoutParams.WRAP_CONTENT));
        lp.setMargins(0,0,0,150);
//        linearLayout.setBackground(getResources().getDrawable(R.drawable.border));
        if (note != null) {
                Log.d("NOTE: ", note);
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            Button delBtn = new Button(this);
            delBtn.setText("[X]");
            delBtn.setWidth(30);
            delBtn.setHeight(30);
            TextView ts = new TextView(this);
            ts.setText(note);
            ts.setLayoutParams(lp);
            ts.setBackground(getResources().getDrawable(R.drawable.border));
            ll.addView(ts);
            ll.addView(delBtn);
            linearLayout.addView(ll);

            delBtn.setOnClickListener( e-> {
                ((ViewGroup)delBtn.getParent().getParent()).removeView((ViewGroup)delBtn.getParent());
                TextView tv = (TextView) ((ViewGroup) delBtn.getParent()).getChildAt(0);
                Log.d("REMOVE FROM FILE-", tv.getText().toString());
                removeFromFile("Notes.txt", tv.getText().toString());
            });
                writeToFile("Notes.txt",note);
        }
        if (notes.size()>0) {
            for (int i = 0; i < notes.size(); i++) {
                LinearLayout ll = new LinearLayout(this);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                Button delBtn = new Button(this);
                delBtn.setText("[X]");
                delBtn.setWidth(30);
                delBtn.setHeight(30);
                TextView ts = new TextView(this);
                ts.setText(notes.get(i));
                ts.setLayoutParams(lp);
               ts.setBackground(getResources().getDrawable(R.drawable.border));
                ll.addView(ts);
                ll.addView(delBtn);
                linearLayout.addView(ll);

                delBtn.setOnClickListener( e-> {
                 ((ViewGroup)delBtn.getParent().getParent()).removeView((ViewGroup)delBtn.getParent());
                 TextView tv = (TextView) ((ViewGroup) delBtn.getParent()).getChildAt(0);
                 Log.d("REMOVE FROM FILE-", tv.getText().toString());
                 removeFromFile("Notes.txt", tv.getText().toString());
                });
            }
        }
    }

    private void removeFromFile(String fileName, String text) {
        ArrayList<String> newNotes = new ArrayList<String>();
        FileOutputStream ostream = null;
        FileInputStream istream = null;
        OutputStreamWriter writer = null;
        InputStreamReader reader = null;
        BufferedWriter bw = null;
        try {
            istream = openFileInput(fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (istream != null) {
            try {
                reader = new InputStreamReader(istream);
                BufferedReader br = new BufferedReader(reader);
                String t = null;
                while ((t = br.readLine()) != null) {
                    if (!t.equals(text)) {
                        newNotes.add(t);
                    }
                }
                br.close();
            }   catch (Exception e) {
                e.printStackTrace();
            }
        }
        deleteFile(fileName);

        try {
            ostream = openFileOutput(fileName,MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (ostream!=null) {
            writer=new OutputStreamWriter(ostream);
            bw = new BufferedWriter(writer);
            try {
                for (int i = 0; i < newNotes.size();i++) {
                    bw.write(newNotes.get(i) + "\n");
                    bw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void writeToFile(String fileName, String contents) {
        /*
            Check if file exists
         */
        FileOutputStream ostream = null;
        OutputStreamWriter writer = null;
        FileWriter fw = null;
        try {
            ostream = openFileOutput(fileName,MODE_APPEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ostream!=null) {
            writer=new OutputStreamWriter(ostream);
            try {
                writer.write(contents+"\n");
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
      //Log.d("DIRECTORY",getApplicationContext().getFileStreamPath(fileName).getAbsolutePath());
    }
    private void readFromFile(String fileName) {
        FileInputStream istream = null;
        InputStreamReader reader = null;
        File file = new File(getFilesDir() + "/"+fileName);
        Log.d("File Path: ", "" +  file.getAbsoluteFile());
        if (file.isFile()) {
            try {
                istream = openFileInput(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (istream != null) {
                reader = new InputStreamReader(istream);
                BufferedReader br = new BufferedReader(reader);
                try {
                    String text;
                    ArrayList<String> newNotes = new ArrayList<String>();
                    while ((text = br.readLine()) != null) {
                        Log.d("READ:", "" + text);
                        newNotes.add(text);
                    }
                    notes = newNotes;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    istream.close();
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}