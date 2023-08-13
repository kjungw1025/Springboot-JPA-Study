package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 도메인 모델 패턴

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 조회
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order); // Order만 세이브 해도 Order domain을 보면 cascade 때문에 orderitem이랑 delivery를 자동으로 persist 해줌
        // cascade의 범위? (어디까지를 cascade 해야하나)
        // order 같은 경우에 order가 orderitem, delivery를 관리하는 이 그림 정도 선에서만 사용해야함
        // 다른 것이 참조할 수 없는, private owner인 경우에만 사용해야 함 -> orderitem, delivery는 order에서만 참조해서 사용함
        // 예를들어, delivery가 다른 곳에서도 참조되어 쓰여진다면 cascade를 무분별하게 사용하면 안됨
        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {

        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        // 주문 취소
        order.cancel();
        // 일반적으로 DB sql을 직접 다루는 mybatis, jdbctemplate등은 this.setStatus(OrderStatus.CANCEL);과 같이 데이터가 변경되면
        // 직접 Update query를 작성해야 함.
        // 그러나 JPA를 활용하면, 데이터가 변경되면서 바뀐 변경 포인트들에 dirty checking(변경 내역 감지)이 발생하면서 DB에 Update query가 저절로 날라감
    }

    /**
     * 주문 검색
     */
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch);
//    }
}
