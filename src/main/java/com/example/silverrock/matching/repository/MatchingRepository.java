package com.example.silverrock.matching.repository;

import com.example.silverrock.matching.Entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {
}
