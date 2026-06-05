package com.example.toolrental.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.toolrental.R;
import com.example.toolrental.database.DatabaseHelper;
import com.example.toolrental.models.User;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister, btnGuest;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnGuest = findViewById(R.id.btnGuest);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            User user = db.login(email, password);
            if (user != null) {
                Toast.makeText(this, getString(R.string.login_success) + ", " + user.getFullName(), Toast.LENGTH_SHORT).show();
                Intent intent;
                if (user.getRole().equals("owner")) {
                    intent = new Intent(this, OwnerToolsActivity.class);
                } else {
                    intent = new Intent(this, ToolsListActivity.class);
                }
                intent.putExtra("userId", user.getId());
                intent.putExtra("userRole", user.getRole());
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

        btnGuest.setOnClickListener(v -> {
            Intent intent = new Intent(this, ToolsListActivity.class);
            intent.putExtra("userId", -1);
            intent.putExtra("userRole", "guest");
            startActivity(intent);
        });
    }
}
