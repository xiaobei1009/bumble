package org.bumble.base.test;

import java.io.File;

public class DelFileTest {
	public static void main( String[] args ) throws Exception
    {
        System.out.println( "DelFileTest!" );
        
        File file = new File("D:\\t.tmp");
        
        file.delete();
    }
}
