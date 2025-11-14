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

    //선택항목
    private String characterDescription;

    //Setter 메서드
    public void setCharacterName() {

    }


    //Getter 메서드
    public String getCharacterName() {
        return characterName;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getCharacterDescription() {
        return characterDescription;
    }


}
