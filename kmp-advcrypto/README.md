# kmp-advcrypto (WIP)
kmp-advcrypto is a cross-platform cryptography library developed with Kotlin Multiplatform. This library allows to perform cryptographic operations on Desktop and Mobile operating systems without developing a crypto module for separate platforms.

## Supported algorithms
Below this text is a list of different category of algorithms and a list with the supported algorithms:
- **Hash Functions:** MD5, SHA-1, SHA-224, SHA-256, SHA-384 and SHA-512
- **Signatures:** CRYSTALS-Dilithium 2, 3 and 5 (Experimental)
- **Ciphers:** AES and RSA

## Dependencies
This library uses a few dependencies to provide correctly-implemented cryptographic algorithms and implementations for you. Below this text, you can see a list of these dependencies:
| Library | Author | Platform  | License
|-|-|-|-|
| [OpenSSL](https://github.com/openssl/openssl) | [OpenSSL](https://github.com/openssl) | Linux | [Apache License 2.0](https://github.com/openssl/openssl/blob/master/LICENSE.txt) |
| [Bouncycastle](https://github.com/bcgit/bc-java) | [Legion of the Bouncy Castle Inc](https://github.com/bcgit) | Android and JVM | [MIT License](https://github.com/bcgit/bc-java/blob/main/LICENSE.md) |

## License
Please respect the license and the work behind this project. This project is licensed under the [Apache-2.0 License](https://github.com/Cach30verfl0w/cash3fl0w/blob/main/kmp-advcrypto/LICENSE.md).
