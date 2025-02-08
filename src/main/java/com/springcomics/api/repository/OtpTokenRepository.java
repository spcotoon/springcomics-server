package com.springcomics.api.repository;

import com.springcomics.api.domain.member.OtpToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OtpTokenRepository extends CrudRepository<OtpToken, Long> {

    Optional<OtpToken> findByEmail(String email);

    void deleteByEmail(String email);
}
