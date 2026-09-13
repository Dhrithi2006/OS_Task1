OS Task 1: Multithreading & Synchronization in Java
This repository contains implementations of two concurrent computing problems using Java's multithreading and concurrency frameworks.

Repository Structure
src/ProducerConsumer.java - Topic 1: Producer-Consumer with monitor locks

src/MatrixMultiplicationThreads.java - Topic 2: Threaded 100x100 matrix multiplication

src/MatrixAnimationGUI.java - Topic 2: Live Swing GUI animation of threads

README.md - Documentation and execution guide

1. Producer-Consumer Problem

Overview

Demonstrates thread synchronization and inter-thread communication using a shared bounded buffer.

Shared Buffer: Bounded queue with a maximum capacity of 3 items.

Synchronization Mechanism: Java intrinsic monitor locks (synchronized methods).

Signaling: wait() and notifyAll() inside guarded condition loops (while) to prevent race conditions, overflow, underflow, and spurious wakeups.

Rate Pacing: The Producer operates at 400 ms intervals while the Consumer runs at 800 ms intervals, explicitly triggering buffer saturation to verify backpressure handling.

How to Run

Bash
javac src/ProducerConsumerDemo.java
java -cp src ProducerConsumerDemo

2. Multi-Threaded 100x100 Matrix Multiplication & Animation

Overview

Performs parallel matrix multiplication (C = A x B) across two 100x100 matrices.

Independent Execution Units: Every output cell C[i][j] is calculated as an isolated thread task (total of 10,000 tasks).

Concurrency Framework: Employs Java's ExecutorService (Executors.newFixedThreadPool) scaled proportionally to available CPU cores (cores * 2).

Race-Condition Free: Disjoint index partitioning ensures that threads write to independent memory coordinates without requiring locks.

Verification: Automatically cross-checks the multi-threaded output against a baseline single-threaded computation to verify numeric accuracy.

Live GUI Animation
Built with Java Swing (MatrixAnimationGUI.java).

Represents the 100x100 matrix on an interactive visual grid.

Cells dynamically transition from dark gray to bright green as individual worker threads complete calculations.

Integrated with a real-time progress bar and a 30 FPS Swing Timer to decouple graphical rendering from the computational thread pool.

How to Run
Console Version (with verification & execution timer):

Bash
javac src/MatrixMultiplicationThreads.java
java -cp src MatrixMultiplicationThreads
GUI Animation Version:

Bash
javac src/MatrixAnimationGUI.java
java -cp src MatrixAnimationGUI
Running in Eclipse IDE
Clone or import this repository into Eclipse as an Existing Java Project.

Expand src inside the Package Explorer.

Right-click on any .java file (ProducerConsumerDemo, MatrixMultiplicationThreads, or MatrixAnimationGUI).

Select Run As -> Java Application.

Prerequisites
JDK: Java SE 8 or higher (Java 17+ recommended)

IDE: Eclipse IDE for Java Developers (or any standard Java IDE)
