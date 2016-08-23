package com.mfashiongallery.emag.express.push.model;

import android.content.Intent;

import java.io.Serializable;

public abstract class Recommendation implements RoyalNotificationItem,
		Serializable {

	private static final long serialVersionUID = 1L;

	public Recommendation() {
		super();
	}

	public String uuid;
	public LaunchType rType;

	public void setRtype(LaunchType rtype) {
		this.rType = rtype;
	}

	public LaunchType getRtype() {
		return this.rType;
	}

	// EXTRA:
	public String title;
	public String display;
	public String mpsContent;

	public String type; // 1 常规；2 URL；3 一句话；
	public String messageId; // newsid
	public String url; // 跳转url
	public String content; // 内容
	public String time; // 发布时间
	public String column; // 0 (默认，同时对没有此标志位的兼容) 1 (跳至历史记录) 2 (直接跳至正文)
	public String channelId; // channelID 用来标识频道
	public String sound; // 提示音
	public String mark; // 后期加入

	public Recommendation(String uuid, String title, String display,
						  String mpsContent, String type, String messageId, String url,
						  String content, String time, String column, String channelId,
						  String sound, String mark) {
		flushData(uuid, title, display, mpsContent, type, messageId, url,
				content, time, column, channelId, sound, mark);
	}

	protected void flushData(String uuid, String title, String display,
			String mpsContent, String type, String messageId, String url,
			String content, String time, String column, String channelId,
			String sound, String mark) {
		this.uuid = uuid;
		this.title = title;
		this.display = display;
		this.mpsContent = mpsContent;
		this.type = type;
		this.messageId = messageId;
		this.url = url;
		this.content = content;
		this.time = time;
		this.column = column;
		this.channelId = channelId;
		this.sound = sound;
		this.mark = mark;
	}

	@Override
	public String getRoyalIdentify() {
		return getUuid();
	}

	@Override
	public String getRoyalTypeName() {
		if (getRtype() != null) {
			return getRtype().name();
		}
		return null;
	}
	
	@Override
	public String getRoyalMessages() {
		return this.messageId;
	}

	@Override
	public String getRoyalNotifyId() {
		return String.valueOf(getNotifyId());
	}
	
	@Override
	public String getRoyalMarkedId() {
		return this.mark == null ? "no_mark" : this.mark;
	}

	@Override
	public Intent getIntent() {
		return null;
	}

	@Override
	public String getText() {
		return this.content;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public String getIcon() {
		return this.display;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int notifyId;

	@Override
	public int getNotifyId() {
		return notifyId;
	}

	@Override
	public void setNotifyId(int id) {
		notifyId = id;
	}
}
