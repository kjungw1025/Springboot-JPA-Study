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
}
