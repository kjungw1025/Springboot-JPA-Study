package jpabook.jpashop.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입니다.") // name은 필수로 받고, 나머지는 선택, name이 없으면 오류 발생
    private String name;

    private String city;
    private String street;
    private String zipcode;

}
