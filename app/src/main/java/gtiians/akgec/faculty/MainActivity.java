package gtiians.akgec.faculty;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout notice = findViewById(R.id.notice);
        LinearLayout gallery = findViewById(R.id.gallery);
        LinearLayout ebook = findViewById(R.id.ebook);
        LinearLayout faculty = findViewById(R.id.faculty);
        LinearLayout delNotice = findViewById(R.id.delNotice);
        LinearLayout logout = findViewById(R.id.logout);
        ImageView admin = findViewById(R.id.administrator);

        admin.setOnClickListener(this);
        notice.setOnClickListener(this);
        gallery.setOnClickListener(this);
        ebook.setOnClickListener(this);
        faculty.setOnClickListener(this);
        delNotice.setOnClickListener(this);
        logout.setOnClickListener(this);


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case  R.id.administrator:
                Toast.makeText(this, "Administrator", Toast.LENGTH_LONG).show();
                break;
            case R.id.notice:
                intent = new Intent(MainActivity.this, PostNoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.gallery:
                intent = new Intent(MainActivity.this, GalleryActivity.class);
                startActivity(intent);
                break;
            case R.id.ebook:
                intent = new Intent(MainActivity.this, PostNotesActivity.class);
                startActivity(intent);
                break;
            case R.id.faculty:
                intent = new Intent(MainActivity.this, FacultyActivity.class);
                startActivity(intent);
                break;
            case R.id.delNotice:
                intent = new Intent(MainActivity.this, DeleteNoticeActivity.class);
                startActivity(intent);
                break;


        }

    }
}