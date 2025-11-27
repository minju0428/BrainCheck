package com.example.braincheck.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PersuasionBattleRoundService {


    public Map<String, Object> processNextRound(Map<String, Object> currentData) {

        String dimension = (String) currentData.get("dimension");
        String evidenceText = (String) currentData.get("evidenceText");

        int currentRound = (Integer) currentData.get("currentRound");
        int persuasionRate = (Integer) currentData.get("persuasionRate");

        @SuppressWarnings("unchecked")
        List<String> battleList = (List<String>) currentData.getOrDefault("battleList", new ArrayList<>());
        @SuppressWarnings("unchecked")
        List<String> userHistoryList = (List<String>) currentData.getOrDefault("userHistoryList", new ArrayList<>());
        @SuppressWarnings("unchecked")
        List<String> aiFeedbackList = (List<String>) currentData.getOrDefault("aiFeedbackList", new ArrayList<>());
        @SuppressWarnings("unchecked")
        List<Integer> persuasionRateList = (List<Integer>) currentData.getOrDefault("persuasionRateList", new ArrayList<>());

        //ai 관련
        //AiPersuasionResult result = aiAnalysisService.analyzeUserEvidence(evidenceText, dimension);

        //설득 기록 리스트에 저장
        userHistoryList.add(evidenceText);
        //aiFeedbackList.add(result.getFeedback());

        //설득률 업데이트
        //int scoreGained = result.getScore();
        int newPersuasionRate = Math.min(100, persuasionRate + scoreGained);
        persuasionRateList.add(newPersuasionRate);

        int nextRound = currentRound + 1;
        String nextDimension = dimension;

        boolean isFinished = false;

        // 3라운드 (최대 라운드)를 초과하거나 설득률이 100%에 도달했을 때 또는 차원 전환을 시도
        if (nextRound > 3 || newPersuasionRate >= 100) {

            // 현재 차원 (dimension)을 battleList에서 제거
            battleList.remove(dimension);

            if (!battleList.isEmpty()) {
                // 다음 불일치 차원으로 전환
                nextDimension = battleList.get(0);
                nextRound = 1; // 라운드 초기화
                newPersuasionRate = 0; // 새 차원의 설득률 초기화

                // 새로운 차원에 대한 기록 리스트 초기화
                userHistoryList = new ArrayList<>();
                aiFeedbackList = new ArrayList<>();
                persuasionRateList = new ArrayList<>();
                persuasionRateList.add(0); // 초기 설득률 0 추가

                log.info("차원 전환 완료: {} -> {}. 라운드 초기화.", dimension, nextDimension);

            } else {
                // battleList가 비어있으면 모든 차원 설득 완료
                nextDimension = "FINAL";
                nextRound = 4; // 최종 상태를 나타냄
                isFinished = true;
                log.info("모든 불일치 차원 설득 완료. 최종 단계로 이동.");
            }
        } else {
            // 3라운드 미만이면 라운드만 증가시키고 차원은 유지
            nextDimension = dimension;
        }

        // 6. 업데이트된 데이터를 반환 맵에 담기
        Map<String, Object> nextStateData = new HashMap<>();
        nextStateData.put("currentRound", nextRound);
        nextStateData.put("persuasionRate", newPersuasionRate);
        nextStateData.put("dimension", nextDimension);
        nextStateData.put("battleList", battleList);
        nextStateData.put("userHistoryList", userHistoryList);
        nextStateData.put("aiFeedbackList", aiFeedbackList);
        nextStateData.put("persuasionRateList", persuasionRateList);
        nextStateData.put("isFinished", isFinished);

        return nextStateData;
    }

}