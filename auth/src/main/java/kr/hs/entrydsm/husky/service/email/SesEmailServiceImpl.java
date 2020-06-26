package kr.hs.entrydsm.husky.service.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SesEmailServiceImpl implements EmailService {

    @Value("${auth.email.send-email}")
    private String senderEmail;

    private final AmazonSimpleEmailServiceAsync amazonSimpleEmailServiceAsync;

    @Override
    public void sendEmail(String receiveEmail, String code) {
        String emailSubject = "Amazon SES test (AWS SDK for Java)";
        String emailContent = createHtml(code);


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

    private String createHtml(String code) {
        return "";
    }

}
