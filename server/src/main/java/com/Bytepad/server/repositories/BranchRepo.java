package com.Bytepad.server.repositories;

import com.Bytepad.server.models.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BranchRepo extends JpaRepository<Branch, UUID> {

    Branch findByBranch(String branch);

}
