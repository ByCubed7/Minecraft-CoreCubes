package io.github.bycubed7.corecubes.twitch;

import com.github.twitch4j.pubsub.events.ChannelSubGiftEvent;
import com.github.twitch4j.pubsub.events.FollowingEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;

public interface ITwitchListener {
	void onRewardRedeemed(RewardRedeemedEvent event);
	void onFollow(FollowingEvent event);
	void onGiftedSub(ChannelSubGiftEvent event);
}
