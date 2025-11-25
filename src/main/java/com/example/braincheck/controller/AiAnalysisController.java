package com.example.braincheck.controller;


import com.example.braincheck.service.AiAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.ObjectView;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/persudade")
public class AiAnalysisController {

    //service 주입을 위한 필드
    private final AiAnalysisService aiAnalysisService;

    //service 주입을 위한 생성자
    @Autowired
    public AiAnalysisController(AiAnalysisService aiAnalysisService) {
        this.aiAnalysisService = aiAnalysisService;
    }

    //AIAnalysisActivity.html 화면 진입 경로 메소드
    @GetMapping("/ai-analysis-result")
    public String aiAnalysis(
            // PredictionController의 userSelectedMbti와 동일한 파라미터 구조
            @RequestParam("ei") String ei,
            @RequestParam("sn") String sn,
            @RequestParam("tf") String tf,
            @RequestParam("jp") String jp,
            @RequestParam("title") String title,
            @RequestParam("characterName") String characterName,
            @RequestParam("category") String category,
            @RequestParam("aiThing")  String aiThing,
            @RequestParam("valueE") String valueE,
            @RequestParam("valueI") String valueI,
            @RequestParam("valueS") String valueS,
            @RequestParam("valueN") String valueN,
            @RequestParam("valueT") String valueT,
            @RequestParam("valueF") String valueF,
            @RequestParam("valueJ") String valueJ,
            @RequestParam("valueP") String valueP,
            Model model) { //html로 데이터 전달을 위한 Model 객체

        String fullMbti = ei + sn + tf + jp;

        Map<String, String> dataForService = new HashMap<>();
        dataForService.put("aiThing", aiThing);
        // Service에서 비교를 위해 사용자 선택 값 전달
        dataForService.put("ei", ei);
        dataForService.put("sn", sn);
        dataForService.put("tf", tf);
        dataForService.put("jp", jp);

        Map<String, Object> processedData = aiAnalysisService.processForPersuade(dataForService);


        String fullAiMbti = (String) processedData.get("aiEi") + (String) processedData.get("aiSn") + (String) processedData.get("aiTf") + (String) processedData.get("aiJp");

        boolean eiMismatch = (boolean) processedData.get("eiMismatch");
        boolean snMismatch = (boolean) processedData.get("snMismatch");
        boolean tfMismatch = (boolean) processedData.get("tfMismatch");
        boolean jpMismatch = (boolean) processedData.get("jpMismatch");

        log.info("=== AI 분석 결과 화면 수신 데이터 확인 ===");
        log.info("최종 MBTI: {}", fullMbti);
        log.info("제목: {}, 등장인물: {}, 카테고리: {}", title, characterName, category);

        System.out.println("AiThing (AI 분석 결과): " + aiThing);
        System.out.println("최종 MBTI: " + fullMbti);
        System.out.println("--- AI 도출 개별 지표 ---");
        System.out.println("AI E/I 유형: " + processedData.get("aiEi"));
        System.out.println("AI S/N 유형: " + processedData.get("aiSn"));
        System.out.println("AI T/F 유형: " + processedData.get("aiTf"));
        System.out.println("AI J/P 유형: " + processedData.get("aiJp"));
        System.out.println("--- MBTI 상세 값 ---");
        System.out.println("E (외향) 값: " + valueE);
        System.out.println("I (내향) 값: " + valueI);
        System.out.println("S (감각) 값: " + valueS);
        System.out.println("N (직관) 값: " + valueN);
        System.out.println("T (사고) 값: " + valueT);
        System.out.println("F (감정) 값: " + valueF);
        System.out.println("J (판단) 값: " + valueJ);
        System.out.println("P (인식) 값: " + valueP);
        System.out.println("--- MBTI 유형 선택 ---");
        System.out.println("E/I 유형: " + ei);
        System.out.println("S/N 유형: " + sn);
        System.out.println("T/F 유형: " + tf);
        System.out.println("J/P 유형: " + jp);


        // Model에 데이터 추가
        model.addAttribute("title", title);
        model.addAttribute("characterName", characterName);
        model.addAttribute("category", category);
        model.addAttribute("aiThing", aiThing);
        model.addAttribute("fullAiMbti", fullAiMbti);
        model.addAttribute("aiEi", processedData.get("aiEi"));
        model.addAttribute("aiSn", processedData.get("aiSn"));
        model.addAttribute("aiTf", processedData.get("aiTf"));
        model.addAttribute("aiJp", processedData.get("aiJp"));

        // E/I, S/N, T/F, J/P 각각의 값과 유형도 모두 전달
        model.addAttribute("valueE", valueE);
        model.addAttribute("valueI", valueI);
        model.addAttribute("valueS", valueS);
        model.addAttribute("valueN", valueN);
        model.addAttribute("valueT", valueT);
        model.addAttribute("valueF", valueF);
        model.addAttribute("valueJ", valueJ);
        model.addAttribute("valueP", valueP);
        model.addAttribute("ei", ei);
        model.addAttribute("sn", sn);
        model.addAttribute("tf", tf);
        model.addAttribute("jp", jp);

        //ai와 사용자 MBTI 불일치 정보
        model.addAttribute("eiMismatch", eiMismatch);
        model.addAttribute("snMismatch", snMismatch);
        model.addAttribute("tfMismatch", tfMismatch);
        model.addAttribute("jpMismatch", jpMismatch);


        return "AIAnalysisActivity";
    }

    @PostMapping("/transfer-to-persuade")
    public String transferToPersuade(
            // 뷰에서 전송된 모든 파라미터를 다시 받음.
            @RequestParam("title") String title,
            @RequestParam("characterName") String characterName,
            @RequestParam("category") String category,
            @RequestParam("aiThing") String aiThing,
            @RequestParam("fullMbti") String fullMbti,
            @RequestParam("valueE") String valueE,
            @RequestParam("valueI") String valueI,
            @RequestParam("valueS") String valueS,
            @RequestParam("valueN") String valueN,
            @RequestParam("valueT") String valueT,
            @RequestParam("valueF") String valueF,
            @RequestParam("valueJ") String valueJ,
            @RequestParam("valueP") String valueP,
            @RequestParam("ei") String ei,
            @RequestParam("sn") String sn,
            @RequestParam("tf") String tf,
            @RequestParam("jp") String jp,
            // 다음 페이지로 데이터를 안전하게 전달하기 위한 객체
            RedirectAttributes redirectAttributes) {

        log.info("=== 다음 단계 (설득) 데이터 전달 시작 ===");

        // 모든 데이터 저장함.
        Map<String, String> persuadeData = new HashMap<>();
        persuadeData.put("title", title);
        persuadeData.put("characterName", characterName);
        persuadeData.put("category", category);
        persuadeData.put("aiThing", aiThing);
        persuadeData.put("fullMbti", fullMbti);
        persuadeData.put("valueE", valueE);
        persuadeData.put("valueI", valueI);
        persuadeData.put("valueS", valueS);
        persuadeData.put("valueN", valueN);
        persuadeData.put("valueT", valueT);
        persuadeData.put("valueF", valueF);
        persuadeData.put("valueJ", valueJ);
        persuadeData.put("valueP", valueP);
        persuadeData.put("ei", ei);
        persuadeData.put("sn", sn);
        persuadeData.put("tf", tf);
        persuadeData.put("jp", jp);







        //service 계층 호출
        Map<String, Object> processedData = aiAnalysisService. processForPersuade(persuadeData);

        redirectAttributes.addAttribute("title", title);
        redirectAttributes.addAttribute("characterName", characterName);
        redirectAttributes.addAttribute("category", category);
        redirectAttributes.addAttribute("aiThing", aiThing);

        // 사용자 수치 정보
        redirectAttributes.addAttribute("valueE", valueE);
        redirectAttributes.addAttribute("valueI", valueI);
        redirectAttributes.addAttribute("valueS", valueS);
        redirectAttributes.addAttribute("valueN", valueN);
        redirectAttributes.addAttribute("valueT", valueT);
        redirectAttributes.addAttribute("valueF", valueF);
        redirectAttributes.addAttribute("valueJ", valueJ);
        redirectAttributes.addAttribute("valueP", valueP);

        // 사용자 MBTI 선택
        redirectAttributes.addAttribute("ei", ei);
        redirectAttributes.addAttribute("sn", sn);
        redirectAttributes.addAttribute("tf", tf);
        redirectAttributes.addAttribute("jp", jp);

        // AI 분석 결과
        // Object 타입이므로 String으로 캐스팅하여 전달
        redirectAttributes.addAttribute("aiEi", String.valueOf(processedData.get("aiEi")));
        redirectAttributes.addAttribute("aiSn", String.valueOf(processedData.get("aiSn")));
        redirectAttributes.addAttribute("aiTf", String.valueOf(processedData.get("aiTf")));
        redirectAttributes.addAttribute("aiJp", String.valueOf(processedData.get("aiJp")));


        redirectAttributes.addAttribute("eiMismatch", processedData.get("eiMismatch"));
        redirectAttributes.addAttribute("snMismatch", processedData.get("snMismatch"));
        redirectAttributes.addAttribute("tfMismatch", processedData.get("tfMismatch"));
        redirectAttributes.addAttribute("jpMismatch", processedData.get("jpMismatch"));

        redirectAttributes.addFlashAttribute("battleList", processedData.get("battleList"));
        redirectAttributes.addFlashAttribute("dimension", processedData.get("dimension"));


        redirectAttributes.addFlashAttribute("userHistoryList", processedData.get("userHistoryList"));
        redirectAttributes.addFlashAttribute("aiFeedbackList", processedData.get("aiFeedbackList"));
        log.info("Flash Attribute로 전달되는 설득 데이터 (분리된 AI 지표 포함): {}", processedData);

        // 설득 대결 시작 페이지로 리다이렉트 (예시 경로: /persuade/start)
        //다음 경로로 고치기
        //return "redirect:/persuade/start";
        return  "redirect:/";
    }
}
