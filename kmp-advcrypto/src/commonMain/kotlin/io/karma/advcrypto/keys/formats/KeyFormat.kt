/*
 * Copyright (c) 2024 Cach30verfl0w
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.karma.advcrypto.keys.formats

/**
 * This enum contains all key file/data formats supported by this library. These formats are used to
 * detect the format of a key in the memory or in a file. This is also used for conversion between
 * key formats.
 *
 * @author Cedric Hammes
 * @since  13/06/2024
 */
enum class KeyFormat {

    /**
     *  This format can contain private keys (RSA or DSA), public keys (RSA or DSA) and X.509
     *  certificates. It is header-less. It is the default format for most browsers. A file can
     *  contain only one certificate. Optionally the certificate can be encrypted. The standard
     *  extension is .cer, but might be .der in some installations.
     *
     *  @see https://wiki.openssl.org/index.php/DER
     */
    DER,

    /**
     * This format can contain private keys (RSA or DSA), public keys (RSA or DSA) and X.509
     * certificates. It is the default format for OpenSSL. It stores the data in either ASN.1
     * or DER format, surrounded by ASCII headers, so is suitable for sending files as text
     * between systems. A file can contain multiple certificates. The standard extension is
     * pem.
     *
     * @see https://en.wikipedia.org/wiki/Privacy-Enhanced_Mail
     * @see https://wiki.openssl.org/index.php/PEM
     */
    PEM,

    /**
     * This is the Cryptographic Message Syntax Standard. A file can contain multiple certificates.
     * Optionally they can be hashed. Optionally a certificate can be accompanied by a private key.
     * As well as the original PKCS #7, there are three revisions: a, b, and c. The standard
     * extensions for these four versions are .spc, .p7a, .p7b and .p7c respectively.
     *
     * @see https://en.wikipedia.org/wiki/PKCS_7
     * @see https://datatracker.ietf.org/doc/html/rfc2315
     */
    PKCS7,

    /**
     * This format can contain private keys and encrypted private key information. It stores the
     * data in base64 encoded data, usually using a DER or PEM structure which is then encrypted.
     * The standard extension is p8.
     *
     * @see https://en.wikipedia.org/wiki/PKCS_8
     * @see https://datatracker.ietf.org/doc/html/rfc5208
     */
    PKCS8,

    /**
     * This is also known as PFX. This format can contain private keys (RSA or DSA), public keys
     * (RSA or DSA) and X.509 certificates. It stores them in a binary format. The standard
     * extension is pfx or p12.
     *
     * @see https://en.wikipedia.org/wiki/PKCS_12
     * @see https://datatracker.ietf.org/doc/html/rfc7292
     */
    PKCS12

}