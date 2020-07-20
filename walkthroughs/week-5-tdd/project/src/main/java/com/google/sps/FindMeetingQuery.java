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

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    List<Event> eventList = new ArrayList(events);

    Collections.sort(eventList, new Comparator<Event>() {
      @Override
      public int compare(Event event1, Event event2) {
        return event1.getWhen().start() - event2.getWhen().start();
      }
    });

    int currentRangeStart = TimeRange.START_OF_DAY;
    int currentRangeEnd;
    int currentRangeDuration;

    boolean sharesAttendees;

    List<TimeRange> availableTimes = new ArrayList<>();

    for (Event event : eventList) {
      sharesAttendees = false;

      for (String person : event.getAttendees()) {
        if (request.getAttendees().contains(person)) {
          sharesAttendees = true;
          break;
        }
      }

      if (!sharesAttendees) continue;

      currentRangeEnd = event.getWhen().start();
      currentRangeDuration = currentRangeEnd - currentRangeStart;

      if (currentRangeEnd >= currentRangeStart) {
        if (currentRangeDuration >= request.getDuration()) {
          availableTimes.add(
            TimeRange.fromStartDuration(currentRangeStart, currentRangeDuration));
        }
        currentRangeStart = event.getWhen().end();
      } else {
        if (event.getWhen().end() > currentRangeStart) {
          currentRangeStart = event.getWhen().end();
        }
      }
    }

    currentRangeEnd = TimeRange.END_OF_DAY;
    currentRangeDuration = currentRangeEnd - currentRangeStart;
    if (currentRangeDuration >= request.getDuration()) {
      availableTimes.add(
        TimeRange.fromStartEnd(currentRangeStart, currentRangeEnd, true));
    }

    return availableTimes;
  }
}
