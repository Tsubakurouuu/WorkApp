package com.example.form;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.repository.MUserMapper;

public class UniqueUserIdValidator implements ConstraintValidator<UniqueUserId, String> {
	
	@Autowired
	private MUserMapper userMapper;
	
	//★userIdの重複チェックをするメソッド
	@Override
	public boolean isValid(String userId, ConstraintValidatorContext context) {
		//ユーザーのuserId一覧取得
		List<String> userIdList = userMapper.selectUserIdList();
		//取得したユーザーIDで繰り返し処理を行う
		for(String existingUserId : userIdList) {
			//もし同一のユーザーIDfが存在すればfalseを返しバリデーションメッセージを表示する
			if(existingUserId.equals(userId)) {
				return false;
			}
		}
		//同一のユーザーIDがなければtrueを返す
		return true;
	}
}