# Java Encrypted P2P Application

A very simple Proof-Of-Concept application, built in Java, that allows the transfer of files within a symmetric encrypted P2P network.

## Project Introduction

- Server
    - A Central server for registering and authenticating clients.
    - Holds a listing of all files being shared by all clients connects at a present moment in the P2P network.
- Client
  - Connects to the above server providing its own identity and file listing.
  - Clients also run their own respective server to provide other clients a method to fetch files.
- Channel
  - Module with the communication logic to be applied when endpoints communicate.
- Commons
  - Shared code between the two above.

### Certificates and Keys

The following certificate hierarchy was generated:
- Root Certificate (Self signed)
  - Intermediate Certificate (Signed by Root Certificate)
    - Client Certificate (Signed by Intermediate Certificate)
  - Server certificate (Signed by Root Certificate)

## Communication Channel

The communication channel is where the logic to receive and read bytes between two already connected sockets resides.  
This can account for, but not only:
- Cryptographic key agreement.
- Byte encryption/decryption.
- Message signing.
- Integration checks.
- ...

When creating a channel, we only have to specify the type of channel we want to create, pass in the needed input parameters and  
all will be taken care of by us.

The main class for the channel is the java class: **com.channel.BaseChannel.java**. This class can behave in multiple  
ways, or in other words, the channel can be of multiple types, which can be:
- Unsecured:
- Shared Keys
- Diffie-Hellman

The difference of behaviour between each of the channel types is related to how two endpoints communicate, how to agree  
to a master secret key, if they use encryption and signatures or certificates. 

These different behaviours are implemented in strategies interfaces:
- KeyAgreementStrategy
- EncryptionStrategy
- SigningStrategy

With their names being self-explanatory, the next chapter has the channel types and what strategies each one uses.

### Strategies

#### Unsecured

The unsecured channel does not have a key agreement, an encryption or any signature. Therefore, it's strategies  
implements are:
  - WithoutKeyAgreementStrategyImpl
  - WithoutEncryptionStrategyImpl
  - WithoutSigningStrategyImpl

#### Shared Keys

The shared keys channel type assumes all endpoints have a pre-shared key that is used to secure the communication. 
Since we have a Key, we have a key agreement, and since it's secure, we have encryption and signing, therefore:
- SharedKeysKeyAgreementStrategyImp
- WithEncryptionStrategyImpl
- WithSigningStrategyImpl

#### Diffie-Hellman
Diffie-Hellman is similar with the above channel type except the key agreement strategy. Here we use the Diffie-Hellman protocol,  
complemented with Station-To-Station validation, as key agreement strategy.
- DiffieHellmanKeyAgreementStrategyImpl
- WithEncryptionStrategyImpl
- WithSigningStrategyImpl


  


