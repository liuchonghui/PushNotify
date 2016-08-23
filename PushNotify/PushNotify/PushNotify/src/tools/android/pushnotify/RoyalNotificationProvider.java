package com.mfashiongallery.emag.express.push.model;

public interface RoyalNotificationProvider<T extends RoyalNotificationItem>
		extends NotificationProvider<T> {

	void clearRoyalNotifications(String royalIdentify);

}
