package ru.practicum.ewm.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.ViewStats;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ViewStatsMapper implements RowMapper<ViewStats> {

    @Override
    public ViewStats mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ViewStats(rs.getString("app"),
                rs.getString("uri"),
                rs.getInt("hits")
        );
    }
}


