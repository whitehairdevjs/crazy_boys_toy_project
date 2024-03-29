package com.example.crazyProject.sign.service;

import com.example.crazyProject.sign.domain.User;


public interface SignService {

    public int idCheck(String userId);
    public int signUp(User user);

    public int signIn(String userId, String password);

    public void logout();

    public int withdrawal(String userId, String password);

    public void insertLog(String userId, String password, String ip);
}
