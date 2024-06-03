
# Cash3Fl0w  
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)  
  
Cash3Fl0w is a finance software developed with Kotlin Multiplatform for mobile devices. This application supports FinTS/HBCI to access information from German banking accounts. This application doesn't use any centralized storage or third-party environment to execute requests etc. Only your handy is used.  
  
## HBCI/FinTS Support  
This application supports the FinTS protocol (formally known as HBCI) specified by the [German Banking Industry Committee](https://die-dk.de/) with support for over 2000 banks in Germany. But [since 2019 the protocol can only be used with authorized products](https://www.hbci-zka.de/register/register_faq.htm). Before you can use the FinTS support, you have to:  
1. Go on the site of the German Banking Industry Committee and [register a FinTS product](https://www.hbci-zka.de/register/prod_register.htm)  
2. After a successful registration you receive a FinTS product number and a list of all public FinTS accesses registered. This product id must be set in a settings file and the bank list must be added to the application (before compilation) *Coming soon*

## Security
This app is made to provide a maximum security to financial and personal information. It doesn't send any
information to third party servers and only establishes connections to the servers of the connected accounts, which provides a lower risk of information leakages.
- On Android the app uses the [KeyStore system](https://developer.android.com/privacy-and-security/keystore) to provide a higher security for keys and keypairs used by the application. Most keys are locked behind an authentication if enabled
- On JVM environments, the application checks whether a TPM is available or not. Without a TPM we store the data in a more insecure data store. With TPM, the keys are stored on the TPM itself and are generated with the key generator of the TPM itself
- For the data transfer feature, the protocol **TLS 1.3**/**TLS 1.2** is used to transfer the data between the devices. This feature only works for devices in the same network
- The user has the ability to lock the application behind an extra layer of authentication to prevent unauthorized access to the application and lock keys behind the authentication scheme
