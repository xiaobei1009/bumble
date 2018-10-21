package org.bumble.manager.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bumble.base.test.BumbleTest;

public class ArrayListRemoveTest extends BumbleTest {
	public static void main( String[] args ) throws Exception
    {
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b1");
		list.add("b2");
		list.add("b3");
		list.add("c");
		list.add("d");
		
		try {
			for (String entry : list) {
				if (entry.equals("b1")) {
					list.remove(entry);
				}
			}
			logger.info(list.toString());	
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
		}
		
		try {
			for (int i = 0; i < list.size(); i++) {
				String entry = list.get(i);
				if (entry.equals("b2")) {
					list.remove(entry);
				}
			}
			logger.info(list.toString());	
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
		}
		
		try {
			for (Iterator<String> it = list.iterator(); it.hasNext();) {
		         String entry = it.next();
		         if (entry.equals("b3")){
		             it.remove();
		         }
		     }
			logger.info(list.toString());	
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.trace(e.getMessage(), e);
		}
    }

	@Override
	public void template(String[] args) throws Exception {
		
	}
}
