package com.gridnine.testing;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Filter {
    public List<Flight> filtrate(List<Flight> flights, String... typesOfFilter) {
        if (typesOfFilter.length == 0) {
            return flights;
        }
        List<Flight> result = flights;
        //Filter the list of flights while typesOfFilter exists.
        for (String s : typesOfFilter) {
            try {
                //call a method by it's name
                result = (List<Flight>) Filter.class.getDeclaredMethod(s, List.class).invoke(this, result);
            } catch (NoSuchMethodException e) {
                System.out.println("Method " + s + " don't find in the class " + getClass().getName());
                e.printStackTrace();
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Delete flying where departing date was in the past
     */
    private List<Flight> departureInThePast(List<Flight> flights) {
        return flights.stream().filter(flight -> flight.getSegments().stream().anyMatch(segment -> segment.getDepartureDate().isAfter(LocalDateTime.now()))).collect(Collectors.toList());
    }

    /**
     * Delete flying where arrival date is before departure date
     */
    private List<Flight> arrivalDateIsBeforeDeparture(List<Flight> flights) {
        return flights.stream().filter(flight -> flight.getSegments().stream().anyMatch(segment -> segment.getArrivalDate().isAfter(segment.getDepartureDate()))).collect(Collectors.toList());
    }

    /**
     * Delete flying where waiting time on the ground more than 2 hours
     */
    private List<Flight> timeOnTheGroundMoreThan2hours(List<Flight> flights) {
        long hours = 0L;
        //create new list
        List<Flight> result = new ArrayList<>();
        //for each flight
        for (int f = 0; f < flights.size(); f++) {
            //calculate the sum of the waiting time between all segments
            for (int s = 0; s < flights.get(f).getSegments().size() - 1; s++) {
                hours += ChronoUnit.HOURS.between(flights.get(f).getSegments().get(s).getArrivalDate(), flights.get(f).getSegments().get(s + 1).getDepartureDate());
            }
            //if the total waiting time is less than 2 hours then add flight to the list
            if (hours < 2) {
                result.add(flights.get(f));
            }
            //reset time
            hours = 0L;
        }
        return result;
    }
}
