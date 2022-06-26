package com.Bytepad.server.repositories;

import com.Bytepad.server.models.Branch;
import com.Bytepad.server.models.Course;
import com.Bytepad.server.models.CourseBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseBranchRepo extends JpaRepository<CourseBranch, UUID> {



    @Query("Select cb.branch from CourseBranch cb where cb.course = ?1")
    List<Branch> findBranchesFromCourse(Course course);

    CourseBranch findByCourseAndBranch(Course course, Branch branch);

    CourseBranch findByCourse_CourseIdAndBranch_BranchId(UUID courseId, UUID branchId);

    List<CourseBranch> findAllByCourse_CourseId(UUID courseId);

    List<CourseBranch> findAllByBranch_BranchId(UUID branchId);

}