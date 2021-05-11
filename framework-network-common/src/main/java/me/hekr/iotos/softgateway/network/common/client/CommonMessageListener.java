package me.hekr.iotos.softgateway.network.common.client;

import me.hekr.iotos.softgateway.network.common.MessageListener;
import me.hekr.iotos.softgateway.network.common.PacketContext;

/** @author iotos */
public interface CommonMessageListener<T> extends MessageListener<PacketContext<T>> {}
