package com.asistec.report.dto;

public record SectionSummaryResponse(

        Long sectionId,
        String sectionName,
        long presentCount,
        long absentCount,
        long lateCount

) {
}