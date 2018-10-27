# Logback OpenWire Appender
Logback OpenWire Appender. Send your logs reliably using the ActiveMQ Client. Don't lose your logs!

This appender does not require Java EE to work, so it may be suitable for a Spring Boot or other standalone application.

While OpenWire is the native protocol for ActiveMQ, any valid ActiveMQ client URL can be used. This opens the possibility for an array of highly available, secured, and multi-protocol clients to be used.

If you use GrayLog, you can read messages off of ActiveMQ using this input: https://github.com/exabrial/graylog-plugin-openwire

## Usage

Maven coordinates:

```
<dependency>
	<groupId>com.github.exabrial</groupId>
	<artifactId>logback-openwire-appender</artifactId>
	<version>1.0.0</version>
	<scope>runtime</scope>
</dependency>
```

You may use any standard Logback encoder, but if you are sending your messages to Graylog, I recommend using the GELF format for messages. A GELF encoder can be found in this project: https://github.com/Moocar/logback-gelf

```
<dependency>
  <groupId>me.moocar</groupId>
  <artifactId>logback-gelf</artifactId>
  <version>0.3</version>
  <scope>runtime</scope>
</dependency>
```


## Configuration

| Property Name      | Example                                                                                                      | Purpose                                                                             |
|--------------------|--------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------|
| brokerUrl          | failover:(ssl://activemq-1.example.com:61616,ssl://activemq-2.example.com:61616)?randomize=false&backup=true | The ActiveMQ client URL. Any valid ActiveMQ client URL can be used.                 |
| queueName          | ch.qos.logback                                                                                               | The Queue name to write logs to                                                 |


### Example logback.xml

Here's a working `logback.xml` that includes the previously mentioned GELF encoder:

```
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
	<contextName>app-artifact-id</contextName>
	<jmxConfigurator />
	<appender
		name="gelf-jms"
		class="com.github.exabrial.logback.ActiveMQAppender">
		<brokerUrl>failover:(ssl://activemq-1.example.com:61616,ssl://activemq-2.example.com:61616)?randomize=false&amp;backup=true</brokerUrl>
		<encoder class="ch.qos.logback.core.encoder.LayoutWrappingEncoder">
			<layout class="me.moocar.logbackgelf.GelfLayout">
				<useLoggerName>true</useLoggerName>
				<useThreadName>true</useThreadName>
				<includeFullMDC>true</includeFullMDC>
				<staticField class="me.moocar.logbackgelf.Field">
					<key>_app</key>
					<value>app-artifact-id</value>
				</staticField>
			</layout>
		</encoder>
	</appender>
	<root level="info">
		<appender-ref ref="gelf-jms" />
	</root>
	<logger
		name="com.example"
		level="debug" />
</configuration>
```
