package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded // 내장 타입으로 @Embedded 어노테이샨 작성
    private Address address;

    @Enumerated(EnumType.STRING)
    // 반드시 enum 타입 사용할 때 STRING으로 하기, ORDINAL은 중간에 다른 상태가 생기면 숫자가 밀려서 DB 조회시 오류 뜸
    private DeliveryStatus status; // READY, CAMP
}
