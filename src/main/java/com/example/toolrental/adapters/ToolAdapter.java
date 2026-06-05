package com.example.toolrental.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toolrental.R;
import com.example.toolrental.models.Tool;
import java.util.List;

public class ToolAdapter extends RecyclerView.Adapter<ToolAdapter.ViewHolder> {
    private List<Tool> tools;
    private OnToolClickListener listener;
    private boolean isOwnerMode = false;

    public interface OnToolClickListener {
        void onToolClick(Tool tool);
        void onEditClick(Tool tool);
        void onDeleteClick(Tool tool);
    }

    public ToolAdapter(List<Tool> tools, OnToolClickListener listener) {
        this.tools = tools;
        this.listener = listener;
    }

    public void setOwnerMode(boolean ownerMode) {
        isOwnerMode = ownerMode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tool, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tool tool = tools.get(position);
        holder.tvName.setText(tool.getName());
        holder.tvDesc.setText(tool.getDescription());
        holder.tvPrice.setText(tool.getPricePerDay() + " ₽/день");

        if (isOwnerMode) {
            holder.ivEdit.setVisibility(View.VISIBLE);
            holder.ivDelete.setVisibility(View.VISIBLE);
            holder.ivEdit.setOnClickListener(v -> listener.onEditClick(tool));
            holder.ivDelete.setOnClickListener(v -> listener.onDeleteClick(tool));
        } else {
            holder.ivEdit.setVisibility(View.GONE);
            holder.ivDelete.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(v -> listener.onToolClick(tool));
        }
    }

    @Override
    public int getItemCount() {
        return tools.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc, tvPrice;
        ImageView ivEdit, ivDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvToolName);
            tvDesc = itemView.findViewById(R.id.tvToolDesc);
            tvPrice = itemView.findViewById(R.id.tvToolPrice);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}
