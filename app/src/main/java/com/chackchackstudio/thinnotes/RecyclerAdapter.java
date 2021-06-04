package com.chackchackstudio.thinnotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final List<NoteModel> mData;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    RecyclerAdapter(Context context, List<NoteModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_main_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        if (mData.get(position).getTitle() == null || mData.get(position).getTitle().isEmpty() || mData.get(position).getTitle().equals(" ")) {
            holder.textTitle.setVisibility(View.GONE);
        } else {
            holder.textTitle.setText(mData.get(position).getTitle());
        }


        if (mData.get(position).getBody().length() > 250) {
            holder.textBody.setText(mData.get(position).getBody().substring(0, 250) + "\n...");
        } else {
            holder.textBody.setText(mData.get(position).getBody());
        }


        holder.textDate.setText(mData.get(position).getDate());

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textTitle;
        TextView textDate;
        TextView textBody;

        ViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDate = itemView.findViewById(R.id.textDate);
            textBody = itemView.findViewById(R.id.textBody);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
