package com.example.braincheck.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@NoArgsConstructor //기본 생성자를 자동으로 생성하는 어노테이션
public class AiAnalysisDto {
    //전달받은 필드 선언
    @NotBlank(message = "선택한 등장인물")
    private String characterName;
    @NotBlank(message = "선택한 제목")
    private String title;
    @NotBlank(message = "선택한 카테고리")
    private String category;

    //ai가 도출한 MBTI
    @NotBlank(message = "ai가 생각하는 MBTI")
    private String aiThing;

    @NotBlank(message = "ai가 생각하는 E/I 유형")
    private String aiEi;

    @NotBlank(message = "ai가 생각하는 S/N 유형")
    private String aiSn;

    @NotBlank(message = "ai가 생각하는 T/F 유형")
    private String aiTf;

    @NotBlank(message = "ai가 생각하는 J/P 유형")
    private String aiJp;

    //ai mbti 퍼센트
    @NotBlank(message = "ai가 생각하는 E의 퍼센트")
    private String valueE;

    @NotBlank(message = "ai가 생각하는 I의 퍼센트")
    private String valueI;

    @NotBlank(message = "ai가 생각하는 S의 퍼센트")
    private String valueS;

    @NotBlank(message = "ai가 생각하는 N의 퍼센트")
    private String valueN;

    @NotBlank(message = "ai가 생각하는 T의 퍼센트")
    private String valueT;

    @NotBlank(message = "ai가 생각하는 F의 퍼센트")
    private String valueF;

    @NotBlank(message = "ai가 생각하는 J의 퍼센트")
    private String valueJ;

    @NotBlank(message = "ai가 생각하는 P의 퍼센트")
    private String valueP;


    //필드 선언
    @NotNull(message = "에너지 방향은 필수 항목입니다.")
    private String ei;
    @NotNull(message = "인식 기능은 필수 항목입니다.")
    private String sn;
    @NotNull(message = "판단은 필수 항목입니다.")
    private String tf;
    @NotNull(message = "생활 양식은 필수 항목입니다.")
    private String jp;

    //ai와 사용자 불일치 정보
    @NotNull(message = "E/I 불일치 여부는 필수 항목입니다.")
    private Boolean eiMismatch;
    @NotNull(message = "S/N 불일치 여부는 필수 항목입니다.")
    private Boolean snMismatch;
    @NotNull(message = "T/F 불일치 여부는 필수 항목입니다.")
    private Boolean tfMismatch;
    @NotNull(message = "J/P 불일치 여부는 필수 항목입니다.")
    private Boolean jpMismatch;


    //스프링이 데이터를 매핑할 수 있도록 하는 것들
    //Getter
    public String getCharacterName() {

        return characterName;
    }

    public String getTitle() {

        return title;
    }

    public String getCategory() {

        return category;
    }

    public String getAiThing() {
        return aiThing;
    }

    public String getAiEi() {
        return aiEi;
    }

    public String getAiSn() {
        return aiSn;
    }

    public String getAiTf() {
        return aiTf;
    }

    public String getAiJp() {
        return aiJp;
    }

    public String getValueE() {
        return valueE;
    }

    public String getValueI() {
        return valueI;
    }

    public String getValueS() {
        return valueS;
    }

    public String getValueN() {
        return valueN;
    }

    public String getValueT() {
        return valueT;
    }

    public String getValueF() {
        return valueF;
    }

    public String getValueJ() {
        return valueJ;
    }

    public String getValueP() {
        return valueP;
    }


    public String getEi() {
        return ei;
    }


    public String getSn() {
        return sn;
    }

    public String getTf() {
        return tf;
    }

    public String getJp() {
        return jp;
    }

    public Boolean getEiMismatch() { return eiMismatch; }
    public Boolean getSnMismatch() { return snMismatch; }
    public Boolean getTfMismatch() { return tfMismatch; }
    public Boolean getJpMismatch() { return jpMismatch; }

    //Setter
    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAiThing(String aiThing) {
        this.aiThing = aiThing;
    }

    public void setAiEi(String aiEi) {
        this.aiEi = aiEi;
    }

    public void setAiSn(String aiSn) {
        this.aiSn = aiSn;
    }

    public void setAiTf(String aiTf) {
        this.aiTf = aiTf;
    }

    public void setAiJp(String aiJp) {
        this.aiJp = aiJp;
    }

    public void setValueE(String valueE) {
        this.valueE = valueE;
    }

    public void setValueI(String valueI) {
        this.valueI = valueI;
    }

    public void setValueS(String valueS) {
        this.valueS = valueS;
    }

    public void setValueN(String valueN) {
        this.valueN = valueN;
    }

    public void setValueT(String valueT) {
        this.valueT = valueT;
    }

    public void setValueF(String valueF) {
        this.valueF = valueF;
    }

    public void setValueJ(String valueJ) {
        this.valueJ = valueJ;
    }

    public void setValueP(String valueP) {
        this.valueP = valueP;
    }

    public void setEi(String ei) {
        this.ei = ei;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setTf(String tf) {
        this.tf = tf;
    }

    public void setJp(String jp) {
        this.jp = jp;
    }

    public void setEiMismatch(Boolean eiMismatch) { this.eiMismatch = eiMismatch; }
    public void setSnMismatch(Boolean snMismatch) { this.snMismatch = snMismatch; }
    public void setTfMismatch(Boolean tfMismatch) { this.tfMismatch = tfMismatch; }
    public void setJpMismatch(Boolean jpMismatch) { this.jpMismatch = jpMismatch; }

}
