#!/bin/bash

config() {
  CONFIG_FILE="$FUSE_HOME/etc/$1.cfg"
}

property() {
  if [[ -n "$2" ]]; then
    echo "$1=$2" >> $CONFIG_FILE
  fi
}

start_fuse() {
  if [[ "$DEBUG" == "true" ]]; then
    $FUSE_HOME/bin/karaf debug
  else
    $FUSE_HOME/bin/karaf
  fi
}

# Remove activemq broker
rm $FUSE_HOME/etc/activemq.xml

config tools.devnull.boteco.channel.email
property income.enable $BOTECO_EMAIL_IN_ENABLE
property outcome.enable $BOTECO_EMAIL_OUT_ENABLE
property income.protocol $BOTECO_EMAIL_IN_PROTOCOL
property income.host $BOTECO_EMAIL_IN_HOST
property income.options $BOTECO_EMAIL_IN_OPTIONS
property outcome.protocol $BOTECO_EMAIL_OUT_PROTOCOL
property outcome.host $BOTECO_EMAIL_OUT_HOST
property outcome.options $BOTECO_EMAIL_OUT_OPTIONS
property outcome.from $BOTECO_EMAIL_OUT_FROM
property income.event.pattern $BOTECO_EMAIL_IN_EVENT_PATTERN
property trace $BOTECO_EMAIL_TRACE
property shutdown.time $BOTECO_EMAIL_SHUTDOWN_TIME

config tools.devnull.boteco.channel.irc
property income.enable $BOTECO_IRC_IN_ENABLE
property outcome.enable $BOTECO_IRC_OUT_ENABLE
property connection.check $BOTECO_IRC_CONNECTION_CHECK
property connection.check.interval $BOTECO_IRC_CONNECTION_CHECK_INTERVAL
property connection.check.trace $BOTECO_IRC_CONNECTION_CHECK_TRACE
property irc.nick $BOTECO_IRC_NICK
property irc.server $BOTECO_IRC_SERVER
property irc.port $BOTECO_IRC_PORT
property bot.command.prefix $BOTECO_IRC_COMMAND_PREFIX
property irc.options $BOTECO_IRC_OPTIONS
property trace $BOTECO_IRC_TRACE
property shutdown.time $BOTECO_IRC_SHUTDOWN_TIME

config tools.devnull.boteco.channel.pushover
property outcome.enable $BOTECO_PUSHOVER_ENABLE
property pushover.token $BOTECO_PUSHOVER_TOKEN
property trace $BOTECO_PUSHOVER_TRACE
property shutdown.time $BOTECO_PUSHOVER_SHUTDOWN_TIME

config tools.devnull.boteco.channel.telegram
property income.enable $BOTECO_TELEGRAM_IN_ENABLE
property outcome.enable $BOTECO_TELEGRAM_OUT_ENABLE
property bot.token $BOTECO_TELEGRAM_TOKEN
property bot.command.expression $BOTECO_TELEGRAM_COMMAND_EXPRESSION
property poll.interval $BOTECO_TELEGRAM_POLL_INTERVAL
property poll.initial-offset $BOTECO_TELEGRAM_POLL_OFFSET
property trace $BOTECO_TELEGRAM_TRACE
property shutdown.time $BOTECO_TELEGRAM_SHUTDOWN_TIME

config tools.devnull.boteco.channel.user
property delivery.consumers $BOTECO_USER_DELIVERY_CONSUMERS
property trace $BOTECO_USER_TRACE

config tools.devnull.boteco.client.jms
property jms.user $BOTECO_JMS_USER
property jms.password $BOTECO_JMS_PASSWORD
property jms.connectionUrl $BOTECO_JMS_URL

config tools.devnull.boteco.rest.api
property rest.path $BOTECO_REST_API_PATH

config tools.devnull.boteco.plugins.mongodb
property db.url $BOTECO_MONGODB_URL

config tools.devnull.boteco.persistence.request
property token.expire $BOTECO_REQUEST_TOKEN_EXPIRATION

config tools.devnull.boteco.plugins.redhat
property status.poll.interval $BOTECO_REDHAT_STATUS_POLL_INTERVAL
property status.poll.delay $BOTECO_REDHAT_STATUS_POLL_DELAY
property shutdown.time $BOTECO_REDHAT_SHUTDOWN_TIME

config tools.devnull.boteco.plugins.stocks
property query.defaults.exchange $BOTECO_STOCKS_DEFAULT_EXCHANGE

echo "org.apache.activemq.SERIALIZABLE_PACKAGES=*" >> $FUSE_HOME/etc/system.properties

start_fuse
