package tools.android.pushnotify;

public interface RoyalNotificationProvider<T extends RoyalNotificationItem>
		extends NotificationProvider<T> {

	void clearRoyalNotifications(String royalIdentify);

}
