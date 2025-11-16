package com.example.braincheck.controller;

import com.example.braincheck.dto.CharacterForm;
import com.example.braincheck.service.CharacterValidationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.management.MonitorInfo;

//입력 폼을 처리하는 컨트롤러 자바 파일
//MBTI 유형 선택하는 UI에 넘겨줄 값들
//다음 ui, java로 값을 넘겨줄 수 있게 하는 코드

//@controller : 해당 클래스가 웹 요청을 처리하는 컨트롤러 역할을 수행함을 Spring에게 알려주는 어노테이션
@Controller
@RequestMapping("/character")
public class CharacterController {


    @GetMapping("/input")
    public String showInput(Model model) {
        //사용자 입력을 받을 '데이터 그릇' 생성
        model.addAttribute("charactterForm", new CharacterForm());
        //입력 화면 반환
        return "InputActivity";
    }

    @PostMapping("/submit")
    public String submitForm(
            //@Valid : 유효성 검사 실행
            //@ModelAttribute : 데이터 바이딩(매핑) 및 모델 추가
            @Valid @ModelAttribute("characterForm") CharacterForm characterForm,

            //BindingResult : 스프링 프레임워크에 내장되어있는 인터페이스
            // 오류 발생 시 사용자가 제출했던 폼 화면으로 돌아갈 수 있게 해주는 인터페이스
            BindingResult bindingResult,

            //RedirectAttributes : 스프링 프레임워크에 내장되어있는 인터페이스
            //리다이렉트 시 데이터 전달
            //리다이렉트 시 다른 자바 파일로 안전하게 넘겨주기 위해 사용됨.
            RedirectAttributes redirectAttributes) {


            //검증 오류 발생 시 InputActivity.html 화면으로 다시 이동
            if(bindingResult.hasErrors()) {
                return "InputActivity";
            }

            //서비스를 통한 ai 검증 로직 부분
            String characterName = characterForm.getCharacterName().trim();
            String title = characterForm.getTitle().trim();
            CharacterValidationService characterValidationService = new CharacterValidationService();



    }



}
