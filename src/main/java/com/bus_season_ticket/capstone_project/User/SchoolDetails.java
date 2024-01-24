package com.bus_season_ticket.capstone_project.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Student_School_details")
public class SchoolDetails {
    @Id
    @GeneratedValue
private Integer schoolId;
private String schAddress;
private String district;
@NotNull
@NotBlank
@Column(nullable = false)
private String indexNumber;

    @OneToOne(mappedBy = "schoolDetails", cascade = CascadeType.ALL)
    private User user ;
}
