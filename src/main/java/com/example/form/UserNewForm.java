package com.example.form;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class UserNewForm {
	
	//ユーザーID
	@NotBlank(groups = ValidGroup1.class)
	@Length(min = 5, groups = ValidGroup2.class)
	@UniqueUserId(groups = ValidGroup1.class)
	private String userId;
	
	//パスワード
	@Pattern(regexp = "^[a-zA-Z0-9]+$", groups = ValidGroup1.class)
	@Length(min = 6, groups = ValidGroup2.class)
	private String password;
	
	//姓
	@NotBlank(groups = ValidGroup1.class)
	private String lastName;
	
	//名
	@NotBlank(groups = ValidGroup1.class)
	private String firstName;
	
	//生年月日
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull(groups = ValidGroup1.class)
	private Date birthday;
	
}
