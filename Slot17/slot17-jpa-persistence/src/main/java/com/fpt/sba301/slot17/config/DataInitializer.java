package com.fpt.sba301.slot17.config;

import com.fpt.sba301.slot17.domain.Category;
import com.fpt.sba301.slot17.domain.Course;
import com.fpt.sba301.slot17.domain.Department;
import com.fpt.sba301.slot17.domain.Employee;
import com.fpt.sba301.slot17.domain.Enrollment;
import com.fpt.sba301.slot17.domain.EnrollmentStatus;
import com.fpt.sba301.slot17.domain.News;
import com.fpt.sba301.slot17.domain.Student;
import com.fpt.sba301.slot17.domain.Tag;
import com.fpt.sba301.slot17.repository.CategoryRepository;
import com.fpt.sba301.slot17.repository.CourseRepository;
import com.fpt.sba301.slot17.repository.DepartmentRepository;
import com.fpt.sba301.slot17.repository.EmployeeRepository;
import com.fpt.sba301.slot17.repository.EnrollmentRepository;
import com.fpt.sba301.slot17.repository.NewsRepository;
import com.fpt.sba301.slot17.repository.StudentRepository;
import com.fpt.sba301.slot17.repository.TagRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initSeedData(CategoryRepository categoryRepository,
                                  NewsRepository newsRepository,
                                  TagRepository tagRepository,
                                  DepartmentRepository departmentRepository,
                                  EmployeeRepository employeeRepository,
                                  StudentRepository studentRepository,
                                  CourseRepository courseRepository,
                                  EnrollmentRepository enrollmentRepository) {
        return args -> {
            if (categoryRepository.count() == 0) {
                // 1. Seed Categories
                Category techCat = new Category("Technology", "Technology and Software Engineering news", true);
                Category eduCat = new Category("Education", "Academic and Curriculum news", true);
                Category studentCat = new Category("Student Life", "Clubs, activities and events", true);
                Category archiveCat = new Category("Archived Notices", "Old announcements", false);

                categoryRepository.save(techCat);
                categoryRepository.save(eduCat);
                categoryRepository.save(studentCat);
                categoryRepository.save(archiveCat);

                // 2. Seed Tags
                Tag tagJpa = tagRepository.save(new Tag("JPA"));
                Tag tagSpring = tagRepository.save(new Tag("Spring Boot"));
                Tag tagCampus = tagRepository.save(new Tag("Campus"));

                // 3. Seed News
                News news1 = new News("Mastering Spring Data JPA in Slot 17",
                        "Learn entity mappings, JpaRepository derived queries, and relationship ownership.",
                        "Admin", techCat);
                news1.addTag(tagJpa);
                news1.addTag(tagSpring);
                newsRepository.save(news1);

                News news2 = new News("Welcome to the New Academic Semester",
                        "Students are advised to review course syllabi and class schedules on edunext.",
                        "Academic Office", eduCat);
                news2.addTag(tagCampus);
                newsRepository.save(news2);

                News news3 = new News("Annual Student Hackathon Registration Open",
                        "Join the annual coding challenge to build modern web and mobile apps.",
                        "Student Union", studentCat);
                news3.addTag(tagCampus);
                news3.addTag(tagSpring);
                newsRepository.save(news3);

                // 4. Seed Department and Employee (Bai 1-5)
                Department itDept = new Department("Information Technology");
                departmentRepository.save(itDept);

                Employee emp1 = new Employee("Alice Nguyen", "alice@fpt.edu.vn", itDept);
                itDept.addEmployee(emp1);
                employeeRepository.save(emp1);

                // 5. Seed Student, Course, Enrollment (Bai 7)
                Student student = studentRepository.save(new Student("Bob Tran", "bob@fpt.edu.vn"));
                Course course = courseRepository.save(new Course("SBA301", "Integrate Single Page Application with Spring Boot"));

                Enrollment enrollment = new Enrollment(student, course, EnrollmentStatus.ENROLLED);
                enrollment.setFinalGrade(new BigDecimal("9.50"));
                enrollmentRepository.save(enrollment);
            }
        };
    }
}
