package com.example.braincheck.controller;

import com.example.braincheck.dto.CharacterFormDto;
import com.example.braincheck.service.AiPromptService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

//입력 폼을 처리하는 컨트롤러 자바 파일
//MBTI 유형 선택하는 UI에 넘겨줄 값들
//다음 ui, java로 값을 넘겨줄 수 있게 하는 코드

@Slf4j
//@controller : 해당 클래스가 웹 요청을 처리하는 컨트롤러 역할을 수행함을 Spring에게 알려주는 어노테이션
@Controller
@RequestMapping("/character")
public class CharacterController {

    //서비스 의존성 주입을 위한 선언
    private final AiPromptService characterValidationService;

    //생성자 추가로 필드 초기화
    public CharacterController(AiPromptService characterValidationService) {
        this.characterValidationService = characterValidationService;
    }


    @GetMapping("/input")
    public String showInput(Model model) {
        //사용자 입력을 받을 '데이터 그릇' 생성
        model.addAttribute("characterFormDto", new CharacterFormDto());
        //입력 화면 반환
        return "InputActivity";
    }

    @PostMapping("/submit")
    public String submitForm(
            //@Valid : 유효성 검사 실행
            //@ModelAttribute : 데이터 바이딩(매핑) 및 모델 추가
            @Valid @ModelAttribute("CharacterFormDto") CharacterFormDto characterForm,

            //BindingResult : 스프링 프레임워크에 내장되어있는 인터페이스
            // 오류 발생 시 사용자가 제출했던 폼 화면으로 돌아갈 수 있게 해주는 인터페이스
            BindingResult bindingResult,

            //RedirectAttributes : 스프링 프레임워크에 내장되어있는 인터페이스
            //리다이렉트 시 데이터 전달
            //리다이렉트 시 다른 자바 파일로 안전하게 넘겨주기 위해 사용됨.
            RedirectAttributes redirectAttributes) {


            //검증 오류 발생 시 InputActivity.html 화면으로 다시 이동
            if(bindingResult.hasErrors()) {
                return "InputActivity";
            }

           try{
               //서비스 메서드를 호출하여 ai 검증 수행
               //AI 답변 받아오는 변수
               String aiValidationResult = characterValidationService.RetrieveDetailedInformation(characterForm);

               //AI 답변 파싱
               Map<String, String> validationMap = parseAiValidationResult(aiValidationResult);

               //파싱된 맵에서 '존재 여부' 'AiThing' 값 추출
               String existence = validationMap.get("존재 여부");
               String aiThingMbti = validationMap.get("AiThing");

               //파싱된 AI가 도출한 MBTI 각각 퍼센트 값 추출
               String valueEStr = validationMap.get("ValueE");
               String valueE = valueEStr.replace("%", "").trim();

               String valueIStr = validationMap.get("ValueI");
               String valueI = valueIStr.replace("%", "").trim();

               String valueSStr = validationMap.get("ValueS");
               String valueS = valueSStr.replace("%", "").trim();

               String valueNStr = validationMap.get("ValueN");
               String valueN = valueNStr.replace("%", "").trim();

               String valueTStr = validationMap.get("ValueT");
               String valueT = valueTStr.replace("%", "").trim();

               String valueFStr = validationMap.get("ValueF");
               String valueF = valueFStr.replace("%", "").trim();

               String valueJStr = validationMap.get("ValueJ");
               String valueJ = valueJStr.replace("%", "").trim();

               String valuePStr = validationMap.get("ValueP");
               String valueP = valuePStr.replace("%", "").trim();


               //AI 검증 결과를 분석하여 추가 로직 수행
               //검증에 성공한 경우
               if("있음".equals(existence)){
                   log.info("AI 검증 성공 :  존재 여부 = 있음. 다음 결과 페이지로 이동");

                   Map<String, String> dataToPass = new HashMap<>();

                   dataToPass.put("제목", characterForm.getTitle());
                   dataToPass.put("등장인물", characterForm.getCharacterName());
                   dataToPass.put("카테고리", characterForm.getCategory());
                   dataToPass.put("AiThing", aiThingMbti);
                   dataToPass.put("valueE", valueE);
                   dataToPass.put("valueI", valueI);
                   dataToPass.put("valueS", valueS);
                   dataToPass.put("valueN", valueN);
                   dataToPass.put("valueT", valueT);
                   dataToPass.put("valueF", valueF);
                   dataToPass.put("valueJ", valueJ);
                   dataToPass.put("valueP", valueP);

                   redirectAttributes.addFlashAttribute("validationMap",dataToPass);

                   log.info("Flash Attribute로 전달되는 데이터: {}", dataToPass);
                   //이 변수들을 넘겨줄 다음 자바 파일 경로
                   return "redirect:/mbti-ui";
               } else {
                   //검증에 실패한 경우
                   String generalErrorMessage = "입력하신 정보 중 일부가 일치하지 않아 분석할 수 없습니다. 다시 확인해주세요.";

                   //항목별 구체적인 에러 메시지
                   if("없음".equals(validationMap.get("등장인물 여부"))){
                       //등장인물: 없음 인 경우
                       // code, args(빈 배열), defaultMessage 순으로 인자를 맞춥니다.
                       bindingResult.reject("validation.failure.character", new Object[0], "입력하신 등장인물 정보가 작품에 존재하지 않습니다.");
                   }
                   if("없음".equals(validationMap.get("제목 여부"))){
                       //제목: 없음 인 경우
                       bindingResult.reject("validation.failure.title", new Object[0], "입력하신 작품 제목이 존재하지 않거나 카테고리와 일치하지 않습니다.");
                   }

                   log.info("AI 검증 실패: 존재 여부='{}' (입력 정보: {} / {})", existence, characterForm.getTitle(), characterForm.getCharacterName());

                   return "InputActivity";
               }



           } catch(Exception e){
               System.err.println("--- AI 검증 중 심각한 오류 발생 ---");
               e.printStackTrace(); // <-- 스택 트레이스를 콘솔에 출력
               System.err.println("--------------------------------");

               //서비스 호출 중 api 에러 발생 시 처리
               bindingResult.reject("service.error", "AI 검증 중 오류가 발생했습니다: " + e.getMessage());
               return "InputActivity";
           }



    }

    private Map<String, String> parseAiValidationResult(String aiValidationResultRaw) {
        //원본 데이터 로그
        log.info("--- AI 원본 응답 파싱 시작 ---");
        log.debug("원본 문자열:\n{}", aiValidationResultRaw);

        //초기 유효성 검사 및  방어 코드
        //입력된 문자열이 없을 경우 즉시 봔환
        //오류를 방지하는 안정 장치
        if(aiValidationResultRaw == null || aiValidationResultRaw.isEmpty()) {
            log.warn("AI 응답 문자열이 null이거나 비어 있습니다. 빈 맵 반환.");
            return new HashMap<>();
        }
        Map<String, String> resultMap = Arrays.stream(aiValidationResultRaw.split("\n")) //줄바꿈을 기준으로 배열을 만듦.
                .filter(line -> line.contains(":")) //line을 검사해서 ":"을 포함하는 줄만 남김.
                .map(line -> {
                    String[] split = line.split(":", 2); //":"을 기준으로 최대 2개로 분리함.

                    //키와 값 두 부분으로 분리가 성공적으로 이루어졌는지 확인하는 조건문
                    if (split.length == 2) { //분리가 된 요소가 2개인지 확인
                        String key = split[0].trim();   //키의 공백 제거
                        String value = split[1].trim(); //값의 공백 제거

                        //파싱 항목 로그
                        log.trace("파싱된 항목: Key='{}', Value='{}'", key, value);

                        return new AbstractMap.SimpleEntry<>(key, value); //키와 값으로 Map Entry 생성 및 반환
                    }
                    log.trace("':'을 포함하지만 유효한 형식(키:값)이 아닌 라인 스킵: {}", line);
                    return null; //분리가 실패했거나 형식이 잘못된 경우 null 반환
                })
                //entry : 스트림에서 현재 처리 중인 Map.Entry객체(키-값)
                //entry != null  :  .map() 단계에서 문자열 파싱이 실패했거나 형식이 맞지 않아 null이 반환된 요소들을 제거
                .filter(entry -> entry != null) //유효하지 않은 항목(위에서 나누고 필요 없어진 부분)을 제거함.


                //Collectors.toMap
                //1. Map.Entry::getKey : Map에서 사용할 키(Key)를 스트림 요소에서 추출
                //2. Map.Entry::getValue : Map에 저장할 값(Value)을 스트림 요소에서 추출
                //3. (existing, replacement) -> replacement : 스트림에 같은 키(Key)가 두 번 이상 나타나는 중복 키 충돌이 발생했을 때, 어떤 값을 선택할지 결정하는 안전 장치
                //4. HashMap::new : 최종적으로 생성될 Map 객체의 구체적인 구현 클래스를 지정
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement)-> replacement,
                        HashMap::new
                ));
        //파싱 결과 로그
        log.info("--- AI 파싱 결과 완료 ({}개 항목) ---", resultMap.size());
        log.debug("최종 파싱된 Map: {}", resultMap);

        // 👇 resultMap을 반환합니다.
        return resultMap;



    }




}
