package ar.edu.itba.pod.socket.service;

public class VisitCounter {
    private int visitCount;

    public void addVisit() {
        visitCount++;

    }

    public int getVisitCount() {
        return visitCount;
    }
}
