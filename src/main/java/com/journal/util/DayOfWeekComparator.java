package com.journal.util;

import com.google.common.collect.Ordering;
import com.journal.data.dto.WeekResponseDto;

import java.util.Arrays;
import java.util.Comparator;

public class DayOfWeekComparator implements Comparator<WeekResponseDto> {

    private final Ordering<String> dayOrdering = Ordering.explicit(Arrays
            .asList("Понеділок", "Вівторок", "Середа", "Четвер", "П`ятниця", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));


    @Override
    public int compare(WeekResponseDto o1, WeekResponseDto o2) {
        return dayOrdering.compare(o1.getDayOfWeek(), o2.getDayOfWeek());
    }
}
