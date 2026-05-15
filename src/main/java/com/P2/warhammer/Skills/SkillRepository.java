package com.P2.warhammer.Skills;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SkillRepository extends MongoRepository<Skill, String> {

    Skill findSkillByName(String name);
    @Query("{ 'Characteristic' : ?0 }")
    List<Skill> findSkillsByCharacteristic(String Characteristic);

}
