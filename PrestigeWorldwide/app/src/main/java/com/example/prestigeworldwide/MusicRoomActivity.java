package com.example.prestigeworldwide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList; //Temp
import java.util.List;
import java.util.Map;
import java.util.Queue; //Temp


//extends AppCompatActivity
public class MusicRoomActivity extends YouTubeBaseActivity {
//  MusicRoom room = MusicRoom.getInstance(false,null,null,null);
  MusicRoom room = new MusicRoom();

  String youtubeURL;
  String queueID;

  EditText URLInput, messageInput;
  Button URLSubmit, messageSubmit;
  YouTubePlayerView youtubePlayer;

  YouTubePlayer youtubePlayerClone;

  YouTubePlayer.OnInitializedListener onInitListener;

  //for dummy chat
  private RecyclerView recyclerView;
  private List<String> mMessages = new ArrayList<>();

  FirebaseFirestore db = FirebaseFirestore.getInstance();
  private DocumentReference roomQuery;
//  Queue<String> queue = new LinkedList<>();


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_music_room);

    // Load music room id from CreateRoom  or RoomList
    Intent intent = getIntent();
    queueID = intent.getData().toString();


    // todo Initialize Firestore and the main RecyclerView
    initFirestore();

    URLSubmit = (Button) findViewById(R.id.button);
    URLInput = (EditText) findViewById(R.id.editText);
    youtubePlayer = (YouTubePlayerView) findViewById(R.id.youtubePlayer);

    //dummy chat
    messageSubmit = (Button) findViewById(R.id.sendButton);
    messageInput = (EditText) findViewById(R.id.editMsg);

    onInitListener = new YouTubePlayer.OnInitializedListener() {
      @Override
      public void onInitializationSuccess(Provider provider, final YouTubePlayer youTubePlayer, boolean b) {

        youtubePlayerClone = youTubePlayer; //makes a clone of the youTubePlayer so we can use it outside the scope of onInitializationSuccess or for any methods inside
        StateChangeListener listener = new StateChangeListener(room, youtubePlayerClone, queueID);

        youTubePlayer.setPlayerStateChangeListener(listener);

//        if(!(room.videoQueue.isEmpty())){ //Whole if statement is temp
//          youTubePlayer.loadVideo(room.videoQueue.remove());
//          pushFirebase();
//          pullFirebase();
//          showToast("Video loaded!");
//        }else {
//          showToast("No song is presented in the queue currently");
//        }
      }

      @Override
      public void onInitializationFailure(Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

      }
    };

    //ONCLICKLISTENER CRASHES WHEN A MUSIC ROOM IS OPENED

//    messageSubmit.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        String message = messageInput.getText().toString();
//        if (message.equals(null)) showToast("No message entered.");
//        else {
//          mMessages.add("You: " + message);
//          showToast("Sent message.");
//        }
//      }
//    });

    URLSubmit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        boolean songNotPresent = false;
        if(room.videoQueue.isEmpty()){ //Temp
          songNotPresent = true;
        }
        youtubeURL = URLInput.getText().toString();
        youtubeURL = extractURL(youtubeURL);
        room.videoQueue.add(youtubeURL); //Temp
        room.queueSize++;
        showToast("Added to queue.");
        if(songNotPresent){ //tries to initialize the first song you see even if nothing in queue
          youtubePlayerClone.loadVideo(room.videoQueue.peek());
        }
        pushFirebase();
        pullFirebase();
      }
    });
    youtubePlayer.initialize(Youtube_Key.getAPI_Key(), onInitListener);
    initialPullFirebase();
  }

  private void initChat(){
    mMessages.add("Welcome!");
    initRecyclerView();
  }

  private void initRecyclerView(){
    RecyclerView recyclerView = findViewById(R.id.chat_room);
    ChatAdapter adapter = new ChatAdapter(this, mMessages);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
  }

  public String extractURL (String URL){
    int index = 0;
    while(URL.charAt(index) != '='){
      index++;
      if(index == URL.length()){
        break;
      }
    }
    URL = URL.substring(index+1,URL.length());
    //Add a throw when string is out of bounds that send a message that the URL is invalid and delete the if statement
    return URL;
  }

  public void showToast(String text){
    Toast.makeText(MusicRoomActivity.this,text, Toast.LENGTH_SHORT).show();
  }

  void pushFirebase()
  {
    FirebaseFirestore fireBaseQueue = FirebaseFirestore.getInstance();
    Map<String, Object> updatedQueue = new HashMap<>();
    showToast(Integer.toString(room.queueSize));
    for (int i = 1; i <= room.queueSize; i++) {
        updatedQueue.put(Integer.toString(i), room.videoQueue.remove());
    }
    fireBaseQueue.collection("queues").document(queueID)
            .set(updatedQueue)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                showToast("Error adding document");
              }
            });
  }
  void pullFirebase()
  {
    FirebaseFirestore fireBaseQueue = FirebaseFirestore.getInstance();
    DocumentReference docRef = fireBaseQueue.collection("queues").document(queueID);
    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
          DocumentSnapshot document = task.getResult();
          if (document.exists()) {
            String value;
            for (int i = 1; i <= 10; i++)
            {
              String j = Integer.toString(i);
              if (document.getString(j) == null)
              {
                break;
              }
              value = document.getString(j);
              room.videoQueue.add(value);
            }
          } else {
            showToast("No such document");
          }
        } else {
          showToast("get failed with ");
        }
      }
    });
  }

  void initialPullFirebase()
  {
    FirebaseFirestore fireBaseQueue = FirebaseFirestore.getInstance();
    DocumentReference docRef = fireBaseQueue.collection("queues").document(queueID);
    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
          DocumentSnapshot document = task.getResult();
          if (document.exists()) {
            String value;
            for (int i = 1; i <= 10; i++)
            {
              String j = Integer.toString(i);
              if (document.getString(j) == null)
              {
                break;
              }
              value = document.getString(j);
              room.videoQueue.add(value);
              room.queueSize++;
            }
            if (!(room.videoQueue.isEmpty())){
              youtubePlayerClone.loadVideo(room.videoQueue.peek());
              showToast(Integer.toString(room.queueSize));
              pullFirebase();
              pushFirebase();
              pullFirebase();
            }
            else {
              showToast("queue is empty");
            }
          } else {
            showToast("No such document");
          }
        } else {
          showToast("get failed with ");
        }
      }
    });
  }

  private void initFirestore() {
    db = FirebaseFirestore.getInstance();
    roomQuery = db.collection("music-room").document(queueID);
  }

  void registerStateChange(StateChangeListener listener) {
    youtubePlayerClone.setPlayerStateChangeListener(listener);
  }

  public void sendChat(View view) {
    messageInput = findViewById(R.id.editMsg);
    String message = messageInput.getText().toString();
    if (message.equals(null)) showToast("No message entered.");
    else {
      mMessages.add("You: " + message);
      showToast("Sent message." + message);
    }

  }

}
