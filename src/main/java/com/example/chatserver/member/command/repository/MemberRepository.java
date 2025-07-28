package com.example.chatserver.member.command.repository;

import com.example.chatserver.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // Optional 객체 => 값 있는 지 없는지
    Optional<Member> findByEmail(String email);
}
