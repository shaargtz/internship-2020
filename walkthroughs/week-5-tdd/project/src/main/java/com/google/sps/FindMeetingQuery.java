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

import java.util.Collection;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    /** 
     * Check edge case that there are no other events, so the meeting can
     * be held at any time.
     */
    if (events.isEmpty()) return Arrays.asList(TimeRange.WHOLE_DAY);

    /** 
     * Check edge case that there are no attendees for the meeting, so it can
     * be held at any time.
     */
    if (meeting.getAttendees().isEmpty()) return Arrays.asList(TimeRange.WHOLE_DAY);

    /** 
     * Check edge case that the duration of the meeting is longer than a day,
     * so the meeting cannot be held.
     */
    if (meeting.getAttendees().isEmpty()) return Arrays.asList();
  }
}
