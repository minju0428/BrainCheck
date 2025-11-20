package com.example.braincheck.dto;
//사용자가 고른 엠비티아이 저장하는 틀 코드
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor //기본 생성자를 자동으로 생성하는 어노테이션
public class PredictionDto {

    //전달받은 필드 선언
    @NotBlank(message = "등장인물 이름은 필수 입력 항목입니다.")
    private String characterName;
    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private String title;
    @NotBlank(message = "카테고리를 선택해 주세요.")
    private String category;

    //필드 선언
    @NotNull(message = "에너지 방향은 필수 항목입니다.")
    private String ei;
    @NotNull(message = "인식 기능은 필수 항목입니다.")
    private String sn;
    @NotNull(message = "판단은 필수 항목입니다.")
    private String tf;
    @NotNull(message = "생활 양식은 필수 항목입니다.")
    private String jp;


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

}
