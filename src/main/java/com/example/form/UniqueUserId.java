package com.example.form;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

//実行時に保持されることを指定している
@Retention(RetentionPolicy.RUNTIME)
//バリデーション制約としてUniqueUserIdValidator.classが使用されることを指定している
@Constraint(validatedBy = UniqueUserIdValidator.class)
//カスタムアノテーションの指定
public @interface UniqueUserId {
	//エラー時に表示されるメッセージ
    String message() default "ユーザーIDが重複しています";
    //バリデーショングループを指定するための属性(defaultに指定)
    Class<?>[] groups() default {};
    //カスタムのペイロードオブジェクトを指定するための属性(バリデーションメッセージのカスタムなど)
    Class<? extends Payload>[] payload() default {};
}
