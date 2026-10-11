package com.fpt.sba301.slot17.service;

import com.fpt.sba301.slot17.domain.Course;
import com.fpt.sba301.slot17.domain.Enrollment;
import com.fpt.sba301.slot17.domain.EnrollmentStatus;
import com.fpt.sba301.slot17.domain.Student;
import com.fpt.sba301.slot17.exception.DuplicateResourceException;
import com.fpt.sba301.slot17.exception.ResourceNotFoundException;
import com.fpt.sba301.slot17.repository.CourseRepository;
import com.fpt.sba301.slot17.repository.EnrollmentRepository;
import com.fpt.sba301.slot17.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnrollmentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(StudentRepository studentRepository, CourseRepository courseRepository,
                             EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional
    public Enrollment enroll(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + courseId));

        if (enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId).isPresent()) {
            throw new DuplicateResourceException("Student is already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment(student, course, EnrollmentStatus.ENROLLED);
        return enrollmentRepository.save(enrollment);
    }
}
