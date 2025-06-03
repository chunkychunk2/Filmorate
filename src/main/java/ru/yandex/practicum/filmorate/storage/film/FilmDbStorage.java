package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Primary
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, Math.toIntExact(film.getDuration()));
            ps.setObject(5, film.getMpa() != null ? film.getMpa().getId() : null);
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            film.setId(keyHolder.getKey().longValue());
        }
        insertFilmGenres(film);
        return film;
    }

    private void insertFilmGenres(Film film) {
        String genreSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(genreSql, film.getId(), genre.getId());
        }
    }


    @Override
    public Collection<Film> findAll() {
        String sql = "SELECT f.*, g.genre_id, g.name AS genre_name " +
                "FROM films f " +
                "LEFT JOIN film_genres fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id = g.genre_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getLong("film_id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            Integer mpaId = rs.getObject("mpa_id", Integer.class);
            if (mpaId != null) {
                film.setMpa(new Mpa(mpaId, null));
            }
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("genre_name"));
            film.getGenres().add(genre);
            return film;
        });
    }

    @Override
    public Film findById(long id) {
        String sql = "SELECT * FROM films WHERE film_id = ?";
        Film film = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Film f = new Film();
            f.setId(rs.getLong("film_id"));
            f.setName(rs.getString("name"));
            f.setDescription(rs.getString("description"));
            f.setReleaseDate(rs.getDate("release_date").toLocalDate());
            f.setDuration(rs.getInt("duration"));
            Integer mpaId = rs.getObject("mpa_id", Integer.class);
            if (mpaId != null) {
                String ratingName = jdbcTemplate.queryForObject(
                        "SELECT name " +
                                "FROM mpa_ratings " +
                                "WHERE mpa_id = ?", String.class, mpaId);
                f.setMpa(new Mpa(mpaId, ratingName));
            }
            return f;
        }, id);
        String genreSql = "SELECT g.genre_id, g.name " +
                "FROM genres g " +
                "JOIN film_genres fg " +
                "ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id = ?";
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(genreSql, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("name"));
            return genre;
        }, id));
        assert film != null;
        film.setGenres(genres);
        return film;
    }

    @Override
    public Film findByName(String name) {
        String sql = "SELECT * FROM films WHERE name = ?";
        Film film = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Film f = new Film();
            f.setId(rs.getLong("film_id"));
            f.setName(rs.getString("name"));
            f.setDescription(rs.getString("description"));
            f.setReleaseDate(rs.getDate("release_date").toLocalDate());
            f.setDuration(rs.getInt("duration"));
            Integer mpaId = rs.getObject("mpa_id", Integer.class);
            if (mpaId != null) {
                String ratingName = jdbcTemplate.queryForObject(
                        "SELECT name " +
                                "FROM mpa_ratings " +
                                "WHERE mpa_id = ?", String.class, mpaId);
                f.setMpa(new Mpa(mpaId, ratingName));
            }
            return f;
        }, name);
        String genreSql = "SELECT g.genre_id, g.name " +
                "FROM genres g " +
                "JOIN film_genres fg " +
                "ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id = ?";
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(genreSql, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("name"));
            return genre;
        }, film.getId()));
        film.setGenres(genres);
        return film;
    }


    @Override
    public Film update(Film film) {
        String checkSql = "SELECT COUNT(*) " +
                "FROM films " +
                "WHERE film_id = ?";
        Integer exists = jdbcTemplate.queryForObject(checkSql, Integer.class, film.getId());
        if (exists == null || exists == 0) {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }
        String sql = "UPDATE films " +
                "SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null,
                film.getId()
        );
        return film;
    }

    @Override
    public Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        Integer mpaId = rs.getObject("mpa_id", Integer.class);
        if (mpaId != null) {
            String ratingName = jdbcTemplate.queryForObject("SELECT name " +
                            "FROM mpa_ratings " +
                            "WHERE mpa_id = ?",
                    String.class, mpaId);
            film.setMpa(new Mpa(mpaId, ratingName));
        }
        return film;
    }

    private List<Genre> getGenres(long filmId) {
        String sql = "SELECT g.genre_id, g.name " +
                "FROM genres g " +
                "JOIN film_genres fg " +
                "ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id = ? ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("name"));
            return genre;
        }, filmId);
    }

    @Override
    public Set<Long> getLikes(long filmId) throws NotFoundException {
        String sql = "SELECT user_id " +
                "FROM likes " +
                "WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"), filmId));
    }

    @Override
    public void addLike(long filmId, long userId) throws NotFoundException {
        String addLikeSql = "INSERT INTO likes (film_id, user_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(addLikeSql, filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) throws NotFoundException {
        String likeCheckSql = "SELECT film_id " +
                "FROM likes " +
                "WHERE film_id = ? " +
                "AND user_id = ?";
        Long like = jdbcTemplate.query(likeCheckSql, new SingleColumnRowMapper<>(Long.class), filmId, userId)
                .stream()
                .findFirst()
                .orElse(null);
        if (like == null) {
            throw new NotFoundException("Like for film with ID " + filmId + " and user with ID " + userId + " not " +
                    "found");
        }
        String removeLikeSql = "DELETE FROM likes " +
                "WHERE film_id = ? " +
                "AND user_id = ?";
        jdbcTemplate.update(removeLikeSql, new Object[]{filmId, userId});
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String popularFilmsSql = "SELECT f.* FROM films f " +
                "LEFT JOIN likes l " +
                "ON f.film_id = l.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.user_id) DESC";
        if (count != null && count > 0) {
            popularFilmsSql += " LIMIT ?";
        } else {
            popularFilmsSql += " LIMIT 10";
        }
        List<Film> popularFilms;
        if (count != null && count > 0) {
            popularFilms = jdbcTemplate.query(popularFilmsSql, this::mapRowToFilm, count);
        } else {
            popularFilms = jdbcTemplate.query(popularFilmsSql, this::mapRowToFilm);
        }
        popularFilms.forEach(film -> film.setGenres(new HashSet<>(getGenres(film.getId()))));
        return popularFilms;
    }

}