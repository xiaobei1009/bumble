package org.bumble.registry.test;

import java.util.UUID;

import org.bumble.base.model.Node;
import org.bumble.base.model.URL;
import org.bumble.registry.data.MngrNode;
import org.bumble.registry.data.RegistryData;

public class TempTest {
	public static void main( String[] args )
    {
        System.out.println( "Temp Test!" );
        
        RegistryData rd = new RegistryData();
        
        MngrNode mngrNode = new MngrNode("m1", new URL("1.2.3:1234"));
        mngrNode.addClient(new Node("s1", new URL("111")));
        mngrNode.addClient(new Node("s2", new URL("222")));
        rd.addMngrNode(mngrNode);
        
        MngrNode mngrNode2 = new MngrNode("m2", new URL("1.2.3:5678"));
        mngrNode2.addClient(new Node("s3", new URL("333")));
        mngrNode2.addClient(new Node("s4", new URL("444")));
        rd.addMngrNode(mngrNode2);
        
        String ret = rd.toJsonString();
        System.out.println(ret);
        
		String uuid = UUID.randomUUID().toString();
		System.out.println(uuid.replaceAll("-", ""));
		
		RegistryData rd2 = new RegistryData();
        rd2.parseJsonString(ret);
        System.out.println(rd2.toJsonString());
    }
}
