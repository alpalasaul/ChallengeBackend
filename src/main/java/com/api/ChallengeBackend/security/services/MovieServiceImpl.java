package com.api.ChallengeBackend.security.services;

import com.api.ChallengeBackend.dto.MovieDTO;
import com.api.ChallengeBackend.exceptions.ResourceNotFoundException;
import com.api.ChallengeBackend.models.Movie;
import com.api.ChallengeBackend.repositories.CharacterRepository;
import com.api.ChallengeBackend.repositories.MovieRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CharacterRepository characterRepository;

    private ModelMapper modelMapper;

    @Override
    public boolean isImage(String image) {
        return movieRepository.existsByImage(image);
    }

    @Override
    public boolean isTitle(String title) {
        return movieRepository.existsByTitle(title);
    }

    @Override
    public Movie saveMovie(Movie movie) {
        movie = movieRepository.save(movie);
        Movie finalMovie = movie;
        movie.getCharacters().forEach(character -> {
            character.setMovie(finalMovie);
            characterRepository.save(character);
        });
        return movie;
    }

    @Override
    @Transactional(readOnly = true)
    public Movie findMovieById(Integer idMovie) {
        return movieRepository.findById(idMovie)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "idMovie", idMovie));
    }

    @Override
    public List<Movie> findMovies() {
        return movieRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> findMovieByTitle(String title) {
        return movieRepository.findByTitle(title);
    }

    @Override
    public List<Movie> orderMovie(String order) {
        if (order.equalsIgnoreCase("ASC")) {
            return movieRepository.findAllByOrderByTitleAsc();
        } else if (order.equalsIgnoreCase("DESC")) {
            return movieRepository.findAllByOrderByTitleDesc();
        } else {
            return movieRepository.findAll();
        }
    }

    @Override
    public Movie updateMovie(MovieDTO movieDTO, Integer idMovie) {
        Movie movie = movieRepository.findById(idMovie)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "idMovie", idMovie));

        movie.setImage(movieDTO.getImage());
        movie.setTitle(movieDTO.getTitle());
        movie.setTimeStamp(movieDTO.getTimeStamp());
        movie.setQualification(movieDTO.getQualification());
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(Integer idMovie) {
        Movie movie = movieRepository.findById(idMovie)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "idMovie", idMovie));
        movieRepository.delete(movie);
    }

    // Convierte entidad a DTO
    private MovieDTO mapDTO(Movie movie) {
        return modelMapper.map(movie, MovieDTO.class);
    }

    // Convierte de DTO a Entidad
    private Movie mapEntity(MovieDTO movieDTO) {
        return modelMapper.map(movieDTO, Movie.class);
    }
}