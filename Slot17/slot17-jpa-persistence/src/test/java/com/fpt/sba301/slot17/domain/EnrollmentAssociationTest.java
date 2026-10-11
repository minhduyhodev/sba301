package com.fpt.sba301.slot17.domain;

import com.fpt.sba301.slot17.exception.DuplicateResourceException;
import com.fpt.sba301.slot17.repository.CourseRepository;
import com.fpt.sba301.slot17.repository.EnrollmentRepository;
import com.fpt.sba301.slot17.repository.StudentRepository;
import com.fpt.sba301.slot17.service.EnrollmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class EnrollmentAssociationTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Test
    @DisplayName("Bai 7: Test Association entity (Enrollment) with metadata")
    void testEnrollmentAssociationEntity() {
        Student student = studentRepository.save(new Student("David Vu", "david@fpt.edu.vn"));
        Course course = courseRepository.save(new Course("PRN212", "Basic Cross-Platform Application"));

        Enrollment enrollment = enrollmentService.enroll(student.getId(), course.getId());

        assertThat(enrollment.getId()).isNotNull();
        assertThat(enrollment.getStudent().getName()).isEqualTo("David Vu");
        assertThat(enrollment.getCourse().getCode()).isEqualTo("PRN212");
        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.ENROLLED);
        assertThat(enrollment.getEnrolledAt()).isNotNull();

        Optional<Enrollment> found = enrollmentRepository.findByStudentIdAndCourseId(student.getId(), course.getId());
        assertThat(found).isPresent();

        // Unique constraint test: Duplicate enrollment should be rejected
        assertThatThrownBy(() -> enrollmentService.enroll(student.getId(), course.getId()))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already enrolled");
    }
}
