package br.com.megahack.api.business;

import br.com.megahack.api.business.interfaces.IAuthorityBO;
import br.com.megahack.api.business.interfaces.ICustomerBO;
import br.com.megahack.api.model.dto.CustomerDTO;
import br.com.megahack.api.model.dto.NewPasswordDTO;
import br.com.megahack.api.model.dto.PasswordUpdateDTO;
import br.com.megahack.api.model.persistence.Authority;
import br.com.megahack.api.model.persistence.Customer;
import br.com.megahack.api.model.persistence.CustomerType;
import br.com.megahack.api.repositories.abstracts.AbstractCustomerRepository;
import br.com.megahack.api.repositories.abstracts.AbstractCustomerTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StandardCustomerBO implements ICustomerBO {

    @Autowired
    private AbstractCustomerRepository customerRepository;


    @Autowired
    private AbstractCustomerTypeRepository customerTypeRepository;

    @Autowired
    private IAuthorityBO authorityBO;

    @Override
    public Customer createNewCustomer(Customer customer) {
        if(!validateCustomerForCreation(customer)) {return null;}
        Authority customerAuthority = authorityBO.getCustomerAuthority();
        customer.getUser().addAuthority(customerAuthority);
        customer.getUser().setPassword(new BCryptPasswordEncoder().encode(customer.getUser().getPassword()));
        return (Customer) customerRepository.save(customer);
    }

    @Override
    @PreAuthorize("hasAuthority('SUPERADMIN') && hasPermission(#customerUuid, 'customer', 'delete')")
    public boolean deleteCustomer(String customerUuid) {
        return customerRepository.deleteByUuid(customerUuid) > 0 ;
    }

    @Override
    @PreAuthorize("hasAuthority('SUPERADMIN') && hasPermission(#customerUuid, 'customer', 'write')")
    public Customer updateCustomer(CustomerDTO customer, String customerUuid) {

        // Required Attributes
        if (customer.getFirstName()==null || customer.getFirstName().isEmpty() ||
            customer.getDocument()==null || customer.getDocument().isEmpty() ||
            customer.getEntity()==null || customer.getUser()==null ||
            customer.getUser().getEmail()==null || customer.getUser().getEmail().isEmpty() ||
            customer.getUser().getUsername()==null || customer.getUser().getUsername().isEmpty())
            return null;

        Customer c = (Customer) customerRepository.findByUuidAndActiveTrue(customerUuid);
        if (c == null) {return null;}

        c.setFirstName(customer.getFirstName());
        c.setLastName(customer.getLastName());
        c.setBirthdate(customer.getBirthdate());
        c.setDocument(customer.getDocument());
        c.setGender(customer.getGender());
        c.setMobile(customer.getMobile());
        c.setPhone(customer.getPhone());
        c.setNickname(customer.getNickname());
        c.setEntity(customer.getEntity());
        c.setMaritalStatus(customer.getMaritalStatus());
        c.getUser().setEmail(customer.getUser().getEmail());
        c.getUser().setUsername(customer.getUser().getUsername());

        customerRepository.save(c);
        return c;
    }

    @Override
    @PreAuthorize("hasPermission(#customerUuid, 'customer', 'write')")
    public boolean updatePassword(PasswordUpdateDTO passwordUpdateDTO, String customerUuid){
        Customer customer = findCustomerByUuid(customerUuid, true);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String existingPassword = passwordUpdateDTO.getOldPassword();
        String dbPassword = customer.getUser().getPassword();

        if (passwordEncoder.matches(existingPassword, dbPassword)) {
            customer.getUser().setPassword(passwordEncoder.encode(passwordUpdateDTO.getNewPassword()));
            customerRepository.save(customer);
            return true;
        }
        return false;
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'write')")
    public Customer findCustomerByUuid(String uuid, boolean mustBeActive) {
        if (mustBeActive){
            return (Customer) customerRepository.findByUuidAndActiveTrue(uuid);
        }
        return (Customer) customerRepository.findByUuid(uuid);
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'write')")
    public Customer findCustomerByUserUsername(String username, boolean mustBeActive) {
        if (mustBeActive) {
            return customerRepository.findByUserUsernameAndActiveTrue(username);
        }
        return customerRepository.findByUserUsername(username);
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'write')")
    public Customer findCustomerByDocument(String document, boolean mustBeActive) {
        if (mustBeActive) {
            return customerRepository.findByDocumentAndActiveTrue(document);
        }
        return customerRepository.findByDocument(document);
    }

    @Override
    @PostAuthorize("hasAuthority('SUPERADMIN') or hasAuthority('ADMIN')")
    public Page<Customer> getAllCustomers(Pageable page) {
        return customerRepository.findAll(page);
    }

    @Override
    @PostAuthorize("hasAuthority('SUPERADMIN') or hasAuthority('ADMIN')")
    public Page<Customer> getAllCustomerType(Pageable page) {
        return customerTypeRepository.findAll(page);
    }

    @Override
    public boolean newPassword(NewPasswordDTO password, String uuid) {
        Customer customer = findCustomerByUuid(uuid, true);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        try {
            customer.getUser().setPassword(passwordEncoder.encode(password.getNewPassword()));
            customerRepository.save(customer);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    @Override
    @PostAuthorize("hasAuthority('SUPERADMIN')  or hasAuthority('ADMIN')")
    public CustomerType createNewCustomerType(String description) {

        CustomerType customerType = customerTypeRepository.findByName(description);

        if(customerType == null ){
            customerType = new CustomerType();
            customerType.setName(description);

            return (CustomerType) customerTypeRepository.save(customerType);
        }

        return null;
    }


    // Should return true if and only if the customer may be created on the database
    // do all your business validation here (because this is inherited and then you can just override it)
    protected boolean validateCustomerForCreation(Customer customer){
        try{
            Customer databaseCustomer = customerRepository.findRegisteredCustomers(customer.getDocument(), customer.getUser().getUsername(), customer.getUser().getEmail());
            return databaseCustomer == null;
        }catch(Exception e){
            return false;
        }
    }
}
