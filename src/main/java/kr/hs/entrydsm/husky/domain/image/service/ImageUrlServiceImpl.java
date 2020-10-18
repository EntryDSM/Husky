package kr.hs.entrydsm.husky.domain.image.service;

import kr.hs.entrydsm.husky.domain.image.util.PrivateKeyLoader;
import lombok.RequiredArgsConstructor;
import org.jets3t.service.CloudFrontService;
import org.jets3t.service.CloudFrontServiceException;
import org.jets3t.service.utils.ServiceUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.Security;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ImageUrlServiceImpl implements ImageUrlService {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Value("${aws.cloudfront.domain}")
    private String distributionDomain;

    @Value("${aws.cloudfront.key_id}")
    private String keyPairId;

    @Value("${aws.cloudfront.exp}")
    private int exp;

    private byte[] derPrivateKey;

    @Override
    public String generateObjectUrl(String objectName)
            throws ParseException, CloudFrontServiceException {

        String policyResourcePath = "https://" + distributionDomain + "/" + objectName;

        ZonedDateTime nowUTC = ZonedDateTime.now(ZoneId.of("UTC")).plusMinutes(exp);
        String dateString = dateFormat.format(nowUTC);

        return CloudFrontService.signUrlCanned(
                policyResourcePath,
                keyPairId,
                derPrivateKey,
                ServiceUtils.parseIso8601Date(dateString)
        );
    }

    @PostConstruct
    public void initPrivateKey() throws IOException {
        PrivateKeyLoader privateKeyLoader = new PrivateKeyLoader();
        this.derPrivateKey = privateKeyLoader.loadPrivateKey();

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

}
