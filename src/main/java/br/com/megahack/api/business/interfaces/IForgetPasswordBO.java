package br.com.megahack.api.business.interfaces;

import br.com.megahack.api.model.persistence.ForgetPassword;

public interface IForgetPasswordBO {
    ForgetPassword saveForgetPassword(String user);

    ForgetPassword findByToken(String token);
}
