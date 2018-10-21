package org.bumble.manager;

import org.bumble.base.Callback;
import org.bumble.base.config.LocalConfigConst;
import org.bumble.base.config.LocalConfigHolder;
import org.bumble.base.log.LogInitializer;
import org.bumble.core.BumbleLogoPrinter;
import org.bumble.core.ShutdownWorker;
import org.bumble.manager.remoting.RemotingTransporterFactory4BMngr;
import org.bumble.registry.RegistryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BumbleManager {
	
	private LogInitializer logInitializer = null;
	private Logger logger = null;
	private RemotingTransporterFactory4BMngr transporterFactory = null;
	
	private static BumbleManager instance = new BumbleManager();
	
	public static BumbleManager getInstance() {
		return instance;
	}
	
	public void init(String[] args) {
		// Override configuration
    	LocalConfigHolder.getInstance().overrideByArgs(args);
    	logInitializer = new LogInitializer();
    	
    	logInitializer.init();
    	
    	logger = LoggerFactory.getLogger(BumbleManager.class);
	}
	
	public void startup(String[] args) throws Exception {
		init(args);
		
    	logger.info("Bumble Manager is starting...\n");
		
    	transporterFactory = RemotingTransporterFactory4BMngr.getInstance();
    	
        // Start the server socket
        transporterFactory.start();
        
        // Add shutdown hook for exiting gracefully
        ShutdownWorker.work(transporterFactory);
        
        // Register this bumble manager to the registry
        RegistryFactory.getRegistry().register();
        
        // Print LOGO
        BumbleLogoPrinter.print(new Callback() {
			public Object doCallback(Object... params) {
				String logoStr = (String) params[0];
				String port = LocalConfigHolder.getInstance().getConfig(LocalConfigConst.BumbleManagerServer.PORT);
				logoStr = logoStr.replace("{{port}}", port);
				return logoStr;
			}
        });
        logger.info( "Bumble Manager is Started!" );
        
	}
	
	public void shutdown() throws Exception {
		logger.info("Bumble Manager is closing...\n");
		transporterFactory.shutdown();
		logger.info( "Bumble Manager is closed!" );
	}
}
