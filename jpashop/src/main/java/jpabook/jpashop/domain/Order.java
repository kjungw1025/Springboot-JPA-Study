package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // @Table을 통해 테이블명을 orders로 변경, orderby 때문에 order는 오류 발생할 수 있기 때문
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id") // 매핑을 어떤 걸로 한 것인지 (foreign key의 이름이 member_id로 됨)
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    // 하나의 주문은 하나의 배송 정보만 가져야 하고, 하나의 배송은 하나의 주문배송 정보만 가져야함 -> 일대일 관계 성립
    @JoinColumn(name = "delivery_id")
    // FK가 있는 Order를 연관 관계의 주인으로 설정
    private Delivery delivery;

    private LocalDateTime orderDate;    // 주문 시간

    @Enumerated(EnumType.STRING)
    // 반드시 enum 타입 사용할 때 STRING으로 하기, ORDINAL은 중간에 다른 상태가 생기면 숫자가 밀려서 DB 조회시 오류 뜸
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]
}
