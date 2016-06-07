package cv.andevelopnica.com.crediveloz.listener;

import cv.andevelopnica.com.crediveloz.Customer;

import java.util.List;

/**
 * Created by Valentine on 9/5/2015.
 */
public interface OnCustomerListChangedListener {
    void onNoteListChanged(List<Customer> customers);
}
