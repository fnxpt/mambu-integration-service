package com.backbase.showcase.accounts.service;

import com.backbase.integration.account.rest.spec.v2.arrangementdetails.ArrangementDetailsGetResponseBody;
import com.backbase.integration.account.rest.spec.v2.balances.BalanceGetResponseBody;
import com.backbase.showcase.accounts.mapper.MambuProductMapper;
import com.backbase.showcase.client.mambu.domain.Loan;
import com.backbase.showcase.client.mambu.domain.Saving;
import com.backbase.showcase.client.mambu.service.LoanService;
import com.backbase.showcase.client.mambu.service.SavingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class ProductServiceTest {

    private ProductService tested;

    @MockBean
    private LoanService loanService;

    @MockBean
    private SavingService savingService;

    @MockBean
    private MambuProductMapper mambuProductMapper;

    private List<Saving> savingList;
    private List<Loan> loanList;
    private ArrangementDetailsGetResponseBody arrangementDetailsGetResponseBody;
    private BalanceGetResponseBody balanceGetResponseBody;

    @Before
    public void setUp() {

        Saving saving = new Saving();
        saving.setId("123456789");

        Loan loan = new Loan();
        loan.setId("987654321");

        savingList = new ArrayList<>();
        savingList.add(saving);

        loanList = new ArrayList<>();
        loanList.add(loan);

        arrangementDetailsGetResponseBody = new ArrangementDetailsGetResponseBody();
        arrangementDetailsGetResponseBody.setBBAN(saving.getId());

        balanceGetResponseBody = new BalanceGetResponseBody();
        balanceGetResponseBody.setArrangementId(saving.getId());

        tested = new ProductService(loanService, savingService, mambuProductMapper);

    }

    @Test
    public void getProductDetailSaving() {

        when(savingService.getSavingById(anyString())).thenReturn(savingList);
        when(mambuProductMapper.savingToArrangementDetails(savingList.stream().findFirst().get())).thenReturn(arrangementDetailsGetResponseBody);

        ArrangementDetailsGetResponseBody response = tested.getProductDetail("123");

        assertNotNull(response);
        assertNotNull(response.getBBAN());

    }

    @Test
    public void getProductDetailLoan() {

        when(savingService.getSavingById(anyString())).thenReturn(null);
        when(loanService.getLoanById(anyString())).thenReturn(loanList);
        when(mambuProductMapper.loanToArrangementDetails(loanList.stream().findFirst().get())).thenReturn(arrangementDetailsGetResponseBody);

        ArrangementDetailsGetResponseBody response = tested.getProductDetail("123");

        assertNotNull(response);
        assertNotNull(response.getBBAN());

    }

    @Test
    public void getProductDetailNoResults() {

        when(savingService.getSavingById(anyString())).thenReturn(null);
        when(loanService.getLoanById(anyString())).thenReturn(null);

        ArrangementDetailsGetResponseBody response = tested.getProductDetail("123");

        assertNull(response.getBBAN());

    }

    @Test
    public void getProductBalance() {

        when(savingService.getSavingByIdList(anyList())).thenReturn(savingList);
        when(loanService.getLoansByIdList(anyList())).thenReturn(loanList);
        when(mambuProductMapper.savingToBalance(savingList.stream().findFirst().get()))
                .thenReturn(balanceGetResponseBody);
        when(mambuProductMapper.loanToBalance(loanList.stream().findFirst().get()))
                .thenReturn(balanceGetResponseBody);

        List<BalanceGetResponseBody> response = tested.getProductBalance(anyList());
        assertNotNull(response);
        assertTrue(!response.isEmpty());

    }

}