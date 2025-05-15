package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public Genre getGenreById(Integer genreId) {
        if (genreId < 1 || !getAllGenres().contains(new Genre(genreId, ""))) {
            throw new NotFoundException("genreId is not found");
        }
        String sql = "SELECT * FROM genres WHERE genre_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> createGenreFromResultSet(rs), genreId)
                .stream().findAny().orElse(null);
    }

    public Set<Genre> getAllGenres() {
        String sql = "SELECT * FROM genres ORDER BY genre_id";
        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> createGenreFromResultSet(rs));
        return new HashSet<>(genres);
    }

    private Genre createGenreFromResultSet(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("genre_id"), rs.getString("name"));
    }

}
