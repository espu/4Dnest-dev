package org.fourdnest.androidclient.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.fourdnest.androidclient.Egg;
import org.fourdnest.androidclient.Egg.fileType;
import org.fourdnest.androidclient.R;
import org.fourdnest.androidclient.Tag;
import org.fourdnest.androidclient.comm.FourDNestProtocol;
import org.fourdnest.androidclient.comm.ThumbnailManager;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * An adapter for serving a list of Egg views to Egg lists in
 * ListStoredEggsFragment. The listed Egg view layouts are defined in layout
 * file egg_element_small
 */
public class EggAdapter extends ArrayAdapter<Egg> {

	/**
	 * Contains the list of Eggs that are served to the list
	 */
	protected List<Egg> eggs;

	/**
	 * The ID of the layout resource that defines a single list element.
	 */
	private int resourceId;

	/**
	 * The ViewGroup(e.g. Listview) that uses this adapter.
	 */
	protected ViewGroup parent;

	/**
	 * Constructs a new EggAdapter.
	 * 
	 * @param parent
	 *            The parent view of the layout element(e.g. ListView) which
	 *            will display the adapter listing.
	 * @param resourceId
	 *            The ID of the layout resource that defines a single list
	 *            element.
	 * @param objects
	 *            List of Eggs that are to be displayed.
	 */
	public EggAdapter(ViewGroup parent, int resourceId, List<Egg> objects) {
		super(parent.getContext(), resourceId, objects);
		this.resourceId = resourceId;
		this.parent = parent;
		this.setNotifyOnChange(true);
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {

		if (this.resourceId == R.layout.egg_element_large) {
			return bindLargeEggView(arg0, arg1);
		} else if (this.resourceId == R.layout.egg_element_small) {
			return bindSmallEggView(arg0, arg1);
		} else if (this.resourceId == R.layout.egg_element_draft) {
			return bindDraftEggView(arg0, arg1);
		} else {
			return null;
		}
	}

	private View bindDraftEggView(int arg0, View arg1) {
		View view = arg1;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(this.resourceId, getParent(), false);
		}
		Egg egg = (Egg) this.getItem(arg0);
		TextView message = (TextView) view.findViewById(R.id.message);
		TextView tags = (TextView) view.findViewById(R.id.tags);
		if(egg.getCaption().length()>0){ //if caption is empty, leave default message (no message)
			message.setText(egg.getCaption());
		}
		List<Tag> tagList = egg.getTags();
		if(tagList.size()>0){ //if there are no tags, leave default message (no tags)
			String tagListString = "";
			
			for (int i = 0; i <tagList.size(); i++){ //No join in Android java ? Could not find it
				if(i==0){
					tagListString = tagListString.concat(tagList.get(0).getName());
				}
				else {
					tagListString = tagListString.concat(", "+tagList.get(i).getName());
				}
				
				}
			tags.setText(tagListString);
			}
		
		return view;
		}

	/**
	 * Binds and returns a large egg view element. Layout defined in
	 * egg_element_large
	 * 
	 * @param arg0
	 *            The array order of this element.
	 * @param arg1
	 *            The view that is to be inflated and binded.
	 * @return An inflated and binded view.
	 */
	private View bindLargeEggView(int arg0, View arg1) {
		View view = arg1;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(this.resourceId, getParent(), false);
		}
		Egg egg = (Egg) this.getItem(arg0);
		fileType type = egg.getMimeType();
		Log.d("FILETYPE", type.toString());
		TextView message = (TextView) view.findViewById(R.id.message);
		TextView date = (TextView) view.findViewById(R.id.date);

		ImageView thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
		if (egg.getMimeType() != Egg.fileType.TEXT) {
			thumbnail.setImageURI(Uri.parse(ThumbnailManager
					.getThumbnailUriString(egg,
							FourDNestProtocol.THUMBNAIL_SIZE_SMALL)));
		}
		TextView author = (TextView) view.findViewById(R.id.author);
		TextView tags = (TextView) view.findViewById(R.id.tags);
		

		author.setText(egg.getAuthor());
		message.setText(egg.getCaption());
		if (egg.getCreationDate() != null) {
		      date.setText(new SimpleDateFormat("dd/MM HH:mm").format(egg.getCreationDate()));
		}

		if (egg.getTags().size() > 0) {
			String eggTags = "";
			for (Tag current : egg.getTags()) {
				eggTags += current.getName() + " ";
			}
			tags.setText(eggTags);
		}

		return view;
	}

	/**
	 * Binds and returns a small egg view element. Layout defined in
	 * egg_element_small
	 * 
	 * @param arg0
	 *            The array order of this element.
	 * @param arg1
	 *            The view that is to be inflated and binded.
	 * @return An inflated and binded view.
	 */
	private View bindSmallEggView(int arg0, View arg1) {
		View view = arg1;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(this.resourceId, getParent(), false);
		}
		// TODO: Value binding
		Egg egg = (Egg) this.getItem(arg0);
		TextView message = (TextView) view.findViewById(R.id.message);
		TextView date = (TextView) view.findViewById(R.id.date);

		// TODO: Proper implementation after thumbnail fetching
		// functionality.
		ImageView thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
		return view;
	}

	/**
	 * Returns the ViewGroup, which displays the Egg elements.
	 * 
	 * @return
	 */
	public final ViewGroup getParent() {
		return parent;
	}

}
