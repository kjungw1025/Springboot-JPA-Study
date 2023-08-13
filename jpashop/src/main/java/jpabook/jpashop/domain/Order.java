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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // 매핑을 어떤 걸로 한 것인지 (foreign key의 이름이 member_id로 됨)
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // 하나의 주문은 하나의 배송 정보만 가져야 하고, 하나의 배송은 하나의 주문배송 정보만 가져야함 -> 일대일 관계 성립
    @JoinColumn(name = "delivery_id")
    // FK가 있는 Order를 연관 관계의 주인으로 설정
    private Delivery delivery;

    private LocalDateTime orderDate;    // 주문 시간

    @Enumerated(EnumType.STRING)
    // 반드시 enum 타입 사용할 때 STRING으로 하기, ORDINAL은 중간에 다른 상태가 생기면 숫자가 밀려서 DB 조회시 오류 뜸
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]

    //=== 연관 관계 편의 메서드 ===//
    // 양방향일 때 원자적으로 한 코드로 해결함
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //=== 생성 메서드 ===//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER); // 처음에 ORDER로 설정함
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //=== 비즈니스 로직 ===//
    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //=== 조회 로직 ===//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        // 내가 주문한 전체 가격은 현재 나한테는 정보가 없고 OrderItem들을 다 더하면 됨

        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
            // getTotalPrice()에서 가져오는 이유
            // 주문할 때 orderItem에 orderPrice(주문 가격), count(주문 수량)이 있기 때문
        }
        return totalPrice;

//        for문이 아닌 다른 작성 방법 (Java8)
//        return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }
}
