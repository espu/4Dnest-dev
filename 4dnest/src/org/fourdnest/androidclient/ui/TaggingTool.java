package org.fourdnest.androidclient.ui;

import java.util.ArrayList;
import java.util.List;

import org.fourdnest.androidclient.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

public class TaggingTool extends LinearLayout {
	/** Tag string used to indicate source in logging */	
	public static final String TAG = TaggingTool.class.getSimpleName();
    private static final String[] FIXME_DUMMYTAGS = new String[] {
        "these", "are", "dummy", "autocomplete", "tags"
    };
	
	private List<TagCheckBox> buttons;
	private List<TagRow> rows;

	public TaggingTool(Context context) {
		super(context);
		this.initialize(context);
	}

	public TaggingTool(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initialize(context);
	}
	
	private void initialize(Context context) {
		this.buttons = new ArrayList<TagCheckBox>();
		this.rows = new ArrayList<TagRow>();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, FIXME_DUMMYTAGS);
        AutoCompleteTextView tagTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_tag);
        if(tagTextView == null) {
        	Log.e(TAG, "autocomplete_tag was null");
        } else {
        	tagTextView.setAdapter(adapter);
        }

		this.addTagRow();
		
		this.setOrientation(VERTICAL);
		this.addTag("Testing tagging tool", true);
		this.addTag("Llllllllllllllllllllllllllllllllllllong", true);
		this.addTag("Short", true);
		this.addTag("tags", true);
	}
	
	public void addTag(String tag, boolean force) {
		TagCheckBox button = new TagCheckBox(getContext(), tag);
		boolean succeeded = false;
		for(TagRow row : this.rows) {
			succeeded = row.offer(button);
			if(succeeded) { break; }
		}
		if(!succeeded && force) {
			succeeded = addTagRow().offer(button);
		}
		if(succeeded) {
			this.buttons.add(button);
		}
	}
	
	private TagRow addTagRow() {
		TagRow newrow = (new TagRow(getContext()));
		this.rows.add(newrow);
		this.addView(newrow);
		return newrow;
	}
	
	private class TagRow extends LinearLayout {
		private int spaceLeft;
		public TagRow(Context context) {
			super(context);
			this.setOrientation(HORIZONTAL);
			this.spaceLeft = 2;	//FIXME replace with screen width based measure
		}
		public boolean offer(TagCheckBox button) {
			if(this.spaceLeft < 1) {
				return false;
			}
			this.addView(button);
			this.spaceLeft--;	//FIXME debug workaround
			//The problem is: can't use getWidth or getMeasuredWidth, as nothing is layouted yet
			return true;
		}
	}
	
	private class TagCheckBox extends CheckBox {
		public TagCheckBox(Context context, String tag) {
			super(context);
			this.setBackgroundResource(R.drawable.tagcheckbox);
			this.setTextColor(Color.BLACK);
			this.setTextSize(12);
			this.setSingleLine(true);
			this.setEllipsize(TruncateAt.END);
			this.setText(tag);
			this.setHeight(60);
			this.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton button, boolean isChecked) {
					if(isChecked) {
						//FIXME define colors
						button.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF00FF00));
					} else {
						button.getBackground().setColorFilter(null);
					}
				}
			});
		}
	}
}
