# IronCore Customer Managed Keys Overview

IronCore's solution for Customer Managed Keys (CMK) is targeted towards SaaS providers looking to accelerate their roadmap to meet their customers' increasing demands for privacy and security of their cloud data. IronCore adds a number of features and integrations and handles a variety of supported Key Management Systems (KMS) including [Google Cloud KMS](https://cloud.google.com/kms), [Amazon KMS](https://aws.amazon.com/kms/), and [Azure Key Vault](https://azure.microsoft.com/en-us/services/key-vault), so your customers can use their platform of choice.

IronCore also protects the KMS configurations using end-to-end encryption and abstracts policy choices away from developers so they can focus on simple integration code without worrying about the complexities of key management, cryptography choices, SIEM integration, etc. The diagram below shows how this works, with IronCore's persistence-free Tenant Security Proxy running in your infrastructure as a Docker container.

![CMK technical architecture](https://d33wubrfki0l68.cloudfront.net/5cc2f05ffe7bf48238abe649d8877b0fa8f58fb7/7e380/afe4036ddee08d9d07d4e060c611271f/cmktechnicalarchitecture.svg)

IronCore's CMK solution relies on three distinct components: the Configuration Broker, the Tenant Security Proxy, and a Tenant Security Client.

## Configuration Broker

The IronCore Configuration Broker is an IronCore-hosted web app which is the connection point between SaaS providers (vendors) and their customers (tenants).

Vendors use the Configuration Broker to provision their CMK tenants and add configuration for the Tenant Security Proxy Docker container. Tenants use the Configuration Broker to manage access to their KMS, regardless of which cloud provider hosts the KMS.

Most importantly, the Configuration Broker is a zero-trust system; all information for both vendors and tenants is stored end-to-end encrypted and never seen in its unencrypted form by IronCore. Both you and your customers can feel safe that the information provided within the Configuration Broker is only ever seen by approved administrators and systems. And better yet, all access is logged and can be audited by the customer.

[Learn More](https://ironcorelabs.com/docs/saas-shield/config-broker/)

## Tenant Security Proxy

The Tenant Security Proxy is a Docker container that is run within your SaaS infrastructure. It is the gateway between your application and your customer's KMS and logging infrastructure, regardless of where that runs. Because this Tenant Security Proxy Docker container runs in your infrastructure, you control its scaling and rollout.

[Learn More](https://ironcorelabs.com/docs/saas-shield/tenant-security-proxy/overview/)

## Tenant Security Client Libraries

The Tenant Security Client Libraries are SDKs provided by IronCore that you integrate into your applications' codebase. These libraries interact with the Tenant Security Proxy, providing simple configuration and method calls to encrypt and decrypt your customers' data. They also generate auditable events that are fed to your customers' logging systems.

The data that you encrypt and decrypt is **never** transferred to the Tenant Security Proxy and always stays directly within your application.
