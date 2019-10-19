package abdelfattah.libya;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class RestaurantFragment extends Fragment {
    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };
    private AudioManager mAudioManager;

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mAudioManager = (AudioManager)getActivity().getSystemService( Context.AUDIO_SERVICE);
        final ArrayList<Place> restaurantArray = new ArrayList<>();
        restaurantArray.add(new Place (R.drawable.matohom_elrubyian,R.string.matohom_elrubyian,R.string.matohom_elrubyian_address,R.raw.restaurant_matohom_elrubyian));
        restaurantArray.add(new Place (R.drawable.matohom_barakudat,R.string.matohom_barakudat,R.string.matohom_barakudat_address,R.raw.restaurant_matohom_barakuda));
        restaurantArray.add(new Place (R.drawable.matohom_francisco,R.string.matohom_francisco,R.string.matohom_francisco_address,R.raw.restaurant_matohom_francisco));
        restaurantArray.add(new Place (R.drawable.beetza_lforno,R.string.beetza_lforno,R.string.beetza_lforno_address,R.raw.restaurant_beetza_lforno));
        restaurantArray.add(new Place (R.drawable.kaffee_latte,R.string.kaffee_latte,R.string.kaffee_latte_address,R.raw.restaurant_kaffee_latte));
        restaurantArray.add(new Place (R.drawable.burj_abwalila,R.string.burj_abwalila,R.string.burj_abwalila_address,R.raw.restaurant_burj_abwalila));
        restaurantArray.add(new Place (R.drawable.matohom_kodo,R.string.matohom_kodo,R.string.matohom_kodo_address,R.raw.restaurant_matohom_kodo));
        restaurantArray.add(new Place (R.drawable.matohom_saaffeer,R.string.matohom_saaffeer,R.string.matohom_saaffeer_address,R.raw.restaurant_matohom_saaffeer));
        restaurantArray.add(new Place (R.drawable.matohom_alatahar,R.string.matohom_alatahar,R.string.matohom_alatahar_address,R.raw.restaurant_matohom_alatahar));
        restaurantArray.add(new Place (R.drawable.elmadeena_elgadeema,R.string.elmadeena_elgadeema,R.string.elmadeena_elgadeema_address,R.raw.restaurant_elmadeena_elgadeema));
        restaurantArray.add(new Place (R.drawable.cafe_di_roma,R.string.cafe_di_roma,R.string.cafe_di_roma_address,R.raw.restaurant_cafe_di_roma));
        restaurantArray.add(new Place (R.drawable.havana_juice,R.string.havana_juice,R.string.havana_juice_address,R.raw.restaurant_havana_juice));
        restaurantArray.add(new Place (R.drawable.storico_ristorante_italiano,R.string.storico_ristorante_italiano,R.string.storico_ristorante_italiano_address,R.raw.restaurant_storico_ristorante_italiano));
        restaurantArray.add(new Place (R.drawable.sahtein_w_hana,R.string.sahtein_w_hana,R.string.sahtein_w_hana_address,R.raw.restaurant_sahtein_w_hana));
        restaurantArray.add(new Place (R.drawable.caffe_casa,R.string.caffe_casa,R.string.caffe_casa_address,R.raw.restaurant_caffe_casa));
        restaurantArray.add(new Place (R.drawable.matohom_murina,R.string.matohom_murina,R.string.matohom_murina_address,R.raw.restaurant_matohom_murina));
        restaurantArray.add(new Place (R.drawable.matohom_hatab,R.string.matohom_hatab,R.string.matohom_hatab_address,R.raw.restaurant_matohom_hatab));
        restaurantArray.add(new Place (R.drawable.matohom_alsaraya,R.string.matohom_alsaraya,R.string.matohom_alsaraya_address,R.raw.restaurant_matohom_alsaraya));


        View rootView = inflater.inflate(R.layout.activity_list, container, false);
        PlaceAdapter placeAdapter = new PlaceAdapter (getContext(), restaurantArray);
        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter( placeAdapter );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Place w = restaurantArray.get(position);
                releaseMediaPlayer();
                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback
                    mMediaPlayer = MediaPlayer.create(getContext(),w.getAudioResourceId());
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }

            }
        });
        return rootView;
    }
    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer(){
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
