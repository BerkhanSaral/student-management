package com.tpe.service.business;

import com.tpe.entity.concretes.business.EducationTerm;
import com.tpe.entity.concretes.business.Lesson;
import com.tpe.entity.concretes.business.LessonProgram;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.payload.mappers.LessonProgramMapper;
import com.tpe.payload.messages.ErrorMessages;
import com.tpe.payload.messages.SuccessMessages;
import com.tpe.payload.request.business.LessonProgramRequest;
import com.tpe.payload.response.ResponseMessage;
import com.tpe.payload.response.business.LessonProgramResponse;
import com.tpe.repository.business.LessonProgramRepository;
import com.tpe.service.validator.DateTimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonProgramService {

    private final LessonProgramRepository lessonProgramRepository;
    private final LessonService lessonService;
    private final EducationTermService educationTermService;
    private DateTimeValidator dateTimeValidator;
    private final LessonProgramMapper lessonProgramMapper;

    public ResponseMessage<LessonProgramResponse> saveLessonProgram(LessonProgramRequest lessonProgramRequest) {

        Set<Lesson> lessons = lessonService.getLessonByLessonIdSet(lessonProgramRequest.getLessonIdList());
        EducationTerm educationTerm = educationTermService.getEducationTermById(lessonProgramRequest.getEducationTermId());
        if (lessons.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.NOT_FOUND_LESSON_IN_LIST);
        }
        dateTimeValidator.checkTimeWithException(lessonProgramRequest.getStartTime(), lessonProgramRequest.getStopTime());

        LessonProgram lessonProgram = lessonProgramMapper.mapLessonProgramRequestToLessonProgram(lessonProgramRequest, lessons, educationTerm);

        LessonProgram savedLessonProgram = lessonProgramRepository.save(lessonProgram);

        return ResponseMessage.<LessonProgramResponse>builder()
                .message(SuccessMessages.LESSON_PROGRAM_SAVE)
                .httpStatus(HttpStatus.CREATED)
                .object(lessonProgramMapper.mapLessonProgramToLessonProgramResponse(savedLessonProgram))
                .build();
    }

    public List<LessonProgramResponse> getAllLessonProgram() {

        return lessonProgramRepository
                .findAll()
                .stream()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toList());
    }


    public LessonProgramResponse getLessonProgramById(Long id) {

        return lessonProgramMapper.mapLessonProgramToLessonProgramResponse(isLessonProgramExist(id));
    }

    private LessonProgram isLessonProgramExist(Long id) {
        return lessonProgramRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_LESSON_PROGRAM_MESSAGE, id))
        );
    }

    public List<LessonProgramResponse> getAllUnassigned() {

        return lessonProgramRepository.findByUsers_IdNull()
                .stream()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toList());
    }

    public ResponseMessage deleteById(Long id) {

        isLessonProgramExist(id);
        lessonProgramRepository.deleteById(id);
        return ResponseMessage.builder()
                .message(SuccessMessages.LESSON_PROGRAM_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public Set<LessonProgramResponse> getAllLessonProgramByUser(HttpServletRequest request) {

        String userName = (String) request.getAttribute("username");

        return lessonProgramRepository.getLessonProgramByUsersUsername(userName)
                .stream()
                .map(lessonProgramMapper::mapLessonProgramToLessonProgramResponse)
                .collect(Collectors.toSet());
    }

}