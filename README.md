# Logback OpenWire Appender
Logback OpenWire Appender. Send your logs reliably using the ActiveMQ Client. Don't lose your logs!

While OpenWire is the native protocol for ActiveMQ, any valid ActiveMQ client URL can be used. This opens the possibility for an array of highly available, secured, and multi-protocol clients to be used.

If you use GrayLog, you can read messages off of ActiveMQ using this input: https://github.com/exabrial/graylog-plugin-openwire

## Usage

## Configuration

| Property Name      | Example                                                                                                      | Purpose                                                                             |
|--------------------|--------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------|
| brokerUrl          | failover:(ssl://activemq-1.example.com:61616,ssl://activemq-2.example.com:61616)?randomize=false&backup=true | The ActiveMQ client URL. Any valid ActiveMQ client URL can be used.                 |
| queueName          | ch.qos.logback                                                                                               | The Queue name to write logs to                                                 |


### Example logback.xml

```
```
