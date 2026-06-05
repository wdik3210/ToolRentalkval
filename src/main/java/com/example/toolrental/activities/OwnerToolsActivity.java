package com.example.toolrental.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toolrental.R;
import com.example.toolrental.adapters.ToolAdapter;
import com.example.toolrental.database.DatabaseHelper;
import com.example.toolrental.models.Tool;

import java.util.List;

public class OwnerToolsActivity extends AppCompatActivity {
    private RecyclerView rvTools;
    private DatabaseHelper db;
    private int userId;
    private List<Tool> toolList;
    private ToolAdapter adapter;
    private Button btnAdd, btnRequests, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_tools);

        userId = getIntent().getIntExtra("userId", -1);
        db = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Устанавливаем заголовок после setSupportActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Мои объявления");
        }

        rvTools = findViewById(R.id.rvTools);
        rvTools.setLayoutManager(new LinearLayoutManager(this));

        btnAdd = findViewById(R.id.btnAddTool);
        btnRequests = findViewById(R.id.btnBookingRequests);
        btnLogout = findViewById(R.id.btnLogout);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditToolActivity.class);
            intent.putExtra("ownerId", userId);
            startActivity(intent);
        });

        btnRequests.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookingRequestsActivity.class);
            intent.putExtra("ownerId", userId);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        loadTools();
    }

    private void loadTools() {
        toolList = db.getToolsByOwner(userId);
        adapter = new ToolAdapter(toolList, new ToolAdapter.OnToolClickListener() {
            @Override
            public void onToolClick(Tool tool) {}

            @Override
            public void onEditClick(Tool tool) {
                Intent intent = new Intent(OwnerToolsActivity.this, AddEditToolActivity.class);
                intent.putExtra("toolId", tool.getId());
                intent.putExtra("ownerId", userId);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Tool tool) {
                new android.app.AlertDialog.Builder(OwnerToolsActivity.this)
                        .setTitle("Удаление")
                        .setMessage("Вы уверены, что хотите удалить инструмент \"" + tool.getName() + "\"?")
                        .setPositiveButton("Да", (dialog, which) -> {
                            db.deleteTool(tool.getId());
                            Toast.makeText(OwnerToolsActivity.this, "Инструмент удалён", Toast.LENGTH_SHORT).show();
                            loadTools();
                        })
                        .setNegativeButton("Нет", null)
                        .show();
            }
        });
        adapter.setOwnerMode(true);
        rvTools.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTools();
    }
}
