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
  // This comparator is used to sort a list of events by start time
  // in ascending order.
  private Comparator<Event> eventComparator = new Comparator<Event>() {
    @Override
    public int compare(Event event1, Event event2) {
      return event1.getWhen().start() - event2.getWhen().start();
    }
  };
  /**
   * Given a set of events and a meeting request, the query returns a list of
   * free time ranges available for the meeting to be scheduled. If no valid
   * time range is found, returns an empty list.
   *
   * Time complexity: 
   * n: number of events
   * m: number of attendees in every event
   * O(nm)
   *
   * Space complexity:
   * n: number of events
   * m: number of available time ranges
   * O(m+n)
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    // We pass the events into a list since the collection can't be sorted,
    // and we sort the list by event start time in ascending order.
    List<Event> eventList = new ArrayList(events);
    Collections.sort(eventList, eventComparator);

    // Since there are no previous events yet, the start of day is also the
    // start of the first range to check available time.
    int pastEventEnd = TimeRange.START_OF_DAY;
    List<TimeRange> availableTimes = new ArrayList<>();
    for (Event event : eventList) {
      if (!eventSharesAttendeesWithRequest(event, request)) {
        // If the event does not share attendes with the request, it means that
        // it does not change time availability, so it's skipped.
        continue;
      }

      int currentEventStart = event.getWhen().start();
      int availableTimeDuration = currentEventStart - pastEventEnd;
      if (currentEventStart > pastEventEnd) {
        // Example: |--A--|   |--B--|
        if (availableTimeDuration >= request.getDuration()) {
          availableTimes.add(TimeRange.fromStartDuration(
              pastEventEnd, availableTimeDuration));
        }
        pastEventEnd = event.getWhen().end();
      } 
      else if (currentEventStart == pastEventEnd || event.getWhen().end() > pastEventEnd) {
        //     Examples: |--A--|--B--|
        //                     or
        //               |--A--|
        //                    |--B--|
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
    if (TimeRange.END_OF_DAY - pastEventEnd >= request.getDuration()) {
      availableTimes.add(TimeRange.fromStartEnd(
          pastEventEnd, TimeRange.END_OF_DAY, /*inclusive=*/true));
    }

    return availableTimes;
  }

  /**
   * This is a helper function to check if an event shares attendees with
   * a meeting request.
   */
  private boolean eventSharesAttendeesWithRequest(Event event, MeetingRequest request) {
    for (String attendee : event.getAttendees()) {
      if (request.getAttendees().contains(attendee)) {
        return true;
      }
    }
    return false;
  }
}
