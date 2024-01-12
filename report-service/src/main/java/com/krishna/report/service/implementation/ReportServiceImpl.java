package com.krishna.report.service.implementation;



import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.krishna.report.controller.ReportController;
import com.krishna.report.dto.QuizDto;
import com.krishna.report.dto.RegistrationDto;
import com.krishna.report.dto.ReportDto;
import com.krishna.report.entity.Report;
import com.krishna.report.exception.ResourceNotFoundException;
import com.krishna.report.externalService.QuizService;
import com.krishna.report.externalService.RegistrationService;
import com.krishna.report.repository.ReportRepository;
import com.krishna.report.service.ReportService;
import com.krishna.report.utility.Message;
import com.krishna.report.utility.SuccessResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the ReportService interface for managing Report.
 */
@Service
public class ReportServiceImpl implements ReportService {

    /**
     * This is Report Repository object that is for calling. the repository.
     * methods.
     */
    @Autowired
    private ReportRepository reportRepository;

    /**
     * This is use to map the category with Dto and viceversa.
     */
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private RegistrationService registrationService;
    
    @Autowired
    private QuizService quizService;

    /**
     * this is logger object that is use to generate log.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ReportController.class);

    /**
     * Adds a new report.
     * @param reportDto To find the questions.
     * @return A message indicating the result of the operation.
     */
    @Override
    public final SuccessResponse createReport(final ReportDto reportDto) {
        Report report = convertIntoEntity(reportDto);
        
        Optional<RegistrationDto> existingUser = Optional.ofNullable(
        		registrationService.getUser(reportDto.getUserEmailId()));
        if (existingUser.isEmpty()) {
            throw new ResourceNotFoundException(
                    Message.QUIZ_NOT_FOUND + reportDto.getUserEmailId());
        }
        
        Optional<QuizDto> existingQuiz = Optional.ofNullable(
        		quizService.getQuizById(reportDto.getQuizId()));
        if (existingQuiz.isEmpty()) {
            throw new ResourceNotFoundException(
                    Message.QUIZ_NOT_FOUND + reportDto.getUserEmailId());
        }
        
        reportRepository.save(report);
        return new SuccessResponse(HttpStatus.CREATED.value(),
                Message.REPORT_CREATED_SUCCESSFULLY);

    }

    /**
     * Retrieves a list of all reports By email.
     * @return A list of reportDto objects representing report.
     */
    @Override
    public final List<ReportDto> findReportByEmailId(final String email) {

        List<Report> reports = reportRepository.findByUserEmailId(email);
        if (reports.isEmpty()) {
            LOGGER.error(Message.REPORT_NOT_FOUND, email);
            throw new ResourceNotFoundException(
                    Message.REPORT_NOT_FOUND + email);
        }
        RegistrationDto user = registrationService.getUser(email);

        
        List<ReportDto> reportDtos = reports.stream()
                .map(this::convertIntoDto)
                .collect(Collectors.toList());

        reportDtos.forEach(reportDto -> reportDto.setUser(user));

        reportDtos.forEach(reportDto -> {
            QuizDto quiz = quizService.getQuizById(reportDto.getQuizId());
            reportDto.setQuiz(quiz);
        });
        
        return reportDtos;
    }

    /**
     * Retrieves a list of all reports By email of all users.
     * @return A list of reportDto objects representing report.
     */
    @Override
    public final List<ReportDto> getAllReport() {
        List<Report> reports = reportRepository.findAll();
        if (reports.isEmpty()) {
            LOGGER.error(Message.REPORT_NOT_FOUND, "No reports found");
            throw new ResourceNotFoundException(
                    Message.REPORT_NOT_FOUND + "No reports found");
        }

        List<ReportDto> reportDtos = reports.stream()
                .map(this::convertIntoDto)
                .collect(Collectors.toList());

        // Get all unique user emails from the reports
        Set<String> userEmails = reports.stream()
                .map(Report::getUserEmailId)
                .collect(Collectors.toSet());

        // Fetch users for all unique emails
        Map<String, RegistrationDto> usersMap = userEmails.stream()
                .collect(Collectors.toMap(
                        email -> email,
                        email -> registrationService.getUser(email)
                ));

        // Set user and quiz information in each ReportDto
        reportDtos.forEach(reportDto -> {
            RegistrationDto user = usersMap.get(reportDto.getUserEmailId());
            reportDto.setUser(user);

            QuizDto quiz = quizService.getQuizById(reportDto.getQuizId());
            reportDto.setQuiz(quiz);
        });

        return reportDtos;
    }


    /**
     * Converts a {@link ReportDto} object into a {@link Report} entity using
     * ModelMapper.
     * @param reportDto The {@link ReportDto} to convert.
     * @return The converted {@link Report} entity.
     */
    public Report convertIntoEntity(final ReportDto reportDto) {
        return modelMapper.map(reportDto, Report.class);
    }

    /**
     * Converts a {@link Report} entity into a {@link ReportDto} using
     * ModelMapper.
     * @param report The {@link Report} entity to convert.
     * @return The converted {@link ReportDto}.
     */
    public ReportDto convertIntoDto(final Report report) {
    	
    	 ReportDto reportDto = new ReportDto();
         reportDto.setReportId(report.getReportId());
         reportDto.setUserEmailId(report.getUserEmailId());
         reportDto.setQuizId(report.getQuizId());
         reportDto.setTotalMarks(report.getTotalMarks());
         reportDto.setMarksObtained(report.getMarksObtained());
         reportDto.setTotalQuestions(report.getTotalQuestions());
         reportDto.setAttemptedQuestions(report.getAttemptedQuestions());
         reportDto.setDateAndTime(report.getDateAndTime());
         reportDto.setWrongAnswers(report.getWrongAnswers());
         return reportDto;
    }

}
