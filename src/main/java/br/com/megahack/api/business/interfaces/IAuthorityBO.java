package br.com.megahack.api.business.interfaces;

import br.com.megahack.api.model.persistence.Authority;

public interface IAuthorityBO {

    Authority getAdminAuthority();

    Authority getCustomerAuthority();

    Authority getSuperadminAuthority();
}
