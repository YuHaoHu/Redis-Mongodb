package com.example.demo.service;

import com.example.demo.domain.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
/*
*
 * @Author 胡玉浩
 * @Description //TODO 
 * @Date 9:18 2018/12/17
 * @Param 
 * @return 
 **/

public interface UserService {
	
	@Cacheable(value="users", key="'user_'+#id")
    User getUser(String id);
	
	@CacheEvict(value="users", key="'user_'+#id",condition="#id!=1")
	void deleteUser(String id);

	@CachePut(value="users")
	User putUser(String id);

	@Cacheable(value="users")
	User getUserNokey(String id);
	
	
}
