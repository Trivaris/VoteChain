# 🗳️🔗 VoteChain

## 🌐 Overview

VoteChain is a **decentralized digital voting system** built on top of **blockchain technology**. It allows for secure, transparent, and immutable voting where no single entity has control over the votes or the ability to alter them. The system uses a combination of **cryptographic signatures**, **QR codes**, and a **blockchain structure** to ensure that votes are securely cast, counted, and kept confidential until the host decides to reveal the results.

---

## 🚀 Features

- **Decentralized and Immutable Voting:** Blockchain ensures that once votes are cast, they cannot be altered or tampered with, providing transparency and security.
- **Privacy Protection:** Votes are stored in a way that prevents premature vote counting, maintaining the secrecy of the results.
- **Keypair-Based Authentication:** Each voter receives a unique keypair, used for securely signing their vote.
- **Blockchain-Powered Results:** Votes are grouped into blocks by miners and added to the blockchain for immutability.
- **Digital Signatures:** Voters digitally sign their chosen candidate, ensuring their vote is both authentic and secure.

---

## 🛠️ How It Works

1. **Host Generates Keypairs:**  
   The host of the voting event generates a set of valid keypairs, each associated with a voter.
   
2. **Voter Receives QR Code:**  
   Voters receive their keypair through email, formatted as a QR code.
   
3. **Voter Casts Vote:**  
   The voter scans the QR code, digitally signs their chosen candidate, and submits the vote.

4. **Vote Sent to the Network:**  
   The vote, which includes a hash of the voter's public key and the digital signature, is sent into the network.

5. **Miners Create Blocks:**  
   Miners collect votes and create blocks that contain the votes. These blocks are broadcast to the network, ensuring the integrity and immutability of the votes.

6. **Vote Hashmap Released:**  
   The server releases a hashmap containing the hashes of the public keys and the public keys themselves. This allows for later verification of votes, ensuring that results remain hidden until the host is ready to reveal them.

---

## 🧰 Key Technologies

- **UI Framework:** Jetpack Compose for Multiplatform
- **Programming Language:** Kotlin
- **QR Code Scanning:** Google ZXing with `com.journeyapps` embedded for Android
- **Blockchain:** Custom implementation using a decentralized peer-to-peer network for vote storage

---

## ⚠️ Issues and Challenges

> **Privacy & Security Concerns:**  
> Broadcasting the votes could expose sensitive information before the results are ready to be counted.

---

## 🚧 Future Plans

- **Implement Gossip Protocol:**  
  Switch from broadcasting to a more secure gossip protocol to improve privacy and prevent unauthorized access to the votes.
  
- **UI Improvements:**  
  Refactor and clean up Composables to ensure a smoother, more user-friendly experience.
  
- **Cross-Platform Support:**  
  Develop versions for iOS to make the application accessible to a wider audience.
  
- **More Testing:**  
  Conduct additional testing to ensure stability, security, and overall system performance.

---

## 🚀 Getting Started

### To run VoteChain locally or on your own system, follow these steps:

1. Clone this repository.
2. Set up the necessary environment for Kotlin and Jetpack Compose.
3. For making signed APKs, set your keys in
4. Compile and run the application via the `assemble` gradle Task for Android and `packageUberFatJarForCurrentOS` for the jar.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

### 🌟 **Contributing** 🌟

Feel free to contribute to this project by submitting pull requests, reporting bugs, or suggesting improvements!

---

## 📧 Contact

For any questions or inquiries, feel free to reach out to the project maintainers. You can open an issue or contact me directly via email.