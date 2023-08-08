package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Repository // Repsotiroy 어노테이션을 사용하면, 컴포넌트 스캔에 의해서 자동으로 스프링 빈으로 관리가 됨
public class MemberRepository {

    @PersistenceContext // JPA가 제공하는 표준 어노테이션
    private EntityManager em;   // Spring이 Entity manager를 만들어서 주입하게 해줌
    
    public void save(Member member) {
        em.persist(member); // JPA가 persist를 통해 저장함
    }
    
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        // em.createQuery -> JPQL 작성
        List<Member> result = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        // Ctrl + Alt + N으로 코드 줄이기
        // return em.createQuery("select m from Member m", Member.class)
        //        .getResultList();
        return result; 
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name) // :name -> 파라미터를 바인딩함
                .getResultList();
    }
}
