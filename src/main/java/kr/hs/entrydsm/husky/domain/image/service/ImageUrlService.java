package kr.hs.entrydsm.husky.domain.image.service;

import org.jets3t.service.CloudFrontServiceException;

import java.io.IOException;
import java.text.ParseException;

public interface ImageUrlService {

    String generateObjectUrl(String objectName) throws ParseException, CloudFrontServiceException;

}
