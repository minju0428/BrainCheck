package com.example.braincheck.controller;
//스프링 부트3 실행 시 메인 화면 열리게 하는 코드

import com.example.braincheck.dto.CharacterFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        //forward :  : 서버 내부에  다른 경로로 전달하는 접두사
        //해당 파일의 경로를 찾아줌.
        return "HomeActivity";
    }

}
