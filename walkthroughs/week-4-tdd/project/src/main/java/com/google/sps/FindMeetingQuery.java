// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    PriorityQueue<TimeRange> busyTimeRanges = getBusyTimeRanges(events, request);
    busyTimeRanges = combineOverlappingTimeRanges(busyTimeRanges);
    return getAvailableMeetingTimes(busyTimeRanges, request);
  }

  private PriorityQueue<TimeRange> getBusyTimeRanges(Collection<Event> events,
      MeetingRequest request) {
    PriorityQueue<TimeRange> busyTimeRanges = new PriorityQueue<>(TimeRange.ORDER_BY_START);

    // Find all time blocks where at least one meeting attendee is busy (attending another event)
    for (Event event : events) {
      if (hasMeetingAttendee(request.getAttendees(), event)) {
        busyTimeRanges.offer(event.getWhen());
      }
    }

    return busyTimeRanges;
  }

  private boolean hasMeetingAttendee(Collection<String> meetingAttendees, Event event) {
      for (String eventAttendee : event.getAttendees()) {
        if (meetingAttendees.contains(eventAttendee)) {
          return true;
        }
      }
    return false;
  }

  private PriorityQueue<TimeRange> combineOverlappingTimeRanges(PriorityQueue<TimeRange> timeRanges) {
    PriorityQueue<TimeRange> combinedTimeRanges = new PriorityQueue<>(TimeRange.ORDER_BY_START);

    while (!timeRanges.isEmpty()) {
      TimeRange timeRange = timeRanges.poll();

      // Combine all subsequent time ranges which overlap with the current time range
      while (!timeRanges.isEmpty() && timeRanges.peek().overlaps(timeRange)) {
        TimeRange nextTimeRange = timeRanges.poll();
        int start = timeRange.start();
        int combinedDuration = Math.max(timeRange.end(), nextTimeRange.end()) - start;
        timeRange = TimeRange.fromStartDuration(start, combinedDuration);
      }

      combinedTimeRanges.offer(timeRange);
    }

    return combinedTimeRanges;
  }

  private Collection<TimeRange> getAvailableMeetingTimes(PriorityQueue<TimeRange> busyTimeRanges,
      MeetingRequest request) {
    ArrayList<TimeRange> availableMeetingTimes = new ArrayList<>();

    long meetingDuration = request.getDuration();
    int currentTime = TimeRange.START_OF_DAY;

    while (!busyTimeRanges.isEmpty()) {
      TimeRange nextBusyTimeRange = busyTimeRanges.poll();
      processFreeTime(currentTime, nextBusyTimeRange.start(), availableMeetingTimes,
          meetingDuration, false);
      currentTime = nextBusyTimeRange.end();
    }

    // Check if the time between the last event and the end of the day can be used for a meeting
    processFreeTime(currentTime, TimeRange.END_OF_DAY, availableMeetingTimes, meetingDuration, true);

    return availableMeetingTimes;
  }

  private void processFreeTime(int currentTime, int nextBusyTime,
      ArrayList<TimeRange> availableMeetingTimes, long meetingDuration,
      boolean isEndOfDay) {
    int freeTimeDuration = nextBusyTime - currentTime;
    if (freeTimeDuration >= meetingDuration) {
      int endTime = currentTime + freeTimeDuration;
      availableMeetingTimes.add(TimeRange.fromStartEnd(currentTime, endTime, isEndOfDay));
    }
  }

}
