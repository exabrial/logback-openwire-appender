package com.github.exabrial.logback;

import ch.qos.logback.core.OutputStreamAppender;

public class ActiveMQAppender<E> extends OutputStreamAppender<E> {
	private String brokerUrl = null;
	private String queueName = "ch.qos.logback";

	@Override
	public void start() {
		if (isStarted()) {
			return;
		} else {
			try {
				setOutputStream(new ActiveMQTextMessageOutputStream(brokerUrl, queueName));
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

	@Override
	public String toString() {
		return "ActiveMQAppender [brokerUrl=" + brokerUrl + ", queueName=" + queueName + "]";
	}
}
