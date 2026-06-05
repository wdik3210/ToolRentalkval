package com.example.toolrental.activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.toolrental.R;
import com.example.toolrental.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etFullName;
    private Spinner spRole;
    private Button btnRegister;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etFullName = findViewById(R.id.etFullName);
        spRole = findViewById(R.id.spRole);
        btnRegister = findViewById(R.id.btnRegister);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(adapter);

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String fullName = etFullName.getText().toString().trim();
            String role = spRole.getSelectedItem().toString().equals("Арендатор") ? "renter" : "owner";

            if (email.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            long result = db.addUser(email, password, role, fullName);
            if (result != -1) {
                Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Ошибка: email уже существует", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
