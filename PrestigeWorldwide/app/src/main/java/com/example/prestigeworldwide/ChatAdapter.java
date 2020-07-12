package com.example.prestigeworldwide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<String> mMessages = new ArrayList<>();
    private Context mContext;

    public ChatAdapter(Context context, List<String> messages) {
        mMessages = messages;
        mContext = context;
    }

    /*public interface OnRoomSelectedListener {
        void onRoomSelected(DocumentSnapshot room);
    }

    private ChatAdapter.OnRoomSelectedListener mListener;

    public ChatAdapter(Query query, ChatAdapter.OnRoomSelectedListener listener) {
        super(query);
        mListener = listener;
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_items, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.messageView.setText(mMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageView;        // message
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            messageView = itemView.findViewById(R.id.sent_message);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}

