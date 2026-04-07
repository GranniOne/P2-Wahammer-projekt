package com.P2.warhammer.Talents;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TalentRepository extends MongoRepository<Talent, String> {

}
