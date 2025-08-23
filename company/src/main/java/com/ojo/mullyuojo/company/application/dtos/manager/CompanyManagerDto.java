package com.ojo.mullyuojo.company.application.dtos.manager;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyManagerDto {
    private Long id;
    private String name;
    private String slackId;
}
