package com.P2.warhammer.campaigns;


import com.P2.warhammer.users.User;
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

    public Campaign addCampaign(String name, User gameMaster) {
        return repository.save(new Campaign(name, gameMaster));

    }
    public List<Campaign> getCampaignsByPlayerAndGameMaster(User players, User gameMaster) {
        return repository.findByPlayersContainingOrGameMaster(players, gameMaster);
    }

    public Campaign addCompletedCampaign(Campaign campaign){
        return repository.save(campaign);
    }

    public Campaign getCampaignById(String id){
        return repository.findCampaignById(id);
    }

    public void deleteCampaignById(String id){
        repository.deleteById(id);
    }

    public List<Campaign> getAllCampaigns() {
        return repository.findAll();
    }

    public List<Campaign> findByGameMaster(User gameMaster){
        return repository.findByGameMaster(gameMaster);
    }


    public void saveCampaign(Campaign campaign){
        repository.save(campaign);
    }
}
