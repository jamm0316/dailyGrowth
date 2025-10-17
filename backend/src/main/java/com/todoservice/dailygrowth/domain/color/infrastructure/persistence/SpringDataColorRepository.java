package com.todoservice.dailygrowth.domain.color.infrastructure.persistence;

import com.todoservice.dailygrowth.domain.color.entity.Color;
import com.todoservice.dailygrowth.domain.color.port.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface SpringDataColorRepository extends JpaRepository<Color, Long> {

    @Repository
    @RequiredArgsConstructor
    class ColorRepositoryImpl implements ColorRepository {
        private final SpringDataColorRepository jpa;

        @Override
        public List<Color> findAll() {return jpa.findAll();}

        @Override
        public Color save(Color color) {return jpa.save(color);}

        @Override
        public Optional<Color> findById(Long id) {return jpa.findById(id);}

        @Override
        public void deleteById(Long id) {jpa.deleteById(id);}
    }
}
