package com.github.exabrial.logback;

import ch.qos.logback.core.OutputStreamAppender;

public class ActiveMQAppender<E> extends OutputStreamAppender<E> {
	private String brokerUrl = null;
	private String queueName = "ch.qos.logback";
	private String username = null;
	private String password = null;

	@Override
	public void start() {
		if (isStarted()) {
			return;
		} else {
			try {
				setOutputStream(new ActiveMQTextMessageOutputStream(brokerUrl, username, password, queueName));
				super.start();
			} catch (Exception e) {
				addError("start() caught exception!", e);
			}
		}
	}

	public String getBrokerUrl() {
		return brokerUrl;
	}

	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "ActiveMQAppender [brokerUrl=" + brokerUrl + ", queueName=" + queueName + "]";
	}
}
