package com.example.prestigeworldwide;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class Firebase_Manager {
  // Variables
  FirebaseFirestore db = FirebaseFirestore.getInstance();
  public String roomId;   // room id
  Map<String, Object> queue = new HashMap<>();
  MusicRoom room;

  // Adds room to music-room collection
  public void AddMusicRoom(MusicRoom room, String roomId) {
// Add a new document with user-generated id
    db.collection("music-room")
      .document(roomId)
      .set(room);
  }

  // Creates queue in queues collection
  public void createQueue(String roomId) {
    db.collection("queues").document(roomId)
      .set(queue);
  }

  // TODO Move to Music Room.
  // Load Music Room properties
  public void loadRoom(String roomId) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef = db.collection("music-room").document(roomId);
    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
      @Override
      public void onSuccess(DocumentSnapshot documentSnapshot) {
        // Convert document to MusicRoom object
        MusicRoom room = documentSnapshot.toObject(MusicRoom.class);
        // TODO display music room stuff
//        showToast(room.getName());
      }
    });
  }

//  ArrayList<MusicRoom> roomList = new ArrayList<MusicRoom>();
//  MusicRoom temp;
//
//  public void GetMusicRooms() {
//
//    // retrieve all documents in a collection
//    // TODO: add snapshot listener to each document
//    ExecutorService executor = Executors.newSingleThreadScheduledExecutor();
//    roomList = new ArrayList<>();
//    db.collection("music-room")
//      .get()
//      .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//        @Override
//        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//          if (task.isSuccessful()) {
//            for (DocumentSnapshot document : task.getResult()) {
////                      executor.execute(() -> {
//              Log.w(TAG, document.getId() + "=>" + document.getData());
//              temp = new MusicRoom((boolean) document.get("isPublic"), document.get("name").toString(), document.get("genre").toString(), document.get("password").toString());
//              MusicRoom room = document.toObject(MusicRoom.class);    // convert document to music room
//              Log.d(TAG, "music room " + room.getName());   // returns music room name
//              roomList.add(temp);
//              roomList.add(room);
//
//              // TODO seems like once we are out of this for loop, roomList values become empty, but they exist inside for loop
////                      });
//            }
//            processGetMusicRooms(roomList);
//          } else {
//            Log.w(TAG, "Error getting documents.", task.getException());
//          }
//
//          Log.d(TAG, "inside oncomplete " + roomList.get(0).getName());
//        }
//      });
//    // TODO: wait for listener to complete, or figure out another way to return documents
//
//
//  }
}
