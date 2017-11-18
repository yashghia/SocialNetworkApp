package com.example.teja.homework07;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by teja on 11/18/17.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView messageTextView, nameTextView,timeTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            messageTextView = (TextView) itemView.findViewById(R.id.message);
            nameTextView = (TextView) itemView.findViewById(R.id.username);
            timeTextView = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
