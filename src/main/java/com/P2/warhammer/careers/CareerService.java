package com.P2.warhammer.careers;


import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class CareerService {
    private final CareerRepository repository;

    public CareerService(CareerRepository repository) {
        this.repository = repository;
    }

    public Career addCareer(String Id, String Name, List<String> levelCharacterticsList, List<String> levelTrappingsList, List<String> levelStatusList, List<List<String>> levelTalentsList, List<List<String>> levelSkillsList, String socialClass) {
        return repository.save(new Career(Id, Name, levelCharacterticsList, levelTrappingsList, levelStatusList, levelTalentsList, levelSkillsList, socialClass));
    }

    public List<Career> getAllCareers() {
        return repository.findAll();
    }
}
