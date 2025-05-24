package com.marcos.personalNotesWebApplication.repositories;

import com.marcos.personalNotesWebApplication.entities.CalendarEventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.UUID;

@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEventEntity,UUID> {

    /**
     * Finds all calendar events that start between the specified start and end dates.
     *
     * @param startTime the start date of the range
     * @param endTime   the end date of the range
     * @param pageable  the pagination information
     * @return a page of calendar events within the specified date range
     */
    Page<CalendarEventEntity> findAllByStartTimeBetween(Instant startTime, Instant endTime, Pageable pageable);
}
