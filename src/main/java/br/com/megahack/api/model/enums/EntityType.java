package br.com.megahack.api.model.enums;

public enum EntityType {
    FISICA("F"), JURIDICA("J");

    private String s;

    EntityType(String s) {
        this.s = s;
    }
}
