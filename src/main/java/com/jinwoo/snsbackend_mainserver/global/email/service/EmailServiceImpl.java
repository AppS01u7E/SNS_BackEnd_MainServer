package com.jinwoo.snsbackend_mainserver.global.email.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSenderImpl mailSender;


    @Value("${email.name}")
    private String from;
    @Value("${email.password}")
    private String password;




    @Override
    public String sendEmail(String email, String random) throws MessagingException, UnsupportedEncodingException {

        mailSender.setUsername(from);
        mailSender.setPassword(password);
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        mailSender.setJavaMailProperties(properties);

        StringBuilder body = new StringBuilder();
        body.append("<body>\n" +
                "    <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n" +
                "    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n" +
                "    <link href=\"https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500&display=swap\" rel=\"stylesheet\">\n" +
                "    \n" +
                "    <div style=\"\n" +
                "    align-items: center;\n" +
                "    width: 700px;\n" +
                "    font-family: 'Noto Sans KR', sans-serif;\">\n" +
                "\n" +
                "    <div style=\"            \n" +
                "    background-color: #4993FA;\n" +
                "    width: 1100px;\n" +
                "    height: 60px;\">\n" +
                "    </div>\n" +
                "\n" +
                "    <div style=\"\n" +
                "    background-color: #eeeeee;\n" +
                "    width: 1100px;\n" +
                "    height: 750px;\n" +
                "    padding: 0px 200px;\n" +
                "    box-sizing: border-box;\n" +
                "    padding-top: 80px;\">\n" +
                "\n" +
                "    <div style=\"            \n" +
                "    background-color: white;\n" +
                "    height: 400px;\n" +
                "    width: 700px;\n" +
                "    align-items: center;\n" +
                "    border-radius: 10px;\n" +
                "    position: relative;\">\n" +
                "\n" +
                "        <img style=\"height: 60px; width: 150px; position: relative; margin-left: 280px; margin-top: 20px;\" src=\"https://sns2ata.s3.ap-northeast-2.amazonaws.com/SERVER_DATA/258342860_906801423594090_4534721152852898421_n.png\"></img>\n" +
                "        <h1 style=\"margin: 0; position: relative; margin-top: 20px; margin-left: 290px; width: 150px;\">인증번호</h1>\n" +
                "        <p style=\"position: relative; margin-top: 20px; margin-left: 150px; width: 200px; text-align: center; position: relative; margin-left: 250px;\">고객님의 인증 번호는 다음과 같습니다.</p>\n" +
                "        <div style=\"margin-top: 50px;\">\n" +
                "        <strong style=\"        \n" +
                "        background-color: rgb(249, 249, 249);\n" +
                "        border: 1px solid rgb(235, 235, 235);\n" +
                "        padding: 10px 30px;\n" +
                "        letter-spacing: 5px; color: dimgray;\n" +
                "        font-size: 30px; position: relative;\n" +
                "        margin-left: 260px;\n" +
                "        width: 150px;\">"+random+"" +
                "        </strong>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <div style=\"position: relative; margin-left: 90px; margin-top: 50px\">\n" +
                "        <p style=\"text-align: center; position: relative; margin-top: 10px; color: gray; width: 500px;\">본인이 시도한 것이 아니라면, 지원팀에 문의해 주세요.</p>\n" +
                "        <p style=\"text-align: center; position: relative; margin-top: 20px; color: gray; width: 500px;\">© 2021 SNS, All Rights Reserved       지원팀: dsm.info.appsolute@gmail.com</p>\n" +
                "        <p style=\"text-align: center; position: relative; margin-top: 30px; color: gray; width: 500px;\">대한민국 대전광역시 유성구 장동 가정북로 76</p>\n" +
                "    </div>\n" +
                "    </div>\n" +
                "    </div> \n" +
                "</body>\n");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");

        mimeMessageHelper.setFrom(from,"SNS_INFO");

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("인증번호");
        mimeMessageHelper.setText(body.toString(), true);


        mailSender.send(message);
        return random;
    }


}
