package com.P2.warhammer.characters;

import com.P2.warhammer.users.User;
import org.springframework.stereotype.Service;
import java.util.List;


/**
 * Service for managing Character data in the MongoDB database.
 * Provides methods yet to be determined.
 *
 * <p>Class should never be manually implemented, its lifetime is managed as a spring bean by spring</p>
 *
 * @see Character
 * @see CharacterRepository
 */
@Service
public class CharacterService {
    private final CharacterRepository repository;

    public CharacterService(CharacterRepository repository) {
        this.repository = repository;
    }

    /**
     *
     * @param name
     * @param user
     * @return
     */

    public Character CreateAndAddCharacter(String name, User user, User GameMaster) {
        return repository.save(new Character(name, user, GameMaster));

    }
    public Character addCharacter(Character character) {
        return repository.save(character);
    }
    public List<Character> getCharactersByUser(String user) {
        return repository.getCharacterByuser(user);
    }

    public List<Character> getAllCharacters() {
        return repository.findAll();
    }
}
