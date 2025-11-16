package com.example.braincheck.Service;

import com.google.api.client.util.Value;
import org.springframework.stereotype.Service;


@Service
public class GeminiService {
    //Gemini API 연동을 위한 설정값들
    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.project.id:}")
    private String projectId;

    @Value("${gemini.location:us-central1}")
    private String location;

    @Value("${gemini.model.name:gemini-pro}")
    private String modelName;

    /**
     * 캐릭터 상세 정보를 조회하는 메서드.
     * @param 넘겨받을 변수 적기
     * @return 그 변수에 대한 상세 내용
     */

    /** 형식
     public 타입  함수명(념겨 받을 변수명){
         try {
            //Vertex AI 초기화
             VertexAI vertexAI = new VertexAI(projectId, location);

            // GenerativeModel 생성
            GenerativeModel model = new GenerativeModel(modelName, vertexAI);

            //프롬프트 구성 : 넘겨 받은 질문이외의 부가 질문 부분
            //String.format() : Java에서 특정 형식(format)에 맞춰 문자열을 생성할 때 사용하는 정적(static) 메서드
            String prompt = String.format(
                    "없음과 있음으로만 답하시오.\n
                    +"   ", 넘겨받은 변수명
            );

            //답변 생성 요청
            //medel.generateContent() : Gemini API를 사용하여 인공지능 모델에게 요청(프롬프트)을 보내고 응답을 생성하도록 지시하는 핵심 메서드
            //ContentMaker.fromString() :  사용자의 텍스트 입력을 Gemini API가 요구하는 형식으로 변환해주는 편의 기능(Helper Method)
            GenerateContentResponse response = medel.generateContent(
                    ContentMaker.fromString(prompt)
            );

            //답변 처리
            //ResponseHandler.getText() : Gemini API 응답 객체에서 모델이 실제로 생성한 텍스트 결과만을 추출하기 위해 사용되는 메서드
            return ResponseHandler.getText(response);

         } catch(IOException e) {
            //에러 발생 시 에러 메시지 반환
            // e.getMessage() : 예외의 원인을 설명하는 상세 메시지
            return "에러 시 발생하는 메시지 적기" +  e.getMessage();
        }




     }
     **/
}
