package com.bus_season_ticket.capstone_project.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "StudentBirthFiles")
public class StudentBirthFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileBirthName;

    private String fileBirthType;

 //columnDefinition = "LONGBLOB"
    @Lob
    @Column(name = "fileBirthData", columnDefinition = "LONGBLOB" )
    private byte[] fileBirthData;
}

