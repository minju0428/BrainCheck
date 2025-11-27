package com.example.braincheck.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PersuasionBattleDto {

    //전달받은 필드 선언
    @NotBlank(message = "선택한 등장인물")
    private String characterName;
    @NotBlank(message = "선택한 제목")
    private String title;
    @NotBlank(message = "선택한 카테고리")
    private String category;

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

    private Boolean eiMismatch;
    private Boolean snMismatch;
    private Boolean tfMismatch;
    private Boolean jpMismatch;

    private List<String> battleList;
    private String dimension;

    private List<String> userHistoryList;
    private List<String> aiFeedbackList;

    @NotNull(message = "현재 라운드는 필수 항목입니다.")
    private Integer currentRound;

    @NotNull(message = "현재 설득률은 필수 항목입니다.")
    private Integer persuasionRate;

    private List<Integer> persuasionRateList;


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


    public Boolean getEiMismatch() {
        return eiMismatch;
    }

    public Boolean getSnMismatch() {
        return snMismatch;
    }

    public Boolean getTfMismatch() {
        return tfMismatch;
    }

    public Boolean getJpMismatch() {
        return jpMismatch;
    }

    public List<String> getBattleList() {
        return battleList;
    }

    public String getDimension() {
        return dimension;
    }

    public List<String> getUserHistoryList() {
        return userHistoryList;
    }

    public List<String> getAiFeedbackList() {
        return aiFeedbackList;
    }

    public Integer getCurrentRound() {
        return currentRound;
    }

    public Integer getPersuasionRate() {
        return persuasionRate;
    }

    public List<Integer> getPersuasionRateList() {
        return persuasionRateList;
    }



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

    public void setEiMismatch(Boolean eiMismatch) {
        this.eiMismatch = eiMismatch;
    }

    public void setSnMismatch(Boolean snMismatch) {
        this.snMismatch = snMismatch;
    }

    public void setTfMismatch(Boolean tfMismatch) {
        this.tfMismatch = tfMismatch;
    }

    public void setJpMismatch(Boolean jpMismatch) {
        this.jpMismatch = jpMismatch;
    }
    public void setBattleList(List<String> battleList) {
        this.battleList = battleList;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }
    public void setUserHistoryList(List<String> userHistoryList) {
        this.userHistoryList = userHistoryList;
    }

    public void setAiFeedbackList(List<String> aiFeedbackList) {
        this.aiFeedbackList = aiFeedbackList;
    }

    public void setCurrentRound(Integer currentRound) {
        this.currentRound = currentRound;
    }

    public void setPersuasionRate(Integer persuasionRate) {
        this.persuasionRate = persuasionRate;
    }

    public void setPersuasionRateList(List<Integer> persuasionRateList) {
        this.persuasionRateList = persuasionRateList;
    }



}