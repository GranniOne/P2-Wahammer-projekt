package com.P2.warhammer.characters;
import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CharacterService {
    private final CharacterRepository repository;

    public CharacterService(CharacterRepository repository) {
        this.repository = repository;
    }

    public Character addCharacter(String name, int age) {
        return repository.save(new Character(name, age));

    }

    public List<Character> getAllCharacters() {
        return repository.findAll();
    }
}
