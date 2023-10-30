# Logback OpenWire Appender
Logback OpenWire Appender. Send your logs reliably using the ActiveMQ Client. Don't lose your logs!

This appender does not require Java EE to work, so it may be suitable for a Spring Boot or other standalone application.

While OpenWire is the native protocol for ActiveMQ, any valid ActiveMQ client URL can be used. This opens the possibility for an array of highly available, secured, and multi-protocol clients to be used.

If you use GrayLog, you can read messages off of ActiveMQ using this input: https://github.com/exabrial/graylog-plugin-openwire

As of 1.1.0, JDK 11 is required.

## Usage

Maven coordinates:

```
<dependency>
	<groupId>com.github.exabrial</groupId>
	<artifactId>logback-openwire-appender</artifactId>
	<version>1.1.2</version>
	<scope>runtime</scope>
</dependency>
```

You may use any standard Logback encoder, but if you are sending your messages to Graylog, I recommend using the GELF format for messages. A GELF encoder can be found in this project: https://github.com/osiegmar/logback-gelf

```
<dependency>
	<groupId>de.siegmar</groupId>
	<artifactId>logback-gelf</artifactId>
	<version>3.0.0</version>
	<scope>runtime</scope>
</dependency>
```


## Configuration

| Property Name      | Optional? | Example                                                                                                      | Purpose                                                                             |
|--------------------|-----------|--------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------|
| brokerUrl          | No        | failover:(ssl://activemq-1.example.com:61616,ssl://activemq-2.example.com:61616)?randomize=false&backup=true | The ActiveMQ client URL. Any valid ActiveMQ client URL can be used.                 |
| username           | Yes       | loguser                                                                                                      | The username to use to establish the connection to ActiveMQ                         |
| password           | Yes       | ${PASSWORD_FROM_ENVIRONMENT_VARIABLE}                                                                        | The password to use to establish the connection to ActiveMQ                         |
| queueName          | No        | ch.qos.logback                                                                                               | The Queue name to write logs to                                                     |

### Example logback.xml

Here's a working `logback.xml` that includes the previously mentioned GELF encoder:

```
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
	<contextName>${project.artifactId}</contextName>
	<jmxConfigurator />
	<appender
		name="gelf-jms"
		class="com.github.exabrial.logback.ActiveMQAppender">
		<queueName>ch.qos.logback</queueName>
		<encoder class="de.siegmar.logbackgelf.GelfEncoder">
			<includeCallerData>true</includeCallerData>
			<includeRootCauseData>true</includeRootCauseData>
			<includeLevelName>true</includeLevelName>
			<shortPatternLayout class="ch.qos.logback.classic.PatternLayout">
				<pattern>%.100ex{short}%.100m</pattern>
			</shortPatternLayout>
			<fullPatternLayout class="ch.qos.logback.classic.PatternLayout">
				<pattern>%msg</pattern>
			</fullPatternLayout>
			<staticField>app:my-app-name</staticField>
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
