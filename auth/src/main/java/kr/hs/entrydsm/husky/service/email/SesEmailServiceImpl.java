package kr.hs.entrydsm.husky.service.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.model.*;
import kr.hs.entrydsm.husky.exceptions.VerifyEmailGenerateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
public class SesEmailServiceImpl implements EmailService {

    @Value("${auth.email.send-email}")
    private String senderEmail;

    private final AmazonSimpleEmailServiceAsync amazonSimpleEmailServiceAsync;

    @Override
    public void sendEmail(String receiveEmail, String code) {
        String emailSubject = "Amazon SES test (AWS SDK for Java)";
        String emailContent = createContent(code);


        SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(receiveEmail))
                .withMessage(
                        new Message()
                        .withSubject(new Content().withCharset("UTF-8").withData(emailSubject))
                        .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(emailContent)))
                )
                .withSource(senderEmail);


        amazonSimpleEmailServiceAsync.sendEmailAsync(request);

    }

    private String createContent(String code) {
        StringBuilder result = new StringBuilder();
        ClassPathResource resource = new ClassPathResource("verifyEmailForm.html");
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            bufferedReader.lines().forEach(result::append);
        } catch (IOException e) {
            throw new VerifyEmailGenerateException();
        }
        return result.toString().replace("{code}", code);
    }

}
