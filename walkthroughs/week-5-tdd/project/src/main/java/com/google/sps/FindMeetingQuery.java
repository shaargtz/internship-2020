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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class only contains the query used to find the time ranges when a
 * meeting request coincides with available time.
 */
public final class FindMeetingQuery {
  /**
     * The way the query works is by going through the sorted events, and
     * if the current event does not have any of the people in the meeting
     * request, the event is skipped. If it does share people with the request,
     * availableTimeDuration is set to the available time from the end of the
     * previous event until the start of the current event. Then, if
     * availableTimeDuration is at least the same size as the duration of the
     * request, a TimeRange from pastEventEnd to currentEventStart is
     * added to the list of available times.
     *
     * Time complexity: 
     * n: number of events
     * m: number of attendees in every event
     * If m > logn, then O(nm)
     * Otherwise, O(nlogn)
     *
     * Space complexity:
     * n: number of events
     * m: number of available time ranges
     * O(m+n)
     */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    List<Event> eventList = new ArrayList(events);
    // Here eventList is sorted by event start time in ascending order.
    Collections.sort(eventList, new Comparator<Event>() {
      @Override
      public int compare(Event event1, Event event2) {
        return event1.getWhen().start() - event2.getWhen().start();
      }
    });
    // Since there are no previous events yet, the start of day is also the
    // start of the first range to check available time.
    int pastEventEnd = TimeRange.START_OF_DAY;
    int currentEventStart;
    int availableTimeDuration;
    List<TimeRange> availableTimes = new ArrayList<>();
    for (Event event : eventList) {
      // Initialized as false since nothing has been compared yet.
      boolean sharesAttendees = false;
      for (String person : event.getAttendees()) {
        if (request.getAttendees().contains(person)) {
          sharesAttendees = true;
          break;
        }
      }
      // If the event does not conflict with the attendees, it's skipped.
      if (!sharesAttendees) continue;
      // Available time goes from the end of the previous event to the current
      // event.
      currentEventStart = event.getWhen().start();
      availableTimeDuration = currentEventStart - pastEventEnd;
      // This case is if the current event starts after the previous one ends.
      // Example: |--A--|   |--B--|
      if (currentEventStart > pastEventEnd) {
        if (availableTimeDuration >= request.getDuration()) {
          availableTimes.add(TimeRange.fromStartDuration(
              pastEventEnd, availableTimeDuration));
        }
        // Regadrdless of whether the duration was enough for the meeting,
        // the next available time will start when the current event ends.
        pastEventEnd = event.getWhen().end();
      } 
      // If there is no room from the previous event to the current event,
      // we just change the beginning of the next available time.
      // Example: |--A--|--B--|
      else if (currentEventStart == pastEventEnd) {
        pastEventEnd = event.getWhen().end();
      }
      // This case is if the current event begins before the previous one ends
      // and the current event isn't absorbed by the previous one, meaning that
      // the next beginning of the next available time is changed.
      // Example: |--A--|
      //              |--B--|
      else if (event.getWhen().end() > pastEventEnd) {
        pastEventEnd = event.getWhen().end();
      }
      // There is no else, since would mean that the current event is
      // completely absorbed by the previous event, meaning that the next
      // available time still starts when the previous event ends.
      // Example: |----A----|
      //            |--B--|
    }

    // Now we just need to check the time from the end of the last meeting
    // until end of day, basically doing the same procedure as before.
    currentEventStart = TimeRange.END_OF_DAY;
    if (currentEventStart - pastEventEnd >= request.getDuration()) {
      availableTimes.add(TimeRange.fromStartEnd(
          pastEventEnd, currentEventStart, /*inclusive=*/true));
    }

    return availableTimes;
  }
}
