package org.fourdnest.androidclient.ui;

import java.util.ArrayList;
import java.util.List;

import org.fourdnest.androidclient.R;
import org.fourdnest.androidclient.Tag;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
	private FlowLayout tagFlowLayout;
	private AutoCompleteTextView tagTextView;

	public TaggingTool(Context context, ViewGroup parent) {
		super(context);
		this.buttons = new ArrayList<TagCheckBox>();
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.taggingtool_layout, this, true);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, FIXME_DUMMYTAGS);
        this.tagTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_tag);
        this.tagFlowLayout = (FlowLayout) findViewById(R.id.tag_flowlayout);
      	tagTextView.setAdapter(adapter);

       	((Button) this.findViewById(R.id.add_tag_button))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				TaggingTool.this.addTag(
					new Tag(TaggingTool.this.tagTextView.getText()),
					true
				);
				TaggingTool.this.tagTextView.setText("");
			}
		});
      	
		this.setOrientation(VERTICAL);
		parent.addView(this);
		
		// DEBUG
		this.addTag(new Tag("Testing tagging tool"), false);
		this.addTag(new Tag("Llllllllllllllllllllllllllllllllllllong"), false);
		this.addTag(new Tag("Checked"), true);
		this.addTag(new Tag("Short"), false);
		this.addTag(new Tag("tags"), false);
	}
	
	public void addTag(Tag tag, boolean checked) {
		TagCheckBox button = new TagCheckBox(getContext(), tag);
		button.setChecked(checked);
		this.tagFlowLayout.addView(button);
		this.buttons.add(button);
	}

	public List<Tag> getCheckedTags() {
		List<Tag> out = new ArrayList<Tag>(this.buttons.size());
		for(TagCheckBox button : this.buttons) {
			if(button.isChecked()) {
				out.add(button.getTag());
			}
		}
		return out;
	}
	
	private class TagCheckBox extends CheckBox {
		private Tag tag;
		public TagCheckBox(Context context, Tag tag) {
			super(context);
			this.tag = tag;
			this.setBackgroundResource(R.drawable.tagcheckbox);
			this.setTextColor(Color.BLACK);
			this.setTextSize(12);
			this.setSingleLine(true);
			this.setEllipsize(TruncateAt.END);
			this.setText(tag.getName());
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
		public Tag getTag() {
			return this.tag;
		}
	}
}
