package kr.hs.entrydsm.husky.domain.image.util;

import org.jets3t.service.utils.ServiceUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class PrivateKeyLoader {

    public byte[] loadPrivateKey() throws IOException {
        return ServiceUtils.readInputStreamToBytes(new ClassPathResource("/secrets/secret.der").getInputStream());
    }

}
