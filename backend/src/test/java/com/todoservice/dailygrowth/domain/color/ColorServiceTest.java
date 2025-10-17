package com.todoservice.dailygrowth.domain.color;

import com.todoservice.dailygrowth.common.baseResponse.BaseResponseStatus;
import com.todoservice.dailygrowth.common.exception.BaseException;
import com.todoservice.dailygrowth.domain.color.entity.Color;
import com.todoservice.dailygrowth.domain.color.port.ColorRepository;
import com.todoservice.dailygrowth.domain.color.application.ColorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ColorServiceTest {

    @Mock
    private ColorRepository colorRepository;

    @InjectMocks
    private ColorService colorService;

    @Test
    @DisplayName("listColor: 모든 색상 목록을 반환한다.")
    public void listColor() throws Exception {
        //given
        Color color1 = Color.create("RED", "#FF0000");
        Color color2 = Color.create("GREEN", "#00FF00");
        when(colorRepository.findAll()).thenReturn(List.of(color1, color2));

        //when
        List<Color> colors = colorService.listColor();

        //then
        assertThat(colors).hasSize(2);
        assertThat(colors.get(0).getName()).isEqualTo("RED");
        assertThat(colors.get(1).getName()).isEqualTo("GREEN");
    }

    @Test
    @DisplayName("getColorByIdOrThrow: 존재 시 반환, 없으면 BaseException 반환")
    public void getByIdOrThrow() throws Exception {
        //given
        Optional<Color> red = Optional.of(Color.create("RED", "#FF0000"));

        //when
        when(colorRepository.findById(1L)).thenReturn(red);
        when(colorRepository.findById(9L)).thenReturn(Optional.empty());

        //then
        assertThat(colorService.getColorByIdOrThrow(1L).getId()).isEqualTo(red.get().getId());
        assertThat(colorService.getColorByIdOrThrow(1L).getName()).isEqualTo(red.get().getName());
        assertThat(colorService.getColorByIdOrThrow(1L).getHexCode()).isEqualTo(red.get().getHexCode());
        assertThatThrownBy(() -> colorService.getColorByIdOrThrow(9L))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.VALIDATION_ERROR.getMessage());
    }

    @Test
    @DisplayName("createColor: 도메인 팩토리, save")
    public void createColor() throws Exception {
        //given
        ArgumentCaptor<Color> captor = ArgumentCaptor.forClass(Color.class);

        //when
        when(colorRepository.save(any(Color.class))).thenAnswer(i -> i.getArgument(0));
        Color saved = colorService.createColor("RED", "#FF0000");

        //then
        verify(colorRepository).save(captor.capture());
        Color toSave = captor.getValue();
        assertThat(toSave.getName()).isEqualTo("RED");
        assertThat(toSave.getHexCode()).isEqualTo("#FF0000");
        assertThat(saved.getName()).isEqualTo("RED");
        assertThat(saved.getHexCode()).isEqualTo("#FF0000");
    }

    @Test
    @DisplayName("updateColor: 조회 -> 엔티티 내부 상태 변경 (save 호출 없이 반환)")
    public void updateColor() throws Exception {
        //given
        Color color = Color.create("RED", "#FF0000");
        when(colorRepository.findById(1L)).thenReturn(Optional.of(color));

        //when
        colorService.updateColor("BLUE", "#0000FF", 1L);

        //then
        assertThat(color.getName()).isEqualTo("BLUE");
        assertThat(color.getHexCode()).isEqualTo("#0000FF");
        verify(colorRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteColor: 삭제")
    public void deleteColor() throws Exception {
        //then
        colorService.deleteColor(1L);
        verify(colorRepository).deleteById(1L);
    }
}
