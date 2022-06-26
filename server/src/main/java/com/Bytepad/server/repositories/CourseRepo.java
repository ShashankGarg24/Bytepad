package com.Bytepad.server.repositories;

import com.Bytepad.server.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CourseRepo extends JpaRepository<Course, UUID> {

    Course findByCourse(String course);

}
