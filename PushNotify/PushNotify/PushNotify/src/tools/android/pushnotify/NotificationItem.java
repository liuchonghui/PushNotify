package tools.android.pushnotify;

import android.content.Intent;

/**
 * Same with com.android.overlay.notification.NotificationItem (final:DO NOT
 * MODIFY)
 * 
 * @author liu_chonghui
 * 
 */
public interface NotificationItem {

	/**
	 * Title for notification.
	 */
	public String getTitle();

	/**
	 * Show Text for notification.
	 */
	public String getText();

	/**
	 * Intent to launch activity.
	 */
	public Intent getIntent();
	
	public int getNotifyId();
	
	public void setNotifyId(int id);

	public String getIcon();

}
