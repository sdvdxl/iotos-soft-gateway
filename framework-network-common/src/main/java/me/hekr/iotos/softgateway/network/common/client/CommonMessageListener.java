package me.hekr.iotos.softgateway.network.common.client;

import me.hekr.iotos.softgateway.network.common.listener.MessageListener;
import me.hekr.iotos.softgateway.network.common.ConnectionContext;

/**
 * <p>CommonMessageListener interface.</p>
 *
 * @version $Id: $Id
 */
public interface CommonMessageListener<T> extends MessageListener<ConnectionContext<T>> {}
