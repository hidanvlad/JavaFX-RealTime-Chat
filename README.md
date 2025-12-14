# Real-Time Desktop Chat Application 💬

A high-performance, multithreaded client-server application built with **Java** and **JavaFX**. This project facilitates real-time, bidirectional messaging across a local network, featuring a modern GUI and robust concurrency management.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-GUI-blue?style=for-the-badge&logo=java&logoColor=white)
![Networking](https://img.shields.io/badge/Networking-Sockets-critical?style=for-the-badge)

## 📋 Overview

This application demonstrates the implementation of a raw **TCP/IP communication protocol** without relying on high-level messaging libraries. It handles multiple concurrent client connections via a threaded server architecture, ensuring non-blocking communication and UI responsiveness.

## ✨ Key Features

* **Real-Time Communication:** Instant bidirectional messaging between multiple users using Java Sockets.
* **Multithreaded Server:** Handles multiple client connections simultaneously using `Java Threads` and `Runnable` interfaces.
* **Modern UI:** A responsive JavaFX interface styled with CSS, featuring:
    * Scrollable chat history.
    * WhatsApp-style message bubbles.
    * Dynamic "Active Users" sidebar.
* **Live System Events:** Broadcasts "User Joined" and "User Left" updates to all connected clients in real-time.

## 🛠️ Technical Implementation

### Architecture & Concurrency
* **Client-Server Model:** The Server listens on a specific port and spawns a new `ClientHandler` thread for every incoming connection.
* **Thread Safety:** Solved concurrency challenges by using `Platform.runLater()` to marshal background network events back onto the main JavaFX Application Thread, preventing UI freezing or threading exceptions.
* **Synchronization:** Implemented synchronized blocks to safely manage the list of active clients and broadcast messages without data races.

### Tech Stack
* **Language:** Java (JDK 17+)
* **GUI:** JavaFX, FXML, CSS
* **Networking:** `java.net.Socket`, `java.net.ServerSocket`
* **I/O:** `BufferedReader`, `PrintWriter`

## 📸 Screenshots

| Login Screen | Chat Interface  |
|:---------:|:-------:|
| <img width="295" height="243" alt="image" src="https://github.com/user-attachments/assets/cd1ee410-a1b2-4de4-8b6f-3d15f0dc8b02" />  | <img width="289" height="261" alt="image" src="https://github.com/user-attachments/assets/bf0bf5c8-0c1a-45eb-9b9f-76fd05a9ba6b" /> |
 

## 🚀 How to Run

1.  **Start the Server:**
    * Run `ServerLauncher.java`.
    * The server will start listening for connections.
2.  **Start Clients:**
    * Run `ClientLauncher.java`.
    * Enter a username and connect.
    * You can run multiple instances of the Client to simulate a group chat.

## 📝 License

This project is open source and available for educational purposes.
