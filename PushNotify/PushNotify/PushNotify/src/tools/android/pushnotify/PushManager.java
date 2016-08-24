package tools.android.pushnotify;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuchonghui on 16/7/6.
 */
public class PushManager {

    protected static PushManager instance;
    protected Map<String, BaseRoyalNotificationProvider<Recommendation>> providers = new HashMap<String, BaseRoyalNotificationProvider<Recommendation>>();

    public static PushManager getInstance() {
        if (null == instance) {
            synchronized (PushManager.class) {
                if (null == instance) {
                    instance = new PushManager();
                }
            }
        }
        return instance;
    }

    public static void setSingletonInstance(PushManager singleton) {
        synchronized (PushManager.class) {
            if (instance != null) {
                instance = null;
            }
            instance = singleton;
        }
    }

    protected PushManager() {
        // TODO
//        if (providers.get(LaunchType.TYPE_NEWS.name()) == null) {
//            providers.put(LaunchType.TYPE_NEWS.name(),
//                    new NewsNotificationProvider<Recommendation>());
//        }
//        for (BaseRoyalNotificationProvider<Recommendation> provider : providers
//                .values()) {
//            NotificationManager.getInstance().registerNotificationProvider(
//                    provider);
//        }
    }

    public void onNewPushData(final Context context, final Extra extra) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (extra == null) {
                    return;
                }
                LaunchType type = PushAssistant.getInstance().parseType(extra);
                if (type == null) {
                    return;
                }
                Recommendation recommendation = PushAssistant.getInstance()
                        .createRecommendationByPushData(type, extra);
                if (recommendation == null || recommendation.getRtype() == null) {
                    return;
                }
                type = recommendation.getRtype();

                BaseRoyalNotificationProvider<Recommendation> provider = getNotificationProvider(type
                        .name());
                if (provider != null) {
                    provider.add(context, recommendation, null);
                }
            }
        });
    }

    public BaseRoyalNotificationProvider<Recommendation> getNotificationProvider(
            String typeName) {
        if (typeName == null || typeName.length() == 0) {
            return null;
        }
        for (Map.Entry<String, BaseRoyalNotificationProvider<Recommendation>> entry : providers
                .entrySet()) {
            String royalTypeName = entry.getKey();
            if (typeName.equalsIgnoreCase(royalTypeName)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public Recommendation getRecommendation(String typeName,
                                            String royalIdentify) {
        BaseRoyalNotificationProvider<Recommendation> provider = getNotificationProvider(typeName);
        if (provider == null) {
            return null;
        }
        return provider.get(royalIdentify);
    }

    public void removeRecommendation(Context context, String notifyId) {
        int id = 0;
        try {
            id = Integer.valueOf(notifyId);
        } catch (Exception e) {
            e.printStackTrace();
            id = 0;
        }
        NotificationManager.getInstance().cancel(context, id);
    }

    public void removeRecommendation(Context context, String royalIdentify, String notifyId, String markedId) {
        int id = 0;
        try {
            id = Integer.valueOf(notifyId);
        } catch (Exception e) {
            e.printStackTrace();
            id = 0;
        }
        NotificationManager.getInstance().cancel(context, id);
    }

    public void removeRecommendation(Context context, Recommendation item) {
        if (item == null || item.getRtype() == null) {
            return;
        }
        BaseRoyalNotificationProvider<Recommendation> provider = getNotificationProvider(item
                .getRtype().name());
        if (provider != null) {
            provider.remove(context, item);
        }
    }
}
