package com.asistec.attendance.controller;




import com.asistec.attendance.dto.SectionResponse;
import com.asistec.attendance.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
@RequiredArgsConstructor
public class SectionController {

    private final SectionRepository sectionRepository;

    @GetMapping
    public List<SectionResponse> getSections() {

        return sectionRepository.findAll()
                .stream()
                .map(section ->
                        new SectionResponse(
                                section.getId(),
                                section.getName()
                        )
                )
                .toList();
    }
}