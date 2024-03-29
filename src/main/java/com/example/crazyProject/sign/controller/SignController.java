package com.example.crazyProject.sign.controller;

import com.example.crazyProject.sign.domain.User;
import com.example.crazyProject.sign.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 로그인관련
 * @author minhyuk
 * @version 2023.02.04 최초작성
 * */
@RestController
@RequestMapping(value = "/sign/")
public class SignController {

    @Autowired
    SignService signService;


    /**
     * id중복확인
     * @author minhyuk
     * @return int (0:가입가능,1:가입불가능)
     * */
    @RequestMapping(value = "idCheck", method = RequestMethod.POST)
    public int idCheck(@RequestBody User user) {
        String userId = user.getUser_id();
        return signService.idCheck(userId);
    }

    /**
     * 회원가입
     * @author minhyuk
     * @return int (1:가입성공)
     * */
    @RequestMapping(value = "signUp", method = RequestMethod.POST)
    public int signUp(@RequestBody User user) {
        return signService.signUp(user);
    }

    /**
     * 로그인
     * @author minhyuk
     * @return String (페이지이동)
     * */
    @RequestMapping(value = "signIn", method = RequestMethod.POST)
    public String signIn(@RequestBody HashMap<String, Object> hMap) {
        HashMap<String, Object> userMap = (HashMap<String,Object>)hMap.get("datas");
        String ip = (String)hMap.get("ip");
        String userId = (String)userMap.get("user_id");
        String password = (String)userMap.get("password");
        int result = signService.signIn(userId, password);
//        로그쌓기
        signService.insertLog(userId,password,ip);
        if(result == 1) {
            return "로그인 성공";
        } else if(result == 2){
            return "비빌번호가 일치하지 않습니다.";
        } else {
            return "아이디가 존재하지 않습니다.";
        }
    }

    @RequestMapping(value = "withdrawal", method = RequestMethod.POST)
    public String withdrawal(@RequestBody User user){
        String userId = user.getUser_id();
        String password= user.getPassword();
        int result = signService.signIn(userId, password);
        if(result == 1) {
            signService.withdrawal(userId,password);
            return "회원탈퇴가 완료되었습니다.";
        } else {
            return "비밀번호가 일치하지 않습니다.";
        }
    }
}
