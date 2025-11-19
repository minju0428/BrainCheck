package com.example.braincheck.controller;

import com.example.braincheck.dto.PredictionDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

//HTML 페이지(View)를 반환하는 데 사용 어노테이션
@Controller
public class PredictionController {

    //html과 연결하는 메서드
    @GetMapping("/prediction")
    public String prediction() {

        //mbtiPredictionActivity.html 연결
        return "mbtiPredictionActivity";
    }
    /**1.(Map/JSON) 방식
     * 화면 이동을 자바스크립트가 담당함.
     * JSON 데이터로 응답*/

    /**2. (String/Redirect) 방식
     * 서버가 데이터를 보낼 때 화면 이동을 직접 담당함.]
     * 리다이렉트 명령("redirect:/경로?파라미터")으로 응답**/

    //MBTI 데이터 저장 메서드
    @PostMapping("/api/mbti/submit")
    public String userMbtiSubmit(@RequestBody PredictionDto predictionDto)  {
        //@RequestBody  : 전송된 JSON이 PredictionDto 객체로 자동 반환

        //자바 코드에서 개별 필드에 직접 접근이 가능함.
        String ei = predictionDto.getEi();
        String sn = predictionDto.getSn();
        String tf = predictionDto.getTf();
        String jp = predictionDto.getJp();
        //전체 mbti
        String fullMbti = ei + sn +tf + jp;

        //데이터 확인 로그
        System.out.println("=== 서버 수신 데이터 확인 ===");
        System.out.println("E/I (ei): " + ei);
        System.out.println("S/N (sn): " + sn);
        System.out.println("T/F (tf): " + tf);
        System.out.println("J/P (jp): " + jp);
        System.out.println("선택한 MBTI: " + fullMbti);

        String userSelectedMbtiRadirectUrl = String.format("radirect:/user-selected-mbti?ei=%s&sn=%s&tf=%s&jp=%s", ei, sn, tf, jp);

        return userSelectedMbtiRadirectUrl;


    }

    //컨트롤러의 데이터 수신 메서드
    @GetMapping("/user-selected-mbti")
    //@RequestParam : url의 쿼리 파라미터 값을 변수로 받음.
    public String userSelectedMbti(@RequestParam("ei") String ei,
                                   @RequestParam("sn") String sn,
                                   @RequestParam("tf") String tf,
                                   @RequestParam("jp") String jp,
                                   Model model) { //html로 데이터 전달을 하기 위한 객체 생성
        String fullMbti = ei + sn +tf + jp;

        model.addAttribute("fullMbti", fullMbti);
        model.addAttribute("ei", ei);
        model.addAttribute("sn", sn);
        model.addAttribute("tf", tf);
        model.addAttribute("jp", jp);

        //다음 이동할 화면 이름을 적으면 됨.
        //return "userSelectedMbti";



    }




}
