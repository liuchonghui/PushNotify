package tools.android.pushnotify;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Same with com.android.overlay.manager.NotificationManager
 * 
 * Manage notifications about message, authentication and subscription.(final:DO
 * NOT MODIFY)
 * 
 * @author liu_chonghui
 */
public class NotificationManager {

	protected static final int BASE_NOTIFICATION_PROVIDER_ID = 0xdead101;

	protected ShowType mShowType = ShowType.MULTI;
	protected int mId = BASE_NOTIFICATION_PROVIDER_ID;

	protected final List<NotificationProvider<? extends NotificationItem>> providers = new ArrayList<NotificationProvider<? extends NotificationItem>>();

	private static NotificationManager instance;

	public static NotificationManager getInstance() {
		if (null == instance) {
			synchronized (NotificationManager.class) {
				if (null == instance) {
					instance = new NotificationManager();
				}
			}
		}
		return instance;
	}

	public static void setSingletonInstance(NotificationManager singleton) {
		synchronized (NotificationManager.class) {
			if (instance != null) {
				instance = null;
			}
			instance = singleton;
		}
	}

	protected NotificationManager() {
	}

	public void setShowType(ShowType type) {
		if (type == null) {
			return;
		}
		mShowType = type;
	}

	/**
	 * Register new provider for notifications.
	 * 
	 * @param provider
	 */
	public void registerNotificationProvider(
			NotificationProvider<? extends NotificationItem> provider) {
		int index = providers.indexOf(provider);
		if (index < 0) {
			providers.add(provider);
		}
	}

	/**
	 * Update notifications for specified provider.
	 * 
	 * @param <T>
	 * @param provider
	 * @param notify
	 *            Ticker to be shown. Can be <code>null</code>.
	 */
	public <T extends NotificationItem> void addNotifications(Context context,
			NotificationProvider<T> provider, T notify) {
		Log.d("NOTIFY", "addNotifications:");
		int id = providers.indexOf(provider);
		if (id == -1) {
			// throw new IllegalStateException(
			// "registerNotificationProvider() must be called from onLoaded() method.");
			Log.d("NOTIFY",
					"registerNotificationProvider() should be called from onLoaded() method.");
			return;
		}

		if (ShowType.SINGLE == mShowType) {
			id += BASE_NOTIFICATION_PROVIDER_ID;
			Log.d("NOTIFY", "ShowType=" + mShowType.name() + ", id:" + id);
			Iterator<? extends NotificationItem> iterator = provider
					.getNotifications().iterator();
			if (!iterator.hasNext()) {
				Log.d("NOTIFY", "NotificationManager.cancel:" + id);
				try {
					android.app.NotificationManager notificationManager;
					notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
					notificationManager.cancel(id);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				NotificationItem top;
				String ticker;
				if (notify == null) {
					top = iterator.next();
					ticker = null;
				} else {
					top = notify;
					ticker = top.getTitle();
				}
				Intent intent = top.getIntent();
				if (intent == null) {
					Log.d("NOTIFY",
							"NotificationItem.getIntent() == null try get from NotificationProvider!");
					try {
						intent = provider.getIntent(context, top);
					} catch (Exception e) {
						intent = null;
					}
				}
				if (intent == null) {
					Log.d("NOTIFY",
							"NotificationProvider.getIntent() == null exit!");
					return;
				}
				Log.d("NOTIFY", "ticker:" + ticker);
				Notification notification = null;
				Notification.Builder builder = null;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					builder = new Notification.Builder(
							context.getApplicationContext())
							.setTicker(ticker)
							.setWhen(System.currentTimeMillis());
					if (provider.getIcon() > 0) {
						builder.setSmallIcon(provider.getIcon());
					}
					if (top.getIcon() != null && top.getIcon().length() > 0) {
						builder.setLargeIcon(BitmapFactory.decodeFile(top.getIcon()));
					}
					builder.setContentTitle(top.getTitle()).setContentText(top.getText());
					notification = builder.getNotification();
					notification.contentIntent = PendingIntent.getActivity(
							context.getApplicationContext(), 0, intent,
							PendingIntent.FLAG_UPDATE_CURRENT);
				} else {
					notification = new Notification(provider.getIcon(), ticker,
							System.currentTimeMillis());
				    notification.setLatestEventInfo(context.getApplicationContext(), top.getTitle(),
							top.getText(),
							PendingIntent.getActivity(
							context.getApplicationContext(), 0, intent,
							PendingIntent.FLAG_UPDATE_CURRENT));
				}
				if (!provider.canClearNotifications()) {
					notification.flags |= Notification.FLAG_NO_CLEAR;
				}
				if (ticker != null) {
					setNotificationDefaults(notification,
							provider.eventsVibro(), provider.getSound(),
							provider.getStreamType(), provider.eventsLightning());
				}
				Log.d("NOTIFY", "notify:" + top.getTitle() + ", " + top.getText() + ", @" + id);
				notify(context, id, notification);
			}
		} else if (ShowType.MULTI == mShowType) {
			id = mId++;
			Log.d("NOTIFY", "ShowType=" + mShowType.name() + ", id:" + id);
			if (notify == null) {
				Log.d("NOTIFY", "ShowType=" + mShowType
						+ ", but notify == null!!!");
				return;
			}
			notify.setNotifyId(id);
			NotificationItem top;
			String ticker;
			top = notify;
			ticker = top.getTitle();
			Intent intent = top.getIntent();
			if (intent == null) {
				Log.d("NOTIFY",
						"NotificationItem.getIntent() == null try get from NotificationProvider!");
				try {
					intent = provider.getIntent(context, top);
				} catch (Exception e) {
					intent = null;
				}
			}
			if (intent == null) {
				Log.d("NOTIFY",
						"NotificationProvider.getIntent() == null exit!");
				return;
			}
			Log.d("NOTIFY", "ticker:" + ticker);
			Notification notification = null;
			Notification.Builder builder = null;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				builder = new Notification.Builder(
						context.getApplicationContext())
						.setTicker(ticker)
						.setWhen(System.currentTimeMillis());
				if (provider.getIcon() > 0) {
					builder.setSmallIcon(provider.getIcon());
				}
				if (top.getIcon() != null && top.getIcon().length() > 0) {
					builder.setLargeIcon(BitmapFactory.decodeFile(top.getIcon()));
				}
				builder.setContentTitle(top.getTitle()).setContentText(top.getText());
				notification = builder.getNotification();
				notification.contentIntent = PendingIntent.getActivity(
						context.getApplicationContext(), 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
			} else {
				notification = new Notification(provider.getIcon(), ticker,
						System.currentTimeMillis());
				notification.setLatestEventInfo(context.getApplicationContext(), top.getTitle(),
						top.getText(),
						PendingIntent.getActivity(
								context.getApplicationContext(), 0, intent,
								PendingIntent.FLAG_UPDATE_CURRENT));
			}
			if (!provider.canClearNotifications()) {
				notification.flags |= Notification.FLAG_NO_CLEAR;
			}
			if (provider.autoCancel()) {
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
			}
			Bundle bundle = intent.getExtras();
			String royalIdentify = bundle.getString("royalIdentify");
			String royalTypeName = bundle.getString("royalTypeName");
			String royalNotifyId = bundle.getString("royalNotifyId");
			String royalMarkedId = bundle.getString("royalMarkedId");
			Log.d("NOTIFY", "royalIdentify:" + royalIdentify +
					", royalTypeName:" + royalTypeName +
					", royalNotifyId:" + royalNotifyId +
					", royalMarkedId:" + royalMarkedId);
			if (ticker != null) {
				setNotificationDefaults(notification,
						provider.eventsVibro(), provider.getSound(),
						provider.getStreamType(), provider.eventsLightning());
			}
//			notification.deleteIntent = clearNotifications;
			Log.d("NOTIFY", "notify:" + top.getTitle() + ", " + top.getText() + ", @" + id);
			notify(context, id, notification);
		}

	}

	public <T extends NotificationItem> void removeNotifications(Context context,
			NotificationProvider<T> provider, T notify) {
		Log.d("NOTIFY", "removeNotifications:");
		if (provider.autoCancel()) {
			return;
		}
		int id = providers.indexOf(provider);
		if (id == -1) {
			// throw new IllegalStateException(
			// "registerNotificationProvider() must be called from onLoaded() method.");
			Log.d("NOTIFY",
					"registerNotificationProvider() should be called from onLoaded() method.");
			return;
		}
		if (ShowType.SINGLE == mShowType) {
			id += BASE_NOTIFICATION_PROVIDER_ID;
			Log.d("NOTIFY", "ShowType=" + mShowType.name() + ", id:" + id);
			Iterator<? extends NotificationItem> iterator = provider
					.getNotifications().iterator();
			if (!iterator.hasNext()) {
				Log.d("NOTIFY", "NotificationManager.cancel:" + id);
				try {
					android.app.NotificationManager notificationManager;
					notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
					notificationManager.cancel(id);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				NotificationItem top;
				String ticker;
				// if (notify == null) {
				top = iterator.next();
				ticker = null;
				// } else {
				// top = notify;
				// ticker = top.getTitle();
				// }
				Intent intent = top.getIntent();
				if (intent == null) {
					Log.d("NOTIFY",
							"NotificationItem.getIntent() == null try get from NotificationProvider!");
					try {
						intent = provider.getIntent(context, top);
					} catch (Exception e) {
						intent = null;
					}
				}
				if (intent == null) {
					Log.d("NOTIFY",
							"NotificationProvider.getIntent() == null exit!");
					return;
				}
				Log.d("NOTIFY", "ticker:" + ticker);
				Notification notification = null;
				Notification.Builder builder = null;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					builder = new Notification.Builder(
							context.getApplicationContext())
							.setTicker(ticker)
							.setWhen(System.currentTimeMillis());
					if (provider.getIcon() > 0) {
						builder.setSmallIcon(provider.getIcon());
					}
					if (top.getIcon() != null && top.getIcon().length() > 0) {
						builder.setLargeIcon(BitmapFactory.decodeFile(top.getIcon()));
					}
					builder.setContentTitle(top.getTitle()).setContentText(top.getText());
					notification = builder.getNotification();
					notification.contentIntent = PendingIntent.getActivity(
							context.getApplicationContext(), 0, intent,
							PendingIntent.FLAG_UPDATE_CURRENT);
				} else {
					notification = new Notification(provider.getIcon(), ticker,
							System.currentTimeMillis());
					notification.setLatestEventInfo(context.getApplicationContext(), top.getTitle(),
							top.getText(),
							PendingIntent.getActivity(
									context.getApplicationContext(), 0, intent,
									PendingIntent.FLAG_UPDATE_CURRENT));
				}
				if (!provider.canClearNotifications()) {
					notification.flags |= Notification.FLAG_NO_CLEAR;
				}
				// if (ticker != null) {
				// setNotificationDefaults(notification,
				// provider.eventsVibro(), provider.getSound(),
				// provider.getStreamType());
				// }
//				notification.deleteIntent = clearNotifications;
				Log.d("NOTIFY",
						"notify:" + top.getTitle() + ", " + top.getText());
				notify(context, id, notification);
			}
		} else if (ShowType.MULTI == mShowType) {
			Log.d("NOTIFY", "ShowType=" + mShowType.name() + ", id:" + id);
			if (notify == null) {
				Log.d("NOTIFY", "ShowType=" + mShowType
						+ ", but notify == null!!!");
				return;
			}
			id = notify.getNotifyId();
			Log.d("NOTIFY", "NotificationManager.cancel:" + id);
			try {
				android.app.NotificationManager notificationManager;
				notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.cancel(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void cancel(Context context, int id) {
		try {
			Log.d("NOTIFY", "NotificationManager.cancel(id):" + id);
			android.app.NotificationManager notificationManager;
			notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sound, vibration and lightning flags.
	 * 
	 * @param notification
	 * @param streamType
	 */
	private void setNotificationDefaults(Notification notification,
			boolean vibro, Uri sound, int streamType, boolean lightning) {
		notification.audioStreamType = streamType;
		notification.defaults = 0;
		notification.sound = sound;
		if (vibro) {
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		if (lightning) {
			notification.defaults |= Notification.DEFAULT_LIGHTS;
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		}
	}

	private void notify(Context context, int id, Notification notification) {
		try {
			android.app.NotificationManager notificationManager;
			notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(id, notification);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cancelAll(Context context) {
		try {
			android.app.NotificationManager notificationManager;
			notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancelAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
