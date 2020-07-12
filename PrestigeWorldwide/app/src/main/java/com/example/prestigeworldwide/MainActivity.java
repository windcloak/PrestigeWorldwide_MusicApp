package com.example.prestigeworldwide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       ImageView iconImage = (ImageView) findViewById(R.id.imageView);
       iconImage.setImageResource(R.drawable.icon);
    }

  public void launchRoomList(View view) {
    Intent intent = new Intent(this, RoomListActivity.class);
    startActivity(intent);
  }

  public void launchCreateRoom(View view) {
    Intent intent = new Intent(this, CreateRoomActivity.class);
    startActivity(intent);
  }

    public void launchMusicRoom(View view) {
        Intent intent = new Intent(this, MusicRoomActivity.class);
        startActivity(intent);
    }
}
