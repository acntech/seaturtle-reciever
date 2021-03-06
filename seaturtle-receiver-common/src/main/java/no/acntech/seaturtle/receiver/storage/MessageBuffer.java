package no.acntech.seaturtle.receiver.storage;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

public abstract class MessageBuffer<T> {

    private static final int QUEUE_SIZE = 1000;
    private final ArrayBlockingQueue<T> queue = new ArrayBlockingQueue<>(QUEUE_SIZE);

    public void put(T message) {
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            throw new StorageException("Operation was interrupted while trying to save message to queue", e);
        }
    }

    public T pop() {
        return queue.poll();
    }

    public List<T> list() {
        return queue.stream().collect(Collectors.toList());
    }
}
