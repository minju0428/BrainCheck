package com.example.braincheck.controller;

import com.example.braincheck.dto.PredictionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
//HTML 페이지(View)를 반환하는 데 사용 어노테이션
@Controller
public class PredictionController {

    //html과 연결하는 메서드
    @GetMapping("/mbti-ui")
    public String prediction(Model model) {

        Map<String, String> validationMap = (Map<String, String>) model.asMap().get("validationMap");

        //characterController에서 데이터가 잘 넘어왔는지 확인하는 로그
        if (validationMap != null && !validationMap.isEmpty()) {
            log.info("Flash Attribute 'validationMap' 수신 성공. 항목 수: {}", validationMap.size());
            log.info("수신된 Map 데이터: {}", validationMap);

            model.addAttribute("title", validationMap.get("제목"));
            model.addAttribute("characterName", validationMap.get("등장인물"));
            model.addAttribute("category", validationMap.get("카테고리"));
        } else {
            log.warn("Flash Attribute 'validationMap'을 찾을 수 없습니다. (직접 URL 접근 등의 경우)");
        }
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
    @ResponseBody //이 어노테이션이 Map을 JSON으로 변환하여 HTTP 응답 본문에 작성하게 함.
    public Map<String, String> userMbtiSubmit(@RequestBody PredictionDto predictionDto)  {
        //@RequestBody  : 전송된 JSON이 PredictionDto 객체로 자동 반환

        //자바 코드에서 개별 필드에 직접 접근이 가능함.
        String title = predictionDto.getTitle();
        String characterName = predictionDto.getCharacterName();
        String category = predictionDto.getCategory();
        String aiThing = predictionDto.getAiThing();

        String ei = predictionDto.getEi();
        String sn = predictionDto.getSn();
        String tf = predictionDto.getTf();
        String jp = predictionDto.getJp();
        //전체 mbti
        String fullMbti = ei + sn +tf + jp;

        //데이터 확인 로그
        System.out.println("=== 서버 수신 데이터 확인 ===");
        System.out.println("선택한 MBTI: " + fullMbti);
        System.out.println("제목: " + title);
        System.out.println("등장인물: " + characterName);
        System.out.println("카테고리: " + category);
        System.out.println("AiThing: " + aiThing);
        System.out.println("E/I (ei): " + ei);
        System.out.println("S/N (sn): " + sn);
        System.out.println("T/F (tf): " + tf);
        System.out.println("J/P (jp): " + jp);
        System.out.println("선택한 MBTI: " + fullMbti);

        String nextUrl = String.format("/user-selected-mbti?ei=%s&sn=%s&tf=%s&jp=%s&title=%s&characterName=%s&category=%s",
                ei, sn, tf, jp,
                title, characterName, category);

        // 클라이언트(JavaScript)에게 보낼 JSON 응답을 Map으로 만듭니다.
        Map<String, String> response = new HashMap<>();
        response.put("fullMbti", fullMbti);
        response.put("nextUrl", nextUrl); // 💡 클라이언트가 이동할 URL 정보 제공

        // Map을 JSON 형태로 반환
        return response;


    }

    //컨트롤러의 데이터 수신 메서드
    @GetMapping("/user-selected-mbti")
    //@RequestParam : url의 쿼리 파라미터 값을 변수로 받음.
    public String userSelectedMbti(@RequestParam("ei") String ei,
                                   @RequestParam("sn") String sn,
                                   @RequestParam("tf") String tf,
                                   @RequestParam("jp") String jp,
                                   @RequestParam("title") String title,
                                   @RequestParam("characterName") String characterName,
                                   @RequestParam("category") String category,
                                   @RequestParam("aiThing")  String aiThing,
                                   Model model) { //html로 데이터 전달을 하기 위한 객체 생성
        String fullMbti = ei + sn +tf + jp;

        // 리다이렉션 후 수신된 데이터 확인
        System.out.println("=== 쿼리 파라미터 수신 데이터 확인 (userSelectedMbti) ===");
        System.out.println("제목 (수신): " + title);
        System.out.println("등장인물 (수신): " + characterName);
        System.out.println("카테고리 (수신): " + category);
        System.out.println("AiThing (수신): " + aiThing);
        System.out.println("E/I (ei): " + ei);
        System.out.println("S/N (sn): " + sn);
        System.out.println("T/F (tf): " + tf);
        System.out.println("J/P (jp): " + jp);
        System.out.println("최종 MBTI: " + fullMbti);

        model.addAttribute("title", title);
        model.addAttribute("characterName", characterName);
        model.addAttribute("category", category);
        model.addAttribute("aiThing", aiThing);
        model.addAttribute("fullMbti", fullMbti);
        model.addAttribute("ei", ei);
        model.addAttribute("sn", sn);
        model.addAttribute("tf", tf);
        model.addAttribute("jp", jp);

        //다음 이동할 화면 이름을 적으면 됨.
        //다음 화면 구현시 바꾸기
        return "HomeActivity";



    }




}
