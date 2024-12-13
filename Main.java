
import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        Random rand = new Random();

        // Користувацький діапазон
        int minRange = -100; // Мінімум діапазону
        int maxRange = 100; // Максимум діапазону
        int arraySize = rand.nextInt(20) + 40;

        System.out.println("Діапазон чисел: [" + minRange + ", " + maxRange + "]");
        System.out.println("Кількість елементів в масиві: " + arraySize);


        // Генерація масиву випадкових чисел
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < arraySize; i++) {
            numbers.add(rand.nextInt((maxRange - minRange) + 1) + minRange);
        }

        System.out.println(numbers);

        // Створення сканера для вводу від користувача
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введіть множник: ");
        int multiplier = scanner.nextInt(); // Зчитування вводу користувача

        System.out.println("Множник: " + multiplier);
        scanner.close();

        // Створення пулу потоків
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // Розбивка на частини для обробки
        int numThreads = 4; // Кількість потоків
        List<Callable<List<Integer>>> tasks = new ArrayList<>();

        // Розділення масиву на частини
        int chunkSize = numbers.size() / numThreads;
        for (int i = 0; i < numThreads; i++) {
            final int start = i * chunkSize;
            final int end = (i == numThreads - 1) ? numbers.size() : (i + 1) * chunkSize;
            List<Integer> subList = numbers.subList(start, end);
            tasks.add(new ArrayMultiplierTask(subList, multiplier));
        }

        // Виконання завдань через Future
        List<Future<List<Integer>>> futures = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (Callable<List<Integer>> task : tasks) {
            futures.add(executorService.submit(task));
        }

        // Обробка результатів
        List<Integer> result = new CopyOnWriteArrayList<>();
        for (Future<List<Integer>> future : futures) {
            try {
                while (!future.isDone());
                if (future.isDone()) {
                    result.addAll(future.get());
                } else if (future.isCancelled()) {
                    System.out.println("Task was cancelled.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Завершуємо роботу executorService
        executorService.shutdown();

        // Виведення результату
        System.out.println("Оброблений масив:");
        for (int number : result) {
            System.out.print(number + " ");
        }

        long endTime = System.currentTimeMillis();
        System.out.println("\nЧас роботи програми: " + (endTime - startTime) + " мс");
    }
}