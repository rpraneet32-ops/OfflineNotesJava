package com.example.offlinenotesjava;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTitle, editContent;
    private Button btnSave;
    private TextView textNotesList;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Setup the screen inputs
        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.btnSave);
        textNotesList = findViewById(R.id.textNotesList);

        // 2. Start the Offline Database
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "my-offline-notes")
                .allowMainThreadQueries() // Simple mode for beginners
                .build();

        // 3. Load any saved notes
        loadNotes();

        // 4. Save Button Action
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = editTitle.getText().toString();
                String c = editContent.getText().toString();

                if (!t.isEmpty()) {
                    Note n = new Note(t, c);
                    db.noteDao().insert(n); // Save to phone storage

                    editTitle.setText(""); // Clear inputs
                    editContent.setText("");
                    loadNotes(); // Refresh list
                }
            }
        });
    }

    // Helper function to show notes
    private void loadNotes() {
        List<Note> notes = db.noteDao().getAllNotes();
        StringBuilder sb = new StringBuilder();

        for (Note n : notes) {
            sb.append("• ").append(n.title).append("\n");
            sb.append("   ").append(n.content).append("\n\n");
        }

        if (notes.isEmpty()) textNotesList.setText("No notes yet.");
        else textNotesList.setText(sb.toString());
    }
}