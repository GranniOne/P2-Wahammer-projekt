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

    public Campaign addCampaign(String name, int age) {
        return repository.save(new Campaign(name, age));

    }

    public List<Campaign> getAllCampaigns() {
        return repository.findAll();
    }
}
