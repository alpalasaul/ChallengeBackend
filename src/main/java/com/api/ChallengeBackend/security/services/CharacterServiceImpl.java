package com.api.ChallengeBackend.security.services;

import com.api.ChallengeBackend.dto.CharacterDTO;
import com.api.ChallengeBackend.exceptions.BlogAppException;
import com.api.ChallengeBackend.exceptions.ResourceNotFoundException;
import com.api.ChallengeBackend.models.Character;
import com.api.ChallengeBackend.models.Movie;
import com.api.ChallengeBackend.repositories.CharacterRepository;
import com.api.ChallengeBackend.repositories.MovieRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CharacterServiceImpl implements CharacterService {

    private ModelMapper modelMapper;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public boolean isImage(String image) {
        return characterRepository.existsByImage(image);
    }

    @Override
    public boolean isHistory(String history) {
        return characterRepository.existsByHistory(history);
    }

    @Override
    public Character addCharacter(Integer idMovie, CharacterDTO characterDTO) {
        Character character = new Character(
                characterDTO.getImage(),
                characterDTO.getName(),
                characterDTO.getAge(),
                characterDTO.getWeight(),
                characterDTO.getHistory());

        Movie movie = movieRepository.findById(idMovie)
                .orElseThrow(() -> new ResourceNotFoundException("pelicula", "idMovie", idMovie));
        character.setMovie(movie);

        return characterRepository.save(character);
    }

    @Override
    @Transactional(readOnly = true)
    public Character findCharacterById(Long idPersonaje) {
        return characterRepository.findById(idPersonaje)
                .orElseThrow(() -> new ResourceNotFoundException("Character", "idPersonaje", idPersonaje));
    }

    @Override
    public List<Character> findCharacters() {
        return characterRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Character> findCharacterByName(String name) {
        return characterRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Character> findCharacterByAge(Integer age) {
        return characterRepository.findAllByAge(age);
    }

    @Override
    public Character updateCharacter(CharacterDTO characterDTO, Long idPersonaje) {
        Character character = characterRepository.findById(idPersonaje)
                .orElseThrow(() -> new ResourceNotFoundException("Character", "idPersonaje", idPersonaje));

        character.setImage(characterDTO.getImage());
        character.setName(characterDTO.getName());
        character.setAge(characterDTO.getAge());
        character.setWeight(characterDTO.getWeight());
        character.setHistory(characterDTO.getHistory());
        return characterRepository.save(character);
    }

    @Override
    public void deleteCharacter(Integer idMovie, Long idPersonaje) {
        Movie movie = movieRepository.findById(idMovie)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "idMovie", idMovie));

        Character character = characterRepository.findById(idPersonaje)
                .orElseThrow(() -> new ResourceNotFoundException("Character", "idPersonaje", idPersonaje));

        characterRepository.delete(character);

        if (!character.getMovie().getIdMovie().equals(movie.getIdMovie())) {
            throw new BlogAppException(HttpStatus.BAD_REQUEST, "El comentario no pertenece a la publicación");
        }
    }

    // Convierte entidad a DTO
    private CharacterDTO mapDTO(Character character) {
        return modelMapper.map(character, CharacterDTO.class);
    }

    // Convierte de DTO a Entidad
    private Character mapEntity(CharacterDTO characterDTO) {
        return modelMapper.map(characterDTO, Character.class);
    }
}
