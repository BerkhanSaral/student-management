package com.tpe.controller.business;

import com.tpe.payload.response.ResponseMessage;
import com.tpe.payload.response.business.EducationTermResponse;
import com.tpe.service.business.EducationTermService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/educationTerm")
@RequiredArgsConstructor
public class EducationTermController {

    private final EducationTermService educationTermService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN',MANAGER','ASSISTANT_MANAGER','TEACHER')")
    public EducationTermResponse getEducationTermById(@PathVariable Long id) {
        return educationTermService.getEducationTermResponseById(id);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('ADMIN',MANAGER','ASSISTANT_MANAGER','TEACHER')")
    public List<EducationTermResponse> getAllEducationTerm() {
        return educationTermService.getAllEducationTerm();
    }

    @GetMapping("/getAllEducationTermByPage")
    @PreAuthorize("hasAnyAuthority('ADMIN',MANAGER','ASSISTANT_MANAGER','TEACHER')")
    public Page<EducationTermResponse> getAllEducationTermByPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "startDate") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ) {
        return educationTermService.getAllEducationTermByPage(page, size, sort, type);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN',MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<?> deleteEducationTermById(@PathVariable Long id) {
        return educationTermService.deleteEducationTermById(id);
    }


}
