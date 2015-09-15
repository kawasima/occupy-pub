package net.unit8.occupypub.cert;

import net.unit8.occupypub.model.Cert;
import net.unit8.occupypub.model.User;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.*;
import org.bouncycastle.pkcs.bc.BcPKCS12MacCalculatorBuilder;
import org.bouncycastle.pkcs.bc.BcPKCS12PBEOutputEncryptorBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS12SafeBagBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author kawasima
 */
public class CertificationAuthority {
    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    private Path pemPath;
    private Path keyPath;

    public CertificationAuthority() {
    }

    public CertificationAuthority(Path pemPath, Path keyPath) {
        this.pemPath = pemPath;
        this.keyPath = keyPath;
    }

    public KeyPair generateKey() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA", "BC");
        return kpGen.generateKeyPair();
    }

    public PKCS10CertificationRequest generateCSR(User user, KeyPair key) throws OperatorCreationException {
        X500Name x500User = new X500NameBuilder()
                .addRDN(BCStyle.C, user.getCountryName())
                .addRDN(BCStyle.ST, user.getProvinceName())
                .addRDN(BCStyle.L,  user.getLocalityName())
                .addRDN(BCStyle.O,  user.getOrganizationName())
                .addRDN(BCStyle.OU, user.getOrganizationUnitName())
                .addRDN(BCStyle.CN, user.getCommonName())
                .addRDN(BCStyle.EmailAddress, user.getEmailAddress())
                .build();
        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
                x500User, key.getPublic());
        user.setPrivateKey(key.getPrivate().getEncoded());
        JcaContentSignerBuilder csBuilder= new JcaContentSignerBuilder("SHA512WithRSAEncryption");
        ContentSigner signer = csBuilder.build(key.getPrivate());
        return p10Builder.build(signer);
    }

    protected void generateCA() throws NoSuchProviderException, NoSuchAlgorithmException, IOException, OperatorCreationException {
        KeyPair pair = generateKey();
        LocalDateTime startDate = LocalDate.now().atStartOfDay();

        X509v3CertificateBuilder builder= new X509v3CertificateBuilder(
                new X500Name("CN=ca"),
                new BigInteger("0"),
                Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(startDate.plusDays(3650).atZone(ZoneId.systemDefault()).toInstant()),
                new X500Name("CN=ca"),
                SubjectPublicKeyInfo.getInstance(pair.getPublic().getEncoded()));
        JcaContentSignerBuilder csBuilder= new JcaContentSignerBuilder("SHA512WithRSAEncryption");
        ContentSigner signer = csBuilder.build(pair.getPrivate());
        X509CertificateHolder holder = builder.build(signer);
        try (PemWriter writer = new PemWriter(new FileWriter(pemPath.toFile()))) {
            writer.writeObject(new PemObject("CERTIFICATE", holder.toASN1Structure().getEncoded()));
        }
        try (PemWriter writer = new PemWriter(new FileWriter(keyPath.toFile()))) {
            writer.writeObject(new PemObject("PRIVATE KEY", pair.getPrivate().getEncoded()));
        }
    }
    protected X509CertificateHolder readCertificate() throws IOException, CertificateException {
        try (PemReader reader = new PemReader(Files.newBufferedReader(pemPath))) {
            PemObject pem = reader.readPemObject();
            return new X509CertificateHolder(pem.getContent());
        }
    }

    protected AsymmetricKeyParameter readKey(Path keyPath) throws IOException {
        try (PemReader reader = new PemReader(Files.newBufferedReader(keyPath))) {
            PemObject pem = reader.readPemObject();
            return PrivateKeyFactory.createKey(pem.getContent());
        }
    }
    public X509Certificate generateCertificate(PKCS10CertificationRequest csr, BigInteger serial, int expireDays) throws NoSuchProviderException, NoSuchAlgorithmException, IOException, OperatorCreationException, CertificateException {
        AlgorithmIdentifier sigAlgorithmId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA512WithRSAEncryption");
        AlgorithmIdentifier digestAlgorithmId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgorithmId);

        X509CertificateHolder caCert = readCertificate();
        System.out.println(caCert.getSubject());

        LocalDateTime startDate = LocalDate.now().atStartOfDay();
        X509v3CertificateBuilder builder= new X509v3CertificateBuilder(
                caCert.getSubject(),
                serial,
                Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(startDate.plusDays(expireDays).atZone(ZoneId.systemDefault()).toInstant()),
                csr.getSubject(),
                SubjectPublicKeyInfo.getInstance(csr.getSubjectPublicKeyInfo()));

        AsymmetricKeyParameter caPrivateKeyParameters = readKey(keyPath);
        ContentSigner contentSigner = new BcRSAContentSignerBuilder(sigAlgorithmId, digestAlgorithmId)
                .build(caPrivateKeyParameters);
        X509CertificateHolder holder = builder.build(contentSigner);
        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(holder);
    }

    public PKCS12PfxPdu generatePKCS12(X509Certificate caCert, Cert clientCert, String password) throws IOException, CertificateException, PKCSException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS12SafeBagBuilder caCertBagBuilder = new JcaPKCS12SafeBagBuilder(caCert);
        X509CertificateHolder clientCertHolder =new X509CertificateHolder(clientCert.getClientCert());
        PKCS12SafeBagBuilder clientCertBagBuilder = new JcaPKCS12SafeBagBuilder(new JcaX509CertificateConverter().setProvider("BC").getCertificate(clientCertHolder));

        PKCS12SafeBagBuilder keyBagBuilder = new JcaPKCS12SafeBagBuilder(
                KeyFactory.getInstance("RSA", "BC").generatePrivate(new PKCS8EncodedKeySpec(clientCert.getPrivateKey())),
                new BcPKCS12PBEOutputEncryptorBuilder(
                        PKCSObjectIdentifiers.pbeWithSHAAnd3_KeyTripleDES_CBC,
                        new CBCBlockCipher((new DESedeEngine()))).build(password.toCharArray()));

        PKCS12PfxPduBuilder pfxPduBuilder = new PKCS12PfxPduBuilder();
        PKCS12SafeBag[] certs = new PKCS12SafeBag[2];
        certs[0] = clientCertBagBuilder.build();
        certs[1] = caCertBagBuilder.build();
        pfxPduBuilder.addData(keyBagBuilder.build());
        return pfxPduBuilder.build(new BcPKCS12MacCalculatorBuilder(), password.toCharArray());
    }
}
