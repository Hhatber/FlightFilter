package com.gridnine.testing;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        get flight's list
        List<Flight> flights = FlightBuilder.createFlights();
        flights.forEach(System.out::println);

        Filter filter = new Filter();

        System.out.println("\nResult without flying where departing date was in the past");
        filter.filtrate(flights, "departureInThePast").forEach(System.out::println);

        System.out.println("\nResult without flying where arrival date is before departure date");
        filter.filtrate(flights, "arrivalDateIsBeforeDeparture").forEach(System.out::println);

        System.out.println("\nResult without flying where waiting time on the ground more than 2 hours");
        filter.filtrate(flights, "timeOnTheGroundMoreThan2hours").forEach(System.out::println);

        System.out.println("\nFor example, a result where all three rules are used");
        filter.filtrate(flights, "departureInThePast", "arrivalDateIsBeforeDeparture", "timeOnTheGroundMoreThan2hours").forEach(System.out::println);
    }
}
