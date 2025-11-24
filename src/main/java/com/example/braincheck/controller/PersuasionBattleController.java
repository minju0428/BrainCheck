package com.example.braincheck.controller;
//처음 설득 페이지

import lombok.extern.slf4j.Slf4j;
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
            Model model
    ) {
        String fullMbti = ei + sn + tf + jp;



        log.info("=== AI 분석 결과 화면 수신 데이터 확인 ===");
        log.info("최종 MBTI: {}", fullMbti);
        log.info("제목: {}, 등장인물: {}, 카테고리: {}", title, characterName, category);


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

            // 다음 페이지로 데이터를 안전하게 전달하기 위한 객체
            RedirectAttributes redirectAttributes){

        //ai와 사용자의 MBTI가 불일치한 경우 저장하는 로직
        List<String> unmatchedAxis = new ArrayList<>(); // 불일치 MBTI 저장하는 변수 (EI, SN, TF, JP)
        List<String> userUnmatchedValue = new ArrayList<>(); // 불일치한 MBTI에 대한 사용자의 선택한 각각의 MBTI


        if (!ei.equals(aiEi)) {
            unmatchedAxis.add("EI");
            userUnmatchedValue.add(ei);
        }
        if (!sn.equals(aiSn)) {
            unmatchedAxis.add("SN");
            userUnmatchedValue.add(sn);
        }
        if (!tf.equals(aiTf)) {
            unmatchedAxis.add("TF");
            userUnmatchedValue.add(tf);
        }
        if (!jp.equals(aiJp)) {
            unmatchedAxis.add("JP");
            userUnmatchedValue.add(jp);
        }
        //리다이렉션은 String 형태로 전달함. 그래서 List를 쉼표로 구분한 String으로 변환
        String unmatchedAxisStr = String.join(",", unmatchedAxis);
        String userUnmatchedValueStr = String.join(",", userUnmatchedValue);

        log.info("불일치 지표 (Axis): {}", unmatchedAxisStr);
        log.info("사용자 불일치 값 (Value): {}", userUnmatchedValueStr);





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

        redirectAttributes.addAttribute("unmatchedAxis", unmatchedAxisStr);
        redirectAttributes.addAttribute("userUnmatchedValue", userUnmatchedValueStr);


        //다음 페이지 리다이렉션 주소
        return "redirect:/PersuasionBattleActivity";


    }







}
