package com.example.braincheck.controller;
//처음 설득 페이지

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.braincheck.service.PersuasionBattleRoundService;

import lombok.extern.slf4j.Slf4j;

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
            @RequestParam("aiThing") String aiThing,
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
        @SuppressWarnings("unchecked")
        List<Integer> persuasionGainList = (List<Integer>) model.asMap().get("persuasionGainList");

        // battleList가 null이면 초기화 (첫 진입 시)
        if (battleList == null || battleList.isEmpty()) {
            battleList = new ArrayList<>();
            if (eiMismatch) {
                battleList.add("EI");
            }
            if (snMismatch) {
                battleList.add("SN");
            }
            if (tfMismatch) {
                battleList.add("TF");
            }
            if (jpMismatch) {
                battleList.add("JP");
            }
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
        if (persuasionGainList == null) {
            persuasionGainList = new ArrayList<>();
        }

        log.info("=== AI 분석 결과 화면 수신 데이터 확인 ===");
        log.info("최종 MBTI: {}", fullMbti);
        log.info("제목: {}, 등장인물: {}, 카테고리: {}", title, characterName, category);

        log.info("불일치 지표 리스트 (battleList): {}", battleList);
        log.info("현재 설득할 차원 (dimension): {}", dimension);
        log.info("userHistoryList 크기: {}, 내용: {}", userHistoryList.size(), userHistoryList);
        log.info("aiFeedbackList 크기: {}, 내용: {}", aiFeedbackList.size(), aiFeedbackList);
        if (!aiFeedbackList.isEmpty()) {
            log.info("마지막 AI 피드백: [{}]", aiFeedbackList.get(aiFeedbackList.size() - 1));
        } else {
            log.warn("⚠️ aiFeedbackList가 비어있습니다!");
        }

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
        model.addAttribute("persuasionGainList", persuasionGainList);//라운드별 설득력 증가량

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
            @RequestParam(value = "userHistoryList", required = false) List<String> userHistoryList,
            @RequestParam(value = "aiFeedbackList", required = false) List<String> aiFeedbackList,
            @RequestParam(value = "persuasionRateList", required = false) List<Integer> persuasionRateList,
            @RequestParam(value = "persuasionGainList", required = false) List<Integer> persuasionGainList,
            // 다음 페이지로 데이터를 안전하게 전달하기 위한 객체
            RedirectAttributes redirectAttributes,
            Model model) {

        // 현재 상태 데이터를 맵으로 준비
        Map<String, Object> currentData = new HashMap<>();
        currentData.put("battleList", battleList != null ? battleList : new ArrayList<>());

        // AI 프롬프트용 메타데이터 (라운드 서비스에서 사용)
        currentData.put("title", title);
        currentData.put("characterName", characterName);
        currentData.put("category", category);
        currentData.put("fullMbti", fullMbti);  // 사용자 최종 MBTI
        currentData.put("aiThing", aiThing);    // AI가 분석한 MBTI

        currentData.put("dimension", dimension);
        currentData.put("currentRound", currentRound);
        currentData.put("persuasionRate", persuasionRate);
        currentData.put("evidenceText", evidenceText);
        currentData.put("userHistoryList", userHistoryList != null ? userHistoryList : new ArrayList<>());
        currentData.put("aiFeedbackList", aiFeedbackList != null ? aiFeedbackList : new ArrayList<>());
        currentData.put("persuasionRateList", persuasionRateList != null ? persuasionRateList : new ArrayList<>());
        currentData.put("persuasionGainList", persuasionGainList != null ? persuasionGainList : new ArrayList<>());

        // Flash Attribute에서 차원별 설득률 Map 가져오기 (이전 차원의 설득률 유지)
        @SuppressWarnings("unchecked")
        Map<String, Integer> existingDimensionPersuasionRateMap = (Map<String, Integer>) model.asMap().get("dimensionPersuasionRateMap");
        if (existingDimensionPersuasionRateMap == null) {
            existingDimensionPersuasionRateMap = new HashMap<>();
        }
        currentData.put("dimensionPersuasionRateMap", existingDimensionPersuasionRateMap);

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
        @SuppressWarnings("unchecked")
        List<Integer> updatedPersuasionGainList = (List<Integer>) nextStateData.get("persuasionGainList");
        @SuppressWarnings("unchecked")
        Map<String, Integer> updatedDimensionPersuasionRateMap = (Map<String, Integer>) nextStateData.get("dimensionPersuasionRateMap");

        log.info("=== 서비스 처리 후 결과 ===");
        log.info("다음 라운드: {}, 새 설득률: {}", nextRound, newPersuasionRate);
        log.info("다음 차원: {}, 완료 여부: {}", nextDimension, isFinished);
        log.info("userHistoryList 크기: {}, 내용: {}", updatedUserHistoryList.size(), updatedUserHistoryList);
        log.info("aiFeedbackList 크기: {}, 내용: {}", updatedAiFeedbackList.size(), updatedAiFeedbackList);
        if (!updatedAiFeedbackList.isEmpty()) {
            log.info("마지막 AI 피드백: [{}]", updatedAiFeedbackList.get(updatedAiFeedbackList.size() - 1));
        } else {
            log.warn("⚠️ updatedAiFeedbackList가 비어있습니다!");
        }
        log.info("차원별 설득률 Map: {}", updatedDimensionPersuasionRateMap);

        if (isFinished) {
            // 최종 결과 페이지로 리다이렉트
            redirectAttributes.addAttribute("title", title);
            redirectAttributes.addAttribute("characterName", characterName);
            redirectAttributes.addAttribute("category", category);
            redirectAttributes.addAttribute("fullMbti", fullMbti);
            redirectAttributes.addAttribute("aiThing", aiThing);
            redirectAttributes.addAttribute("ei", ei);
            redirectAttributes.addAttribute("sn", sn);
            redirectAttributes.addAttribute("tf", tf);
            redirectAttributes.addAttribute("jp", jp);
            redirectAttributes.addAttribute("aiEi", aiEi);
            redirectAttributes.addAttribute("aiSn", aiSn);
            redirectAttributes.addAttribute("aiTf", aiTf);
            redirectAttributes.addAttribute("aiJp", aiJp);
            redirectAttributes.addAttribute("eiMismatch", eiMismatch);
            redirectAttributes.addAttribute("snMismatch", snMismatch);
            redirectAttributes.addAttribute("tfMismatch", tfMismatch);
            redirectAttributes.addAttribute("jpMismatch", jpMismatch);

            // 차원별 설득률 Map을 Flash Attribute로 전달
            redirectAttributes.addFlashAttribute("dimensionPersuasionRateMap", updatedDimensionPersuasionRateMap);

            return "redirect:/persuade/final";
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

        // 다음 라운드/차원 정보 전달
        redirectAttributes.addAttribute("currentRound", nextRound);
        redirectAttributes.addAttribute("persuasionRate", newPersuasionRate);

        // 히스토리 및 차원 상태는 Flash Attribute로 전달
        redirectAttributes.addFlashAttribute("battleList", updatedBattleList);
        redirectAttributes.addFlashAttribute("dimension", nextDimension);
        redirectAttributes.addFlashAttribute("userHistoryList", updatedUserHistoryList);
        redirectAttributes.addFlashAttribute("aiFeedbackList", updatedAiFeedbackList);
        redirectAttributes.addFlashAttribute("persuasionRateList", updatedPersuasionRateList);
        redirectAttributes.addFlashAttribute("persuasionGainList", updatedPersuasionGainList);
        redirectAttributes.addFlashAttribute("dimensionPersuasionRateMap", updatedDimensionPersuasionRateMap);

        //다음 페이지 리다이렉션 주소
        return "redirect:/persuade/start";

    }

    @GetMapping("/final")
    public String finalResult(
            @RequestParam("title") String title,
            @RequestParam("characterName") String characterName,
            @RequestParam("category") String category,
            @RequestParam("fullMbti") String fullMbti,
            @RequestParam("aiThing") String aiThing,
            @RequestParam("ei") String ei,
            @RequestParam("sn") String sn,
            @RequestParam("tf") String tf,
            @RequestParam("jp") String jp,
            @RequestParam("aiEi") String aiEi,
            @RequestParam("aiSn") String aiSn,
            @RequestParam("aiTf") String aiTf,
            @RequestParam("aiJp") String aiJp,
            @RequestParam("eiMismatch") boolean eiMismatch,
            @RequestParam("snMismatch") boolean snMismatch,
            @RequestParam("tfMismatch") boolean tfMismatch,
            @RequestParam("jpMismatch") boolean jpMismatch,
            Model model) {

        // Flash Attribute에서 차원별 설득률 Map 가져오기
        @SuppressWarnings("unchecked")
        Map<String, Integer> dimensionPersuasionRateMap = (Map<String, Integer>) model.asMap().get("dimensionPersuasionRateMap");

        if (dimensionPersuasionRateMap == null) {
            dimensionPersuasionRateMap = new HashMap<>();
        }

        // 차원별 설득률 계산 (불일치한 차원만)
        int eiPersuasionRate = eiMismatch ? dimensionPersuasionRateMap.getOrDefault("EI", 0) : 100;
        int snPersuasionRate = snMismatch ? dimensionPersuasionRateMap.getOrDefault("SN", 0) : 100;
        int tfPersuasionRate = tfMismatch ? dimensionPersuasionRateMap.getOrDefault("TF", 0) : 100;
        int jpPersuasionRate = jpMismatch ? dimensionPersuasionRateMap.getOrDefault("JP", 0) : 100;

        // 평균 설득률 계산
        int totalRate = 0;
        int count = 0;
        if (eiMismatch) {
            totalRate += eiPersuasionRate;
            count++;
        }
        if (snMismatch) {
            totalRate += snPersuasionRate;
            count++;
        }
        if (tfMismatch) {
            totalRate += tfPersuasionRate;
            count++;
        }
        if (jpMismatch) {
            totalRate += jpPersuasionRate;
            count++;
        }
        int averagePersuasionRate = count > 0 ? totalRate / count : 100;

        // 설득한 차원 개수 계산
        int persuadedDimensions = 0;
        if (eiMismatch && eiPersuasionRate > 0) {
            persuadedDimensions++;
        }
        if (snMismatch && snPersuasionRate > 0) {
            persuadedDimensions++;
        }
        if (tfMismatch && tfPersuasionRate > 0) {
            persuadedDimensions++;
        }
        if (jpMismatch && jpPersuasionRate > 0) {
            persuadedDimensions++;
        }

        // 모델에 데이터 추가
        model.addAttribute("title", title);
        model.addAttribute("characterName", characterName);
        model.addAttribute("category", category);
        model.addAttribute("fullMbti", fullMbti);
        model.addAttribute("aiThing", aiThing);
        model.addAttribute("ei", ei);
        model.addAttribute("sn", sn);
        model.addAttribute("tf", tf);
        model.addAttribute("jp", jp);
        model.addAttribute("aiEi", aiEi);
        model.addAttribute("aiSn", aiSn);
        model.addAttribute("aiTf", aiTf);
        model.addAttribute("aiJp", aiJp);
        model.addAttribute("eiMismatch", eiMismatch);
        model.addAttribute("snMismatch", snMismatch);
        model.addAttribute("tfMismatch", tfMismatch);
        model.addAttribute("jpMismatch", jpMismatch);
        model.addAttribute("eiPersuasionRate", eiPersuasionRate);
        model.addAttribute("snPersuasionRate", snPersuasionRate);
        model.addAttribute("tfPersuasionRate", tfPersuasionRate);
        model.addAttribute("jpPersuasionRate", jpPersuasionRate);
        model.addAttribute("averagePersuasionRate", averagePersuasionRate);
        model.addAttribute("persuadedDimensions", persuadedDimensions);

        log.info("=== 최종 결과 페이지 ===");
        log.info("차원별 설득률: EI={}, SN={}, TF={}, JP={}", eiPersuasionRate, snPersuasionRate, tfPersuasionRate, jpPersuasionRate);
        log.info("평균 설득률: {}, 설득한 차원: {}", averagePersuasionRate, persuadedDimensions);

        return "FinalResultAactivity";
    }

}
