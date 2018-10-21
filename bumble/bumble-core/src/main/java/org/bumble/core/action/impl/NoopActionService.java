package org.bumble.core.action.impl;

import java.nio.channels.SocketChannel;

import org.bumble.core.action.Action;
import org.bumble.core.action.ActionService;

public class NoopActionService implements ActionService {

	public void execute(Action action, SocketChannel channel, Object... param) {
		// Do nothing
	}

}
