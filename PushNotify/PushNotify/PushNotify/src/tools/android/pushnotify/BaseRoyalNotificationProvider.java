package tools.android.pushnotify;

import android.content.Context;
import android.content.Intent;

import java.util.Iterator;

public class BaseRoyalNotificationProvider<T extends RoyalNotificationItem>
		extends BaseNotificationProvider<T> implements
		RoyalNotificationProvider<T> {

	public BaseRoyalNotificationProvider(int icon) {
		super(icon);
	}

	@Override
	public void clearRoyalNotifications(String royalIdentify) {
		for (Iterator<T> iterator = items.iterator(); iterator.hasNext();) {
			if (royalIdentify != null
					&& royalIdentify.equals(iterator.next().getRoyalIdentify())) {
				iterator.remove();
			}
		}
	}

	public T get(String royalIdentify) {
		for (T item : items) {
			if (item == null || item.getRoyalIdentify() == null) {
				continue;
			}
			String identify = item.getRoyalIdentify();
			if (identify.equalsIgnoreCase(royalIdentify)) {
				return item;
			}
		}
		return null;
	}

	public boolean remove(Context context, String royalIdentify) {
		return remove(context, get(royalIdentify));
	}

	@Override
	public Intent getIntent(Context context, NotificationItem item) {
		throw new UnsupportedOperationException();
	}

}
