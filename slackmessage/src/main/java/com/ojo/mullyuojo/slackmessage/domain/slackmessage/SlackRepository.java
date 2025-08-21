package com.ojo.mullyuojo.slackmessage.domain.slackmessage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackRepository extends JpaRepository<SlackMessage, Long> {
}
