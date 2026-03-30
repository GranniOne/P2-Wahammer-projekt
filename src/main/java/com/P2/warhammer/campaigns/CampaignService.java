package com.P2.warhammer.campaigns;


import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service for managing Campaign data in the MongoDB database.
 * Provides methods yet to be determined.
 *
 * <p>Class should never be manually implemented, its lifetime is managed as a spring bean by spring</p>
 *
 * @see Campaign
 * @see CampaignRepository
 */
@Service
public class CampaignService {
    private final CampaignRepository repository;

    public CampaignService(CampaignRepository repository) {
        this.repository = repository;
    }

    public Campaign addCampaign(String name, String gameMaster) {
        return repository.save(new Campaign(name, gameMaster));

    }

    public List<Campaign> getAllCampaigns() {
        return repository.findAll();
    }
}
