package com.ftn.activityapp.ai.category.wellness;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WellnessRecommendation {

    private String headline;
    private String wellnessTip;
    private String restTip;
    private List<WellnessActionCard> actionCards;
}
