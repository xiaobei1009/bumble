package org.bumble.base.test;

import java.io.File;

public class FileTest {
	public static void main( String[] args ) throws Exception
    {
        System.out.println( "FileTest!" );
        
        File file = new File("C:\\opt\\B\\C");
        if (!file.exists()) {
        	System.out.println("not exists, create");
        	file.mkdirs();
        } else {
        	System.out.println("exists");
        }
        
    }
}
