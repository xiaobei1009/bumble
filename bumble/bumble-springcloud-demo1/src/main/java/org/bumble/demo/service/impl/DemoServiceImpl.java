package org.bumble.demo.service.impl;

import org.bumble.demo.client.Demo2Client;
import org.bumble.demo.dao.MyUserMapper;
import org.bumble.demo.domain.MyUser;
import org.bumble.demo.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.bumble.client.BumbleTxn;

@Service("demoService")
public class DemoServiceImpl implements DemoService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MyUserMapper myUserMapper;
	
	@Autowired
    private Demo2Client demo2Client;
	
	@Override
	@Transactional
	@BumbleTxn
	public String hello() {
		
		MyUser user = new MyUser();
		user.setComment("created by demo1");
		
		myUserMapper.insert(user);
		
		String demo2ClientRet = demo2Client.hello();
		logger.info(demo2ClientRet);
		
		String ret = demo2ClientRet + 
			"<br>" +
			"hello msg from demo1 service";
		
		return ret;
	}

	@Override
	@Transactional
	@BumbleTxn
	public String helloWithErr() {
		
		MyUser user = new MyUser();
		user.setComment("created by demo1");
		
		myUserMapper.insert(user);
		
		String demo2ClientRet = demo2Client.helloWithErr();
		logger.info(demo2ClientRet);
		
		String ret = demo2ClientRet + 
			"<br>" +
			"hello msg from demo1 service";
		
		return ret;
	}
}
