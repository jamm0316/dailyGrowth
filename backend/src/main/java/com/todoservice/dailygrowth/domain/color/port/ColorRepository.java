package com.todoservice.dailygrowth.domain.color.port;

import com.todoservice.dailygrowth.domain.color.entity.Color;

import java.util.List;
import java.util.Optional;

public interface ColorRepository {
    List<Color> findAll();
    Color save(Color color);
    Optional<Color> findById(Long id);
    void deleteById(Long id);
}
