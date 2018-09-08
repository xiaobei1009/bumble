package org.bumble.manager.test;

import org.bumble.core.action.Action;
import org.bumble.core.action.ActionService;
import org.bumble.core.action.ActionServiceFactory;

public class TestReflect {
	public static void main( String[] args ) throws Exception
    {
		
		ActionService actionService = (ActionService) Class.forName("org.bumble.core.action.impl.HeartReqClientActionService").newInstance();
		actionService.execute(null, null, null, null);
		
		ActionService as = ActionServiceFactory.getInstance().getService(new Action("heart-req-client"));
		as.execute(null, null, null, null);
		
    }
}
