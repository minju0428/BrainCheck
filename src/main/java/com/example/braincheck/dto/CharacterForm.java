package com.example.braincheck.dto;
//InputActivity.html의 입력 데이터를 받아오는 코드

import jakarta.validation.constraints.NotBlank;

public class CharacterForm {
    //필수항목
    @NotBlank(message = "등장인물 이름은 필수 입력 항목입니다.")
    private String characterName;

    //필수항목
    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private String title;

    //필수항목
    @NotBlank(message = "카테고리를 선택해 주세요.")
    private String category;


    //Setter 메서드 : 객체의 데이터를 외부에서 설정(수정)할 수 있는 통로를 제공
    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    //Getter 메서드 : 객체의 데이터를 외부에서 읽어갈 수 있는 통로를 제공
    public String getCharacterName() {

        return characterName;
    }

    public String getTitle() {

        return title;
    }

    public String getCategory() {

        return category;
    }




}
