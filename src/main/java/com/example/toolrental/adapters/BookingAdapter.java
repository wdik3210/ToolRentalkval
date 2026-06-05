package com.example.toolrental.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toolrental.R;
import com.example.toolrental.database.DatabaseHelper;
import com.example.toolrental.models.Booking;
import com.example.toolrental.models.Tool;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    private List<Booking> bookings;
    private OnBookingActionListener listener;
    private DatabaseHelper db;
    private boolean hideDeleteIcon = false; // Флаг для скрытия иконки удаления

    public interface OnBookingActionListener {
        void onDeleteClick(Booking booking);
        void onBookingClick(Booking booking); // Для запросов (подтверждение/отмена)
    }

    public BookingAdapter(List<Booking> bookings, OnBookingActionListener listener) {
        this.bookings = bookings;
        this.listener = listener;
    }

    public void setDatabaseHelper(DatabaseHelper db) {
        this.db = db;
    }

    public void setHideDeleteIcon(boolean hide) {
        this.hideDeleteIcon = hide;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = bookings.get(position);

        // Получаем название инструмента
        if (db != null) {
            Tool tool = db.getToolById(booking.getToolId());
            if (tool != null) {
                holder.tvToolName.setText(tool.getName());
            } else {
                holder.tvToolName.setText("Инструмент #" + booking.getToolId());
            }
        } else {
            holder.tvToolName.setText("Инструмент #" + booking.getToolId());
        }

        holder.tvBookingDates.setText(booking.getStartDate() + " - " + booking.getEndDate());

        // Устанавливаем статус
        String status = booking.getStatus();
        if (status.equals("pending")) {
            holder.tvBookingStatus.setText("⏳ Ожидает подтверждения");
            holder.tvBookingStatus.setTextColor(0xFFFF9800);
        } else if (status.equals("confirmed")) {
            holder.tvBookingStatus.setText("✅ Подтверждено");
            holder.tvBookingStatus.setTextColor(0xFF4CAF50);
        } else {
            holder.tvBookingStatus.setText("❌ Отменено");
            holder.tvBookingStatus.setTextColor(0xFFD32F2F);
        }

        holder.tvBookingTotal.setText(String.format("Сумма: %.2f ₽", booking.getTotalPrice()));

        // Скрываем иконку удаления если нужно
        if (hideDeleteIcon) {
            holder.ivDeleteBooking.setVisibility(View.GONE);
        } else {
            holder.ivDeleteBooking.setVisibility(View.VISIBLE);
            holder.ivDeleteBooking.setOnClickListener(v -> {
                if (booking.getStatus().equals("pending")) {
                    listener.onDeleteClick(booking);
                }
            });
        }

        // Обработчик клика по всей карточке (для запросов)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookingClick(booking);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvToolName, tvBookingDates, tvBookingStatus, tvBookingTotal;
        ImageView ivDeleteBooking;

        ViewHolder(View itemView) {
            super(itemView);
            tvToolName = itemView.findViewById(R.id.tvToolName);
            tvBookingDates = itemView.findViewById(R.id.tvBookingDates);
            tvBookingStatus = itemView.findViewById(R.id.tvBookingStatus);
            tvBookingTotal = itemView.findViewById(R.id.tvBookingTotal);
            ivDeleteBooking = itemView.findViewById(R.id.ivDeleteBooking);
        }
    }
}
