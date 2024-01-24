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
@Table(name = "PrincipleLetter")
public class ApprovalLetter {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        private String Name;

        private String Type;

        @Lob
        @Column(name = "Data", columnDefinition = "LONGBLOB" )
        private byte[] Data;

}
