package com.example.braincheck.controller;
//처음 설득 페이지

import com.example.braincheck.service.PersuasionBattleRoundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/persuade")
public class PersuasionBattleController {

    // MBTI 설득 고정 순서
    private static final List<String> AXIS_ORDER = Arrays.asList("EI", "SN", "TF", "JP");

    private final PersuasionBattleRoundService persuasionBattleRoundService;

    @Autowired
    public PersuasionBattleController(PersuasionBattleRoundService persuasionBattleRoundService) {
        this.persuasionBattleRoundService = persuasionBattleRoundService;
    }

    @GetMapping("/start")
    public String persuasionBattle(
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
            @RequestParam("aiEi") String aiEi,
            @RequestParam("aiSn") String aiSn,
            @RequestParam("aiTf") String aiTf,
            @RequestParam("aiJp") String aiJp,
            @RequestParam("eiMismatch") boolean eiMismatch,
            @RequestParam("snMismatch") boolean snMismatch,
            @RequestParam("tfMismatch") boolean tfMismatch,
            @RequestParam("jpMismatch") boolean jpMismatch,
            @RequestParam(value = "currentRound", defaultValue = "1") int currentRound,
            @RequestParam(value = "persuasionRate", defaultValue = "0") int persuasionRate,
            Model model) {
        String fullMbti = ei + sn + tf + jp;

        // 주로 제네릭 타입(List<String>)과 로우 타입(List - 제네릭을 명시하지 않은 타입) 간의 변환이나 캐스팅(Casting) 과정에서 발생하는 경고 무시하는 어노테이션
        @SuppressWarnings("unchecked")
        List<String> battleList = (List<String>) model.asMap().get("battleList");  //불일치한 MBTI
        String dimension = (String) model.asMap().get("dimension");//현재 설득 MBTI

        @SuppressWarnings("unchecked")
        List<String> userHistoryList = (List<String>) model.asMap().get("userHistoryList");
        @SuppressWarnings("unchecked")
        List<String> aiFeedbackList = (List<String>) model.asMap().get("aiFeedbackList");
        @SuppressWarnings("unchecked")
        List<Integer> persuasionRateList = (List<Integer>) model.asMap().get("persuasionRateList");

        // battleList가 null이면 초기화 (첫 진입 시)
        if (battleList == null || battleList.isEmpty()) {
            battleList = new ArrayList<>();
            if (eiMismatch) battleList.add("EI");
            if (snMismatch) battleList.add("SN");
            if (tfMismatch) battleList.add("TF");
            if (jpMismatch) battleList.add("JP");
        }

        // dimension이 null이면 첫 번째 불일치 차원으로 설정
        if (dimension == null && !battleList.isEmpty()) {
            dimension = battleList.get(0);
        }

        // 히스토리 리스트들이 null이면 초기화
        if (userHistoryList == null) {
            userHistoryList = new ArrayList<>();
        }
        if (aiFeedbackList == null) {
            aiFeedbackList = new ArrayList<>();
        }
        if (persuasionRateList == null) {
            persuasionRateList = new ArrayList<>();
            persuasionRateList.add(0); // 초기 설득률 0
        }

        log.info("=== AI 분석 결과 화면 수신 데이터 확인 ===");
        log.info("최종 MBTI: {}", fullMbti);
        log.info("제목: {}, 등장인물: {}, 카테고리: {}", title, characterName, category);

        log.info("불일치 지표 리스트 (battleList): {}", battleList);
        log.info("현재 설득할 차원 (dimension): {}", dimension);

        System.out.println("--- 사용자 MBTI 유형 선택 ---");
        System.out.println("E/I 유형: " + ei);
        System.out.println("S/N 유형: " + sn);
        System.out.println("T/F 유형: " + tf);
        System.out.println("J/P 유형: " + jp);
        System.out.println("최종 사용자 MBTI: " + fullMbti);
        System.out.println("AiThing (AI 분석 결과): " + aiThing);
        System.out.println("--- AI 도출 개별 지표 ---");
        System.out.println("AI E/I 유형: " + aiEi);
        System.out.println("AI S/N 유형: " + aiSn);
        System.out.println("AI T/F 유형: " + aiTf);
        System.out.println("AI J/P 유형: " + aiJp);
        System.out.println("--- MBTI 상세 값 ---");
        System.out.println("E (외향) 값: " + valueE);
        System.out.println("I (내향) 값: " + valueI);
        System.out.println("S (감각) 값: " + valueS);
        System.out.println("N (직관) 값: " + valueN);
        System.out.println("T (사고) 값: " + valueT);
        System.out.println("F (감정) 값: " + valueF);
        System.out.println("J (판단) 값: " + valueJ);
        System.out.println("P (인식) 값: " + valueP);


        //Model에 데이터 추가
        model.addAttribute("title", title);
        model.addAttribute("characterName", characterName);
        model.addAttribute("category", category);
        model.addAttribute("aiThing", aiThing);
        model.addAttribute("fullMbti", fullMbti);
        model.addAttribute("aiEi", aiEi);
        model.addAttribute("aiSn", aiSn);
        model.addAttribute("aiTf", aiTf);
        model.addAttribute("aiJp", aiJp);

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

        model.addAttribute("eiMismatch", eiMismatch);
        model.addAttribute("snMismatch", snMismatch);
        model.addAttribute("tfMismatch", tfMismatch);
        model.addAttribute("jpMismatch", jpMismatch);

        model.addAttribute("currentRound", currentRound);
        model.addAttribute("persuasionRate", persuasionRate); //설득률

        model.addAttribute("battleList", battleList);
        model.addAttribute("dimension", dimension);

        model.addAttribute("userHistoryList", userHistoryList);
        model.addAttribute("aiFeedbackList", aiFeedbackList);
        model.addAttribute("persuasionRateList", persuasionRateList);//설득률 리스트


        return "PersuasionBattleActivity";

    }

    @PostMapping("/submit")
    public String ubmitBattleResult(
            // 뷰에서 전송된 모든 파라미터를 다시 받음.
            @RequestParam("title") String title,
            @RequestParam("characterName") String characterName,
            @RequestParam("category") String category,
            @RequestParam("aiThing") String aiThing,
            @RequestParam("fullMbti") String fullMbti,
            @RequestParam("aiEi") String aiEi,
            @RequestParam("aiSn") String aiSn,
            @RequestParam("aiTf") String aiTf,
            @RequestParam("aiJp") String aiJp,
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
            @RequestParam("eiMismatch") boolean eiMismatch,
            @RequestParam("snMismatch") boolean snMismatch,
            @RequestParam("tfMismatch") boolean tfMismatch,
            @RequestParam("jpMismatch") boolean jpMismatch,

            //라운드, 설득률, 논거 텍스트
            @RequestParam("currentRound") int currentRound,
            @RequestParam("persuasionRate") int persuasionRate,
            @RequestParam("evidenceText") String evidenceText,

            @RequestParam(value = "battleList", required = false) List<String> battleList,
            @RequestParam(value = "dimension", required = false) String dimension,

            @RequestParam(value = "persuasionRateList", required = false) List<Integer> persuasionRateList,
            // 다음 페이지로 데이터를 안전하게 전달하기 위한 객체
            RedirectAttributes redirectAttributes){

        // 현재 상태 데이터를 맵으로 준비
        Map<String, Object> currentData = new HashMap<>();
        currentData.put("battleList", battleList != null ? battleList : new ArrayList<>());
        currentData.put("dimension", dimension);
        currentData.put("currentRound", currentRound);
        currentData.put("persuasionRate", persuasionRate);
        currentData.put("evidenceText", evidenceText);
        currentData.put("userHistoryList", userHistoryList != null ? userHistoryList : new ArrayList<>());
        currentData.put("aiFeedbackList", aiFeedbackList != null ? aiFeedbackList : new ArrayList<>());
        currentData.put("persuasionRateList", persuasionRateList != null ? persuasionRateList : new ArrayList<>());

        // 서비스 호출하여 다음 라운드 데이터 가져오기
        Map<String, Object> nextStateData = persuasionBattleRoundService.processNextRound(currentData);

        // 서비스에서 받은 결과 처리
        int nextRound = (Integer) nextStateData.get("currentRound");
        int newPersuasionRate = (Integer) nextStateData.get("persuasionRate");
        String nextDimension = (String) nextStateData.get("dimension");
        boolean isFinished = (Boolean) nextStateData.get("isFinished");

        @SuppressWarnings("unchecked")
        List<String> updatedBattleList = (List<String>) nextStateData.get("battleList");
        @SuppressWarnings("unchecked")
        List<String> updatedUserHistoryList = (List<String>) nextStateData.get("userHistoryList");
        @SuppressWarnings("unchecked")
        List<String> updatedAiFeedbackList = (List<String>) nextStateData.get("aiFeedbackList");
        @SuppressWarnings("unchecked")
        List<Integer> updatedPersuasionRateList = (List<Integer>) nextStateData.get("persuasionRateList");

        log.info("=== 서비스 처리 후 결과 ===");
        log.info("다음 라운드: {}, 새 설득률: {}", nextRound, newPersuasionRate);
        log.info("다음 차원: {}, 완료 여부: {}", nextDimension, isFinished);


        if (isFinished) {
            //최종 결과 페이지로 가는 경로
            //retrun "redirect:/persuade/final";
        }




        redirectAttributes.addAttribute("title", title);
        redirectAttributes.addAttribute("characterName", characterName);
        redirectAttributes.addAttribute("category", category);
        redirectAttributes.addAttribute("aiThing", aiThing);
        redirectAttributes.addAttribute("aiEi", aiEi);
        redirectAttributes.addAttribute("aiSn", aiSn);
        redirectAttributes.addAttribute("aiTf", aiTf);
        redirectAttributes.addAttribute("aiJp", aiJp);
        redirectAttributes.addAttribute("valueE", valueE);
        redirectAttributes.addAttribute("valueI", valueI);
        redirectAttributes.addAttribute("valueS", valueS);
        redirectAttributes.addAttribute("valueN", valueN);
        redirectAttributes.addAttribute("valueT", valueT);
        redirectAttributes.addAttribute("valueF", valueF);
        redirectAttributes.addAttribute("valueJ", valueJ);
        redirectAttributes.addAttribute("valueP", valueP);
        redirectAttributes.addAttribute("ei", ei);
        redirectAttributes.addAttribute("sn", sn);
        redirectAttributes.addAttribute("tf", tf);
        redirectAttributes.addAttribute("jp", jp);



        redirectAttributes.addAttribute("eiMismatch", eiMismatch);
        redirectAttributes.addAttribute("snMismatch", snMismatch);
        redirectAttributes.addAttribute("tfMismatch", tfMismatch);
        redirectAttributes.addAttribute("jpMismatch", jpMismatch);

        redirectAttributes.addAttribute("currentRound", currentRound);
        redirectAttributes.addAttribute("persuasionRate", persuasionRate);

        redirectAttributes.addFlashAttribute("battleList", battleList);
        redirectAttributes.addFlashAttribute("dimension", dimension);

        redirectAttributes.addFlashAttribute("persuasionRateList", persuasionRateList);

        //다음 페이지 리다이렉션 주소
        return "redirect:/persuade/start";


    }







}
