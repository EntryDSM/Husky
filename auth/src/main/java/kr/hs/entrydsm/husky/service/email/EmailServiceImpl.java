package kr.hs.entrydsm.husky.service.email;

import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendEmail(String email, String code) {
        // 인증 메일 보내는 로직
    }

}
