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

public class HistoryFragment extends Fragment {
    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener () {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer ();
        }
    };
    private AudioManager mAudioManager;

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener () {
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
                mMediaPlayer.pause ();
                mMediaPlayer.seekTo ( 0 );
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start ();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer ();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mAudioManager = (AudioManager) getActivity ().getSystemService ( Context.AUDIO_SERVICE );
        final ArrayList<Place> historyArray = new ArrayList<> ();
        historyArray.add ( new Place ( R.drawable.ruins_of_cyrene, R.string.aruins_of_cyrene, R.string.adalaj_stepwell_address, R.raw.history_aruins_of_cyrene ) );
        historyArray.add ( new Place ( R.drawable.garama, R.string.garama, R.string.garama_address, R.raw.history_garama ) );
        historyArray.add ( new Place ( R.drawable.tolmeitha, R.string.tolmeitha, R.string.tolmeitha_address, R.raw.history_tolmeitha ) );
        historyArray.add ( new Place ( R.drawable.susah, R.string.susah, R.string.susah_address, R.raw.history_susah ) );
        historyArray.add ( new Place ( R.drawable.sabratha, R.string.sabratha, R.string.sabratha_address, R.raw.history_sabratha ) );
        historyArray.add ( new Place ( R.drawable.awjilah, R.string.awjilah, R.string.awjilah_address, R.raw.history_awjilah ) );
        historyArray.add ( new Place ( R.drawable.marcus_aurelius, R.string.marcus_aurelius, R.string.marcus_aurelius_address, R.raw.history_marcus_aurelius ) );
        historyArray.add ( new Place ( R.drawable.labidat_alkubraa, R.string.labidat_alkubraa, R.string.labidat_alkubraa_address, R.raw.history_labidat ) );
        historyArray.add ( new Place ( R.drawable.ghadames_mosque, R.string.ghadames_mosque, R.string.ghadames_mosque_address, R.raw.history_ghadames ) );
        historyArray.add ( new Place ( R.drawable.el_latama_benghazi, R.string.el_latama_benghazi, R.string.el_latama_benghazi_address, R.raw.history_el_latama ) );

        View rootView = inflater.inflate ( R.layout.activity_list, container, false );
        PlaceAdapter placeAdapter = new PlaceAdapter ( getContext (), historyArray );
        ListView listView = rootView.findViewById ( R.id.list );
        listView.setAdapter ( placeAdapter );
        listView.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Place w = historyArray.get ( position );
                releaseMediaPlayer ();
                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus ( mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT );

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback
                    mMediaPlayer = MediaPlayer.create ( getContext (), w.getAudioResourceId () );
                    mMediaPlayer.start ();
                    mMediaPlayer.setOnCompletionListener ( mOnCompletionListener );
                }

            }
        } );
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop ();
        releaseMediaPlayer ();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release ();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus ( mOnAudioFocusChangeListener );
        }
    }
}