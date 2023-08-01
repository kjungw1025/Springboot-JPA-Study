package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello (Model model) { // Model : Spring UI에 있는 Model에 데이터를 담아서 Controller에서 view에 넘길 수 있음
        model.addAttribute("data", "hello!!!");
        return "hello"; // hello.html에 넘김
    }
}
