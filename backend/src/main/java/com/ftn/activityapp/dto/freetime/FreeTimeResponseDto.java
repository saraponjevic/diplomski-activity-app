package com.ftn.activityapp.dto.freetime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreeTimeResponseDto {
    private String mainSuggestion;
    private String headline;
    private List<FreeTimeCategoryGroupDto> categoryGroups;
}
