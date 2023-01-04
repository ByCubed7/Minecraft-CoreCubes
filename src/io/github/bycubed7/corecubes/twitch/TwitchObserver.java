package io.github.bycubed7.corecubes.twitch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.User;
import com.github.twitch4j.helix.domain.UserList;
import com.github.twitch4j.pubsub.events.ChannelSubGiftEvent;
import com.github.twitch4j.pubsub.events.FollowingEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;

import io.github.bycubed7.corecubes.CubePlugin;
import io.github.bycubed7.corecubes.managers.ConfigManager;
import io.github.bycubed7.corecubes.managers.Debug;
import io.github.bycubed7.corecubes.managers.Manager;

public class TwitchObserver extends Manager {

    private TwitchClient twitchClient;
    private ConfigManager config;
    
    private List<ITwitchListener> eventListeners;
    
	public TwitchObserver(CubePlugin _plugin) {
		super(_plugin, -1);
		
		eventListeners = new ArrayList<ITwitchListener>();
		
		// Get the usage data
		config = new ConfigManager(plugin, "twitch");
		
		// Initailize the client
		twitchClient = TwitchClientBuilder.builder()
            .withClientId(config.getString("api.id"))
            .withClientSecret(config.getString("api.secret"))
			.withDefaultAuthToken(new OAuth2Credential("twitch", config.getString("api.oauth")))
			.withEnableHelix(true)
			.withEnablePubSub(true)
			.build();
			
		// Subscribe to all listener events
		for (Object element : config.getList("users")) {
			Dictionary<String, String> userdata = (Dictionary<String, String>) element;
			User channel = getUserFromName(userdata.get("twitch"));
			
			// Enable events to be listened to
			twitchClient.getClientHelper().enableFollowEventListener(channel.getDisplayName());
			
			// If the oauth has not been provided, we can only do this much
			if (userdata.get("oauth2") == null) continue;
			OAuth2Credential auth = new OAuth2Credential("twitch", userdata.get("oauth2"));
			
			// TODO: We should be keeping track of the events we listen to
			// TODO: What happens if the Auth is incorrect?
			twitchClient.getPubSub().listenForChannelPointsRedemptionEvents(auth, channel.getId());
			twitchClient.getPubSub().listenForSubscriptionEvents(auth, channel.getId());
			twitchClient.getPubSub().listenForChannelSubGiftsEvents(auth, channel.getId());
		}		

		// Redirect events
		twitchClient.getEventManager().onEvent(RewardRedeemedEvent.class, this::onRewardRedeemed);
		twitchClient.getEventManager().onEvent(FollowingEvent.class, this::onFollow);
		twitchClient.getEventManager().onEvent(ChannelSubGiftEvent.class, this::onChannelSubGiftEvent);
	}
	
	@Override
	public void run() {}
	
	public User getUserFromName(String username) {
		UserList resultList = twitchClient.getHelix().getUsers(null, null, Arrays.asList(username)).execute();
		return resultList.getUsers().get(0);
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// Event Subscribers
	
	public void subscribe(ITwitchListener listener) {
		eventListeners.add(listener);
	}
	
	public void unsubscribe(ITwitchListener listener) {
		eventListeners.remove(listener);
	}
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// Event usage
	// When an event is called, it's passed onto the listener objects listed

	public void onRewardRedeemed(RewardRedeemedEvent event) {
		Debug.log("Holy moly this actually works");
		for (ITwitchListener listener : eventListeners)
			listener.onRewardRedeemed(event);
	}
	
	public void onFollow(FollowingEvent event) {
		for (ITwitchListener listener : eventListeners)
			listener.onFollow(event);
	}
	
	public void onChannelSubGiftEvent(ChannelSubGiftEvent event) {
		for (ITwitchListener listener : eventListeners)
			listener.onGiftedSub(event);
	}
	
	
}
