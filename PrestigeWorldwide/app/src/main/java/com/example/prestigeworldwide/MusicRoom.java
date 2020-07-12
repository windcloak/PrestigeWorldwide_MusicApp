package com.example.prestigeworldwide;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MusicRoom {
  private static MusicRoom instance;
  private String genre;
  private String name;
  private String password;
  private boolean isPublic;   // true for public, false for private
  //private String name;
  //private String genre;
  //private String password;  // only set for private
  Queue<String> videoQueue = new LinkedList<>(); //Temp
  int queueSize = 0;

  private ArrayList<Chat> chat;

  // public constructor that takes no arguments
  public MusicRoom() {
  }

  // Initialize properties
  public MusicRoom(boolean isPublic, String name, String genre, String password) {
    this.isPublic = isPublic;
    this.name = name;
    this.genre = genre;
    this.password = password;
    this.chat = new ArrayList<>();
  }

  // Subclass Chat
  private class Chat {
    private String name;
    private String message;

    // Constructor to make new Chat object
    public Chat(String name, String message){
      this.name = name;
      this.message = message;
    }

    public String getName() {
      return name;
    }

    public String getMessage() {
      return message;
    }

  }

  // public getter for each property

  public boolean getIsPublic() {
    return isPublic;
  }

  public String getName() {
    return name;
  }

  public String getGenre() {
    return genre;
  }

  public String getPassword() {
    return password;
  }

  public ArrayList<Chat> getChat() {
    return chat;
  }

  public static MusicRoom getInstance(boolean tpublic, String tname, String tGenre, String tPassword) {
    if( instance == null ){
      instance = new MusicRoom(tpublic,tname,tGenre,tPassword);
    }
    return instance;
  }

}
