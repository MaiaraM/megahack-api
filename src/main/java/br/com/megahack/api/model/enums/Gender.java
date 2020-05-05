package br.com.megahack.api.model.enums;

public enum Gender {
    MASCULINO("M"), FEMININO("F"), OUTROS("O");

    private String s;

    Gender(String s) {
        this.s = s;
    }
}
