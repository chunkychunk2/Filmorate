package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public Mpa getMpaById(Integer mpaId) {
        if (mpaId < 1 || !getAllMpa().contains(new Mpa(mpaId, ""))) {
            throw new NotFoundException("mpaId is not found");
        }
        String sql = "SELECT * FROM mpa_ratings WHERE mpa_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> createMpaFromResultSet(rs), mpaId)
                .stream().findAny().orElse(null);
    }

    public Set<Mpa> getAllMpa() {
        String sql = "SELECT * FROM mpa_ratings ORDER BY mpa_id";
        List<Mpa> mpas = jdbcTemplate.query(sql, (rs, rowNum) -> createMpaFromResultSet(rs));
        return new HashSet<>(mpas);
    }

    private Mpa createMpaFromResultSet(ResultSet rs) throws SQLException {
        return new Mpa(rs.getInt("mpa_id"), rs.getString("name"));
    }

}