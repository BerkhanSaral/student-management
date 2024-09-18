package com.tpe.controller.user;

import com.tpe.payload.request.user.TeacherRequest;
import com.tpe.payload.response.ResponseMessage;
import com.tpe.payload.response.user.StudentResponse;
import com.tpe.payload.response.user.TeacherResponse;
import com.tpe.payload.response.user.UserResponse;
import com.tpe.service.user.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<ResponseMessage<TeacherResponse>> saveTeacher(
            @RequestBody @Valid TeacherRequest teacherRequest) {

        return ResponseEntity.ok(teacherService.saveTeacher(teacherRequest));
    }

    @GetMapping("/getAllStudentByAdvisorUsername")
    // http://localhost:8080/teacher/getAllStudentByAdvisorUsername  + GET
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    public List<StudentResponse> getAllStudentByAdvisorUsername(HttpServletRequest request) {

        String userName = request.getHeader("username");
        return teacherService.getAllStudentByAdvisorUsername(userName);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN',MANAGER','ASSISTANT_MANAGER')")
    @PutMapping("/update/{userId}")
    public ResponseMessage<TeacherResponse> updateTeacherForManagers(@RequestBody @Valid TeacherRequest teacherRequest,
                                                                     @PathVariable Long userId){
        return  teacherService.updateTeacherForManagers(teacherRequest,userId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN',MANAGER','ASSISTANT_MANAGER')")
    @PatchMapping("/saveAdvisorTeacher/{teacherId}")
    public ResponseMessage<UserResponse> saveAdvisorTeacher(@PathVariable Long teacherId){
        return teacherService.saveAdvisorTeacher(teacherId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN',MANAGER','ASSISTANT_MANAGER')")
    @DeleteMapping("/deleteAdvisorTeacherById/{id}")
    public ResponseMessage<UserResponse> deleteAdvisorTeacherById(@PathVariable Long id){
        return teacherService.deleteAdvisorTeacherById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN',MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getAllAdvisorTeacher")
    public List<UserResponse> getAllAdvisorTeacher(){
        return teacherService.getAllAdvisorTeacher();
    }

}
