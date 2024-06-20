package ru.practicum.ewm.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.compilation.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query(value = "SELECT * FROM compilation c WHERE (:pinned is null or c.pinned = :pinned)", nativeQuery = true)
    Page<Compilation> findAllByPinnedIsNullOrPinned(Boolean pinned, PageRequest pageRequest);
}