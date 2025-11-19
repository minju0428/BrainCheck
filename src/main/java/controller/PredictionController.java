package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PredictionController {
    @GetMapping("/prediction")
    public String prediction() {

        //mbtiPredictionActivity.html 연결
        return "mbtiPredictionActivity";
    }
}
