import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Application {

    private static final String NEW_GAME_PROMPT = "[N]ew game or [e]xit?";
    private static final Path DICTIONARY_PATH = Path.of("src/main/resources/russian-nouns.txt");

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Set<Character> usedLetters = new HashSet<>();
    private static final Set<String> usedWords = new HashSet<>();

    private static int mistakesCounter;
    private static int correctAnswers;
    private static StringBuilder hiddenWord;

    public static void main(String[] args) {
        startApplication();
    }

    private static void startApplication() {
        while (true) {
            System.out.println(NEW_GAME_PROMPT);
            String command = SCANNER.nextLine().toLowerCase();

            switch (command) {
                case "n" -> startNewGame();
                case "e" -> System.exit(0);
                default -> System.out.println("Invalid command");
            }
        }
    }

    private static void startNewGame() {
        resetGame();
        List<String> dictionary = loadDictionary();
        String guessedWord = getRandomWord(dictionary);
        hiddenWord = new StringBuilder("-".repeat(guessedWord.length()));

        while (!isGameOver(guessedWord)) {
            Character playerInput = getPlayerInput();
            processPlayerInput(playerInput, guessedWord);
            displayGameState();
        }
    }

    private static void resetGame() {
        usedLetters.clear();
        mistakesCounter = 0;
        correctAnswers = 0;
    }

    private static boolean isGameOver(String guessedWord) {
        if (correctAnswers == guessedWord.length()) {
            System.out.println("Congrats! You guessed the word.");
            return true;
        } else if (mistakesCounter >= 6) {
            System.out.println("You lose! The correct word was: " + guessedWord);
            return true;
        }
        return false;
    }


    private static List<String> loadDictionary() {
        try {
            return Files.readAllLines(DICTIONARY_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load dictionary", e);
        }
    }

    private static String getRandomWord(List<String> dictionary) {
        String randomWord;
        do {
            randomWord = dictionary.get((int) (Math.random() * dictionary.size()));
        } while (usedWords.contains(randomWord));

        usedWords.add(randomWord);
        return randomWord;
    }

    private static Character getPlayerInput() {
        while (true) {
            System.out.print("Enter a letter: ");
            String input = SCANNER.nextLine();

            if (input.length() == 1 && input.matches("[а-я]")) {
                char guessedChar = input.charAt(0);
                if (!usedLetters.contains(guessedChar)) {
                    usedLetters.add(guessedChar);
                    return guessedChar;
                } else {
                    System.out.println("You've already used this letter.");
                }
            } else {
                System.out.println("Invalid input. Please enter a single lowercase Cyrillic letter.");
            }
        }
    }

    private static void processPlayerInput(Character guessedChar, String guessedWord) {
        if (guessedWord.contains(guessedChar.toString())) {
            revealGuessedLetters(guessedChar, guessedWord);
        } else {
            mistakesCounter++;
            showHangman();
        }
    }

// отобразить часть виселицы / открыть буквы в маске

    private static void revealGuessedLetters(Character guessedChar, String guessedWord) {
        for (int i = 0; i < guessedWord.length(); i++) {
            if (guessedWord.charAt(i) == guessedChar) {
                hiddenWord.setCharAt(i, guessedChar);
                correctAnswers++;
            }
        }
    }

    private static void displayGameState() {
        System.out.println("Current word: " + hiddenWord);
        System.out.println("Mistakes: " + mistakesCounter);
        System.out.println("Used letters: " + usedLetters);
    }

    private static void showHangman() {
        String[] hangmanStages = {
                """
                        +---+
                        |   |
                            |
                            |
                            |
                            |
                    =========
                """,
                """
                        +---+
                        |   |
                        O   |
                            |
                            |
                            |
                    =========
                """,
                """
                        +---+
                        |   |
                        O   |
                        |   |
                            |
                            |
                    =========
                """,
                """
                        +---+
                        |   |
                        O   |
                       /|   |
                            |
                            |
                    =========
                """,
                """
                        +---+
                        |   |
                        O   |
                       /|\\  |
                            |
                            |
                    =========
                """,
                """
                        +---+
                        |   |
                        O   |
                       /|\\  |
                       /    |
                            |
                    =========
                """,
                """
                        +---+
                        |   |
                        O   |
                       /|\\  |
                       / \\  |
                            |
                    =========
                """
        };
        System.out.println(hangmanStages[mistakesCounter]);
    }

}