package cv.andevelopnica.com.crediveloz.utilities;

import cv.andevelopnica.com.crediveloz.Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentine on 10/18/2015.
 */
public class SampleData {
    public static List<Customer> addSampleCustomers(){
        List<Customer> customers = new ArrayList<Customer>();

        Customer customer1 = new Customer();
        customer1.setId((long) 1);
        customer1.setName("Debbie Sam");
        customers.add(customer1);


        Customer customer2 = new Customer();
        customer2.setId((long) 2);
        customer2.setName("Keisha Williams");
        customers.add(customer2);


        Customer customer3 = new Customer();
        customer3.setId((long) 3);
        customer3.setName("Gregg McQuire");
        customers.add(customer3);


        Customer customer4 = new Customer();
        customer4.setId((long) 4);
        customer4.setName("Jamal Puma");
        customers.add(customer4);


        Customer customer5 = new Customer();
        customer5.setId((long) 5);
        customer5.setName("Dora Keesler");
        customers.add(customer5);

        Customer customer6 = new Customer();
        customer6.setId((long) 6);
        customer6.setName("Anthony Lopez");
        customers.add(customer6);

        Customer customer7 = new Customer();
        customer7.setId((long) 7);
        customer7.setName("Ricardo Weisel");
        customers.add(customer7);

        Customer customer8 = new Customer();
        customer8.setId((long) 8);
        customer8.setName("Angele Lu");
        customers.add(customer8);


        Customer customer9 = new Customer();
        customer9.setId((long) 9);
        customer9.setName("Brendon Suh");
        customers.add(customer9);


        Customer customer10 = new Customer();
        customer10.setId((long) 10);
        customer10.setName("Pietro Augustino");
        customers.add(customer10);


        Customer customer11 = new Customer();
        customer11.setId((long) 11);
        customer11.setName("Matt Zebrotta");
        customers.add(customer11);

        Customer customer12 = new Customer();
        customer12.setId((long) 12);
        customer12.setName("James McGiney");
        customers.add(customer12);


        return customers;

    }
}
