package com.P2.warhammer.characters;

import com.P2.warhammer.campaigns.Campaign;
import com.P2.warhammer.users.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link Character} entities in MongoDB.
 * Extends {@link MongoRepository} to provide standard CRUD operations.
 * Includes custom query methods yet to be determined.
 *
 * @see Character
 * @see MongoRepository
 */
@Repository
public interface CharacterRepository extends MongoRepository<Character, String> {

    List<Character> getCharactersByUser(User user);

    List<Character> findByCampaign(Campaign campaign);


}
