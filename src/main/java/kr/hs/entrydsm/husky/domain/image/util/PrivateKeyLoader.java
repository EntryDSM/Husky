package kr.hs.entrydsm.husky.domain.image.util;

import org.jets3t.service.utils.ServiceUtils;

import java.io.FileInputStream;
import java.io.IOException;

public class PrivateKeyLoader {

    private final String privateKeyFilePath = this.getClass().getResource("/secrets/secret.der").getPath();

    public byte[] loadPrivateKey() throws IOException {
        return ServiceUtils.readInputStreamToBytes(new FileInputStream(privateKeyFilePath));
    }

}
