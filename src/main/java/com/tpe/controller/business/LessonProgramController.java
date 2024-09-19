package com.tpe.controller.business;

import com.tpe.payload.request.business.LessonProgramRequest;
import com.tpe.payload.response.ResponseMessage;
import com.tpe.payload.response.business.LessonProgramResponse;
import com.tpe.service.business.LessonProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lessonPrograms")
public class LessonProgramController {

    private final LessonProgramService lessonProgramService;


    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<LessonProgramResponse> saveLessonProgram(@RequestBody @Valid LessonProgramRequest lessonProgramRequest) {
        return lessonProgramService.saveLessonProgram(lessonProgramRequest);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public List<LessonProgramResponse> getAllLessonProgram() {
        return lessonProgramService.getAllLessonProgram();
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public LessonProgramResponse getLessonProgramById(@PathVariable Long id) {
        return lessonProgramService.getLessonProgramById(id);
    }

    @GetMapping("/getAllUnassigned")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    public List<LessonProgramResponse> getAllUnassigned() {
        return lessonProgramService.getAllUnassigned();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage deleteById(@PathVariable Long id) {
        return lessonProgramService.deleteById(id);
    }

    @GetMapping("/getAllLessonProgramByTeacher")
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public Set<LessonProgramResponse> getAllByTeacher(HttpServletRequest request) {
        return lessonProgramService.getAllLessonProgramByUser(request);
    }

    @GetMapping("/getAllLessonProgramByStudent")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public Set<LessonProgramResponse> getAllByStudent(HttpServletRequest httpServletRequest) {
        return lessonProgramService.getAllLessonProgramByUser(httpServletRequest);
    }


}


