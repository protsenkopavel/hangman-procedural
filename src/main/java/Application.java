import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Application {

    private static final String NEW_GAME = "[N]ew game or [e]xit?";

    private static final Path pathToDictionary = Path.of("resources/russian-nouns.txt");

    private static final Scanner scanner = new Scanner(System.in);

    private static final List<String> usedWords = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Hello World!");
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
        usedWords.clear();
    }

// загрузить словарь в приложение

    private static List<String> readDictionaryAndReturnWord() throws IOException {
        List<String> dictionary = Files.readAllLines(pathToDictionary);
        return dictionary;
    }

// выбрать случайное слово (в пределах одной игры исключить возможность повтора)

    private static String getWord(List<String> dictionary) {
        Random random = new Random();
        int randomIndex = random.nextInt(dictionary.size());
        if (!usedWords.contains(dictionary.get(randomIndex))) {
            return dictionary.get(randomIndex);
        } else {
            randomIndex = random.nextInt(dictionary.size());
            usedWords.add(dictionary.get(randomIndex));
            return dictionary.get(randomIndex);
        }
    }

// отобразить маску слова

// ввод -> валидность -> в зависимости от ввода проверить наличие данной буквы в слове

// отобразить часть виселицы / открыть буквы в маске

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