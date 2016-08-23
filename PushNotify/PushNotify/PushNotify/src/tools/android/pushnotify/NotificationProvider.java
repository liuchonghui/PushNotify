package tools.android.pushnotify;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.Collection;

/**
 * Same with com.android.overlay.notification.NotificationProvider(final: DO NOT
 * MODIFY)
 * 
 * @author liu_chonghui
 * 
 */
public interface NotificationProvider<T extends NotificationItem> {

	/**
	 * List of notifications.
	 */
	public Collection<T> getNotifications();

	/**
	 * Whether notification can be cleared.
	 */
	public boolean canClearNotifications();

	/**
	 * Whether notification can be auto cancel.
	 */
	public boolean autoCancel();

	/**
	 * Clear notifications.
	 */
	public void clearNotifications();

	/**
	 * vibrate
	 */
	public boolean eventsVibro();

	/**
	 * Sound for notification.
	 */
	public Uri getSound();

	/**
	 * Audio stream type for notification.
	 */
	public int getStreamType();

	/**
	 * lightning
	 */
	public boolean eventsLightning();

	/**
	 * Resource id for notification.
	 */
	public int getIcon();

	/**
	 * Intent for notification (If NotificationItem getIntent() == null).
	 */
	public Intent getIntent(Context context, NotificationItem item);

}
