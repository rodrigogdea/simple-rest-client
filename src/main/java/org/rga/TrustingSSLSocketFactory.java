package org.rga;

import org.apache.http.HttpHost;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;

import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * TrustingSSLSocketFactory permite crear conexiones SSL que aceptan cualquier certificado autofirmado.
 *
 * @since 1.0
 */
public class TrustingSSLSocketFactory extends SocketFactory implements LayeredConnectionSocketFactory {
    private static final TrustingSSLSocketFactory instance = new TrustingSSLSocketFactory();
    private final SSLSocketFactory sslSocketFactory;

    private TrustingSSLSocketFactory() {
        X509TrustManager trustManager = new TrustingX509TrustManager();

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(new KeyManager[0], new TrustManager[]{trustManager}, new SecureRandom());

            sslSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Obtiene la instancia de {@code TrustingSSLSocketFactory}
     *
     * @return TrustingSSLSocketFactory
     */
    public static TrustingSSLSocketFactory get() {
        return instance;
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslSocketFactory.createSocket();
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return sslSocketFactory.createSocket(host, port);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        return sslSocketFactory.createSocket(host, port, localHost, localPort);
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return sslSocketFactory.createSocket(host, port);
    }

    @Override
    public Socket createSocket(InetAddress host, int port, InetAddress localHost, int localPort) throws IOException {
        return sslSocketFactory.createSocket(host, port, localHost, localPort);
    }

    @Override
    public Socket createLayeredSocket(Socket socket, String target, int port, HttpContext context) throws IOException, UnknownHostException {
        return sslSocketFactory.createSocket(socket, target, port, true);
    }

    @Override
    public Socket createSocket(HttpContext context) throws IOException {
        return sslSocketFactory.createSocket();
    }

    @Override
    public Socket connectSocket(int connectTimeout, Socket sock, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException {
        return sslSocketFactory.createSocket(host.getHostName(), host.getPort());
    }

    private static class TrustingX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
