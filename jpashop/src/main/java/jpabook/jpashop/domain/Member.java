package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue // 시퀀스 값 사용
    @Column(name = "member_id") // @Column을 통해 id 이름을 member_id로 수정
    private Long id;
    private String name;
    
    @Embedded // 내장 타입을 포함했다는 어노테이션으로 매핑
    // 내장 타입을 쓸 때는 @Embedded나 @Embeddable로 둘 중에 하나만 있어도 되나, 보통은 두 개다 해줌
    private Address address;

    @OneToMany(mappedBy = "member") // 하나의 회원이 여러 개의 상품을 주문할 수 있기 때문에 일대다
    // 연관 관계의 주인이 아니므로 mappedBy = member : order 테이블 위에 있는 member 필드에 의해서 매핑된 것이라는 의미, "읽기 전용이 됨"
    private List<Order> orders = new ArrayList<>();
}
