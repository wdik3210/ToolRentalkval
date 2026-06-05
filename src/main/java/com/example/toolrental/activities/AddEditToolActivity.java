package com.example.toolrental.activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.toolrental.R;
import com.example.toolrental.database.DatabaseHelper;
import com.example.toolrental.models.Tool;

public class AddEditToolActivity extends AppCompatActivity {
    private EditText etName, etDesc, etPrice;
    private Button btnSave;
    private DatabaseHelper db;
    private int ownerId;
    private int toolId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_tool);

        db = new DatabaseHelper(this);
        ownerId = getIntent().getIntExtra("ownerId", -1);
        toolId = getIntent().getIntExtra("toolId", -1);

        etName = findViewById(R.id.etToolName);
        etDesc = findViewById(R.id.etToolDesc);
        etPrice = findViewById(R.id.etToolPrice);
        btnSave = findViewById(R.id.btnSaveTool);

        if (toolId != -1) {
            Tool tool = db.getToolById(toolId);
            if (tool != null) {
                etName.setText(tool.getName());
                etDesc.setText(tool.getDescription());
                etPrice.setText(String.valueOf(tool.getPricePerDay()));
            }
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();
            double price;
            try {
                price = Double.parseDouble(etPrice.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Введите корректную цену", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            if (toolId == -1) {
                db.addTool(name, desc, price, "", ownerId);
                Toast.makeText(this, "Инструмент добавлен", Toast.LENGTH_SHORT).show();
            } else {
                db.updateTool(toolId, name, desc, price, "");
                Toast.makeText(this, "Инструмент обновлён", Toast.LENGTH_SHORT).show();
            }
            finish();
        });
    }
}
