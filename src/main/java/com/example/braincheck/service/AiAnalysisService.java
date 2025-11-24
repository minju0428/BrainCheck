package com.example.braincheck.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AiAnalysisService {

    //ai가 생각하는 mbti 각각 분리하는 메서드
    public Map<String, Object>  processForPersuade(Map<String, String> persuadeData) {
        //불일치 정보도 저장할 수 있는 변수
        Map<String, Object> processedData = new HashMap<>(persuadeData);
        String aiThing = persuadeData.get("aiThing");

        if(aiThing != null && aiThing.length() == 4) {

            //aiThing 문자열로 분리
            String aiEi = String.valueOf(aiThing.charAt(0));
            String aiSn = String.valueOf(aiThing.charAt(1));
            String aiTf = String.valueOf(aiThing.charAt(2));
            String aiJp = String.valueOf(aiThing.charAt(3));

            //분리된 값 추가
            processedData.put("aiEi", aiEi);
            processedData.put("aiSn", aiSn);
            processedData.put("aiTf", aiTf);
            processedData.put("aiJp", aiJp);


            //불일치 판별을 위한 사용자 데이터 가져오기
            String userEi = persuadeData.get("ei");
            String userSn = persuadeData.get("sn");
            String userTf = persuadeData.get("tf");
            String userJp = persuadeData.get("jp");

            // AI MBTI와 사용자 선택 MBTI 비교 (불일치 여부 확인)
            processedData.put("eiMismatch", !userEi.equals(aiEi));
            processedData.put("snMismatch", !userSn.equals(aiSn));
            processedData.put("tfMismatch", !userTf.equals(aiTf));
            processedData.put("jpMismatch", !userJp.equals(aiJp));

        } else {
            log.error("aiThing 데이터가 유효하지 않거나 4글자가 아닙니다: {}", aiThing);
            processedData.put("aiEi", "");
            processedData.put("aiSn", "");
            processedData.put("aiTf", "");
            processedData.put("aiJp", "");
            processedData.put("eiMismatch", false);
            processedData.put("snMismatch", false);
            processedData.put("tfMismatch", false);
            processedData.put("jpMismatch", false);
        }

        log.info("Service: AI MBTI 지표 분리 처리 완료.");

        //처리한 데이터 반환
        return processedData;
    }
}
