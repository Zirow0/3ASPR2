import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

public class ArrayMultiplierTask implements Callable<List<Integer>> {
    private final List<Integer> numbers;
    private final int multiplier;

    public ArrayMultiplierTask(List<Integer> numbers, int multiplier) {
        this.numbers = numbers;
        this.multiplier = multiplier;
    }

    @Override
    public List<Integer> call() {
        List<Integer> resultList = new CopyOnWriteArrayList<>();
        for (int number : numbers) {
            resultList.add(number * multiplier);
        }
        return resultList;
    }
}
