package com.emt.common

class CertUtils implements Serializable {
    @CoverageIgnoreGenerated
    static String getSslKeyPath(String cert_name) {
        return "/etc/pki/tls/private/${cert_name}.privkey.pem"
    }
    @CoverageIgnoreGenerated
    static String getSslCertPath(String cert_name) {
        return "/etc/pki/tls/certs/${cert_name}.pem"
    }
    
}
