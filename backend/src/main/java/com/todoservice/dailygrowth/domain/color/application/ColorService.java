package com.todoservice.dailygrowth.domain.color.application;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.exception.BaseException;
import com.todoservice.dailygrowth.domain.color.entity.Color;
import com.todoservice.dailygrowth.domain.color.port.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorService {
    private final ColorRepository colorRepository;

    public List<Color> listColor() {
        return colorRepository.findAll();
    }

    public Color getColorByIdOrThrow(Long id) {
        return colorRepository.findById(id)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.VALIDATION_ERROR));
    }

    public Color createColor(String name, String hexCode) {
        Color color = Color.create(name, hexCode);
        return colorRepository.save(color);
    }

    public void deleteColor(Long id) {
        colorRepository.deleteById(id);
    }

    public Color updateColor(String name, String hexCode, Long id) {
        Color color = getColorByIdOrThrow(id);
        color.changeName(name);
        color.changeHexCode(hexCode);
        return color;
    }
}
