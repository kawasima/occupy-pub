package net.unit8.occupypub.cert;

import net.unit8.occupypub.model.User;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.junit.Test;

import java.math.BigInteger;
import java.security.KeyPair;

/**
 * @author kawasima
 */
public class GeneratorTest {
    @Test
    public void testGenerator() throws Exception {
        CertificationAuthority certificationAuthority = new CertificationAuthority();
        User user = new User();
        user.setCountryName("JP");
        user.setProvinceName("Tokyo");
        user.setLocalityName("Shinjuku-ku");
        user.setOrganizationName("TIS");
        user.setOrganizationUnitName("ADC");
        user.setEmailAddress("kawasima1016@gmail.com");
        user.setCommonName("kawasima");

        certificationAuthority.generateCA();
        KeyPair pair = certificationAuthority.generateKey();
        PKCS10CertificationRequest csr = certificationAuthority.generateCSR(user, pair);
        certificationAuthority.generateCertificate(csr, BigInteger.ONE, 3);
    }
}
