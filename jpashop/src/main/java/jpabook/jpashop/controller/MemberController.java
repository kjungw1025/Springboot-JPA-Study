package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm()); // controller에서 view로 넘어갈 때 데이터를 담아서 넘김, memberForm이라는 빈 객체를 가지고 감
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    // @Valid를 하면 MemberForm에 있는 jakarta validation 기능을 쓴다고 인지하여 @NotEmpty 같은 어노테이션을 validation 해줌
    // BindingResult : 스프링에서 제공, 앞에 @Valid 부분에서 오류가 있으면 원래는 controller에 안넘어오고 팅겨나가는데
    //                  BindingResult가 있으면 오류가 여기에 담겨서 실행이 됨
    public String create(@Valid MemberForm form, BindingResult result) {

        // BindingResult에 에러가 담겨있으면 createMemberForm에 다시 보내주는데,
        // 스프링이 thymeleaf가 모두 integration가 되어 있기도 하고 BindingResult를 해당 form에 가져와서 사용할 수 있게 도와줌
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";    // /주소로 넘어감
    }

    // 등록할 때(create)는 폼 객체를 썼는데, 뿌릴 때(List<Member>)는 Member Entity 그대로 출력하고 있음
    // 단순하게 Member 안 Entity들을 손댈게 없는 상황 이므로, 그대로 출력하는 형태로 썻지만, 실무에서는 DTO로 변환해서 화면에 꼭 필요한 데이터들만 가지고 출력하는 걸 권장
    // API를 만들 때는 이유를 불문하고 Entity를 외부로 반환해서는 안됨
    //      1. 코드가 그대로 노출될 수 있음 (패스워드)
    //      2. API 스펙이 변해버림 (Entity에 로직을 추가했는데, 그거 때문에 스펙이 변해서 불안전 스펙이 되버림)
    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
//        model.addAttribute("members", memberService.findMembers());
        return "members/memberList";
    }
}
