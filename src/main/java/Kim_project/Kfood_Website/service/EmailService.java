package Kim_project.Kfood_Website.service;

import Kim_project.Kfood_Website.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@Transactional
public class EmailService {
    private final JavaMailSender javaMailSender;

    //인증 문구에 사용될 문자 (영문 소문자 및 숫자)
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    
    //인증 문구 길이
    private static final int STRING_LENGTH = 6;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    //인증용 랜덤문자 만들기
    public String randomAuth() {
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < STRING_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }
    
    //계정가입시 이메일 인증용 메일보내기
    public String sendEmail(String emailAddress) {
        SimpleMailMessage message = new SimpleMailMessage();

        //네이버는 setFrom해줘야 보안주체에 안걸린다. (application.yaml의 유저세팅이랑 반드시 같아야한다)
        message.setFrom("rlatjswo159874@naver.com");
        //메일 받는사람
        message.setTo(emailAddress);
        //메일제목
        message.setSubject("Email verification for K-food Recipe");

        //인증문자
        String text = randomAuth();
        message.setText("Your authentication text is... [ " + text + " ]");
        javaMailSender.send(message);
        
        //인증문자 반환
        return text;
    }

    //패스워드 찾기 이메일 보내기
    public void sendPasswordEmail(String emailAddress, Member member) {
        SimpleMailMessage message = new SimpleMailMessage();

        //네이버는 setFrom해줘야 보안주체에 안걸린다. (application.yaml의 유저세팅이랑 반드시 같아야한다)
        message.setFrom("rlatjswo159874@naver.com");
        //메일 받는사람
        message.setTo(emailAddress);
        //메일제목
        message.setSubject("Password for K-food Recipe");

        String text = "Your Password is... [ " + member.getMemberPassword() + " ]";

        message.setText(text);
        javaMailSender.send(message);
    }

}