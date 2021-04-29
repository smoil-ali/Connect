package com.reactive.connect.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reactive.connect.Interfaces.InterestListener;
import com.reactive.connect.R;
import com.reactive.connect.databinding.ItemInterestBinding;
import com.reactive.connect.model.InterestClass;

import java.util.List;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.ViewHolder> {

    final String TAG = InterestAdapter.class.getSimpleName();
    Context context;
    List<InterestClass> list;
    InterestListener listener;

    public void setListener(InterestListener listener) {
        this.listener = listener;
    }

    public InterestAdapter(Context context, List<InterestClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_interest,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InterestClass interestClass = list.get(position);
        holder.binding.text.setText(interestClass.getInterest());
        holder.binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    listener.onInterestChecked(interestClass.getInterest());
                else
                    listener.onInterestUnChecked(interestClass.getInterest());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemInterestBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemInterestBinding.bind(itemView);
        }
    }
}
