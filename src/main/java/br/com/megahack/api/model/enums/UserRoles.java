package br.com.megahack.api.model.enums;

public enum UserRoles {
    SUPERADMIN("SUPERADMIN"), ADMIN("ADMIN"),CUSTOMER("CUSTOMER") ;

    private String s;

    UserRoles(String s) {
        this.s = s;
    }
}
