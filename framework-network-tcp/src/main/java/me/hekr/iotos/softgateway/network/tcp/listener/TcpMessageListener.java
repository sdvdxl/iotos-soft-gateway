package me.hekr.iotos.softgateway.network.tcp.listener;

import me.hekr.iotos.softgateway.network.common.listener.MessageListener;
import me.hekr.iotos.softgateway.network.tcp.TcpServerPacketContext;

/** @author iotos */
public interface TcpMessageListener<T> extends MessageListener<TcpServerPacketContext<T>> {}
