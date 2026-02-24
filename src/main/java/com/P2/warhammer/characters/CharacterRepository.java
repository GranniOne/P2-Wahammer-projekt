package com.P2.warhammer.characters;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CharacterRepository extends MongoRepository<Character, String> {
}
