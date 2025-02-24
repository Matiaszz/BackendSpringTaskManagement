package dev.matias.TaskManagement.repositories;

import dev.matias.TaskManagement.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
