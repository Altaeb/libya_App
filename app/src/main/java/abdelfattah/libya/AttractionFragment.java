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

public class AttractionFragment extends Fragment {
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
        final ArrayList<Place> attractionArray = new ArrayList<> ();
        attractionArray.add ( new Place ( R.drawable.desert_and_camel, R.string.desert_and_camel, R.string.desert_and_camel_address, R.raw.attraction_desert_and_camel ) );
        attractionArray.add ( new Place ( R.drawable.mount_owainat, R.string.mount_owainat, R.string.mount_owainat_address, R.raw.attraction_owainat ) );
        attractionArray.add ( new Place ( R.drawable.red_saraya_museum, R.string.red_saraya_museum, R.string.red_saraya_museum_address, R.raw.attraction_saraya ) );
        attractionArray.add ( new Place ( R.drawable.aljabal_algharbiu, R.string.aljabal_algharbiu, R.string.aljabal_algharbiu_address, R.raw.attraction_aljabal_algharbiu ) );
        attractionArray.add ( new Place ( R.drawable.qabr_own, R.string.qabr_own, R.string.qabr_own_address, R.raw.attraction_qabr_own ) );
        attractionArray.add ( new Place ( R.drawable.aljabal_al_akhdar, R.string.aljabal_al_akhdar, R.string.aljabal_al_akhdar_address, R.raw.attraction_aljabal_al_akhdar ) );
        attractionArray.add ( new Place ( R.drawable.midan_alshuhada, R.string.midan_alshuhada, R.string.midan_alshuhada_address, R.raw.attraction_midan_alshuhada ) );
        attractionArray.add ( new Place ( R.drawable.waw_alnaamus, R.string.waw_alnaamus, R.string.waw_alnaamus_address, R.raw.attraction_waw_alnaamus ) );
        attractionArray.add ( new Place ( R.drawable.jamie_qarjiun, R.string.jamie_qarjiun, R.string.jamie_qarjiun_address, R.raw.attraction_jamie_qarjiun ) );
        attractionArray.add ( new Place ( R.drawable.masrah_sabaratah, R.string.masrah_sabaratah, R.string.masrah_sabaratah_address, R.raw.attraction_masrah_sabaratah ) );
        attractionArray.add ( new Place ( R.drawable.qalhat_sabha, R.string.qalhat_sabha, R.string.qalhat_sabha_address, R.raw.attraction_qalhat_sabha ) );
        attractionArray.add ( new Place ( R.drawable.filha_seyleen, R.string.filha_seyleen, R.string.filha_seyleen_address, R.raw.attraction_filha_seyleen ) );
        attractionArray.add ( new Place ( R.drawable.fawahat_arkono, R.string.fawahat_arkono, R.string.fawahat_arkono_address, R.raw.attraction_fawahat_arkono ) );
        attractionArray.add ( new Place ( R.drawable.mothhaf_janzur, R.string.mothhaf_janzur, R.string.mothhaf_janzur_address, R.raw.attraction_mothhaf_janzur ) );
        attractionArray.add ( new Place ( R.drawable.mothaf_ghadamis, R.string.mothaf_ghadamis, R.string.mothaf_ghadamis_address, R.raw.attraction_mothaf_ghadamis ) );
        attractionArray.add ( new Place ( R.drawable.mothaf_libya, R.string.mothaf_libya, R.string.mothaf_libya_address, R.raw.attraction_mothaf_libya ) );

        View rootView = inflater.inflate ( R.layout.activity_list, container, false );
        PlaceAdapter placeAdapter = new PlaceAdapter ( getContext (), attractionArray );
        ListView listView = rootView.findViewById ( R.id.list );
        listView.setAdapter ( placeAdapter );
        listView.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Place w = attractionArray.get ( position );
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