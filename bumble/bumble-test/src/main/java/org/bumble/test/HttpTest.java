package org.bumble.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.bumble.base.test.BumbleTest;
import org.bumble.utils.HttpUtil;

public class HttpTest extends BumbleTest {

	@Override
	public void template(String[] args) throws Exception {
		
	}
	
	public static void main(String[] args) throws Exception {
		String url = "https://sso.hikvision.com/serviceValidate";
		//String url = "https://www.baidu.com";
		
		Map<String, String> map = new HashMap<String, String>();
		String ret = HttpUtil.post(url, map, "UTF-8");
		logger.info(ret);
		
		URL ourl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) ourl.openConnection();
		
		final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line;
        final StringBuffer stringBuffer = new StringBuffer(255);

        synchronized (stringBuffer) {
            while ((line = in.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            String ret2 = stringBuffer.toString();
            logger.info(ret2);
        }
	}
}
