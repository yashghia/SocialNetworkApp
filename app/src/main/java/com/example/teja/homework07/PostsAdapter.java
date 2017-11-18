package com.example.teja.homework07;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by teja on 11/18/17.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    public static ArrayList<Message> messagesArrayList = new ArrayList<>();
    static AlertDialog.Builder builder;
    Context context;
    public PostsAdapter(ArrayList<Message> messages, Context context) {
        this.context = context;
        this.messagesArrayList = messages;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.posts_message_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PrettyTime p = new PrettyTime();
        User user = new User();
        Message message = messagesArrayList.get(position);
        holder.details = message;
        holder.messageTextView.setText(message.getMessage());
        holder.nameTextView.setText(message.getName());
        Date date= null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS").parse(message.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.timeTextView.setText(p.format(date));
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView messageTextView, nameTextView,timeTextView;
        static Message details;
        public ViewHolder(View itemView) {
            super(itemView);
            messageTextView = (TextView) itemView.findViewById(R.id.message);
            nameTextView = (TextView) itemView.findViewById(R.id.username);
            timeTextView = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
