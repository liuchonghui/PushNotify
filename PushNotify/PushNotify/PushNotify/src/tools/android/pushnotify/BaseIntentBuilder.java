package tools.android.pushnotify;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Same with com.android.overlay.entity.BaseIntentBuilder(final: DO NOT MODIFY)
 * 
 * @author liu_chonghui
 */
public class BaseIntentBuilder<T extends BaseIntentBuilder<?>> {

	private final Context context;
	private final ComponentName comp;

	public BaseIntentBuilder(Context ctx, ComponentName component) {
		super();
		this.context = ctx;
		this.comp = component;
	}

	public Intent build() {
		if (context != null && comp != null) {
			return createActivityInitValue(context, comp);
		} else {
			return new Intent();
		}
	}

	Intent createActivityInitValue(Context ctx,
								   ComponentName component) {
		if (ctx == null || component == null) {
			return null;
		}

		Intent intent = new Intent();
		intent.setComponent(component);
		try {
			if (ctx.getPackageManager().resolveActivity(intent,
					PackageManager.MATCH_DEFAULT_ONLY) == null) {
				if (ctx.getPackageManager().resolveService(intent,
						PackageManager.MATCH_DEFAULT_ONLY) == null) {
					intent = null;
				}
			}
		} catch (Exception e) {
			intent = null;
		}

		return intent;
	}
}