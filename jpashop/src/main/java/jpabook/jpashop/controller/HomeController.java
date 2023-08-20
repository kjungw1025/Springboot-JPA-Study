package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
// slf4j를 통해서 로그를 기록함
@Slf4j
public class HomeController {

//    아래 방법 대신, slf4j를 통해서 직접 기록
//    Logger log = LoggerFactory.getLogger(getClass());
    @RequestMapping("/")
    public String home() {
        log.info("home controller");
        return "home";  // home.html로 찾아감
    }
}
