package me.hekr.iotos.softgateway.network.common.client;

import me.hekr.iotos.softgateway.network.common.listener.MessageListener;
import me.hekr.iotos.softgateway.network.common.ConnectionContext;

/** @author iotos */
public interface CommonMessageListener<T> extends MessageListener<ConnectionContext<T>> {}
