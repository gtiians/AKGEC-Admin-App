package gtiians.akgec.faculty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressWarnings("CommentedOutCode")
public class LoginActivity extends AppCompatActivity {

    private EditText adminEmail,adminPassword;

    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        adminEmail = findViewById(R.id.user_email);
        adminPassword = findViewById(R.id.user_password);
//      pwdShow = findViewById(R.id.pwd_show);
        Button loginBtn = findViewById(R.id.login_button);

        SharedPreferences sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getString("isLogin","false").equals("yes")) {
            openDash();
        }

        /*pwdShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adminPassword.getInputType() == 144){
                    adminPassword.setInputType(129);
                    pwdShow.setText("hide");
                }else {
                     adminPassword.setInputType(144);
                     pwdShow.setText("show");
                }
                adminPassword.setSelection(adminPassword.getText().length());
            }
        });
        */

        loginBtn.setOnClickListener(view -> validateData());

    }


    private void validateData() {
        String email = adminEmail.getText().toString();
        String pwd = adminPassword.getText().toString();
        if (email.isEmpty()){
            adminEmail.setError("Please Enter Your Email");
            adminEmail.requestFocus();
        }else if(pwd.isEmpty()){
            adminPassword.setError("Please Enter the Password");
            adminPassword.requestFocus();
        }else if (email.equals("admin@gmail.com") && pwd.equals("akgec")){
            editor.putString("isLogin","yes");
            editor.commit();
            openDash();
        }else {
            Toast.makeText(this, "Please Check Your Email & Password Again", Toast.LENGTH_LONG).show();
        }
    }

    private void openDash() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

}