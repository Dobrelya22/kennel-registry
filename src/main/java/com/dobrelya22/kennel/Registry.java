import java.time.LocalDate;
import java.util.*;

// Базовый абстрактный класс Animal
abstract class Animal {
    private static int counter = 1;
    protected final int id = counter++;
    protected String name;
    protected LocalDate birth;
    protected List<String> commands = new ArrayList<>();

    public Animal(String name, LocalDate birth, String... cmds) {
        this.name = name;
        this.birth = birth;
        this.commands.addAll(Arrays.asList(cmds));
    }

    public void learn(String cmd) {
        commands.add(cmd);
    }

    public List<String> getCommands() {
        return commands;
    }

    @Override
    public String toString() {
        return id + " " + getClass().getSimpleName() + " " + name;
    }
}

// Промежуточные классы
class Pet extends Animal {
    public Pet(String n, LocalDate b, String... c) {
        super(n, b, c);
    }
}
class PackAnimal extends Animal {
    public PackAnimal(String n, LocalDate b, String... c) {
        super(n, b, c);
    }
}

// Конкретные классы
class Dog extends Pet {
    public Dog(String n, LocalDate b, String... c) { super(n, b, c); }
}
class Cat extends Pet {
    public Cat(String n, LocalDate b, String... c) { super(n, b, c); }
}
class Hamster extends Pet {
    public Hamster(String n, LocalDate b, String... c) { super(n, b, c); }
}

class Horse extends PackAnimal {
    public Horse(String n, LocalDate b, String... c) { super(n, b, c); }
}
class Donkey extends PackAnimal {
    public Donkey(String n, LocalDate b, String... c) { super(n, b, c); }
}
class Camel extends PackAnimal {
    public Camel(String n, LocalDate b, String... c) { super(n, b, c); }
}

// Счетчик для try-with-resources
class Counter implements AutoCloseable {
    private int value = 0;
    private boolean closed = false;

    public void add() {
        if (closed) throw new IllegalStateException("Counter closed!");
        value++;
    }

    public int get() {
        return value;
    }

    @Override
    public void close() {
        closed = true;
    }
}

// Основной класс реестра животных
public class Registry {
    private static final List<Animal> animals = new ArrayList<>();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            switch (menu()) {
                case 1 -> addAnimal();
                case 2 -> showCommands();
                case 3 -> teachAnimal();
                case 0 -> {
                    System.out.println("Пока!");
                    return;
                }
                default -> System.out.println("Неверный выбор");
            }
        }
    }

    private static int menu() {
        System.out.print("""
                1. Завести новое животное
                2. Показать команды животного
                3. Обучить животное новой команде
                0. Выход
                Выбор: """);
        return Integer.parseInt(sc.nextLine());
    }

    private static void addAnimal() {
        try (Counter cnt = new Counter()) {
            System.out.print("Имя: ");
            String name = sc.nextLine();
            System.out.print("Дата (YYYY-MM-DD): ");
            LocalDate d = LocalDate.parse(sc.nextLine());
            System.out.print("Вид (dog/cat/hamster/horse/donkey/camel): ");
            String k = sc.nextLine().toLowerCase();

            Animal a = switch (k) {
                case "dog"     -> new Dog(name, d);
                case "cat"     -> new Cat(name, d);
                case "hamster" -> new Hamster(name, d);
                case "horse"   -> new Horse(name, d);
                case "donkey"  -> new Donkey(name, d);
                case "camel"   -> new Camel(name, d);
                default          -> throw new IllegalArgumentException("Неизвестный вид");
            };
            animals.add(a);
            cnt.add();
            System.out.println("Добавлено: " + a);
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    private static Animal find() {
        System.out.print("ID: ");
        int id = Integer.parseInt(sc.nextLine());
        return animals.stream().filter(a -> a.id == id).findFirst()
                      .orElseThrow(() -> new NoSuchElementException("Нет такого животного"));
    }

    private static void showCommands() {
        Animal a = find();
        System.out.println("Команды: " + a.getCommands());
    }

    private static void teachAnimal() {
        Animal a = find();
        System.out.print("Новая команда: ");
        a.learn(sc.nextLine());
    }
}
