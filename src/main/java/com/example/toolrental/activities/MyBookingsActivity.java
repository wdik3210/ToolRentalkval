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

public class MyBookingsActivity extends AppCompatActivity {
    private RecyclerView rvBookings;
    private DatabaseHelper db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        userId = getIntent().getIntExtra("userId", -1);
        db = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Мои бронирования");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rvBookings = findViewById(R.id.rvBookings);
        rvBookings.setLayoutManager(new LinearLayoutManager(this));

        loadBookings();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void loadBookings() {
        List<Booking> bookings = db.getBookingsByRenter(userId);
        BookingAdapter adapter = new BookingAdapter(bookings, new BookingAdapter.OnBookingActionListener() {
            @Override
            public void onDeleteClick(Booking booking) {
                new android.app.AlertDialog.Builder(MyBookingsActivity.this)
                        .setTitle("Отмена бронирования")
                        .setMessage("Вы уверены, что хотите отменить это бронирование?")
                        .setPositiveButton("Да", (dialog, which) -> {
                            db.deleteBooking(booking.getId());
                            Toast.makeText(MyBookingsActivity.this, "Бронирование отменено", Toast.LENGTH_SHORT).show();
                            loadBookings();
                        })
                        .setNegativeButton("Нет", null)
                        .show();
            }

            @Override
            public void onBookingClick(Booking booking) {
                // Не используется в MyBookingsActivity
            }
        });
        adapter.setDatabaseHelper(db);
        adapter.setHideDeleteIcon(false); // Показываем иконку удаления (но она скроется для confirmed в самом адаптере)
        rvBookings.setAdapter(adapter);
    }
}
