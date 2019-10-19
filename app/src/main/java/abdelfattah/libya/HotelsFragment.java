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

public class HotelsFragment extends Fragment {
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
        final ArrayList<Place> hotelArray = new ArrayList<> ();
        hotelArray.add(new Place(R.drawable.funduq_korinthia,R.string.funduq_korinthia,R.string.funduq_korinthia_address,R.raw.hotel_funduq_korinthia));
        hotelArray.add(new Place(R.drawable.funduq_ryksos_alnasr,R.string.funduq_ryksos_alnasr,R.string.funduq_ryksos_alnasr_address,R.raw.hotel_funduq_ryksos_alnasr));
        hotelArray.add(new Place(R.drawable.funduq_sahraa_libya,R.string.funduq_sahraa_libya,R.string.funduq_sahraa_libya_address,R.raw.hotel_funduq_sahraa_libya));
        hotelArray.add(new Place(R.drawable.funduq_bab_albahr,R.string.funduq_bab_albahr,R.string.funduq_bab_albahr_address,R.raw.hotel_funduq_bab_albahr));
        hotelArray.add(new Place(R.drawable.funduq_elwedaan,R.string.funduq_elwedaan,R.string.funduq_elwedaan_address,R.raw.hotel_funduq_elwedaan));
        hotelArray.add(new Place(R.drawable.funduq_j_w_mariut,R.string.funduq_j_w_mariut,R.string.funduq_j_w_mariut_address,R.raw.hotel_funduq_j_w_mariut));
        hotelArray.add(new Place(R.drawable.radisson_blu_elmahary,R.string.radisson_blu_elmahary,R.string.radisson_blu_elmahary_address,R.raw.hotel_radisson_blu_elmahary));
        hotelArray.add(new Place(R.drawable.funduq_plasma,R.string.funduq_plasma,R.string.funduq_plasma_address,R.raw.hotel_funduq_plasma));
        hotelArray.add(new Place(R.drawable.funduq_alnaher,R.string.funduq_alnaher,R.string.funduq_alnaher_address,R.raw.hotel_funduq_alnaher));
        hotelArray.add(new Place(R.drawable.elfosol_elarrbhha,R.string.elfosol_elarrbhha,R.string.elfosol_elarrbhha_address,R.raw.hotel_elfosol_elarrbhha));
        hotelArray.add(new Place(R.drawable.eltawfeeq_bilaazaa,R.string.eltawfeeq_bilaazaa,R.string.eltawfeeq_bilaazaa_address,R.raw.hotel_eltawfeeq_bilaazaa));
        hotelArray.add(new Place(R.drawable.funduq_alkhan,R.string.funduq_alkhan,R.string.funduq_alkhan_address,R.raw.hotel_funduq_alkhan));
        hotelArray.add(new Place(R.drawable.funduq_fikturia,R.string.funduq_fikturia,R.string.funduq_fikturia_address,R.raw.hotel_funduq_fikturia));
        hotelArray.add(new Place(R.drawable.funduq_alrayyan,R.string.funduq_alrayyan,R.string.funduq_alrayyan_address,R.raw.hotel_funduq_alrayyan));
        hotelArray.add(new Place(R.drawable.funduq_harun,R.string.funduq_harun,R.string.funduq_harun_address,R.raw.hotel_funduq_harun));
        hotelArray.add(new Place(R.drawable.four_bwynts_sheraton,R.string.four_bwynts_sheraton,R.string.four_bwynts_sheraton_address,R.raw.hotel_four_bwynts_sheraton));
        hotelArray.add(new Place(R.drawable.funduq_topacts,R.string.funduq_topacts,R.string.funduq_topacts_address,R.raw.hotel_funduq_topacts));
        hotelArray.add(new Place(R.drawable.funduq_albatra,R.string.funduq_albatra,R.string.funduq_albatra_address,R.raw.hotel_funduq_albatra));

        View rootView = inflater.inflate(R.layout.activity_list, container, false);
        PlaceAdapter placeAdapter = new PlaceAdapter (getContext(), hotelArray);
        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter( placeAdapter );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Place w = hotelArray.get(position);
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
