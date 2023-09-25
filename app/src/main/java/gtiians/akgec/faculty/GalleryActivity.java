package gtiians.akgec.faculty;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

@SuppressWarnings("ImplicitArrayToString")
public class GalleryActivity extends AppCompatActivity {


    private Spinner imageCategory;
    private ImageView galleryImageView;

    private String category;
    private final int REQ = 1;
    private Bitmap bitmap;

    private DatabaseReference reference;
    private StorageReference storageReference;
    String downloadUrl = "";
    ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        pd = new ProgressDialog(this);

        CardView selectImage = findViewById(R.id.addGalleryImage);
        imageCategory = findViewById(R.id.image_category);
        Button uploadImage = findViewById(R.id.uploadImageBtn);
        galleryImageView = findViewById(R.id.galleryImageView);


        reference = FirebaseDatabase.getInstance().getReference().child("Gallery");
        storageReference = FirebaseStorage.getInstance().getReference().child("Gallery");


        //creating Spinner list of category here this is String type static Array
        String[] items = new String[]{"Select Category", "Convocation", "Alumni Meet", "Freshers Party","Farewell Party","Independence Day","Republic Day","Holi Celebration","Deepawali Celebration","Vishwakarma Puja"};

        //setting the adapter on Spinner(imageCategory)
        imageCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items));

        imageCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //'category' will store that's value which is selected by admin in spinner items
                category = imageCategory.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        selectImage.setOnClickListener(view -> openGallery());

        uploadImage.setOnClickListener(view -> {
            //first we check the user selected the images or not through 'bitmap'
            if (bitmap == null){
                Toast.makeText(GalleryActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
            }//than we check user selected the cat through 'category'
            else if (category.equals("Select Category")){
                Toast.makeText(GalleryActivity.this, "Please Select Image Category", Toast.LENGTH_SHORT).show();
            }//when we got the both
            else{
                pd.setMessage("Uploading..");
                pd.show();
                //here called a method to upload image
                uploadImage();
            }
        });


    }


    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);
    }


    //method created to upload images
    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] finalimg = baos.toByteArray();
        //creating filepath to store data
        final StorageReference filePath;
        filePath = storageReference.child(finalimg+"jpg");
        //uploading
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(GalleryActivity.this, task -> {
            if (task.isSuccessful()){
                uploadTask.addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                    //we get the url of img in downloadUrl
                    downloadUrl = String.valueOf(uri);
                    //now we are going to upload data
                    uploadData();
                }));
            }else{
                pd.dismiss();
                Toast.makeText(GalleryActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //this is the code to store your data in firebase database
    private void uploadData() {
        //here by passing 'category' we store the data in that cat which selected by user
        DatabaseReference databaseReference = reference.child(category);
        final String uniquekey = databaseReference.push().getKey();
        //now we set the data
        if (uniquekey != null) {
            databaseReference.child(uniquekey).setValue(downloadUrl).addOnSuccessListener(aVoid -> {
                pd.dismiss();
                Toast.makeText(GalleryActivity.this, "Thank You", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                pd.dismiss();
                Toast.makeText(GalleryActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
            });
        }
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
            galleryImageView.setImageBitmap(bitmap);
        }
    }

}