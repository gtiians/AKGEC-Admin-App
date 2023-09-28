package gtiians.akgec.faculty;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class PostNoticeActivity extends AppCompatActivity {


    ImageView noticeView;
    private static final int REQ = 1;
    private EditText noticeTitle;

    private Bitmap bitmap;
    String downloadUrl= "";

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_notice);

        CardView selectNotice = findViewById(R.id.selectNotice);
        noticeTitle = findViewById(R.id.noticeTitle);
        Button postNoticeBtn = findViewById(R.id.postNoticeBtn);
        noticeView = findViewById(R.id.noticeView);

        databaseReference = FirebaseDatabase.getInstance("https://console.firebase.google.com/u/0/project/akgec-gzb-app/database/akgec-gzb-app-default-rtdb/data/~2F").getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("News Feed");
        pd = new ProgressDialog(this);


        selectNotice.setOnClickListener(view -> {
            //creating this, to open gallery
            openGallery();
        });

        postNoticeBtn.setOnClickListener(view -> {
            //checking title is not empty
            if (noticeTitle.getText().toString().isEmpty()){
                noticeTitle.setError("Please Enter Notice Title");
                noticeTitle.requestFocus();
            }else if(bitmap == null){
                //upload data to firebase
                uploadData();
            }else{
                //when getting img through bitmap and title than using this method store in firebase
                postNotice();
            }
        });


    }


    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQ);
    }

    //uploading data
    private void postNotice() {
        //progress bar showing
        pd.setMessage("Uploading...");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //compressing the image before upload to firebase
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] finalimg = baos.toByteArray();

        //creating filepath to store data in firebase storage
        final StorageReference filePath;
        filePath = storageReference.child(Arrays.toString(finalimg) +"jpg");
        //uploading task
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(PostNoticeActivity.this, task -> {
            if (task.isSuccessful()){
                uploadTask.addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                    downloadUrl = String.valueOf(uri);
                    uploadData();
                }));
            }else{
                pd.dismiss();
                Toast.makeText(PostNoticeActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //storing data in firebase storage
    private void uploadData() {
        DatabaseReference dbRef = databaseReference.child("News Feed");
        //getting unique key, to store data on firebase within
        final String uniqueKey = dbRef.push().getKey();
        //getting the title
        String title = noticeTitle.getText().toString();
        //getting the currentDate
        Calendar calForDate = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("dd--MM--yy");
        String date = currentDate.format(calForDate.getTime());
        //getting the currentTime
        Calendar calForTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());
        //here NoticeData comes from NoticeData Java class file where created constructor
        NoticeData noticeData = new NoticeData(title,downloadUrl,date,time,uniqueKey);
        /*storing in firebase but we will not know so +1 method
        reference.child(uniqueKey).setValue(noticeData);*/
        if (uniqueKey != null) {
            dbRef.child(uniqueKey).setValue(noticeData).addOnSuccessListener(aVoid -> {
                pd.dismiss();
                Toast.makeText(PostNoticeActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                pd.dismiss();
                Toast.makeText(PostNoticeActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            //will get the img in uri and initialize to bitmap
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
            }
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //set on img(bitmap) noticeView to show preview in down
            noticeView.setImageBitmap(bitmap);
        }
    }

}