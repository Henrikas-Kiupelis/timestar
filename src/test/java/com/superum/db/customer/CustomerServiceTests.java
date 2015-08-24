package com.superum.db.customer;


import com.superum.TimeStarBackEndApplication;
import com.superum.db.group.student.Student;
import com.superum.db.group.student.StudentService;
import com.superum.exception.DatabaseException;
import com.superum.helper.PartitionAccount;
import com.superum.utils.FakeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;

import static com.superum.utils.FakeUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TimeStarBackEndApplication.class})


public class CustomerServiceTests {

    public CustomerService customerService;
    public CustomerQueries customerQuaries;
    public StudentService studentService;
    public CustomerDAO customerDAO;

    @Before
    public void itit() {

        customerQuaries = mock(CustomerQueries.class);
        studentService = mock(StudentService.class);
        customerDAO = mock(CustomerDAO.class);
        customerService = new CustomerServiceImpl(customerDAO, customerQuaries, studentService);

    }

    @Test
    public void testAddingCustomer() {
        int id = 1;
        PartitionAccount account = makeFakePartitionAccount();
        Customer customer = makeFakeCustomer(id);
        Customer addedCustomer = customer.withoutId();

        when(customerDAO.create(addedCustomer, account.partitionId())).thenReturn(customer);

        Customer retrievedTeacher = customerDAO.create(addedCustomer, account.partitionId());

        assertEquals("The added teacher should be the same as retrieved one;",
                customer, retrievedTeacher);

        verify(customerDAO, times(1)).create(addedCustomer, account.partitionId());


    }

    @Test
    public void testFindingCustomer() {
        int id = 1;
        PartitionAccount account = makeFakePartitionAccount();
        Customer customer = makeFakeCustomer(id);

        when(customerDAO.read(id, account.partitionId())).thenReturn(customer);


        Customer retrievedCustomer = customerService.findCustomer(id, account.partitionId());

        assertEquals("Original customer should be the same as retrieved one",
                customer, retrievedCustomer);

        verify(customerDAO, times(1)).read(id, account.partitionId());

    }

    @Test
    public void testUpdatingCustomer() {

        int id = 1;
        PartitionAccount account = makeFakePartitionAccount();
        Customer customer = makeFakeCustomer(id);
        Customer updatedCustomer = makeFakeCustomer(id + 1).withId(id);

        when(customerDAO.update(updatedCustomer, account.partitionId())).thenReturn(customer);

        Customer retrievedCustomer = customerService.updateCustomer(updatedCustomer, account.partitionId());

        assertEquals("The original customer should be the same as retrieved one",
                customer, retrievedCustomer);

        verify(customerDAO, times(1)).update(updatedCustomer, account.partitionId());

    }

    @Test(expected = DatabaseException.class)
    public void testUpdatingNonExistingTeacher() {

        int id = Integer.MAX_VALUE;
        PartitionAccount account = makeFakePartitionAccount();
        Customer nonExistentCustomer = makeFakeCustomer(id);

        when(customerDAO.update(nonExistentCustomer, account.partitionId())).thenThrow(new DatabaseException());

        customerService.updateCustomer(nonExistentCustomer, account.partitionId());
    }

    @Test
    public void testDeletingCustomer() {

        int id = 1;
        PartitionAccount account = makeFakePartitionAccount();
        Customer customer = makeFakeCustomer(id);
        List<Student> fakeStudents = makeSomeFakes(2, FakeUtils::makeFakeStudent);

        when(studentService.deleteForCustomer(id, account.partitionId())).thenReturn(fakeStudents);
        when(customerDAO.delete(id, account.partitionId())).thenReturn(customer);

        Customer retrievedCustomer = customerService.deleteCustomer(id, account.partitionId());

        assertEquals("The original customer should be the same as retrieved one",
                customer, retrievedCustomer);

        verify(customerDAO, times(1)).delete(id, account.partitionId());
        verify(studentService, times(1)).deleteForCustomer(id, account.partitionId());
    }

    @Test
    public void testFindingCustomersForTeacher(){
        int teacherId = 1;
        PartitionAccount account = makeFakePartitionAccount();
        List<Customer> fakeCustomers = Collections.singletonList(makeFakeCustomer(teacherId));

        when(customerQuaries.readAllForTeacher(teacherId, account.partitionId())).thenReturn(fakeCustomers);

        List<Customer> retrievedCustomers = customerService.findCustomersForTeacher(teacherId, account.partitionId());

        assertEquals("Fake customers should be the same as retrieved ones",
                fakeCustomers, retrievedCustomers);

        verify(customerQuaries, times(1)).readAllForTeacher(teacherId, account.partitionId());
    }

    @Test
    public void testGettingAllCustomers(){
        PartitionAccount account = makeFakePartitionAccount();
        List<Customer> fakeCustomers = makeSomeFakes(2, FakeUtils::makeFakeCustomer);

        when(customerDAO.readAll(account.partitionId())).thenReturn(fakeCustomers);

        List<Customer> retrievedCustomer = customerService.getAllCustomers(account.partitionId());

        assertEquals("Fake customers should be the same as retrieved ones",
                fakeCustomers, retrievedCustomer);

        verify(customerDAO, times(1)).readAll(account.partitionId());
    }





}

