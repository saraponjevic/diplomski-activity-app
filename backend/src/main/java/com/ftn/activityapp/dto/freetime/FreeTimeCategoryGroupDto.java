package com.ftn.activityapp.dto.freetime;

import com.ftn.activityapp.enums.FreeTimeCategory;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreeTimeCategoryGroupDto {
    private FreeTimeCategory category;
    private List<FreeTimeActivityDto> activities;
}
