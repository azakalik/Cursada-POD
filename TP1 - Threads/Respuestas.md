# Ejercicio 1

Se puede obtener cualquiera de las siguientes combinaciones:
- 1 2 A B
- 1 A 2 B
- 1 A B 2
- A B 1 2

# Ejercicio 2
El codigo:
``` java
public class ThreadStateViewer {  
    public static void main(String[] args) throws InterruptedException {  
        String lock = "lock";  
        Thread thread = new Thread(() -> {  
			System.out.printf("Hello!, my state is %s%n", Thread.currentThread().getState());  
            try {  
                Thread.sleep(2000);  
                synchronized (lock) {  
                    lock.wait();  
                }  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
        });  
        System.out.printf("Thread state: %s%n", thread.getState());  
        thread.start();  
        Thread.sleep(500);  
        System.out.printf("Thread state: %s%n", thread.getState());  
        Thread.sleep(2000);  
        System.out.printf("Thread state: %s%n", thread.getState());  
        synchronized (lock) {  
            lock.notifyAll();  
        }  
        thread.join();  
        System.out.printf("Thread state: %s%n", thread.getState());  
    }  
}
```

Imprime esto:
Thread state: NEW
Hello!, my state is RUNNABLE
Thread state: TIMED_WAITING
Thread state: WAITING
Thread state: TERMINATED