package com.guba.app.domain.dto;

import java.time.LocalDate;
import java.time.YearMonth;

public record RangoDTO(
        YearMonth inicio,
        YearMonth fin
) {
}
