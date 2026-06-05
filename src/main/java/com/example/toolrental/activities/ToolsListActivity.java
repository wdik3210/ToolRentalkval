package com.example.toolrental.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toolrental.R;
import com.example.toolrental.adapters.ToolAdapter;
import com.example.toolrental.database.DatabaseHelper;
import com.example.toolrental.models.Tool;

import java.util.Calendar;
import java.util.List;

public class ToolsListActivity extends AppCompatActivity {
    private RecyclerView rvTools;
    private DatabaseHelper db;
    private int userId;
    private String userRole;
    private List<Tool> toolList;
    private ToolAdapter adapter;
    private Button btnMyBookings, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_list);

        userId = getIntent().getIntExtra("userId", -1);
        userRole = getIntent().getStringExtra("userRole");
        db = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.announcements);
        }

        rvTools = findViewById(R.id.rvTools);
        rvTools.setLayoutManager(new LinearLayoutManager(this));

        btnMyBookings = findViewById(R.id.btnMyBookings);
        btnLogout = findViewById(R.id.btnLogout);

        if ("guest".equals(userRole) || userId == -1) {
            btnMyBookings.setVisibility(View.GONE);
        } else {
            btnMyBookings.setVisibility(View.VISIBLE);
            btnMyBookings.setOnClickListener(v -> {
                Intent intent = new Intent(this, MyBookingsActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            });
        }

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        loadTools();
    }

    private void loadTools() {
        toolList = db.getAllTools();
        adapter = new ToolAdapter(toolList, new ToolAdapter.OnToolClickListener() {
            @Override
            public void onToolClick(Tool tool) {
                if ("guest".equals(userRole) || userId == -1) {
                    Toast.makeText(ToolsListActivity.this, R.string.please_register_first, Toast.LENGTH_LONG).show();
                } else {
                    showBookingDialog(tool);
                }
            }

            @Override
            public void onEditClick(Tool tool) {}

            @Override
            public void onDeleteClick(Tool tool) {}
        });
        adapter.setOwnerMode(false);
        rvTools.setAdapter(adapter);
    }

    private void showBookingDialog(Tool tool) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_booking, null);
        TextView tvToolName = view.findViewById(R.id.tvToolName);
        EditText etStartDate = view.findViewById(R.id.etStartDate);
        EditText etEndDate = view.findViewById(R.id.etEndDate);
        Button btnConfirm = view.findViewById(R.id.btnConfirmBooking);

        tvToolName.setText(tool.getName());

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener startDateListener = (view1, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            etStartDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        };
        DatePickerDialog.OnDateSetListener endDateListener = (view1, year, month, dayOfMonth) -> {
            etEndDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        };

        etStartDate.setOnClickListener(v -> new DatePickerDialog(ToolsListActivity.this, startDateListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());
        etEndDate.setOnClickListener(v -> new DatePickerDialog(ToolsListActivity.this, endDateListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());

        builder.setView(view);
        android.app.AlertDialog dialog = builder.create();

        btnConfirm.setOnClickListener(v -> {
            String start = etStartDate.getText().toString();
            String end = etEndDate.getText().toString();
            if (start.isEmpty() || end.isEmpty()) {
                Toast.makeText(ToolsListActivity.this, R.string.select_dates, Toast.LENGTH_SHORT).show();
                return;
            }
            double total = 1 * tool.getPricePerDay();
            db.addBooking(tool.getId(), userId, start, end, "pending", total);
            Toast.makeText(ToolsListActivity.this, R.string.booking_sent, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}
