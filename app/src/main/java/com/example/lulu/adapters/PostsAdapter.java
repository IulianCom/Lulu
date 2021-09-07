package com.example.lulu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lulu.R;
import com.example.lulu.classes.Post;


import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {
    ArrayList<Post> list;
    Context context;

    public PostsAdapter(ArrayList<Post> list, Context context){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_item, parent,false);
        return new PostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
        Post currentPost = list.get(position);
        holder.text.setText(currentPost.getText());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PostsViewHolder extends RecyclerView.ViewHolder{
        private TextView text;
        public PostsViewHolder(@NonNull View itemView){
            super(itemView);
            text =itemView.findViewById(R.id.tv_post);
        }
    }
}
