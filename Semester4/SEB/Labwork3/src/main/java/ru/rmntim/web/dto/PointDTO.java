package ru.rmntim.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rmntim.web.entity.PointEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointDTO {
    private double x;
    private double y;
    private double r;
    private boolean result;
    private Long userId;

    public static PointDTO fromEntity(PointEntity entity) {
        return PointDTO.builder()
                .x(entity.getX())
                .y(entity.getY())
                .r(entity.getR())
                .result(entity.isResult())
                .userId(entity.getUser().getId())
                .build();
    }
}

