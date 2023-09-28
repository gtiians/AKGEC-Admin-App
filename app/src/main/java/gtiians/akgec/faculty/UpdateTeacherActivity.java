package gtiians.akgec.faculty;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class UpdateTeacherActivity extends AppCompatActivity {

    private ImageView updateTeacherImage;
    private EditText updateTeacherName, updateTeacherPost, updateTeacherEmail;

    private String name,email,image,post;

    private final int REQ = 1;

    private Bitmap bitmap = null;

    private DatabaseReference reference;
    private StorageReference storageReference;
    private String downloadUrl;
    private ProgressDialog pd;

    private String category, uniqueKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);

        uniqueKey = getIntent().getStringExtra("key");
        category = getIntent().getStringExtra("category");

        name = getIntent().getStringExtra("name");
        post = getIntent().getStringExtra("post");
        email = getIntent().getStringExtra("email");
        image = getIntent().getStringExtra("image");

        updateTeacherImage = findViewById(R.id.updateTeacherImage);
        updateTeacherName = findViewById(R.id.updateTeacherName);
        updateTeacherPost = findViewById(R.id.updateTeacherPost);
        updateTeacherEmail = findViewById(R.id.updateTeacherEmail);

        Button updateTeacherBtn = findViewById(R.id.updateTeacherBtn);
        Button deleteTeacherBtn = findViewById(R.id.deleteTeacherBtn);

        reference = FirebaseDatabase.getInstance("https://console.firebase.google.com/u/0/project/akgec-gzb-app/database/akgec-gzb-app-default-rtdb/data/~2F").getReference().child("Faculties");      //initializing
        storageReference = FirebaseStorage.getInstance().getReference().child("Faculties");

        pd = new ProgressDialog(this);

        try {
            Picasso.get().load(image).into(updateTeacherImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateTeacherName.setText(name);
        updateTeacherPost.setText(post);
        updateTeacherEmail.setText(email);
        updateTeacherImage.setOnClickListener(view -> openGallery());

        updateTeacherBtn.setOnClickListener(view -> {

            name = updateTeacherName.getText().toString();
            post = updateTeacherPost.getText().toString();
            email = updateTeacherEmail.getText().toString();
            checkValidation();
        });

        deleteTeacherBtn.setOnClickListener(view -> deleteData());



    }



    private void checkValidation() {
        if (name.isEmpty()){
            updateTeacherName.setError("Please Enter Name");
            updateTeacherName.requestFocus();
        }else if (post.isEmpty()){
            updateTeacherPost.setError("Please Enter Post");
            updateTeacherPost.requestFocus();
        }else if (email.isEmpty()){
            updateTeacherEmail.setError("Please Enter Email");
            updateTeacherEmail.requestFocus();
        }else if (bitmap == null){
            updateData(image);
        }else {
            uploadImage();
        }

    }

    private void updateData(String s) {

        HashMap<String, Object> hp = new HashMap<>();
        hp.put("name", name);
        hp.put("post", post);
        hp.put("email", email);
        hp.put("image", s);

        reference.child(category).child(uniqueKey).updateChildren(hp).addOnSuccessListener(o -> {

            Toast.makeText(UpdateTeacherActivity.this, "Faculty Updated Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateTeacherActivity.this, FacultyActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }).addOnFailureListener(e -> Toast.makeText(UpdateTeacherActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show());
    }

    private void uploadImage() {
        pd.setMessage("Updating...");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);  //compressing before upload
        byte[] finalimg = baos.toByteArray();

        //creating filepath to store data
        final StorageReference filePath;
        filePath = storageReference.child("Faculties").child(Arrays.toString(finalimg) +"jpg");

        //uploading
        final UploadTask uploadTask = filePath.putBytes(finalimg);

        uploadTask.addOnCompleteListener(UpdateTeacherActivity.this, task -> {
            if (task.isSuccessful()){
                uploadTask.addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                    downloadUrl = String.valueOf(uri);
                    updateData(downloadUrl);
                }));
            }else{
                pd.dismiss();
                Toast.makeText(UpdateTeacherActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void deleteData() {
        pd.setMessage("Deleting...");
        pd.show();
        reference.child(category).child(uniqueKey).removeValue().addOnCompleteListener(task -> {

            Toast.makeText(UpdateTeacherActivity.this, "Faculty Deleted Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateTeacherActivity.this,FacultyActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }).addOnFailureListener(e -> {
            Toast.makeText(UpdateTeacherActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        });
    }


    private void openGallery() {

        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ && resultCode == RESULT_OK){
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
            }
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateTeacherImage.setImageBitmap(bitmap);
        }
    }


}