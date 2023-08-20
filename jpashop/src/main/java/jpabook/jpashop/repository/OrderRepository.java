package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAllNoDynamicQuery(OrderSearch orderSearch) {
        return em.createQuery("select o from Order o join o.member m" +
                "where o.status = :status" +
                "and o.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
//                .setFirstResult(100)    // 100부터 시작해서 가져온다는 의미. 아래와 합치면 100번부터 시작해서 총 1000개 가져온다는 의미
                .setMaxResults(1000)    // 나오는 결과 개수 제한 (ex. 최대 1000개)
                .getResultList();
        // @@@ 하지만 이렇게 작성하면 status = null이거나 name = null일 때 문제가 생길 수 있다 @@@
    }

    /**
     * JPQL로 처리하는 동적 쿼리 (번거롭고, 실수로 인한 버그가 발생할 수 있어 잘 안씀 -> MyBatis를 통해 이와 같은 동적 쿼리 문제를 해소할 수 있음)
     */
    public List<Order> findAllByString(OrderSearch orderSearch) {
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            }
            else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);
        
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    /**
     * JPA Criteria
     * 실무에서 잘 안씀 (유지보수가 힘듦)
     */
//    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
//        Root<Order> o = cq.from(Order.class);
//        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
//        List<Predicate> criteria = new ArrayList<>();
//
//        //주문 상태 검색
//        if (orderSearch.getOrderStatus() != null) {
//            Predicate status = cb.equal(o.get("status"),
//                    orderSearch.getOrderStatus());
//            criteria.add(status);
//        }
//        //회원 이름 검색
//        if (StringUtils.hasText(orderSearch.getMemberName())) {
//            Predicate name =
//                    cb.like(m.<String>get("name"), "%" +
//                            orderSearch.getMemberName() + "%");
//            criteria.add(name);
//        }
//
//        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
//        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
//        return query.getResultList();
//    }
    /**
     * Querydsl 추천!
     */
//    public List<Order> findAll(OrderSearch orderSearch) {
//        QOrder order = QOrder.order;
//        QMember member = QMember.member;
//
//        return query
//                .select(order)
//                .from(order)
//                .join(order.member, member)
//                .where(statusEq(orderSearch.getOrderStatus()),
//                        nameLike(orderSearch.getMemberName))
//                .limit(1000)
//                .fetch();
//    }
//
//    private BooleanExpression statusEq(OrderStatus statusCond) {
//        if (statusCond == null) {
//            return null;
//        }
//        return order.status.eq(statusCond);
//    }
//
//    private BooleanExpression nameLike(String nameCond) {
//        if (!StringUtils.hasText(nameCond)) {
//            return null;
//        }
//        return member.name.like(nameCond);
//    }
}
