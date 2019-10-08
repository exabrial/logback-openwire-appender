package com.github.exabrial.logback;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ActiveMQTextMessageOutputStream extends OutputStream {
	private final String brokerUrl;
	private String username;
	private String password;
	private final String queueName;
	private String state = "uninitialized";
	private Writer writer;
	private Connection connection;
	private Queue queue;
	private MessageProducer producer;
	private Session session;

	public ActiveMQTextMessageOutputStream(String brokerUrl, String username, String password, String queueName) {
		this.brokerUrl = brokerUrl;
		this.username = username;
		this.password = password;
		this.queueName = queueName;
		try {
			ActiveMQConnectionFactory connectionFactory;
			if (username != null && !"".equals(username)) {
				connectionFactory = new ActiveMQConnectionFactory(username, password, brokerUrl);
			} else {
				connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
			}
			connection = connectionFactory.createConnection();
			connection.start();
			connection.setExceptionListener(new ExceptionListener() {
				@Override
				public void onException(JMSException exception) {
					throw new RuntimeException(exception);
				}
			});
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			queue = session.createQueue(queueName);
			producer = session.createProducer(queue);
			state = "initialized";
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void write(int out) throws IOException {
		if (writer == null) {
			state = "writing";
			writer = new StringWriter();
		}
		writer.write(out);
	}

	@Override
	public void flush() throws IOException {
		state = "flushing";
		try {
			final String buffer = writer.toString();
			writer = null;
			TextMessage textMessage = session.createTextMessage(buffer);
			producer.send(textMessage);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		} finally {
			state = "initialized";
		}
	}

	@Override
	public void close() throws IOException {
		state = "closing";
		try {
			connection.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			session = null;
			connection = null;
			queue = null;
			producer = null;
			writer = null;
			state = "closed";
		}
	}

	@Override
	public String toString() {
		return "ActiveMQTextMessageOutputStream [state=" + state + ", queueName=" + queueName + ", brokerUrl=" + brokerUrl + "]";
	}
}
