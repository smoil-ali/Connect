package com.reactive.connect.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reactive.connect.Activities.PostActivity;
import com.reactive.connect.Fragments.ProfileFragment;
import com.reactive.connect.R;
import com.reactive.connect.Utils.Constants;
import com.reactive.connect.databinding.ItemPostBinding;
import com.reactive.connect.model.PostClass;
import com.reactive.connect.model.ProfileClass;

import java.util.HashMap;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;

import static android.content.Context.MODE_PRIVATE;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    final String TAG = PostAdapter.class.getSimpleName();
    Context context;
    List<PostClass> postClassList;
    private FirebaseUser firebaseUser;

    public PostAdapter(Context context, List<PostClass> postClassList) {
        this.context = context;
        this.postClassList = postClassList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        PostClass post = postClassList.get(position);
        YoYo.with(Techniques.FadeInDown).playOn(holder.binding.cardView);

        Glide.with(context).load(post.getPostImage())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                .into(holder.binding.postImage
                );

        holder.binding.toggle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                holder.binding.toggle.setText(holder.binding.description.isExpanded() ? R.string.expand : R.string.collapse);
                holder.binding.description.toggle();
            }
        });

        holder.binding.toggle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                if (holder.binding.description.isExpanded())
                {
                    holder.binding.description.collapse();
                    holder.binding.toggle.setText(R.string.expand);
                }
                else
                {
                    holder.binding.description.expand();
                    holder.binding.toggle.setText(R.string.collapse);
                }
            }
        });

        holder.binding.description.addOnExpandListener(new ExpandableTextView.OnExpandListener()
        {
            @Override
            public void onExpand(final ExpandableTextView view)
            {
                Log.d("df", "ExpandableTextView expanded");
            }

            @Override
            public void onCollapse(final ExpandableTextView view)
            {
                Log.d("db", "ExpandableTextView collapsed");
            }
        });

        if (post.getDescription().equals("")){
            holder.binding.description.setVisibility(View.GONE);
        } else {
            holder.binding.description.setVisibility(View.VISIBLE);
            holder.binding.description.setText(post.getDescription());
        }

        if (holder.binding.description.getLineHeight() < 2){
            holder.binding.toggle.setVisibility(View.GONE);
            holder.binding.postImage.setVisibility(View.INVISIBLE);
        }else{
            holder.binding.toggle.setVisibility(View.VISIBLE);
        }

        publisherInfo(holder.binding.imageProfile, holder.binding.username, post.getPublisher());
        isLiked(post.getPostId(), holder.binding.like);
        nrLikes(holder.binding.likes, post.getPostId());

        holder.binding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.binding.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId())
                            .child(firebaseUser.getUid()).setValue(true);
                    addNotification(post.getPublisher(), post.getPostId());
                    holder.binding.like.startAnimation(holder.imgbounce);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });


        holder.binding.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("profileid", post.getPublisher());
                editor.apply();

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new ProfileFragment()).commit();
            }
        });

        holder.binding.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("profileid", post.getPublisher());
                editor.apply();

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new ProfileFragment()).commit();
            }
        });

        holder.binding.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("postid", post.getPostId());
                editor.apply();

//                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.container,
//                        new PostDetailFragment()).commit();
            }
        });


        holder.binding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.edit:
                                editPost(post);
                                return true;
                            case R.id.delete:
                                final String id = post.getPostId();
                                FirebaseDatabase.getInstance().getReference(Constants.POST)
                                        .child(post.getPostId()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    deleteNotifications(id, firebaseUser.getUid());
                                                }
                                            }
                                        });
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.post_menu);
                if (!post.getPublisher().equals(firebaseUser.getUid())){
                    popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                }
                popupMenu.show();
            }
        });

    }



    @Override
    public int getItemCount() {
        return postClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemPostBinding binding;
        public Animation imgbounce;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemPostBinding.bind(itemView);
            imgbounce = AnimationUtils.loadAnimation(context, R.anim.imgbounce);
        }
    }
    private void editPost(PostClass postClass) {
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra(Constants.PARAMS,postClass);
        context.startActivity(intent);
    }

    private void publisherInfo(final ImageView image_profile, final TextView username, final String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.USERS).child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProfileClass user = dataSnapshot.getValue(ProfileClass.class);
                Glide.with(context).load(user.getImage()).into(image_profile);
                username.setText(user.getFullName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG,databaseError.getMessage());
            }
        });
    }

    private void isLiked(final String postid, final ImageView imageView){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.active_like);
                    imageView.setTag("liked");
                } else{
                    imageView.setImageResource(R.drawable.disable_like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void nrLikes(final TextView likes, String postId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG,databaseError.getMessage());
            }
        });

    }

    private void addNotification(String userid, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", "liked your post");
        hashMap.put("postid", postid);
        hashMap.put("ispost", true);
        reference.push().setValue(hashMap);

    }

    private void deleteNotifications(final String postid, String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.child("postid").getValue().equals(postid)){
                        snapshot.getRef().removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
