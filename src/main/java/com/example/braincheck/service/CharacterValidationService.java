package com.example.braincheck.service;
//CharacterController.java 파일에 지정된 변수들을 ai로 검증하는 코드 파일

import com.example.braincheck.dto.CharacterFormDto;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class CharacterValidationService {

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
    public CharacterValidationService(
            @Value("${gemini.project.id:}") String projectId,
            @Value("${gemini.location:us-central1}") String location,
            @Value("${gemini.model.name:gemini-2.5-flash}") String modelName) throws IOException{

        //
        this.vertexAI = new VertexAI(projectId, location);
        this.model = new GenerativeModel(modelName, vertexAI);
    }

    @PreDestroy
    public void close() {
        if(vertexAI != null) {
            try{
                vertexAI.close();
            } catch (Exception e) {
                System.err.println("VertexAI 객체 닫기 실패: " + e.getMessage());
            }
        }
    }


    //상세 정보 조회 함수
     public String  RetrieveDetailedInformation(CharacterFormDto characterFormDto) {
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
                             + "존재하는 경우에만 '있음'으로 답하시오.\n"
                             + "'존재 여부'는 등장인물과 카테고리가 모두 제목과 연결될 때만 '있음'으로 답하시오.\n"
                             + "\n"

                             // 출력 형식 요청
                             + "아래 형식 외의 어떠한 설명이나 부가적인 내용도 포함하지 말고 오직 이 형식대로만 답하십시오:\n"

                             +"등장인물: %s\n"
                             + "제목: %s\n"
                             + "카테고리: %s\n"

                             +"등장인물 여부:\n"
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




 }
