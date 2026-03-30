package com.ftn.activityapp.ai.category.freetime;

import com.ftn.activityapp.dto.freetime.FreeTimeCategoryGroupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FreeTimeRecommendation {

    private String mainSuggestion;
    private String headline;
    private List<FreeTimeCategoryGroupDto> categoryGroups;
}
