package com.example.silverrock.matching.repository;
import com.example.silverrock.matching.Entity.Matching;
import com.example.silverrock.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchingRequestRepository extends JpaRepository<Matching, Long> {
    Optional<List<Matching>> findMatchingByReceiver(User user);

    Optional<List<Matching>> findMatchingBySender(User user);
    List<Matching> findBySenderAndReceiverAndSuccess(User sender, User receiver, boolean success);
}
