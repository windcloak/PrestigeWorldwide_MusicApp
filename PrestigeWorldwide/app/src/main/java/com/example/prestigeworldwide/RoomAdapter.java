package com.example.prestigeworldwide;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;


public class RoomAdapter extends FirestoreAdapter<RoomAdapter.ViewHolder>
{

  public interface OnRoomSelectedListener {
    void onRoomSelected(DocumentSnapshot room);
  }

  private OnRoomSelectedListener mListener;

  public RoomAdapter(Query query, OnRoomSelectedListener listener) {
      super(query);
      mListener = listener;
  }


  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return new ViewHolder(inflater.inflate(R.layout.roomlist_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(getSnapshot(position), mListener);
  }
  static class ViewHolder extends RecyclerView.ViewHolder {

    TextView nameView;        // name
    TextView genreView;      // genre
    TextView typeView;      // public or private

    public ViewHolder(View itemView) {
      super(itemView);
      nameView = itemView.findViewById(R.id.item_room_name);
      genreView = itemView.findViewById(R.id.item_room_genre);
      typeView = itemView.findViewById(R.id.item_room_type);
    }

    public void bind(final DocumentSnapshot snapshot,
                     final OnRoomSelectedListener listener) {

      // convert document to MusicRoom
     MusicRoom room = snapshot.toObject(MusicRoom.class);

      Resources resources = itemView.getResources();

      nameView.setText(room.getName());
      genreView.setText("Genre: " + room.getGenre());
      // if isPublic is true, set type to "public"
      String roomType = room.getIsPublic() ? "Type: Public" : "Type: Private";
      typeView.setText(roomType);

      // Click listener
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (listener != null) {
            listener.onRoomSelected(snapshot);
          }
        }
      });
    }

  }

}
