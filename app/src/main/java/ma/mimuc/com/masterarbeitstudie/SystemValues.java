package ma.mimuc.com.masterarbeitstudie;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;

/**
 * Created by Raphael on 11.11.2015.
 */
public class SystemValues {
    private Context context;

    public SystemValues(Context context){
        this.context=context;
    }

    public int getMediaVolume(){
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audio.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public int getRingToneVolume(){
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audio.getStreamVolume(AudioManager.STREAM_RING);
    }

    public boolean wifiEnabled(){
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

        if (wifi.isWifiEnabled()){
            return true;
        }
        else return  false;
    }



}
