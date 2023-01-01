package com.example.recforrest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recforrest.model.Recommendation;

import java.util.List;

class  RecommendationViewHolder extends RecyclerView.ViewHolder{
    TextView nameOfRes;
    TextView address;
    List<Recommendation> data;

    public RecommendationViewHolder(@NonNull View itemView, RecommendationRecyclerAdapter.OnItemClickListener listener, List<Recommendation> data) {
        super(itemView);
        this.data = data;
        nameOfRes = itemView.findViewById(R.id.row_res_name);
        address = itemView.findViewById(R.id.row_address);

//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int pos = getAdapterPosition();
//                listener.onItemClick(pos);
//            }
//        });
    }

    public void bind(Recommendation r, int pos) {
        nameOfRes.setText(r.getNameOfRes());
        address.setText(r.getAddress());
    }
}

public class RecommendationRecyclerAdapter extends RecyclerView.Adapter<RecommendationViewHolder>{
    public static interface OnItemClickListener{
        void onItemClick(int pos);
    }

    OnItemClickListener listener;
    LayoutInflater inflater;
    List<Recommendation> data;

    public RecommendationRecyclerAdapter(LayoutInflater inflater, List<Recommendation> data){
        this.inflater = inflater;
        this.data = data;
    }


    void setListener(OnItemClickListener l){
        this.listener = l;
    }
    @NonNull
    @Override
    public RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recommendations_list_row, parent, false);
        return new RecommendationViewHolder(view, listener, data);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationViewHolder holder, int position) {
        Recommendation r = data.get(position);
        holder.bind(r, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
