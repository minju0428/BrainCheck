package com.example.braincheck.service;
//CharacterController.java 파일에 지정된 변수들을 ai로 검증하는 코드 파일

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.braincheck.dto.CharacterFormDto;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;

import jakarta.annotation.PreDestroy;

@Service
public class AiPromptService {

    //Gemini API 연동을 위한 설정값들
    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.project.id:}")
    private String projectId;

    @Value("${gemini.location:us-central1}")
    private String location;

    @Value("${gemini.model.name:gemini-2.5-flash}")
    private String modelName;

    //초기화 필드
    private final VertexAI vertexAI;
    private final GenerativeModel model;

    //피라미터가 있는 생성자만 남겨 spring이 이를 사용함.
    public AiPromptService(
            @Value("${gemini.project.id:}") String projectId,
            @Value("${gemini.location:us-central1}") String location,
            @Value("${gemini.model.name:gemini-2.5-flash}") String modelName) throws IOException {

        //
        this.vertexAI = new VertexAI(projectId, location);
        this.model = new GenerativeModel(modelName, vertexAI);
    }

    @PreDestroy
    public void close() {
        if (vertexAI != null) {
            try {
                vertexAI.close();
            } catch (Exception e) {
                System.err.println("VertexAI 객체 닫기 실패: " + e.getMessage());
            }
        }
    }

    //상세 정보 조회 함수
    public String RetrieveDetailedInformation(CharacterFormDto characterFormDto) {
        try {
            String characterName = characterFormDto.getCharacterName();
            String title = characterFormDto.getTitle();
            String category = characterFormDto.getCategory();

            //프롬프트 구성 : 넘겨 받은 질문이외의 부가 질문 부분
            //String.format() : Java에서 특정 형식(format)에 맞춰 문자열을 생성할 때 사용하는 정적(static) 메서드
            String prompt = String.format(
                    // 질문1
                    "주어진 정보(등장인물, 제목, 카테고리)의 실제 존재 여부 및 연관성을 판단하여, 요청된 형식에 맞춰 '있음' 또는 '없음'으로만 답하시오."
                            + "\n\n"
                            // 전달 받은 입력에 대한 데이터
                            + "--- 데이터 ---\n"
                            + "등장인물: %s\n"
                            + "제목: %s\n"
                            + "카테고리: %s\n"
                            + "-------------\n\n"
                            // 응답 조건
                            + "AiThing이 부분은 등장인물, 제목, 카테고리를 보고 mbti를 도출하시오. (답변 예시) ESTP\n"
                            + "여부 부분 응답 조건 : 존재하는 경우에만 '있음'으로 답하시오.\n"
                            + "'존재 여부'는 등장인물과 카테고리가 모두 제목과 연결될 때만 '있음'으로 답하시오."
                            + "Value부분은 등장인물, 제목, 카테고리를 보고 도출한 mbti에 대한 퍼센트로 답하시오.( E/I, S/N, T/F, P/J 각각 총합이 100퍼센트여야함.)"
                            + "\n"
                            + "\n"
                            // 출력 형식 요청
                            + "아래 형식 외의 어떠한 설명이나 부가적인 내용도 포함하지 말고 오직 이 형식대로만 답하십시오:\n"
                            + "등장인물: %s\n"
                            + "제목: %s\n"
                            + "카테고리: %s\n"
                            + "AiThing:\n"
                            + "ValueE: \n"
                            + "ValueI: \n"
                            + "ValueS: \n"
                            + "ValueN: \n"
                            + "ValueT: \n"
                            + "ValueF: \n"
                            + "ValueJ: \n"
                            + "ValueP: \n"
                            + "등장인물 여부:\n"
                            + "제목 여부:\n"
                            + "카테고리 여부:\n"
                            + "존재 여부:\n",
                    characterName,
                    title,
                    category,
                    characterName,
                    title,
                    category
            );

            //답변 생성 요청
            //medel.generateContent() : Gemini API를 사용하여 인공지능 모델에게 요청(프롬프트)을 보내고 응답을 생성하도록 지시하는 핵심 메서드
            //ContentMaker.fromString() :  사용자의 텍스트 입력을 Gemini API가 요구하는 형식으로 변환해주는 편의 기능(Helper Method)
            GenerateContentResponse response = model.generateContent(
                    ContentMaker.fromString(prompt)
            );

            // 답변 처리
            String aiResponseText = ResponseHandler.getText(response);

            //콘솔 로그 추가
            System.out.println("--- Gemini AI 응답 시작 ---");
            System.out.println("입력된 프롬프트: \n" + prompt); // 프롬프트도 함께 출력하면 디버깅에 유용합니다.
            System.out.println("AI 답변 내용: \n" + aiResponseText);
            System.out.println("--- Gemini AI 응답 종료 ---");
            //ResponseHandler.getText() : Gemini API 응답 객체에서 모델이 실제로 생성한 텍스트 결과만을 추출하기 위해 사용되는 메서드
            return aiResponseText;

        } catch (IOException e) {
            //에러 발생 시 에러 메시지 반환
            // e.getMessage() : 예외의 원인을 설명하는 상세 메시지
            return "에러 시 발생하는 메시지 적기" + e.getMessage();
        }
    }

    //설득 배틀 분석 함수 - 사용자의 논거를 분석하고 AI 피드백과 설득률 반환
    public String analyzePersuasionEvidence(
            String characterName,
            String title,
            String category,
            String dimension,
            String userMbtiType,
            String aiMbtiType,
            String evidenceText,
            int currentRound) {

        try {
            // 차원별 설명 매핑
            String dimensionDescription = "";
            switch (dimension) {
                case "EI":
                    dimensionDescription = "에너지 방향 (외향 E vs 내향 I)";
                    break;
                case "SN":
                    dimensionDescription = "인식 방식 (감각 S vs 직관 N)";
                    break;
                case "TF":
                    dimensionDescription = "판단 기준 (사고 T vs 감정 F)";
                    break;
                case "JP":
                    dimensionDescription = "생활 양식 (판단 J vs 인식 P)";
                    break;
            }

            // 프롬프트 구성
            String prompt = String.format(
                    "당신은 생각한 MBTI는 AI 분석 쪽입니다. 사용자가 제시한 논거를 분석하여 설득력을 평가하고 피드백을 제공하세요.\n\n"
                            + "--- 작품 정보 ---\n"
                            + "제목: %s\n"
                            + "등장인물: %s\n"
                            + "카테고리: %s\n"
                            + "---------------\n\n"
                            + "--- 설득 상황 ---\n"
                            + "현재 차원: %s (%s)\n"
                            + "사용자 주장: %s는 '%s' 타입이다\n"
                            + "AI 분석: %s는 '%s' 타입이다\n"
                            + "현재 라운드: %d/3\n"
                            + "---------------\n\n"
                            + "--- 사용자의 논거 ---\n"
                            + "%s\n"
                            + "-------------------\n\n"
                            + "--- 분석 기준 ---\n"
                            + "1. 구체성: 작품 속 구체적인 장면, 대사, 행동을 언급했는가?\n"
                            + "2. 논리성: MBTI 이론에 근거한 논리적 연결이 있는가?\n"
                            + "3. 타당성: 제시한 근거가 해당 MBTI 유형을 뒷받침하는가?\n"
                            + "4. 설득력: 전반적으로 설득력 있는 주장인가?\n"
                            + "---------------\n\n"
                            + "--- 응답 조건 ---\n"
                            + "Score: 0부터 100까지의 정수로 AI 입장에서 얼마나 설득되었는지 설득력 점수를 부여하시오.\n"
                            + "  - 매우 우수한 논거: 30-40점\n"
                            + "  - 좋은 논거: 20-29점\n"
                            + "  - 보통 논거: 10-19점\n"
                            + "  - 부족한 논거: 0-9점\n"
                            + "Feedback: 아주 짧은 한국어 한 문장으로만 답하시오.\n"
                            + "  - 최대 25자 이내의 간단한 문장으로 쓰고, 불필요한 설명은 금지\n"
                            + "  - 예시: '구체적 근거가 좋아요, 한두 장면만 더 언급해 보세요.'\n"
                            + "---------------\n\n"
                            + "아래 형식 외의 어떠한 설명이나 부가적인 내용도 포함하지 말고 오직 이 형식대로만 답하십시오:\n\n"
                            + "Score: \n"
                            + "Feedback: \n",
                    title,
                    characterName,
                    category,
                    dimension,
                    dimensionDescription,
                    characterName,
                    userMbtiType,
                    characterName,
                    aiMbtiType,
                    currentRound,
                    evidenceText
            );

            // 답변 생성 요청
            GenerateContentResponse response = model.generateContent(
                    ContentMaker.fromString(prompt)
            );

            // 답변 처리
            String aiResponseText = ResponseHandler.getText(response);

            // 콘솔 로그 추가
            System.out.println("--- 설득 배틀 AI 분석 시작 ---");
            System.out.println("차원: " + dimension + " (" + dimensionDescription + ")");
            System.out.println("라운드: " + currentRound);
            System.out.println("사용자 논거: \n" + evidenceText);
            System.out.println("AI 분석 결과: \n" + aiResponseText);
            System.out.println("--- 설득 배틀 AI 분석 종료 ---");

            return aiResponseText;

        } catch (IOException e) {
            // 에러 발생 시 에러 메시지 반환
            return "설득 분석 중 오류 발생: " + e.getMessage();
        }
    }

}
