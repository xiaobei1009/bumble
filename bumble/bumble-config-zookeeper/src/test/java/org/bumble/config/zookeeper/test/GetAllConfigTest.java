package org.bumble.config.zookeeper.test;

import org.bumble.base.test.BumbleTest;
import org.bumble.config.Configurator;
import org.bumble.config.ConfiguratorFactory;

public class GetAllConfigTest extends BumbleTest {
	public static void main( String[] args )
    {
		Configurator configurator = ConfiguratorFactory.getConfigurator();
        String allConfig = configurator.getAllConfig();
        logger.info(allConfig);
    }

	@Override
	public void template(String[] args) throws Exception {
		
	}
}
