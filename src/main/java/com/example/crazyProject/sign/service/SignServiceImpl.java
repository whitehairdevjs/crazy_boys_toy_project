package com.example.crazyProject.sign.service;

import com.example.crazyProject.mapper.sign.UserMapper;
import com.example.crazyProject.sign.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * 회원가입, 로그인, 로그아웃
 * @author minhyuk
 * */
@Service
@Transactional
public class SignServiceImpl implements SignService{

    @Autowired
    UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /** 아이디 중복 체크
     * @author mhlee
     * @return int (0:가입가능, 1:가입불가능)*/
    @Override
    public int idCheck(String userId) {
        int result = 1;
        HashMap<String,Object> resMap = userMapper.idCheck(userId);
        // db기준 number은 바로 int로 캐스트가 안됨
        result = Integer.parseInt(String.valueOf(resMap.get("chk")));
        return result;
    }

    /** 회원가입
     * @author mhlee
     * @return int (1:성공)*/
    @Transactional
    @Override
    public int signUp(User user) {
        int result = 0;
        String realPw = user.getPassword();
        String encodePw = passwordEncoder.encode(realPw);
        user.setPassword(encodePw);
        result = userMapper.signUp(user);
        return result;
    }

    /** 로그인
     * @author mhlee
     * @return int (1:성공,2:비밀번호불일치,3:아이디존재x)
     * */
    @Override
    public int signIn(String userId, String password) {
        int result = 0;
        HashMap<String, Object> resMap = userMapper.signIn(userId);
        if(resMap == null) { //아이디없음
            result = 3;
        } else {
            String encodePw = (String) resMap.get("password");
            if (passwordEncoder.matches(password, encodePw)) {
                result = 1;
            } else {
                result = 2;
            }
        }
        return result;
    }

    /** 로그아웃 */
    @Override
    public void logout() {

    }

    @Override
    public int withdrawal(String userId, String password){
        int result = 0;
        HashMap<String, Object> resMap = userMapper.signIn(userId);
        String encodePw = (String) resMap.get("password");
        if (passwordEncoder.matches(password, encodePw)) {
            //탈퇴성공
            userMapper.withdrawal(userId);
            result = 1;
        } else {
            //탈퇴실패
            result = 2;
        }
        return result;
    }

    @Override
    public void insertLog(String userId, String password, String ip){
        userMapper.insertLog(userId,password,ip);
    }
}
