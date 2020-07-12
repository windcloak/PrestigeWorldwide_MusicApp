package com.example.prestigeworldwide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class CreateRoomActivity extends AppCompatActivity {

  private boolean isPublicRoom = true;
  private EditText nameEditText, genreEditText, passwordEditText;
  private TextView passwordLabel;
  private String roomName;
  private String roomGenre;
  private String roomPassword;
  private String roomId;
  boolean isPrivateChecked = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_room);
  }

  // Set up room object from user-entered text
  private void setupRoom(){
    nameEditText = findViewById(R.id.name_text);
    genreEditText = findViewById(R.id.genre_text);
    roomName = nameEditText.getText().toString();
    roomGenre = genreEditText.getText().toString();
    roomId = roomIdGenerator(roomName, roomGenre);
    // Add password for private room
    if (!this.isPublicRoom) {
      roomPassword = passwordEditText.getText().toString();
    } else {
      roomPassword = "";
    }
  }

private String roomIdGenerator(String name, String genre){
    Random rand = new Random();
    // Generate random integers in range 0 to 999
    int randomNumber = rand.nextInt(10000);
    String id = name + "-" + genre + "-" + randomNumber;
    return id;
  }

  public String getRoomId(){
    return this.roomId;
  }

  // When Create Music Room button is clicked
  public void launchMusicRoom(View view) {
    Intent intent = new Intent(this, MusicRoomActivity.class);

    setupRoom();
//    MusicRoom room = MusicRoom.getInstance(this.isPublicRoom, this.roomName, this.roomGenre, this.roomPassword);
    MusicRoom room = new MusicRoom(this.isPublicRoom, this.roomName, this.roomGenre, this.roomPassword);

    new Firebase_Manager().AddMusicRoom(room, this.roomId);   // Adds a music room
    new Firebase_Manager().createQueue(this.roomId);

    // Pass music room ID to MusicRoom for their queue
    intent.setData(Uri.parse(this.roomId));
    startActivity(intent);
    // TODO delete later
    displayToast("Room added successfully");
  }

  public void onRadioButtonClicked(View view) {

    // Is the button now checked?
    boolean checked = ((RadioButton) view).isChecked();
    // Check which radio button was clicked.
    switch (view.getId()) {
      case R.id.public_room:
        if (checked)
          // Public room
          this.isPublicRoom = true;
          if (this.isPrivateChecked) {
            passwordEditText.setVisibility(View.INVISIBLE);
            passwordLabel.setVisibility(View.INVISIBLE);
          }
        break;
      case R.id.private_room:
        if (checked)
          // Private room
          this.isPublicRoom = false;
          this.isPrivateChecked = true;
          // Display password entering fields
          passwordEditText = findViewById(R.id.password_text);
          passwordEditText.setVisibility(View.VISIBLE);
          passwordLabel = findViewById(R.id.password_label);
          passwordLabel.setVisibility(View.VISIBLE);
        break;
      default:
        // Do nothing.
        break;
    }
  }

  public void displayToast(String message){
    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
  }
}
