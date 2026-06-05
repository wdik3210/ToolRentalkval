package com.example.toolrental.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toolrental.R;
import com.example.toolrental.adapters.BookingAdapter;
import com.example.toolrental.database.DatabaseHelper;
import com.example.toolrental.models.Booking;

import java.util.List;

public class BookingRequestsActivity extends AppCompatActivity {
    private RecyclerView rvRequests;
    private DatabaseHelper db;
    private int ownerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_requests);

        ownerId = getIntent().getIntExtra("ownerId", -1);
        db = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Запросы");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rvRequests = findViewById(R.id.rvRequests);
        rvRequests.setLayoutManager(new LinearLayoutManager(this));

        loadRequests();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void loadRequests() {
        List<Booking> bookings = db.getBookingsByToolOwner(ownerId);
        BookingAdapter adapter = new BookingAdapter(bookings, new BookingAdapter.OnBookingActionListener() {
            @Override
            public void onDeleteClick(Booking booking) {
                // Не используется в BookingRequestsActivity
            }

            @Override
            public void onBookingClick(Booking booking) {
                // Показываем диалог для подтверждения/отмены
                new android.app.AlertDialog.Builder(BookingRequestsActivity.this)
                        .setTitle("Действие")
                        .setItems(new String[]{"Подтвердить", "Отменить"}, (dialog, which) -> {
                            if (which == 0) {
                                db.updateBookingStatus(booking.getId(), "confirmed");
                                Toast.makeText(BookingRequestsActivity.this, "Бронирование подтверждено", Toast.LENGTH_SHORT).show();
                            } else {
                                db.updateBookingStatus(booking.getId(), "cancelled");
                                Toast.makeText(BookingRequestsActivity.this, "Бронирование отменено", Toast.LENGTH_SHORT).show();
                            }
                            loadRequests();
                        }).show();
            }
        });
        adapter.setDatabaseHelper(db);
        adapter.setHideDeleteIcon(true); // Скрываем иконку удаления в окне запросов
        rvRequests.setAdapter(adapter);
    }
}
