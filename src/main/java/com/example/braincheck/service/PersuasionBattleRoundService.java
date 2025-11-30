package com.example.braincheck.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PersuasionBattleRoundService {

    private final AiPromptService aiPromptService;

    @Autowired
    public PersuasionBattleRoundService(AiPromptService aiPromptService) {
        this.aiPromptService = aiPromptService;
    }

    public Map<String, Object> processNextRound(Map<String, Object> currentData) {

        String dimension = (String) currentData.get("dimension");
        String evidenceText = (String) currentData.get("evidenceText");

        int currentRound = (Integer) currentData.get("currentRound");
        int persuasionRate = (Integer) currentData.get("persuasionRate");

        // AI 프롬프트에 필요한 추가 정보들
        String title = (String) currentData.get("title");
        String characterName = (String) currentData.get("characterName");
        String category = (String) currentData.get("category");
        String userMbtiType = (String) currentData.get("fullMbti"); // 사용자 최종 MBTI
        String aiMbtiType = (String) currentData.get("aiThing");    // AI가 분석한 MBTI

        @SuppressWarnings("unchecked")
        List<String> battleList = (List<String>) currentData.getOrDefault("battleList", new ArrayList<>());
        @SuppressWarnings("unchecked")
        List<String> userHistoryList = (List<String>) currentData.getOrDefault("userHistoryList", new ArrayList<>());
        @SuppressWarnings("unchecked")
        List<String> aiFeedbackList = (List<String>) currentData.getOrDefault("aiFeedbackList", new ArrayList<>());
        @SuppressWarnings("unchecked")
        List<Integer> persuasionRateList = (List<Integer>) currentData.getOrDefault("persuasionRateList", new ArrayList<>());
        @SuppressWarnings("unchecked")
        List<Integer> persuasionGainList = (List<Integer>) currentData.getOrDefault("persuasionGainList", new ArrayList<>());

        // 3라운드는 마지막 라운드이므로 AI 피드백을 받지 않음
        int scoreGained = 0;
        String feedback = "";

        if (currentRound < 3) {
            // 1-2라운드: AI 프롬프트 서비스 호출 (AI에게 현재 라운드의 논거를 분석 요청)
            // 이전 라운드 설득문도 함께 전달 (현재 라운드의 evidenceText는 아직 리스트에 추가되지 않았으므로 userHistoryList가 이전 라운드들임)
            String aiResponseText = aiPromptService.analyzePersuasionEvidence(
                    characterName,
                    title,
                    category,
                    dimension,
                    userMbtiType,
                    aiMbtiType,
                    evidenceText,
                    currentRound,
                    userHistoryList // 이전 라운드 설득문 리스트 전달
            );

            // 2. AI 응답 파싱 (Score, Feedback 추출)
            log.info("=== AI 원본 응답 ===");
            log.info("{}", aiResponseText);
            log.info("==================");

            AiRoundResult aiRoundResult = parseAiPersuasionResponse(aiResponseText);

            log.info("=== 파싱 결과 ===");
            log.info("Score: {}, Feedback: [{}]", aiRoundResult.getScore(), aiRoundResult.getFeedback());
            log.info("================");

            scoreGained = aiRoundResult.getScore();
            feedback = aiRoundResult.getFeedback();
        } else {
            // 3라운드: AI 피드백 없이 설득문만 저장
            log.info("3라운드 (마지막 라운드)이므로 AI 피드백을 받지 않습니다.");
        }

        // 3. 설득 기록 리스트에 저장
        userHistoryList.add(evidenceText);
        if (!feedback.isEmpty()) {
            aiFeedbackList.add(feedback);
        }

        log.info("리스트에 추가 완료. userHistoryList 크기: {}, aiFeedbackList 크기: {}",
                userHistoryList.size(), aiFeedbackList.size());

        // 4. 설득률 업데이트 (누적, 최대 100까지)
        // 3라운드는 scoreGained가 0이므로 설득률이 증가하지 않음
        int newPersuasionRate = Math.min(100, persuasionRate + scoreGained);
        persuasionRateList.add(newPersuasionRate);
        if (scoreGained > 0) {
            persuasionGainList.add(scoreGained);
        }

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
                persuasionGainList = new ArrayList<>();

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
        log.info("=== 반환 전 리스트 상태 ===");
        log.info("userHistoryList 크기: {}, 내용: {}", userHistoryList.size(), userHistoryList);
        log.info("aiFeedbackList 크기: {}, 내용: {}", aiFeedbackList.size(), aiFeedbackList);
        log.info("========================");

        Map<String, Object> nextStateData = new HashMap<>();
        nextStateData.put("currentRound", nextRound);
        nextStateData.put("persuasionRate", newPersuasionRate);
        nextStateData.put("dimension", nextDimension);
        nextStateData.put("battleList", battleList);
        nextStateData.put("userHistoryList", userHistoryList);
        nextStateData.put("aiFeedbackList", aiFeedbackList);
        nextStateData.put("persuasionRateList", persuasionRateList);
        nextStateData.put("persuasionGainList", persuasionGainList);
        nextStateData.put("isFinished", isFinished);

        return nextStateData;
    }

    /**
     * AiPromptService.analyzePersuasionEvidence()의 텍스트 응답을 파싱해서 Score(정수)와
     * Feedback(문장)을 추출하는 메서드.
     *
     * 기대 형식: Score: 35 Feedback: 사용자의 논리가 구체적이고 설득력이 있습니다...
     */
    private AiRoundResult parseAiPersuasionResponse(String aiResponseText) {
        if (aiResponseText == null) {
            log.warn("AI 응답이 null 입니다. 기본값(Score=0, Feedback=공백)을 사용합니다.");
            return new AiRoundResult(0, "");
        }

        int score = 0;
        String feedback = "";

        try {
            // Score 파싱
            Pattern scorePattern = Pattern.compile("Score:\\s*(\\d+)");
            Matcher scoreMatcher = scorePattern.matcher(aiResponseText);
            if (scoreMatcher.find()) {
                score = Integer.parseInt(scoreMatcher.group(1));
                // 0~100 범위를 벗어나면 방어적으로 보정
                if (score < 0) {
                    score = 0;
                }
                if (score > 100) {
                    score = 100;
                }
            } else {
                log.warn("AI 응답에서 Score를 찾지 못했습니다. 응답 내용: {}", aiResponseText);
            }

            // Feedback 파싱 (대소문자 무시, 여러 줄 포함)
            Pattern feedbackPattern = Pattern.compile("Feedback:\\s*(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher feedbackMatcher = feedbackPattern.matcher(aiResponseText);
            if (feedbackMatcher.find()) {
                feedback = feedbackMatcher.group(1).trim();
                if (feedback.isEmpty()) {
                    log.warn("AI 응답에서 Feedback이 비어있습니다. 응답 내용: {}", aiResponseText);
                } else {
                    log.info("Feedback 파싱 성공: [{}]", feedback);
                }
            } else {
                log.warn("AI 응답에서 Feedback 키워드를 찾지 못했습니다. 응답 내용: {}", aiResponseText);
                // Feedback 키워드가 없으면 Score를 제외한 나머지 텍스트를 피드백으로 사용
                String textWithoutScore = aiResponseText.replaceAll("(?i)Score:\\s*\\d+", "").trim();
                if (!textWithoutScore.isEmpty()) {
                    feedback = textWithoutScore;
                    log.info("Feedback 키워드 없음. 전체 텍스트를 피드백으로 사용: [{}]", feedback);
                }
            }
        } catch (Exception e) {
            log.error("AI 응답 파싱 중 오류 발생. 응답: {}, 에러: {}", aiResponseText, e.getMessage(), e);
        }

        return new AiRoundResult(score, feedback);
    }

    /**
     * 한 라운드에 대한 AI 분석 결과를 담는 간단한 DTO. (Score, Feedback)
     */
    private static class AiRoundResult {

        private final int score;
        private final String feedback;

        public AiRoundResult(int score, String feedback) {
            this.score = score;
            this.feedback = feedback;
        }

        public int getScore() {
            return score;
        }

        public String getFeedback() {
            return feedback;
        }
    }

}
