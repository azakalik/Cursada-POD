package ar.edu.itba.pod.concurrency.service;

import ar.edu.itba.pod.concurrency.service.GenericService;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Optional;

/**
 * Basic implementation of {@link GenericService}.
 */
public class GenericServiceImpl implements GenericService {
    private final Float mutex = 0F;
    private int visits = 0;
    private final Queue<String> messageQueue = new LinkedList<String>();

    @Override
    public String echo(String message) {
        return message;
    }

    @Override
    public String toUpper(String message) {
        return Optional.ofNullable(message).map(String::toUpperCase).orElse(null);
    }

    @Override
    public synchronized void addVisit() {
        visits += 1;
    }

    @Override
    public synchronized int getVisitCount() {
        return visits;
    }

    @Override
    public boolean isServiceQueueEmpty() {
        synchronized (messageQueue){
            return messageQueue.isEmpty();
        }
    }

    @Override
    public void addToServiceQueue(String name) {
        synchronized (messageQueue) {
            if (name == null)
                throw new NullPointerException("No one in queue");
            messageQueue.add(name);
        }
    }

    @Override
    public String getFirstInServiceQueue() {
        synchronized (messageQueue){
            return Optional.ofNullable(messageQueue.poll()).orElseThrow(() -> new IllegalStateException("No one in queue"));
        }
    }
}
