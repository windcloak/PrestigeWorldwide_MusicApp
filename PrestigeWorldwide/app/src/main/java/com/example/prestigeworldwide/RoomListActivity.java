package com.example.prestigeworldwide;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import static android.content.ContentValues.TAG;

public class RoomListActivity extends AppCompatActivity implements RoomAdapter.OnRoomSelectedListener{
  FirebaseFirestore db = FirebaseFirestore.getInstance();
  private Query mQuery;
  private RecyclerView recyclerView;
  private RoomAdapter mAdapter;
  private RecyclerView.LayoutManager layoutManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_room_list);

    recyclerView = findViewById(R.id.rv_room_list);

    // Initialize Firestore and the main RecyclerView
    initFirestore();
    initRecyclerView();
}

  private void initFirestore() {
    db = FirebaseFirestore.getInstance();
    mQuery = db.collection("music-room");
  }

  private void initRecyclerView() {
    if (mQuery == null) {
      Log.w(TAG, "No query, not initializing RecyclerView");
      displayToast("recycler view failed");
      return;
    }

    mAdapter = new RoomAdapter(mQuery, this) {

      @Override
      protected void onDataChanged() {
        // Show/hide content if the query returns empty.
        if (getItemCount() == 0) {
          recyclerView.setVisibility(View.GONE);
          displayToast("Could not get items");
        } else {
          recyclerView.setVisibility(View.VISIBLE);
        }
      }

      @Override
      protected void onError(FirebaseFirestoreException e) {
        // Show a snackbar on errors
        displayToast("problem loading firebase");
      }
    };

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(mAdapter);
  }

  public void launchCreateRoom(View view) {
    Intent intent = new Intent(this, CreateRoomActivity.class);
    startActivity(intent);
  }

  public void joinMusicRoom(View view) {
    Intent intent = new Intent(this, MusicRoomActivity.class);
    startActivity(intent);
  }

  @Override
  public void onStart() {
    super.onStart();

    // Start listening for Firestore updates
    if (mAdapter != null) {
     mAdapter.startListening();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (mAdapter != null) {
      mAdapter.stopListening();
    }
  }


  public void displayToast(String message){
    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onRoomSelected(DocumentSnapshot room) {
    // Go to the selected music room
    Intent intent = new Intent(this, MusicRoomActivity.class);
    // get id of room and pass it in intent
    intent.setData(Uri.parse(room.getId()));
    startActivity(intent);

  }
}
