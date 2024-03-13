package ar.edu.itba.pod.concurrency.exercises.e1;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Optional;

/**
 * Basic implementation of {@link GenericService}.
 */
public class GenericServiceImpl implements GenericService {
    private int visits = 0;
    private Queue<String> messageQueue = new LinkedList<String>();

    @Override
    public String echo(String message) {
        return message;
    }

    @Override
    public String toUpper(String message) {
        return Optional.ofNullable(message).map(String::toUpperCase).orElse(null);
    }

    @Override
    public void addVisit() {
        visits += 1;
    }

    @Override
    public int getVisitCount() {
        return visits;
    }

    @Override
    public boolean isServiceQueueEmpty() {
        return messageQueue.isEmpty();
    }

    @Override
    public void addToServiceQueue(String name) {
        if (name == null)
            throw new NullPointerException("No one in queue");
        messageQueue.add(name);
    }

    @Override
    public String getFirstInServiceQueue() {
        return Optional.ofNullable(messageQueue.poll()).orElseThrow(() -> new IllegalStateException("No one in queue"));
    }
}
