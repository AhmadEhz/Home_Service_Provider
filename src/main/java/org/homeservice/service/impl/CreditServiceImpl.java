package org.homeservice.service.impl;

import org.homeservice.entity.Credit;
import org.homeservice.entity.Transaction;
import org.homeservice.entity.TransactionType;
import org.homeservice.repository.CreditRepository;
import org.homeservice.service.CreditService;
import org.homeservice.service.TransactionService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.InsufficientAmountException;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Scope("singleton")
public class CreditServiceImpl extends BaseServiceImpl<Credit, Long, CreditRepository> implements CreditService {
    private final TransactionService transactionService;

    protected CreditServiceImpl(CreditRepository repository, TransactionService transactionService) {
        super(repository);
        this.transactionService = transactionService;
    }

    @Override
    @Transactional
    public void deposit(Long id, Long amount) {
        checkAmount(amount);
        Credit credit = loadById(id).orElseThrow(() -> new NotFoundException("Credit not found."));
        credit.deposit(amount);
        update(credit);
        transactionService.save(new Transaction(credit, amount, TransactionType.DEPOSIT));
    }

    @Override
    @Transactional
    public void withdraw(Long id, Long withdrawalAmount) {
        checkAmount(withdrawalAmount);
        Credit credit = loadById(id).orElseThrow(() -> new NotFoundException("Credit not found."));
        if (!credit.isSufficientAmount(withdrawalAmount))
            throw new InsufficientAmountException();
        credit.withdraw(withdrawalAmount);
        update(credit);
        transactionService.save(new Transaction(credit, withdrawalAmount, TransactionType.WITHDRAW));
    }

    @Override
    public Optional<Credit> loadBySpecialist(Long specialistId) {
        return repository.findBySpecialistId(specialistId);
    }

    @Override
    public Optional<Credit> loadByCustomer(Long customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    @Transactional
    public void cardToCard(Long sourceId, Long destinationId, Long amount) {
        checkAmount(amount);
        Credit sourceCredit = loadById(sourceId).orElseThrow(() -> new NotFoundException("Source Credit not found."));
        Credit destinationCredit = loadById(destinationId).orElseThrow(
                () -> new NotFoundException("Destination Credit not found."));
        if (!sourceCredit.isSufficientAmount(amount))
            throw new InsufficientAmountException();
        sourceCredit.withdraw(amount);
        destinationCredit.deposit(amount);
        update(sourceCredit);
        update(destinationCredit);
        transactionService.save(new Transaction(sourceCredit, destinationCredit, amount, TransactionType.CARD_TO_CARD));
    }

    private void checkAmount(Long amount) throws IllegalArgumentException {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount should not be less than zero.");
    }
}
