package tools.android.pushnotify;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Base provider for the notifications to be displayed.
 * 
 * Same with com.android.overlay.notification.BaseNotificationProvider (final:DO
 * NOT MODIFY)
 * 
 * @author liu_chonghui
 */
public class BaseNotificationProvider<T extends NotificationItem> implements
		NotificationProvider<T> {

	protected final Collection<T> items;
	private final int icon;
	private boolean canClearNotifications;
	private boolean autoCancel;

	public BaseNotificationProvider(int icon) {
		super();
		this.items = new ArrayList<T>();
		this.icon = icon;
		canClearNotifications = true;
		autoCancel = true;
	}

	public void add(Context context, T item, Boolean notify) {
		boolean exists = items.remove(item);
		if (notify == null) {
			notify = !exists;
		}
		items.add(item);
		NotificationManager.getInstance().addNotifications(context, this,
				notify ? item : null);
	}

	public boolean remove(Context context, T item) {
		boolean result = items.remove(item);
		if (result) {
			NotificationManager.getInstance().removeNotifications(context, this, item);
		}
		return result;
	}

	public void setCanClearNotifications(boolean canClearNotifications) {
		this.canClearNotifications = canClearNotifications;
	}

	@Override
	public Collection<T> getNotifications() {
		return Collections.unmodifiableCollection(items);
	}

	@Override
	public boolean canClearNotifications() {
		return canClearNotifications;
	}

	@Override
	public boolean autoCancel() {
		return autoCancel;
	}

	@Override
	public void clearNotifications() {
		items.clear();
	}

	@Override
	public boolean eventsVibro() {
		return false;
	}

	@Override
	public Uri getSound() {
		return Settings.System.DEFAULT_NOTIFICATION_URI;
	}

	@Override
	public int getStreamType() {
		return AudioManager.STREAM_NOTIFICATION;
	}

	@Override
	public boolean eventsLightning() {
		return false;
	}

	@Override
	public int getIcon() {
		return icon;
	}

	@Override
	public Intent getIntent(Context context, NotificationItem item) {
		return null;
	}

}
