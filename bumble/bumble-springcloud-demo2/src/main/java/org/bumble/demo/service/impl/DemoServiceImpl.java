package org.bumble.demo.service.impl;

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
	
	@Override
	@Transactional
	@BumbleTxn
	public String newUser() {
		MyUser user = new MyUser();
		user.setComment("created by demo2");
		myUserMapper.insert(user);
		//int a = 1 / 0;
		String ret = "new user created by demo2 service";
		return ret;
	}
	
	@Override
	@Transactional
	@BumbleTxn
	public String newUserWithErr() {
		MyUser user = new MyUser();
		user.setComment("created by demo2");
		myUserMapper.insert(user);
		int a = 1 / 0;
		logger.debug(String.valueOf(a));
		String ret = "new user created by demo2 service";
		return ret;
	}
}
