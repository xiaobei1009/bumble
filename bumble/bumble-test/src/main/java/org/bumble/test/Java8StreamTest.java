package org.bumble.test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bumble.base.test.BumbleTest;

public class Java8StreamTest extends BumbleTest {

	@Override
	public void template(String[] args) throws Exception {
		
	}

	public static void main(String[] args) throws Exception {
		List<String> list = new ArrayList<String>();
		list.add("aa");
		list.add("bb");
		list.add("cc");
		
		list.forEach(e -> System.out.println(e));
		
		List<String> list2 = list.stream().map(e -> e.equals("bb") ? "xx" : e).collect(Collectors.toList());
		logger.info("{}", list2);
	}
}
