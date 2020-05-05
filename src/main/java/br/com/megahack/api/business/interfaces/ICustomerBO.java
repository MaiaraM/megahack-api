package br.com.megahack.api.business.interfaces;

import br.com.megahack.api.model.dto.CustomerDTO;
import br.com.megahack.api.model.dto.NewPasswordDTO;
import br.com.megahack.api.model.dto.PasswordUpdateDTO;
import br.com.megahack.api.model.persistence.Customer;
import br.com.megahack.api.model.persistence.CustomerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICustomerBO {

    Customer createNewCustomer(Customer customer);

    boolean deleteCustomer(String uuid);

    Customer updateCustomer(CustomerDTO customer, String uuid);

    boolean updatePassword(PasswordUpdateDTO passwordUpdateDTO, String uuid);

    Customer findCustomerByUuid(String uuid, boolean mustBeActive);

    Customer findCustomerByUserUsername(String username, boolean mustBeActive);

    Customer findCustomerByDocument(String document, boolean mustBeActive);

    Page<Customer> getAllCustomers(Pageable page);

    CustomerType createNewCustomerType(String description);

    boolean newPassword(NewPasswordDTO password, String uuid);

    Page<Customer> getAllCustomerType(Pageable page);
}
