package com.bus_season_ticket.capstone_project.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

        ADMIN_READ("admin:read"),
        ADMIN_UPDATE("admin:update"),
        ADMIN_CREATE("admin:create"),
        ADMIN_DELETE("Admin delete"),
        Manager_READ("manager:read"),
        Manager_UPDATE("manager:update"),
         Manager_CREATE("manager:create"),
         Manager_DELETE("manager: delete");
private  final  String permission;
}
