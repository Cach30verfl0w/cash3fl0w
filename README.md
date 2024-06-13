# Cash3Fl0w
![kmp-advcrypto actions badge](https://github.com/cach30verfl0w/cash3fl0w/actions/workflows/kmp-advcrypto.yml/badge.svg) [![kmp-advcrypto License](https://img.shields.io/badge/License-Apache_2.0-blue.svg?label=kmp-advcrypto%20License)](https://apache.org/licenses/LICENSE-2.0) [![cash3fl0w License](https://img.shields.io/badge/License-GPLv3-blue.svg?label=cash3fl0w%20License)](https://www.gnu.org/licenses/gpl-3.0)

Cash3Fl0w is a finance software developed with Kotlin Multiplatform for mobile devices and desktop/JVM. This application supports FinTS/HBCI to access information from German banking accounts. This application doesn't use any centralized storage or third-party environment to execute requests etc. Only your handy is used.

## HBCI/FinTS Support
This application supports the FinTS protocol (formerly known as HBCI) specified by the [German Banking Industry Committee](https://die-dk.de/) with support for over 2000 banks in Germany. But [since 2019 the protocol can only be used with authorized products](https://www.hbci-zka.de/register/register_faq.htm). Before you can use the FinTS support, you have to:
1. Go on the site of the German Banking Industry Committee and [register a FinTS product](https://www.hbci-zka.de/register/prod_register.htm) 2. After a successful registration you receive a FinTS product number and a list of all public FinTS accesses registered. This product id must be set in a settings file and the bank list must be added to the application (before compilation) *Coming soon*

## License
Please respect the following licenses and the work behind this project:
- GNU General Public License 2.0 for the app itself
- Apache License 2.0 for the cryptography API (`kmp-advcrypto`) used in the app

