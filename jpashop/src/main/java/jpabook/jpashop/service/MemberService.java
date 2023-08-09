package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// 여기에 @Transactional 쓰고, 아래 findMembers()와 findOne()위에 @Transactional(readOnly = true)로 작성해도 됨
// 또는, 읽기 전용 트랜잭션이 더 많으니 여기에 @Transactional(readOnly = true)를 쓰고 join() 위에 @Transactional 써도 됨
public class MemberService {

    // 필드 인젝션
//    @Autowired // MemberService는 MemberRepository를 사용하기 때문에 @Autowired를 통해서 스프링 빈에 등록 되어 있는 MemberRepository를 자동으로 인젝션
//    // 단점 : 테스트할 때 바꿔야하는데 바꿀 수 없음
    
    // 변경할 일이 없기 때문에 이 필드를 final로 설정하는걸 권장
    private final MemberRepository memberRepository;

    // Setter 인젝션
    // Ctrl + Shitf + A -> Setter 검색 -> Generate Setter (Setter 인젝션)
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        // 장점 : 몫 같은 걸 직접 주입해 줄 수 있음
//        // 단점 : 런타임 시점에 바꿀 수 있음 (치명적)
//        this.memberRepository = memberRepository;
//    }

    // 생성자(Constructor) 인젝션 (가장 선호) -> "여기서 한단계 더 나아가면 @RequiredArgsConstructor 사용함 : final이 있는 필드만 가지고 생성자를 만들어줌"
    // Ctrl + Shitf + A -> Constructor 검색 -> (생성자 인젝션)
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        // 한번 생성할 때 완성이 되기 때문에, 중간에 set해서 변경할 수 없음
        // 테스트 케이스를 작성할 때 Mock()이나 여러가지 주입을 직접 해줘야함
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 가입
     */
    // Member 객체를 join에 넘기는 방식으로 설계했음
    @Transactional  // JPA의 모든 데이터 변경이나 로직들은 가급적이면 트랜잭션 안에서 실행 되어야 함. 그래야 LAZY loading 이런거 다 됨
    // Transactional은 org.springframework.transaction.annotation.Transactional과 jakarta.transcation.Transactional 두 가지가 있음
    // 그러나 spring에서 제공하는 Transactional이 제공하는 옵션들이 더 많아서, 이걸로 쓰는걸 권장
    public Long join(Member member) {
        validateDuplicateMember(member);    // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //Exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    @Transactional(readOnly = true) // readOnly = true 옵션을 주면(읽기 전용 Transactional), JPA가 조회하는 곳에서는 성능을 최적화 해줌
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    
    // 회원 ID로 특정 회원 조회 (단건 조회)
    @Transactional(readOnly = true) // readOnly = true 옵션을 주면(읽기 전용 Transactional), JPA가 조회하는 곳에서는 성능을 최적화 해줌
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
