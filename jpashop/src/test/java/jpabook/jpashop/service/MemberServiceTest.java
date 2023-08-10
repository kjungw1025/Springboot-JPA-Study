package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

// 순수한 단위 테스트로만 만들게 아니라, JPA가 DB까지 실제 작동하는 것을 보여주기 위해 완전히 Spring과 integration해서 테스트 진행
// --> RunWith, SpringBootTest
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional  // 데이터 변경이 필요하므로 사용. 롤백 기능 수행
// JPA에서 같은 Transactional 안에서 id 값이 똑같으면 같은 영속성 컨텍스트에서 똑같은 걸로 관리됨
public class MemberServiceTest {

    // 테스트 케이스에 참조할게 없으므로 아래와 같이 Autowired 설정
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;    // Test를 통해서 insert문이 나가는 걸 확인하기 위한 설정
    
    @Test
//    @Rollback(false) // 이렇게하면 롤백하지 않고 커밋하게 되어 insert문까지 테스트에서 사용하는 것을 확인할 수 있음
    public void 회원가입() throws Exception {
        //given (이렇게 주어졌을 때)
        Member member = new Member();
        member.setName("kim");

        //when (이렇게 하면)
        Long savedId = memberService.join(member);
        // join할 때 persist는 기본적으로 insert문이 안나감.
        // 데이터베이스 트랜잭션이 커밋될 때, JPA insert query가 나가는 것

        //then (이렇게 되어야 한다)
        em.flush(); // Test를 통해서 insert문이 나가는 걸 확인하기 위한 설정
        // import static org.junit.Assert.*;
        // Assert.assertEquals();
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        memberService.join(member2);
//        try {
//            memberService.join(member2);    // 여기서 예외가 발생해야 한다 (throw new IllegalStateException("이미 존재하는 회원입니다.");)
//        } catch (IllegalStateException e) {
//            return;
//        }
        // 위 try catch문 코드를 단축하는 방법 @Test(expected = IllegalStateException.class)

        //then
        // Assert.fail()
        // 위에서 코드가 돌다가 여기로 오면 안됨 (테스트 코드를 잘못 작성 했다는 뜻)
        fail("예외가 발생해야 한다.");
    }
}