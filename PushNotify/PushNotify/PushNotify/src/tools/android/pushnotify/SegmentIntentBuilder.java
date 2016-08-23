package tools.android.pushnotify;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Same with com.android.overlay.entity.SegmentIntentBuilder(final: DO NOT
 * MODIFY)
 * 
 * @author liu_chonghui
 */
public class SegmentIntentBuilder<T extends SegmentIntentBuilder<?>> extends
		BaseIntentBuilder<T> {

	private final HashMap<String, String> segments;

	public SegmentIntentBuilder(Context context, ComponentName component) {
		super(context, component);
		segments = new HashMap<String, String>();
	}

	protected int getSegmentCount() {
		return segments.size();
	}

	@SuppressWarnings("unchecked")
	public T addSegment(String segment, String value) {
		segments.put(segment, value);
		return (T) this;
	}

	protected void preBuild() {
		// TODO
	}

	@Override
	public Intent build() {
		preBuild();
		Intent intent = super.build();
		Bundle bundle = new Bundle();
		Iterator<?> iter = segments.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
			String key = entry.getKey();
			String value = entry.getValue();
			bundle.putString(key, value);
		}
		intent.putExtras(bundle);
		return intent;
	}

	public static String getSegment(Intent intent, String key) {
		String extraString = null;
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			extraString = bundle.getString(key);
		}
		return extraString;
	}

}