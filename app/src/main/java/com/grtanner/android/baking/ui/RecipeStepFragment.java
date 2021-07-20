/*
Copyright ©2018 Glen Tanner

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.grtanner.android.baking.ui;

import android.app.Activity;
import android.net.Uri;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.grtanner.android.baking.data.Step;

/**
 * A fragment representing a single Recipe step detail screen.
 * This fragment is either contained in a {@link RecipeDetailActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepActivity}
 * on handsets.
 */
public class RecipeStepFragment extends Fragment {

    private static final String NOT_FOUND = "https://media.istockphoto.com/videos/error-internet-page-not-found-video-id1005335648";
    private static final String AUTOPLAY = "autoplay";
    private static final String CURRENT_WINDOW_INDEX = "current_window_index";
    private static final String PLAYBACK_POSITION = "playback_position";

    private Step mStep;
    private Activity mActivity;

    private PlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;

    private long mPlaybackPosition;
    private int mCurrentWindow;
    private boolean mAutoPlay = true;

    private String mVideoUrl;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mStep = getArguments().getParcelable(getResources().getString(R.string.step));
        }

        if (savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable(getResources().getString(R.string.step));
        }

        mActivity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) mActivity.findViewById(R.id.activity_recipe_step_detail_toolbar_layout);
        if (appBarLayout != null) {
           appBarLayout.setTitle(getResources().getString(R.string.step_num) + mStep.getId());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the UI (view)
        // Hook up any data sources the Fragment needs.
        // Return view to host activity
        View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);

        TextView tv_title = (TextView) rootView.findViewById(R.id.recipe_step_title);
        TextView tv_content = (TextView) rootView.findViewById(R.id.recipe_step_detail);

        tv_title.setText(mStep.getShortDescription());
        tv_content.setText(mStep.getDescription());

        mPlayerView = (PlayerView) rootView.findViewById(R.id.player_view);

        if(!mStep.getVideoURL().isEmpty()) {
            this.mVideoUrl = mStep.getVideoURL();
        } else {
            this.mVideoUrl = NOT_FOUND;
        }

        if (savedInstanceState != null) {
            mPlaybackPosition = savedInstanceState.getLong(PLAYBACK_POSITION, 0);
            mCurrentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX, 0);
            mAutoPlay = savedInstanceState.getBoolean(AUTOPLAY, true);
        }

        initializePlayer();

        return rootView;
        // Video playback position is restored by implementing onSaveInstanceState() and onActivityCreated()
        // See line 269.
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
        // Seek to the last position of the player.
        mPlayer.seekTo(mPlaybackPosition);
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            pausePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
           pausePlayer();
        }
    }

    @Override
    // https://stackoverflow.com/questions/43959176/how-i-get-exoplayer-to-resume-a-video-in-my-activitys-onstart-method
    public void onDestroy() {
        super.onDestroy();
        if (Util.SDK_INT > 23) {
            pausePlayer();
            releasePlayer();
        }
    }

    private void initializePlayer() {
        if (mPlayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create the player with previously created TrackSelector
            mPlayer = ExoPlayerFactory.newSimpleInstance(mActivity, trackSelector);

            // Load the default controller
            mPlayerView.setUseController(true);
            mPlayerView.requestFocus();

            // Measures bandwidth during playback. Can be null if not required.
            DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                    mActivity,
                    Util.getUserAgent(mActivity, mActivity.getResources().getString(R.string.app_name)),
                    defaultBandwidthMeter);

            // Produces Extractor instances for parsing the media data.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            // This is the MediaSource representing the media to be played.
            MediaSource mediaSource = new ExtractorMediaSource(
                    Uri.parse(mVideoUrl),
                    dataSourceFactory,
                    extractorsFactory,
                    null,
                    null);

            // Load the SimpleExoPlayerView with the created player
            mPlayerView.setPlayer(mPlayer);

            // Prepare the player with the source.
            mPlayer.prepare(mediaSource);

            // Autoplay the video when the player is ready
            mPlayer.setPlayWhenReady(mAutoPlay);
        }
    }

    private void pausePlayer() {
        if (mPlayer != null) {
            mAutoPlay = mPlayer.getPlayWhenReady();
            mPlaybackPosition = mPlayer.getCurrentPosition();
            mCurrentWindow = mPlayer.getCurrentWindowIndex();
            // Pause the player
            mPlayer.setPlayWhenReady(false);
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(getResources().getString(R.string.step), mStep);

        if (mPlayer != null) {
            outState.putLong(PLAYBACK_POSITION, mPlayer.getCurrentPosition());
            outState.putInt(CURRENT_WINDOW_INDEX, mCurrentWindow);
            outState.putBoolean(AUTOPLAY, mAutoPlay);
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null){
            mStep = savedInstanceState.getParcelable(getResources().getString(R.string.step));
            mPlaybackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
            mCurrentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX);
            mAutoPlay = savedInstanceState.getBoolean(AUTOPLAY);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            mStep = savedInstanceState.getParcelable(getResources().getString(R.string.step));
            mPlaybackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
            mCurrentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX);
            mAutoPlay = savedInstanceState.getBoolean(AUTOPLAY);
        }
    }
}
