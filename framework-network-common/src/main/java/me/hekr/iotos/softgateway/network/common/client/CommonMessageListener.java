package me.hekr.iotos.softgateway.network.common.client;

import me.hekr.iotos.softgateway.network.common.listener.MessageListener;
import me.hekr.iotos.softgateway.network.common.PacketContext;

/** @author iotos */
public interface CommonMessageListener<T> extends MessageListener<PacketContext<T>> {}
