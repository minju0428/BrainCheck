package com.example.braincheck.controller;
//GeminiService.java 파일에 변수를 넘겨준 후 다시 받은 그 변수에 대한 유효성 검사는 하는 파일

import com.example.braincheck.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gemini")
public class GeminiController {
    private GeminiService geminiService;

    //생성자를 통한 의존성을 주입
    @Autowired
    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    /**
     * 등장인물 이름을 기반으로 캐릭터 상세 정보를 조회합니다.
     *
     * @param 변수명
     * @return 변수에 대한 상세 설명
     */


    /**
    @GetMapping("/해당주소")
    public 타입 함수이름 (@RequestParam 타입 변수명){
        try{
            //Service로 넘겨주는 부분
            String 넘겨줄 변수명1 = geminiService.서비스파일에서 지정한 함수이름(변수명.trim());

            //Service에서 반환된 값을 검사하는 부분
            //ResponseEntity.status() : HTTP 응답 전체(상태 코드, 헤더, 본문)를 표현하는 객체, 클라이언트에게 요청 처리 결과를 정확하게 하기 위해서

            if(넘겨줄 변수명1 .contains("오류가 발생했습니다")
                || 넘겨줄 변수명1 .contains("예상치 못한 오류")){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(넘겨줄 변수명1 );
            }
            return ResponseEntity.ok(넘겨줄 변수명1);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류가 발생했습니다 :" + e.getMessage());
        }
    }**/

}
