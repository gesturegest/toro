/*
 * Copyright (c) 2018 Nam Nguyen, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package toro.demo.mopub;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ui.PlayerView;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.media.VolumeInfo;
import im.ene.toro.widget.Container;

/**
 * @author eneim (2018/03/13).
 */

public class VideoViewHolder extends BaseViewHolder implements ToroPlayer {

  protected final PlayerView playerView;
  protected VolumeAwareHelper helper;
  private Uri videoUri;

  VideoViewHolder(ViewGroup parent, LayoutInflater inflater, int layoutRes) {
    super(parent, inflater, layoutRes);
    playerView = itemView.findViewById(R.id.playerView);
  }

  @Override void bind(Object item) {
    super.bind(item);
    videoUri = Uri.parse("file:///android_asset/bbb/video.mp4");
  }

  @NonNull @Override public View getPlayerView() {
    return playerView;
  }

  @NonNull @Override public PlaybackInfo getCurrentPlaybackInfo() {
    return helper != null ? helper.getLatestPlaybackInfo() : new PlaybackInfo();
  }

  @Override
  public void initialize(@NonNull Container container, @Nullable PlaybackInfo playbackInfo) {
    if (videoUri == null) throw new IllegalStateException("Video is null.");
    if (helper == null) {
      helper = new VolumeAwareHelper(this, videoUri);
    }
    helper.initialize(container, playbackInfo);
    // Always do this after initialize.
    // [Restore user's Volume] if playbackInfo is a VolumeAwarePlaybackInfo --> it was cached and returned here.
    // This case, there will be a saved VolumeInfo so we use it instead.
    if (playbackInfo instanceof VolumeAwarePlaybackInfo) {
      helper.setVolumeInfo(((VolumeAwarePlaybackInfo) playbackInfo).getVolumeInfo());
    } else {
      helper.setVolumeInfo(new VolumeInfo(true, 0.75f));
    }
  }

  @Override public void play() {
    if (helper != null) helper.play();
  }

  @Override public void pause() {
    if (helper != null) helper.pause();
  }

  @Override public boolean isPlaying() {
    return helper != null && helper.isPlaying();
  }

  @Override public void release() {
    if (helper != null) {
      helper.release();
      helper = null;
    }
  }

  @Override public boolean wantsToPlay() {
    return ToroUtil.visibleAreaOffset(this, itemView.getParent()) >= 0.65;
  }

  @Override public int getPlayerOrder() {
    return getAdapterPosition();
  }

  @Override public void onSettled(Container container) {
    // Do nothing
  }
}
