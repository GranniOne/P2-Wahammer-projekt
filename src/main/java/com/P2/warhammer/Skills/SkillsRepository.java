package com.P2.warhammer.Skills;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SkillsRepository extends MongoRepository<Skills, String> {
}
