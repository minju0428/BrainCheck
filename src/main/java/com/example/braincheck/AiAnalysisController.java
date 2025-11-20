package com.example.braincheck;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AiAnalysisController {

    //AIAnalysisActivity.html 화면 진입 경로 메소드
    @GetMapping("/ai-analysis")
    public String aiAnalysis() {
        return "AIAnalsisActivity";
    }
}
