package gtiians.akgec.faculty;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class PostNotesActivity extends AppCompatActivity {


    private EditText notesTitle;
    private String notesName, title;
    private Uri notesData;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    String downloadUrl= "";
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_notes);

        pd = new ProgressDialog(this);

        CardView addNotes = findViewById(R.id.addNotes);
        notesTitle = findViewById(R.id.notesTitle);
        Button uploadNotesBtn = findViewById(R.id.uploadNotesBtn);
        TextView notesTextView = findViewById(R.id.notesTextView);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        addNotes.setOnClickListener(view -> openGallery());

        uploadNotesBtn.setOnClickListener(view -> {
            title = notesTitle.getText().toString();
            if (title.isEmpty()){
                notesTitle.setError("Please Enter the Title");
                notesTitle.requestFocus();
            }else if (notesData == null){
                Toast.makeText(PostNotesActivity.this, "Please Select Notes", Toast.LENGTH_SHORT).show();
            }else {
                postNotes();
            }
        });

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("pdf/docs/ppt");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        int REQ = 1;
        startActivityForResult(Intent.createChooser(intent,"Select Notes"), REQ);
    }


    private void postNotes() {
        pd.setTitle("Please Wait...");
        pd.setMessage("Uploading Notes");
        pd.show();
        StorageReference reference = storageReference.child("pdf/"+ notesName + "-" +System.currentTimeMillis()+ ".pdf");
        reference.putFile(notesData).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isComplete());
            Uri uri = uriTask.getResult();
            uploadData(String.valueOf(uri));
        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(PostNotesActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        });
    }


    private void uploadData(String downloadUrl) {
        String uniqueKey = databaseReference.child("Study Notes").push().getKey();
        HashMap hp = new HashMap();
        hp.put("notesTitle",title);
        hp.put("notesUrl",downloadUrl);
        if (uniqueKey != null) {
            databaseReference.child("Study Notes").child(uniqueKey).setValue(hp).addOnCompleteListener(task -> {
                pd.dismiss();
                Toast.makeText(PostNotesActivity.this, "Thank You", Toast.LENGTH_SHORT).show();
                notesTitle.setText("");
            }).addOnFailureListener(e -> {
                pd.dismiss();
                Toast.makeText(PostNotesActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
            });
        }
    }

}