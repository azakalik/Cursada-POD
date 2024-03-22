package ej2and3;

public class ThreadSafeStack extends Stack {
    @Override
    synchronized public void push(final int n) {
        if (top == MAX_SIZE) {
            throw new IllegalStateException("stack full");
        }
        values[top++] = n;
    }

    @Override
    synchronized public int pop() {
        if (top == 0) {
            throw new IllegalStateException("stack empty");
        }
        return values[--top];
    }
}