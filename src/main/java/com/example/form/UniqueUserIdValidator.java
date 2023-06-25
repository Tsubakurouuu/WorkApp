package com.example.form;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.repository.MUserMapper;

public class UniqueUserIdValidator implements ConstraintValidator<UniqueUserId, String> {
	
	@Autowired
	private MUserMapper userMapper;
	
	@Override
	public boolean isValid(String userId, ConstraintValidatorContext context) {
		List<String> userIdList = userMapper.selectUserIdList();
		for(String existingUserId : userIdList) {
			if(existingUserId.equals(userId)) {
				return false;
			}
		}
		return true;
	}
}