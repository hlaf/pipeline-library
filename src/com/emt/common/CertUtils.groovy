package com.emt.common


class CertUtils implements Serializable {
    static String getSslKeyPath(String cert_name) {
        return "/etc/pki/tls/private/${cert_name}.privkey.pem"
    }

    static String getSslCertPath(String cert_name) {
        return "/etc/pki/tls/certs/${cert_name}.pem"
    }
    
}
