package com.demo.app.p50mobileappdemo;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;

import org.json.JSONObject;

public class MusicPlayer {
    private static MusicPlayer instance;
    MediaPlayer mpintro;
    MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
    HarmonyServiceConnection harmonyServiceConnection;

    public void initHosCommunication(Context context) {
        harmonyServiceConnection = new HarmonyServiceConnection(context);
        harmonyServiceConnection.connect();
    }


    String[] songs = {"intro.mp3","winter.mp3", "spring.mp3"};
    int currentSong = -1;

    public boolean play(Context context) {
        try {
            JSONObject joc = new JSONObject();
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Music/";
            if (currentSong < songs.length){
                currentSong++;
            }
            else{
                currentSong = -1;
            }

            String song = path + songs[currentSong];
            if (mpintro != null){
                mpintro.stop();
            }
            mpintro = MediaPlayer.create(context, Uri.parse(song));

            mpintro.setLooping(true);
            mpintro.start();
            metaRetriever.setDataSource(song);
            String title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String artist = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            long duration = Long.parseLong(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            long minutes = (duration / 1000) / 60;
            long seconds = (duration / 1000) % 60;
            joc.put("duration", minutes + ":" + (seconds < 10 ? "0"+seconds : seconds));
            joc.put("title", title);
            joc.put("artist", artist);
            harmonyServiceConnection.sendData(joc.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void stop() {
        try {
            if (mpintro != null) {
                JSONObject joc = new JSONObject();
                joc.put("duration", "");
                joc.put("title", "");
                joc.put("artist", "");
                mpintro.stop();
                harmonyServiceConnection.sendData(joc.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static MusicPlayer getInstance() {
        if (instance == null) {
            instance = new MusicPlayer();
        }
        return instance;
    }
}
