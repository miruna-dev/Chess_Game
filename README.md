# Java Chess Game

A desktop chess application built with Java and Swing. It features a graphical user interface, user account management, persistent game states using JSON, and an automated computer opponent. The project is designed with strong Object-Oriented Programming (OOP) principles and standard design patterns.

## Features

* **User Management**: Users can create accounts, log in, and track their accumulated points across multiple games.
* **Game Dashboard**: From the main menu, players can start a new game (selecting an alias and piece color), resume a previously saved game, delete games, or log out.
* **Complete Chess Rules**: The game engine enforces standard chess rules, including:
  * Legal move validation and highlighting on the board.
  * Prevention of moves that would leave the king in check.
  * Check and checkmate detection.
  * Draw conditions: stalemate, insufficient material, and threefold repetition.
  * Pawn promotion.
* **Computer Opponent**: A built-in automated opponent that calculates all available legal moves and executes one at random, featuring a simulated response delay for a natural pacing.
* **Data Persistence**: All user accounts, scores, and ongoing game states (including move history) are serialized and saved locally in JSON files (`accounts.json` and `games.json`).

## Architecture and Design Patterns

The application is structured around a Model-View-Controller (MVC) architecture to ensure a clear separation between the graphical interface, the game logic, and data management. 

Key design patterns implemented include:
* **Strategy Pattern**: Used extensively for piece movement logic. The `MoveStrategy` interface allows each piece type to define its own movement rules. This decouples the movement algorithms from the piece entities themselves.
* **Composition**: The `QueenMoveStrategy` is built by composing the `RookMoveStrategy` and `BishopMoveStrategy`, promoting code reuse.
* **Factory Method**: A `Factory` class handles the instantiation of specific chess pieces based on character inputs, centralizing the creation logic used during board initialization.
* **Singleton**: The `Main` class operates as a Singleton to manage the active user session, handle state transitions, and coordinate JSON read/write operations globally.
* **Observer**: Swing's event-driven model (ActionListeners, MouseAdapters) is used to handle UI interactions without polling.

## Project Structure

The source code is organized into specific packages to maintain modularity:

* `main`: Contains the application entry point, session management, and JSON persistence utilities.
* `view`: Contains the Swing GUI implementation. It uses a `CardLayout` within a main `JFrame` to seamlessly switch between the Login, Menu, and Game screens.
* `model`: The core domain of the application. It includes the board logic, game state management, player data, and the abstract `Piece` class along with its specific subclasses.
* `moveStrategies`: The implementations of the `MoveStrategy` interface for calculating valid trajectories on the board.
* `interfaces`: Core contracts used across the application.
* `exceptions`: Custom domain exceptions (e.g., `InvalidMoveException`).

## Technologies Used

* **Language**: Java (Standard Edition)
* **GUI**: Java Swing (JFrame, JPanel, CardLayout, Graphics rendering)
* **Data Serialization**: `json-simple` library for parsing and writing JSON files.

## Getting Started

### Prerequisites
* Java Development Kit (JDK) 8 or higher.
* The `json-simple` library included in your classpath.

### Running the Application
Compile the source files and run the `Main` class located in the `main` package. The application will automatically generate `accounts.json` and `games.json` in the root directory upon the first execution or registration if they do not already exist.
