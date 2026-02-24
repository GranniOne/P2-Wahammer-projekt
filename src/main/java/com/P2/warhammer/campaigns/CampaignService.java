package com.P2.warhammer.campaigns;
import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterRepository;
import org.springframework.stereotype.Service;

import java.util.List;


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
