package com.tpe.service.user;

import com.tpe.entity.concretes.user.User;
import com.tpe.entity.enums.RoleType;
import com.tpe.exception.ConflictException;
import com.tpe.payload.mappers.UserMapper;
import com.tpe.payload.messages.ErrorMessages;
import com.tpe.payload.messages.SuccessMessages;
import com.tpe.payload.request.user.TeacherRequest;
import com.tpe.payload.response.ResponseMessage;
import com.tpe.payload.response.user.StudentResponse;
import com.tpe.payload.response.user.TeacherResponse;
import com.tpe.payload.response.user.UserResponse;
import com.tpe.repository.user.UserRepository;
import com.tpe.service.helper.MethodHelper;
import com.tpe.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final UserRepository userRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final UserRoleService userRoleService;
    private PasswordEncoder passwordEncoder;
    private final MethodHelper methodHelper;

    public ResponseMessage<TeacherResponse> saveTeacher(TeacherRequest teacherRequest) {

        uniquePropertyValidator.checkDuplicate(
                teacherRequest.getUsername()
                , teacherRequest.getSsn()
                , teacherRequest.getPhoneNumber()
                , teacherRequest.getEmail()
        );
        User teacher = userMapper.mapTeacherRequestToUser(teacherRequest);
        teacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));

        teacher.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));

        if (teacherRequest.getIsAdvisorTeacher()) {
            teacher.setIsAdvisor(Boolean.TRUE);
        } else {
            teacher.setIsAdvisor(Boolean.FALSE);
        }

        User savedTeacher = userRepository.save(teacher);

        return ResponseMessage.<TeacherResponse>builder()
                .message(SuccessMessages.TEACHER_SAVE)
                .object(userMapper.mapUserToTeacherResponse(savedTeacher))
                .build();
    }


    public List<StudentResponse> getAllStudentByAdvisorUsername(String userName) {

        User teacher = methodHelper.isUserExistsByUsername(userName);
        methodHelper.checkAdvisor(teacher);

        return userRepository.findByAdvisorTeacherId(teacher.getId())
                .stream()
                .map(userMapper::mapUserToStudentResponse)
                .collect(Collectors.toList());
    }

    public ResponseMessage<TeacherResponse> updateTeacherForManagers(TeacherRequest teacherRequest, Long userId) {

        User user = methodHelper.isUserExist(userId);

        methodHelper.checkRole(user, RoleType.TEACHER);

        uniquePropertyValidator.checkUniqueProperties(user, teacherRequest);

        User updatedTeacher = userMapper.mapTeacherRequestToUpdatedUser(teacherRequest, userId);

        updatedTeacher.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));

        updatedTeacher.setUserRole(userRoleService.getUserRole(RoleType.TEACHER));

        User savedTeacher = userRepository.save(updatedTeacher);

        return ResponseMessage.<TeacherResponse>builder()
                .object(userMapper.mapUserToTeacherResponse(savedTeacher))
                .message(SuccessMessages.TEACHER_UPDATE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<UserResponse> saveAdvisorTeacher(Long teacherId) {

        User teacher = methodHelper.isUserExist(teacherId);

        methodHelper.checkRole(teacher, RoleType.TEACHER);

        if (Boolean.TRUE.equals(teacher.getIsAdvisor())) {
            throw new ConflictException(
                    String.format(ErrorMessages.ALREADY_EXIST_ADVISOR_MESSAGE, teacherId));
        }

        teacher.setIsAdvisor(Boolean.TRUE);
        userRepository.save(teacher);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.ADVISOR_TEACHER_SAVE)
                .object(userMapper.mapUserToUserResponse(teacher))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<UserResponse> deleteAdvisorTeacherById(Long teacherId) {

        User teacher = methodHelper.isUserExist(teacherId);

        methodHelper.checkRole(teacher, RoleType.TEACHER);

        methodHelper.checkAdvisor(teacher);

        teacher.setIsAdvisor(Boolean.FALSE);

        userRepository.save(teacher);

        List<User> allStudent = userRepository.findByAdvisorTeacherId(teacherId);
        if (!allStudent.isEmpty()) {
            allStudent.forEach(student -> student.setAdvisorTeacherId(null));
        }
        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.ADVISOR_TEACHER_DELETE)
                .object(userMapper.mapUserToUserResponse(teacher))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public List<UserResponse> getAllAdvisorTeacher() {

        return userRepository.findAllByAdvisor(Boolean.TRUE)
                .stream()
                .map(userMapper::mapUserToUserResponse)
                .collect(Collectors.toList());

    }
}
