package org.bumble.config.zookeeper.test;

import com.alibaba.fastjson.JSONObject;

public class TestJson {

    public static void main( String[] args )
    {
        System.out.println( "Test Hello World!" );
        JSONObject jsnObj = new JSONObject();
		
		jsnObj.put("aaa", "111");
		jsnObj.put("bbb", "222");
        String ret = jsnObj.toJSONString();
        System.out.println(ret);
    }
}
