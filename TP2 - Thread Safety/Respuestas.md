# Ejercicio 1
![](Pasted%20image%2020240320133811.png)
VERDADERO.
Es equivalente porque al ponerle synchronized al metodo, se esta usando la clase (no la instancia) como el lock. Tener en cuenta para la respuesta que la funcion es `static`.
Ademas, los siguientes code snippets son identicos:
```java
public class A {
    int method() {
	    synchronized (this) {
		    // code
		}
	}
}
```
```java
public class A {
    synchronized int method() {
	    // code
	}
}
```
En conclusion, cuando el metodo es estatico el keyword `synchronized` usa la clase como lock, y cuando el metodo no es estatico se utiliza la instancia de la clase como lock.

# Ejercicios 2 y 3
Resueltos en IntelliJ. Arreglar los metodos es tan facil como agregarles la keyword `synchronized`.

# Ejercicio 4
## Parte A
```java
 public class CountingFactorizer implements Servlet {
     private Long count = 0L;
    
     public void service(ServletRequest req, ServletResponse resp) {
        synchronized (count) {
           BigInteger i = extractFromRequest(req);
           BigInteger[] factors = factor(i);
           ++count;
           encodeIntoResponse(resp, factors);
        }
    }
}

public Long getCount() {
    synchronized (count) {
        return count;
    }
}
```
El problema es que se esta utilizando la variable `Long count` como lock. Esto es un error, porque los locks deben ser variables de tipo `final` que no se puedan sobreescribir.
El error que puede ocurrir en este caso es que cuando se aumente la variable, el puntero de la misma va a cambiar (por lo que el lock se reiniciaria). Esto es porque estariamos creando un nuevo objeto de tipo `Long` y asignandolo a la variable.
## Parte B
```java
 public class ExpensiveObjectFactory {
    private ExpensiveObject instance = null;
    
    //es un singleton a todos les retorno la misma instancia.
    public ExpensiveObject getInstance() {
        if (instance == null) {
           instance = new ExpensiveObject(); //tarda mucho en construir.
        } return instance;
    }
}
```
El error en este caso es que dos threads pueden llamar al metodo `getInstance` al mismo tiempo. Esto ocasionaria que se creen y retornen dos instancias distintas de `ExpensiveObject`, haciendo que deje de ser un Singleton.
## Parte C
```java
public class Account {
    private double balance;
    private int id;
    
    public void withdraw(double amount) {
        balance -= amount;
    }
    
    public void deposit(double amount) {
        balance += amount;
    }
    
    public static void transfer(Account from, Account to, double amount) {
        synchronized (from) {
            synchronized (to) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        } 
    }
}
```
El problema con esta es que podria ocurrir un deadlock. Supongamos que existen las variables `account1` y `account2`. Ambas llaman al metodo transfer al mismo tiempo, y le quieren transferir a la otra persona.
Supongamos tambien que ambas llegan a ejecutar el primer bloque `synchronized`. Lo que ocurriria aca es que `account1` tomaria su lock, y `account2` tomaria su lock.
Sin embargo, `account1` se bloquearia esperando a que se desbloquee el lock de `account2`, y `account2` se bloqueria esperando a que se desbloquee el lock de `account1`. Ambas cuentas quedarian bloqueadas infinitamente.
## Parte D
```java
 public class MovieTicketsAverager {
	 public int tope;
	 public List<Movie> movies;
	 /** */
	 public MovieTicketsAverager(int tope, List<Movie> movies) {
		 super();
		 this.tope = tope;
		 this.movies = new ArrayList<>(movies);
	}
	
	public double average() {
		return movies
		.stream()
		.filter(movie -> movie.getYear() > tope)
		.collect(Collectors.averagingInt(movie -> movie.getTicketsSold()));
	}
}

class Movie {
	private int year;
	private String title;
	private int ticketsSold;
	
	public Movie(int year, String title, int ticketsSold) {
		super();
		this.year = year;
		this.title = title;
		this.ticketsSold = ticketsSold;
	}
	
	public int getYear() { return year; }
	
	public String getTitle() { return title; }
	
	public int getTicketsSold() { return ticketsSold; }

}
```
Este codigo no pareceria tener problemas

# Ejercicio 5
Resuelto en IntelliJ