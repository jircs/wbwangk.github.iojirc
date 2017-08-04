/*
 * Created on 3-okt-07
 * 来源：http://javasecurity.wikidot.com/example-item-1
 */
package be.lovadina.test.https;
 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.security.Certificate;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
 
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
 
/**
 * @author theSecurityDuke
 *
 */
public class MutualAuthenticationHTTP {
    private static final boolean debug = true;
 
    public static void main(String[] args) {
        String url = "https://www.google.com/";
        String keyStoreFileName = "Y:\\deleteme\\key.jks";
        String keyStorePassword = "WebAS";
        String trustStoreFileName = "Y:\\deleteme\\key.jks";
        String trustStorePassword = "WebAS";
        String alias = "mycert001";
 
        try {
            //create key and trust managers
            KeyManager[] keyManagers = createKeyManagers(keyStoreFileName, keyStorePassword, alias);
            TrustManager[] trustManagers = createTrustManagers(trustStoreFileName, trustStorePassword);
            //init context with managers data   
            SSLSocketFactory factory = initItAll(keyManagers, trustManagers);
            //get the url and display content
            doitAll(url, factory);
 
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
 
    private static void doitAll(String urlString, SSLSocketFactory sslSocketFactory) throws IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) connection).setSSLSocketFactory(sslSocketFactory);
        }
        int x;
        while ((x = ((InputStream) connection.getContent()).read()) != -1) {
            System.out.print(new String(new byte[] {(byte) x }));
        }
    }
 
    private static SSLSocketFactory initItAll(KeyManager[] keyManagers, TrustManager[] trustManagers)
        throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("SSLv3");
        //TODO investigate: could also be "SSLContext context = SSLContext.getInstance("TLS");" Why?
        context.init(keyManagers, trustManagers, null);
        SSLSocketFactory socketFactory = context.getSocketFactory();
        return socketFactory;
    }
 
    private static KeyManager[] createKeyManagers(String keyStoreFileName, String keyStorePassword, String alias)
        throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        //create Inputstream to keystore file
        java.io.InputStream inputStream = new java.io.FileInputStream(keyStoreFileName);
        //create keystore object, load it with keystorefile data
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(inputStream, keyStorePassword == null ? null : keyStorePassword.toCharArray());
        //DEBUG information should be removed
        if (debug) {
            printKeystoreInfo(keyStore);
        }
 
        KeyManager[] managers;
        if (alias != null) {
            managers =
                new KeyManager[] {
                     new MutualAuthenticationHTTP().new AliasKeyManager(keyStore, alias, keyStorePassword)};
        } else {
            //create keymanager factory and load the keystore object in it 
            KeyManagerFactory keyManagerFactory =
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyStorePassword == null ? null : keyStorePassword.toCharArray());
            managers = keyManagerFactory.getKeyManagers();
        }
        //return 
        return managers;
    }
 
    private static TrustManager[] createTrustManagers(String trustStoreFileName, String trustStorePassword)
        throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        //create Inputstream to truststore file
        java.io.InputStream inputStream = new java.io.FileInputStream(trustStoreFileName);
        //create keystore object, load it with truststorefile data
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(inputStream, trustStorePassword == null ? null : trustStorePassword.toCharArray());
        //DEBUG information should be removed
        if (debug) {
            printKeystoreInfo(trustStore);
        }
        //create trustmanager factory and load the keystore object in it 
        TrustManagerFactory trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        //return 
        return trustManagerFactory.getTrustManagers();
    }
 
    private static void printKeystoreInfo(KeyStore keystore) throws KeyStoreException {
        System.out.println("Provider : " + keystore.getProvider().getName());
        System.out.println("Type : " + keystore.getType());
        System.out.println("Size : " + keystore.size());
 
        Enumeration en = keystore.aliases();
        while (en.hasMoreElements()) {
            System.out.println("Alias: " + en.nextElement());
        }
    }
    private class AliasKeyManager implements X509KeyManager {
 
        private KeyStore _ks;
        private String _alias;
        private String _password;
 
        public AliasKeyManager(KeyStore ks, String alias, String password) {
            _ks = ks;
            _alias = alias;
            _password = password;
        }
 
        public String chooseClientAlias(String[] str, Principal[] principal, Socket socket) {
            return _alias;
        }
 
        public String chooseServerAlias(String str, Principal[] principal, Socket socket) {
            return _alias;
        }
 
        public X509Certificate[] getCertificateChain(String alias) {
            try {
                java.security.cert.Certificate[] certificates = this._ks.getCertificateChain(alias);
                if(certificates == null){throw new FileNotFoundException("no certificate found for alias:" + alias);}
                X509Certificate[] x509Certificates = new X509Certificate[certificates.length];
                System.arraycopy(certificates, 0, x509Certificates, 0, certificates.length);
                return x509Certificates;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
 
        public String[] getClientAliases(String str, Principal[] principal) {
            return new String[] { _alias };
        }
 
        public PrivateKey getPrivateKey(String alias) {
            try {
                return (PrivateKey) _ks.getKey(alias, _password == null ? null : _password.toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
 
        public String[] getServerAliases(String str, Principal[] principal) {
            return new String[] { _alias };
        }
 
    }
}
