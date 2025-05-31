package com.travelbank.accounts.service.impl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.travelbank.accounts.dto.AccountsDto;
import com.travelbank.accounts.dto.CardsDto;
import com.travelbank.accounts.dto.CustomerDetailsDto;
import com.travelbank.accounts.dto.LoansDto;
import com.travelbank.accounts.entity.Accounts;
import com.travelbank.accounts.entity.Customer;
import com.travelbank.accounts.exception.ResourceNotFoundException;
import com.travelbank.accounts.mapper.AccountsMapper;
import com.travelbank.accounts.mapper.CustomerMapper;
import com.travelbank.accounts.repository.AccountsRepository;
import com.travelbank.accounts.repository.CustomerRepository;
import com.travelbank.accounts.service.ICustomersService;
import com.travelbank.accounts.service.client.CardsFeignClient;
import com.travelbank.accounts.service.client.LoansFeignClient;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;

    }
}