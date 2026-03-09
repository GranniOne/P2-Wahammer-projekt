package com.P2.warhammer.campaigns;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for managing {@link Campaign} entities in MongoDB.
 * Extends {@link MongoRepository} to provide standard CRUD operations.
 * Includes custom query methods yet to be determined.
 *
 * @see Character
 * @see MongoRepository
 */
@Repository
public interface CampaignRepository extends MongoRepository<Campaign, String> {
}
