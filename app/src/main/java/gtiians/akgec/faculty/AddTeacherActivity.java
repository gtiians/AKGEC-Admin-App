package gtiians.akgec.faculty;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddTeacherActivity extends AppCompatActivity {

    private ImageView addTeacherImage;
    private EditText addTeacherName, addTeacherPost, addTeacherEmail;
    private Spinner addTeacherCategory;
    private Button addTeacherBtn;
    private final int REQ = 1;
    private Bitmap bitmap = null;
    private String category;
    private String name, email, post, downloadUrl = "";
    private DatabaseReference reference, databaseReference;
    private StorageReference storageReference;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        addTeacherImage = findViewById(R.id.addTeacherImage);
        addTeacherName = findViewById(R.id.addTeacherName);
        addTeacherPost = findViewById(R.id.addTeacherPost);
        addTeacherEmail = findViewById(R.id.addTeacherEmail);
        addTeacherCategory = findViewById(R.id.addTeacherCategory);
        addTeacherBtn = findViewById(R.id.addTeacherBtn);

        pd = new ProgressDialog(this);

        String[] items = new String[]{"Select Departments","Applied Sciences & Humanities","Civil Engineering","Computer Science and Engineering","Electrical and Electronics Engineering","Electronics and Communication Engineering","Mechanical Engineering","Information Technology"};

        reference = FirebaseDatabase.getInstance().getReference().child("Faculties");
        storageReference = FirebaseStorage.getInstance().getReference().child("Faculties");

        //setting the adapter on Spinner(imageCategory)
        addTeacherCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items));
        //setting the setOnItemClickListener on spinner(imageCategory)
        addTeacherCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //'category' will store that's value which is selected by admin in spinner items
                category = addTeacherCategory.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        addTeacherImage.setOnClickListener(v -> {
            openGallery();
        });

        addTeacherBtn.setOnClickListener(v -> {
            //to check our every field containing something or not
            checkValidation();
        });
    }


    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);
    }

    private void checkValidation() {
        name = addTeacherName.getText().toString();
        post = addTeacherPost.getText().toString();
        email = addTeacherEmail.getText().toString();
        //checking the values !=0
        if (name.isEmpty()){
            addTeacherName.setError("Please Enter the Name");
            addTeacherName.requestFocus();
        }else if (post.isEmpty()){
            addTeacherPost.setError("Please Enter the Postname");
            addTeacherPost.requestFocus();
        }else if (email.isEmpty()){
            addTeacherEmail.setError("Please Enter the Email Address");
            addTeacherName.requestFocus();
        }else if(category.equals("Select Departments")){
            Toast.makeText(this, "Please Select Department of Teacher", Toast.LENGTH_SHORT).show();
        }else if (bitmap == null){
            insertData();
        }else{
            uploadImage();
        }
    }

    private void uploadImage() {
        pd.setMessage("Uploading...");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);  //compressing before upload
        byte[] finalimg = baos.toByteArray();

        //creating filepath to store data
        final StorageReference filePath;
        filePath = storageReference.child("Faculties").child(finalimg+"jpg");

        //uploading
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(AddTeacherActivity.this, task -> {
            if (task.isSuccessful()){
                uploadTask.addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                    downloadUrl = String.valueOf(uri);
                    insertData();
                }));
            }else{
                pd.dismiss();
                Toast.makeText(AddTeacherActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertData() {

        databaseReference = reference.child(category);
        final String uniqueKey = databaseReference.push().getKey();
        //here NoticeData comes from NoticeData Java class file where created constructor
        TeacherData teacherData = new TeacherData(name, post, email, downloadUrl, uniqueKey);
        /*storing in firebase but we will not know so +1 method
        reference.child(uniqueKey).setValue(noticeData);*/
        databaseReference.child(uniqueKey).setValue(teacherData).addOnSuccessListener(aVoid -> {
            pd.dismiss();
            Toast.makeText(AddTeacherActivity.this, "Thank You", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(AddTeacherActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
        });
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
            addTeacherImage.setImageBitmap(bitmap);
        }
    }

}