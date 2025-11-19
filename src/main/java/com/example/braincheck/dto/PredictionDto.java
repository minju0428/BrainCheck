package com.example.braincheck.dto;
//사용자가 고른 엠비티아이 저장하는 틀 코드
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class PredictionDto {

    //필드 선언
    private String ei;
    private String sn;
    private String tf;
    private String jp;


    //스프링이 데이터를 매핑할 수 있도록 하는 것들
    //Getter
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
