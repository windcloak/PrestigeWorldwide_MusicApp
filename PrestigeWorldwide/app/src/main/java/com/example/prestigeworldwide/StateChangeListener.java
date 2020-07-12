package com.example.prestigeworldwide;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class StateChangeListener implements YouTubePlayer.PlayerStateChangeListener {
    MusicRoom room;
    YouTubePlayer YTplayer;
    private String queueID;

    public StateChangeListener(MusicRoom room, YouTubePlayer YTplayer, String queueID) {
        this.room = room;
        this.YTplayer = YTplayer;
        this.queueID = queueID;
    }

    @Override
    public void onLoading() {
        //empty. can implement later
    }

    @Override
    public void onLoaded(String s) {
        //empty. can implement later
    }

    @Override
    public void onAdStarted() {
        //empty. can implement later
    }

    @Override
    public void onVideoStarted() {
        //empty. can implement later
    }

    @Override
    public void onVideoEnded() {
        if (room.queueSize > 1)
        {
            room.videoQueue.remove();
        }
        if(!(room.videoQueue.isEmpty())){ //Whole if statement is temp
            YTplayer.loadVideo(room.videoQueue.peek());
            room.queueSize--;
            pushFirebase();
            pullFirebase();
        }else {
        }
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        //empty. can implement later
    }

    void pushFirebase()
    {
        FirebaseFirestore fireBaseQueue = FirebaseFirestore.getInstance();
        Map<String, Object> updatedQueue = new HashMap<>();
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
//              youtubePlayerClone.loadVideo(videoQueue.peek());
//              showToast(document.getString(j));
                        }
//            showToast( "DocumentSnapshot data: " + document.getData());
                    } else {
                    }
                } else {
                }
            }
        });
    }
}
