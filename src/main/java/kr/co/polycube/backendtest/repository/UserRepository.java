package kr.co.polycube.backendtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.polycube.backendtest.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
