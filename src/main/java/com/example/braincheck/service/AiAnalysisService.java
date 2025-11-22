package com.example.braincheck.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class AiAnalysisService {

    //ai가 생각하는 mbti 각각 분리하는 메서드
    public Map<String, String> processForPersuade(Map<String, String> persuadeData) {

        String aiThing = persuadeData.get("aiThing");

        if(aiThing != null && aiThing.length() == 4) {

            //aiThing 문자열로 분리
            String aiEi = String.valueOf(aiThing.charAt(0));
            String aiSn = String.valueOf(aiThing.charAt(1));
            String aiTf = String.valueOf(aiThing.charAt(2));
            String aiJp = String.valueOf(aiThing.charAt(3));

            //분리된 값 추가
            persuadeData.put("aiEi", aiEi);
            persuadeData.put("aiSn", aiSn);
            persuadeData.put("aiTf", aiTf);
            persuadeData.put("aiJp", aiJp);
        } else {
            log.error("aiThing 데이터가 유효하지 않거나 4글자가 아닙니다: {}", aiThing);
        }

        log.info("Service: AI MBTI 지표 분리 처리 완료.");

        //처리한 데이터 반환
        return persuadeData;
    }
}
