package tools.android.pushnotify;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class BaseRoyaltyIntentBuilder<T extends BaseRoyaltyIntentBuilder<?>>
		extends SegmentIntentBuilder<T> {

	private String royalIdentify;
	private String royalTypeName;
	private String royalNotifyId;
	private String royalMarkedId;
	private String royalMessages;

	public BaseRoyaltyIntentBuilder(Context context, ComponentName component) {
		super(context, component);
	}

	@SuppressWarnings("unchecked")
	public T setRoyalIdentify(String royalIdentify) {
		this.royalIdentify = royalIdentify;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setRoyalTypeName(String royalTypeName) {
		this.royalTypeName = royalTypeName;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setRoyalNotifyId(String royalNotifyId) {
		this.royalNotifyId = royalNotifyId;
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T setRoyalMarkedId(String royalMarkedId) {
		this.royalMarkedId = royalMarkedId;
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T setRoyalMessages(String royalMessages) {
		this.royalMessages = royalMessages;
		return (T) this;
	}

	@Override
	protected void preBuild() {
		super.preBuild();
		if (royalIdentify == null || royalTypeName == null) {
			return;
		}
		if (getSegmentCount() != 0) {
			throw new IllegalStateException();
		}
		addSegment("royalIdentify", royalIdentify);
		addSegment("royalTypeName", royalTypeName);
		addSegment("royalNotifyId", royalNotifyId);
		addSegment("royalMarkedId", royalMarkedId);
		if (royalMessages != null) {
			addSegment("royalMessages", royalMessages);
		}
	}

	public static String getRoyalIdentify(Intent intent) {
		return getSegment(intent, "royalIdentify");
	}

	public static String getRoyalTypeName(Intent intent) {
		return getSegment(intent, "royalTypeName");
	}

	public static String getRoyalNotifyId(Intent intent) {
		return getSegment(intent, "royalNotifyId");
	}
	
	public static String getRoyalMarkedId(Intent intent) {
		return getSegment(intent, "royalMarkedId");
	}
	
	public static String getRoyalMessages(Intent intent) {
		return getSegment(intent, "royalMessages");
	}
}