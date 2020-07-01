package com.google.sps;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;

/**
 * Schedules available meeting times based on a meeting request
 * and a collection of events which have already been scheduled.
 */
public class MeetingScheduler {
  private final Collection<Event> events;
  private final MeetingRequest request;
  private ArrayList<TimeRange> availableMeetingTimes;
  private Event[] eventsOrderedByStartTime;
  private Deque<TimeRange> busyTimeRanges;
  private boolean isCached;
  private int currentTime;

  private MeetingScheduler(Collection<Event> events, MeetingRequest request) {
    this.events = events;
    this.request = request;
    this.availableMeetingTimes = new ArrayList<>();
    this.eventsOrderedByStartTime = new Event[events.size()];
    this.busyTimeRanges = new ArrayDeque<>();
    this.isCached = false;
    this.currentTime = TimeRange.START_OF_DAY;
  }

  /**
   * Returns a meeting scheduler containing the specified events and meeting request.
   * @param events The specified events.
   * @param request The specified request.
   * @return A meeting scheduler containing the specified events and meeting request.
   * @throws IllegalArgumentException If events or request is null.
   */
  public static MeetingScheduler with(Collection<Event> events, MeetingRequest request)
      throws IllegalArgumentException {
    if (events == null || request == null) {
      throw new IllegalArgumentException("events and request must both be non-null.");
    }
    return new MeetingScheduler(events, request);
  }

  /**
   * Generates and returns a collection of available meeting times.
   * @return A collection of available meeting times.
   */
  public Collection<TimeRange> getAvailableMeetingTimes() {
    if (!isCached) {

      // Order events by start time
      eventsOrderedByStartTime = events.toArray(eventsOrderedByStartTime);
      Arrays.sort(eventsOrderedByStartTime,
          (event1, event2) -> TimeRange.ORDER_BY_START.compare(event1.getWhen(), event2.getWhen()));
      
      // Generate busy time ranges
      for (Event event : eventsOrderedByStartTime) {
        if (!Collections.disjoint(request.getAttendees(), event.getAttendees())) {
          processBusyTimeRange(event.getWhen());
        }
      }

      generateAvailableMeetingTimes();

      isCached = true;
    }
    return availableMeetingTimes;
  }

  private void processBusyTimeRange(TimeRange busyTimeRange) {
    if (busyTimeRanges.isEmpty() || !busyTimeRange.overlaps(busyTimeRanges.peekLast())) {
      busyTimeRanges.offer(busyTimeRange);
    } else {
      TimeRange previousBusyTimeRange = busyTimeRanges.pollLast();
      busyTimeRanges.offer(TimeRange.fromStartEnd(previousBusyTimeRange.start(),
          Math.max(previousBusyTimeRange.end(), busyTimeRange.end()), false));
    }
  }

  private void generateAvailableMeetingTimes() {
    generateMeetingsBetweenBusyTimeRanges();
    generateEndOfDayMeeting();
  }

  private void generateMeetingsBetweenBusyTimeRanges() {
    while (!busyTimeRanges.isEmpty()) {
      TimeRange nextBusyTimeRange = busyTimeRanges.poll();
      if (request.getDuration() <= nextBusyTimeRange.start() - currentTime) {
        availableMeetingTimes.add(TimeRange.fromStartEnd(currentTime, nextBusyTimeRange.start(), false));
      }
      currentTime = nextBusyTimeRange.end();
    }
  }

  private void generateEndOfDayMeeting() {
    int endOfDayTime = TimeRange.END_OF_DAY;
    if (request.getDuration() <= endOfDayTime - currentTime) {
      availableMeetingTimes.add(TimeRange.fromStartEnd(currentTime, endOfDayTime, true));
    }
  }
}
