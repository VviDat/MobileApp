package com.example.bt4.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt4.MainActivity;
import com.example.bt4.OfficialActivity;
import com.example.bt4.R;
import com.google.gson.JsonObject;

import java.util.List;

public class PoliticianAdapter extends RecyclerView.Adapter<PoliticianAdapter.PoliticianViewHolder> {

    private List<JsonObject> mListPolitician;

    public PoliticianAdapter(List<JsonObject> mListPolitician) {
        this.mListPolitician = mListPolitician;
    }

    @NonNull
    @Override
    public PoliticianViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.politician_item,parent, false);

        return new PoliticianViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PoliticianViewHolder holder, int position) {
        JsonObject item = mListPolitician.get(position);
        if (item == null) {
            return;
        }
        holder.officesPolitician.setText(item.get("office").getAsString());
        holder.namePolitician.setText(item.get("name").getAsString() + " (" + item.get("party") +")");

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(view.getContext(), OfficialActivity.class);
                intent.putExtra("Data",item.toString());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListPolitician != null ? mListPolitician.size() : 0;
    }

    public static class PoliticianViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView officesPolitician, namePolitician;
        private ItemClickListener itemClickListener;

        public PoliticianViewHolder(@NonNull View itemView) {
            super(itemView);
            officesPolitician = itemView.findViewById(R.id.offices);
            namePolitician = itemView.findViewById(R.id.namePolitician);

            itemView.setOnClickListener(this);
        }

        public  void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }
    }
}
