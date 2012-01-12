package org.fourdnest.androidclient.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fourdnest.androidclient.FourDNestApplication;
import org.fourdnest.androidclient.Nest;
import org.fourdnest.androidclient.NestManager;
import org.fourdnest.androidclient.Tag;
import org.fourdnest.androidclient.comm.Protocol;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * A service that keeps caches of recent and/or popular tags,
 * both local and remote. 
 */
public class TagSuggestionService extends IntentService {
	/** Tag string used to indicate source in logging */	
	public static final String TAG = TagSuggestionService.class.getSimpleName();

	/** Intent should have this category when remote tags should be updated */
	public static final String UPDATE_REMOTE_TAGS = "UPDATE_REMOTE_TAGS_CATEGORY";

	/** How long to initially wait before fetching remote tags */
	private static final long FIRST_INTERVAL = 0;

	private static final int REMOTE_TAG_COUNT = 1024;
	
	private Map<Integer, List<Tag>> lastUsedTags;
	private Map<Integer, HashSet<Tag>> localTags;
	private Map<Integer, List<Tag>> remoteTags;
	private int maxSize;

	private static FourDNestApplication app;
	
	/**
	 * Constructor, simply calls super. Never used explicitly in user code.
	 */
	public TagSuggestionService() {
		super(TagSuggestionService.class.getName());
	}
	/**
	 * Constructor, simply calls super. Never used explicitly in user code.
	 * @param name Name of service
	 */	
	public TagSuggestionService(String name) {
		super(name);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
		this.lastUsedTags = new HashMap<Integer, List<Tag>>();
		this.localTags = new HashMap<Integer, HashSet<Tag>>();
		this.remoteTags = new HashMap<Integer, List<Tag>>();

		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setInexactRepeating(	//Be power efficient: we don't need exact timing
        		AlarmManager.ELAPSED_REALTIME,	// Don't wake up phone just for this
        		FIRST_INTERVAL,								
        		AlarmManager.INTERVAL_HOUR,		// Update frequency
        		PendingIntent.getService(
        				app,					// The context
        				0,
        				new Intent(app, TagSuggestionService.class),
        				0
        		)
        );
	}

	public static void setApp(FourDNestApplication fourdnestapp) {
		app = fourdnestapp;
	}
	

	/**
	 * Returns a list of all seen tags, for use in for example autocompletion.
	 * The returned tags are a combination of local and remote tags.
	 * @return A list of tags
	 */
	public synchronized List<Tag> getTags(Context context) {
		FourDNestApplication app = (FourDNestApplication) context;
		Integer currentNestId = Integer.valueOf(app.getCurrentNestId());
		List<Tag> out = new ArrayList<Tag>(this.maxSize);
		Set<Tag> lt = this.localTags.get(currentNestId);
		if(lt != null) {
			out.addAll(lt);
		}
		List<Tag> rt = this.remoteTags.get(currentNestId);
		if(lt != null) {
			out.addAll(rt);
		}
		return out;
	}
	
	/**
	 * Stores the tags used in sending an Egg,
	 * so that when sending the next one they can be retrieved
	 * and used as suggestions.
	 * @param tags The tags attached to the last sent Egg.
	 */
	public synchronized void SetLastUsedTags(Context context, List<Tag> tags) {
		FourDNestApplication app = (FourDNestApplication) context;
		Integer currentNestId = Integer.valueOf(app.getCurrentNestId());
		this.lastUsedTags.put(currentNestId, tags);
	}

	/**
	 * @return The tags attached to the last sent Egg.
	 */
	public synchronized List<Tag> getLastUsedTags(Context context) {
		FourDNestApplication app = (FourDNestApplication) context;
		Integer currentNestId = Integer.valueOf(app.getCurrentNestId());
		List<Tag> out = this.lastUsedTags.get(currentNestId);
		if(out == null) return new ArrayList<Tag>();
		return out;
	}
	
	/**
	 * Default Intent handler for IntentService. All Intents get sent here.
	 * Updates the remote tag cache.
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "onHandleIntent called");
		
		// If Intent it categorized as update intent, act accordingly 
		if(intent.hasCategory(UPDATE_REMOTE_TAGS)) {
			updateRemoteTags();
		}
	}
	/**
	 * Loops through all Nests updating their tag cache
	 */
	private synchronized void updateRemoteTags() {
		NestManager nestManager = app.getNestManager();
		List<Nest> nests = nestManager.listNests();
		for(Nest nest : nests) {
			Integer nestId = Integer.valueOf(nest.getId());
			Protocol protocol = nest.getProtocol();
			List<Tag> tags = protocol.topTags(REMOTE_TAG_COUNT);
			if(tags != null) {
				Log.d(TAG, "Tags updated for Nest with id " + nestId);
				this.remoteTags.put(nestId, tags);
			} else {
				Log.w(TAG, "Nest with id " + nestId + " returned null topTags");
			}
		}
	}
}
