package com.todoservice.dailygrowth.domain.color;

import com.todoservice.dailygrowth.domain.color.entity.Color;
import com.todoservice.dailygrowth.domain.color.infrastructure.persistence.SpringDataColorRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class SpringDataColorRepositoryTest {
    @Autowired
    SpringDataColorRepository colorRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("색상저장조회")
    public void 색상_저장_조회() throws Exception {
        //given
        Color color = Color.create("RED", "#FF0000");

        //when
        Color save = colorRepository.save(color);
        em.clear();

        Color savedColor = colorRepository.findById(save.getId()).orElseThrow();

        //then
        assertThat(savedColor.getId()).isNotNull();
        assertThat(savedColor.getName()).isEqualTo("RED");
        assertThat(savedColor.getHexCode()).isEqualTo("#FF0000");
    }

    @Test
    @DisplayName("수정(더티체킹): 조회, 변경, flush 시 반영")
    public void update_dirty_checking() throws Exception {
        //given
        Color color = colorRepository.saveAndFlush(Color.create("RED", "#FF0000"));
        Long id = color.getId();
        Color managed = colorRepository.findById(id).orElseThrow();

        //when
        managed.changeName("BLUE");
        managed.changeHexCode("#0000FF");
        colorRepository.flush();

        //then
        em.clear();
        Color found = colorRepository.findById(id).orElseThrow();
        assertThat(found.getName()).isEqualTo("BLUE");
        assertThat(found.getHexCode()).isEqualTo("#0000FF");
    }

    @Test
    @DisplayName("삭제")
    public void delete() throws Exception {
        //given
        Color color = colorRepository.saveAndFlush(Color.create("RED", "#FF0000"));
        Long id = color.getId();

        //when
        colorRepository.deleteById(id);
        colorRepository.flush();

        //then
        assertThat(colorRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("유니크 제약: name/hexCode 중복 저장 시 제약")
    public void unique_constraints() throws Exception {
        //given
        colorRepository.saveAndFlush(Color.create("RED", "#FF0000"));

        //then
        assertThatThrownBy(() -> colorRepository.saveAndFlush(Color.create("RED", "#FF1111")))
                .isInstanceOf(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> colorRepository.saveAndFlush(Color.create("BLUE", "#FF0000")))
                .isInstanceOf(DataIntegrityViolationException.class);

    }
}