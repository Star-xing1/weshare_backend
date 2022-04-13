package com.weshare.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

// 生成4位随机验证码
@Component
public class Verification_code {

    private String str="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private StringBuilder token=new StringBuilder(6);

    public Verification_code() {
    }

    public String creatCAPTCHA(){
        for(int i=0;i<6;i++)
        {
            char ch=str.charAt(new Random().nextInt(str.length()));
            token.append(ch);
        }
        return token.toString();
    }
}
