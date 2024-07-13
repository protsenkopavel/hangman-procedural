import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Application {

    private static final String NEW_GAME = "[N]ew game or [e]xit?";

    private static final Path pathToDictionary = Path.of("src/main/resources/russian-nouns.txt");

    private static final Scanner scanner = new Scanner(System.in);

    private static final List<String> usedWords = new ArrayList<>();

    private static int mistakesCounter;

    private static int correctAnswer;

    private static StringBuilder hiddenWord;

    public static void main(String[] args) {
        startGame();
    }

// запуск приложения (начать новую игру? / выбрать сложность)

    public static void startGame() {
        while (true) {

            System.out.println(NEW_GAME);

            String command = scanner.nextLine().toLowerCase();

            switch (command) {
                case "n" -> startGameLoop();
                case "e" -> System.exit(0);
                default -> System.out.println("Invalid command");
            }
        }
    }

    private static void startGameLoop() {
        boolean flag = true;

        usedWords.clear();
        mistakesCounter = 0;
        correctAnswer = 0;

        List<String> dictionary = readDictionaryAndReturnWord();
        String guessedWord = getWord(dictionary);
        hiddenWord = new StringBuilder("-".repeat(guessedWord.length()));

        while (flag) {
            Character playerInput = getPlayerInput();
            checkLetter(playerInput, guessedWord);
            flag = checkConditions(guessedWord);
        }

    }

    private static boolean checkConditions(String guessedWord) {
        if (correctAnswer == guessedWord.length()) {
            System.out.println("Congrats!");
            return false;
        } else if (mistakesCounter == guessedWord.length()) {
            System.out.println("You lose, try again");
            return false;
        }
        return true;
    }

// загрузить словарь в приложение

    private static List<String> readDictionaryAndReturnWord() {
        try {
            return Files.readAllLines(pathToDictionary);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

// выбрать случайное слово (в пределах одной игры исключить возможность повтора)

    private static String getWord(List<String> dictionary) {
        int randomInt = (int) (Math.random() * dictionary.size());
        String randomWord = "";
        boolean accept = false;

        while (!accept) {
            randomWord = dictionary.get(randomInt);
            accept = !usedWords.contains(randomWord);
        }

        return randomWord;
    }

    // отобразить маску слова
    private static String hideWord(String randomWord) {
        return randomWord.replaceAll("[а-яА-Я]", "-");
    }

// ввод -> валидность -> в зависимости от ввода проверить наличие данной буквы в слове

    private static Character getPlayerInput() {
        while (true) {
            String inputChar = scanner.nextLine();

            if (inputChar.length() != 1) {
                System.out.println("Input only one letter you want to check");
                continue;
            }

            return inputChar.charAt(0);
        }
    }

    private static void checkLetter(Character guessedChar, String guessedWord) {
        if (guessedWord.contains(guessedChar.toString())) {
            System.out.println(showGuessedLetters(guessedChar, guessedWord));
            System.out.println("Ошибок совершено" + mistakesCounter);
        } else {
            System.out.println(showGuessedLetters(guessedChar, guessedWord));
            showHangman();
            System.out.println("Ошибок совершено" + ++mistakesCounter);
        }

    }

// отобразить часть виселицы / открыть буквы в маске

    private static String showGuessedLetters(Character guessedChar, String guessedWord) {


        for (int i = 0; i < guessedWord.length(); i++) {
            if (guessedWord.charAt(i) == guessedChar) {
                hiddenWord.setCharAt(i, guessedChar);
                correctAnswer++;
            }
        }
        return hiddenWord.toString();
    }

    private static void showHangman() {
        System.out.println("""
                                +---+
                                |   |
                                O   |
                               /|\\  |
                               / \\  |
                                    |
                            =========
                """);
    }

// после завершения игры предложить повторную игру или завершить работу

}

/*Загадывается слово — пишется любые две буквы слова и отмечает места для остальных букв чертами
(существует также вариант, когда изначально все буквы слова неизвестны).
Также рисуется виселица с петлёй.
Игрок предлагает букву, которая может входить в это слово. Если такая буква есть в слове,
то буква пишется вместо соответствующим этой букве чертами — столько раз, сколько она встречается в слове.
Если такой буквы нет, то к виселице добавляется круг в петле, изображающий голову.
Игрок продолжает отгадывать буквы до тех пор, пока не отгадает всё слово.
За каждый неправильный ответ первый игрок добавляет одну часть туловища к виселице (обычно их 6: голова, туловище,
2 руки и 2 ноги, существует также вариант с 8 частями — добавляются ступни, а также самый длинный вариант,
когда сначала за неотгаданную букву рисуются части самой виселицы).
Если туловище в виселице нарисовано полностью, то отгадывающий игрок проигрывает, считается повешенным.
Если игроку удаётся угадать слово, он выигрывает и может загадывать слово.*/