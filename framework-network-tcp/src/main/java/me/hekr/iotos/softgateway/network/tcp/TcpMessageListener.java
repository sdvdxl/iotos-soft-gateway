package me.hekr.iotos.softgateway.network.tcp;

import me.hekr.iotos.softgateway.network.common.MessageListener;

/** @author iotos */
public interface TcpMessageListener<T> extends MessageListener<TcpServerPacketContext<T>> {}
