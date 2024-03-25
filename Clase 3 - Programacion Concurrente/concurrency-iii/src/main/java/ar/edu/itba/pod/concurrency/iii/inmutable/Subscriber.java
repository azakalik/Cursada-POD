package ar.edu.itba.pod.concurrency.iii.inmutable;

import java.util.Date;
import java.util.List;

// Solutions to concurrency problems:
// 1. When receiving a mutable Object by constructor, save a copy and not the reference
// 2. When returning internal objects by getters, return copies and not references
// 3. Final and private for internal attributes
public class Subscriber {
    private final Integer id;
    private final String fullName;
    private final Date dateOfBirth;
    private final List<Subscription> subscriptions;

    public Subscriber(Integer id, String fullName, Date dateOfBirth,  List<Subscription> subscriptions) {
        this.id = id;
        this.fullName = fullName;
        this.dateOfBirth = new Date(dateOfBirth.getTime()); // Save a copy of the Date
        this.subscriptions = List.copyOf(subscriptions); // Save a copy of the list
    }

    public Integer getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public Date getDateOfBirth() {
        return new Date(dateOfBirth.getTime());
    }

    public List<Subscription> getSubscriptions() {
        return List.copyOf(subscriptions);
    }
}
