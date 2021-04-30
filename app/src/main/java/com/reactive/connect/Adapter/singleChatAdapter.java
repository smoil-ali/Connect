package com.reactive.connect.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reactive.connect.Activities.ChatActivity;
import com.reactive.connect.R;
import com.reactive.connect.Utils.Constants;
import com.reactive.connect.databinding.RecycleListSingleUserBinding;
import com.reactive.connect.model.Conv;
import com.reactive.connect.model.ProfileClass;

import java.util.List;

public class singleChatAdapter extends RecyclerView.Adapter<singleChatAdapter.ViewHolder> {
    final String TAG = singleChatAdapter.class.getSimpleName();
    List<Conv> list;
    Context context;

    public singleChatAdapter(List<Conv> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_list_single_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Conv conv = list.get(position);
        YoYo.with(Techniques.FadeInDown).playOn(holder.binding.container);
        getUserInfo(conv.getUserId(),holder);


        holder.binding.circleImageViewUserImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra(Constants.PARAMS,conv.getUserId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecycleListSingleUserBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecycleListSingleUserBinding.bind(itemView);
        }
    }

    void getUserInfo(String id,ViewHolder holder){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.USERS);
        databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfileClass profileClass = snapshot.getValue(ProfileClass.class);
                Glide.with(context).load(profileClass.getImage())
                        .into(holder.binding.circleImageViewUserImage);
                holder.binding.textViewSingleListName.setText(profileClass.getFullName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
