package kr.hs.entrydsm.husky.domain.auth.service.email;

public interface EmailService {
    void sendEmail(String email, String code);
    void sendPasswordChangeEmail(String email, String code);
}
