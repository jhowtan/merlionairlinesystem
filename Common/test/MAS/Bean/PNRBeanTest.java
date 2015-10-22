package MAS.Bean;

import MAS.Entity.Flight;
import MAS.Entity.Itinerary;
import MAS.Entity.PNR;
import MAS.Entity.SpecialServiceRequest;
import MAS.Exception.NotFoundException;
import MAS.TestEJB;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PNRBeanTest {

    @Before
    public void setUp() throws Exception {
        TestEJB.init();
    }

    @Test
    public void testUpdatePNR() throws Exception {
        PNR newPNR = new PNR();
        try {
            TestEJB.pnrBean.updatePNR(newPNR);
            fail();
        } catch (Exception e) {}
    }

    @Test
    public void testGetPassengerNumber() throws Exception {
        PNR pnr = new PNR();
        pnr.setPassengers(Arrays.asList("LAU/JONATHAN", "HO/DARYL"));
        assertEquals(1, TestEJB.pnrBean.getPassengerNumber(pnr, "LAU/JONATHAN"));
        assertEquals(2, TestEJB.pnrBean.getPassengerNumber(pnr, "HO/DARYL"));

        try {
            TestEJB.pnrBean.getPassengerNumber(pnr, "NONEXISTENT/NAME");
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void testGetItineraryNumber() throws Exception {
        PNR pnr = new PNR();
        Itinerary itinerary1 = new Itinerary();
        itinerary1.setFlightCode("MA123");
        Itinerary itinerary2 = new Itinerary();
        itinerary2.setFlightCode("MA456");
        pnr.setItineraries(Arrays.asList(itinerary1, itinerary2));
        Flight flight1 = new Flight();
        flight1.setCode("MA123");
        Flight flight2 = new Flight();
        flight2.setCode("MA456");
        Flight nonexistentFlight = new Flight();
        nonexistentFlight.setCode("MA789");
        assertEquals(1, TestEJB.pnrBean.getItineraryNumber(pnr, flight1));
        assertEquals(1, TestEJB.pnrBean.getItineraryNumber(pnr, "MA123"));
        assertEquals(2, TestEJB.pnrBean.getItineraryNumber(pnr, flight2));
        assertEquals(2, TestEJB.pnrBean.getItineraryNumber(pnr, "MA456"));

        try {
            TestEJB.pnrBean.getItineraryNumber(pnr, nonexistentFlight);
            fail();
        } catch (NotFoundException e) {}

        try {
            TestEJB.pnrBean.getItineraryNumber(pnr, "MA789");
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void testGetPassengerSpecialServiceRequests() throws Exception {
        PNR pnr = new PNR();
        pnr.setPassengers(Arrays.asList("LAU/JONATHAN", "HO/DARYL"));
        SpecialServiceRequest ssr1 = new SpecialServiceRequest();
        SpecialServiceRequest ssr2 = new SpecialServiceRequest();
        SpecialServiceRequest ssr3 = new SpecialServiceRequest();
        ssr1.setPassengerNumber(1);
        ssr2.setPassengerNumber(1);
        ssr3.setPassengerNumber(2);
        pnr.setSpecialServiceRequests(Arrays.asList(ssr1, ssr2, ssr3));

        assertEquals(2, TestEJB.pnrBean.getPassengerSpecialServiceRequests(pnr, 1).size());
        assertEquals(1, TestEJB.pnrBean.getPassengerSpecialServiceRequests(pnr, 2).size());
        assertEquals(0, TestEJB.pnrBean.getPassengerSpecialServiceRequests(pnr, 3).size());
    }

    @Test
    public void testGetPassengerSpecialServiceRequest() throws Exception {
        PNR pnr = new PNR();
        pnr.setPassengers(Arrays.asList("LAU/JONATHAN", "HO/DARYL"));
        SpecialServiceRequest ssr1 = new SpecialServiceRequest();
        SpecialServiceRequest ssr2 = new SpecialServiceRequest();
        ssr1.setPassengerNumber(1);
        ssr2.setPassengerNumber(2);
        ssr1.setActionCode("FFP");
        ssr1.setValue("MA/81111111");
        ssr1.setItineraryNumber(1);
        ssr2.setActionCode("FFP");
        ssr2.setValue("MA/81111112");
        ssr2.setItineraryNumber(1);
        pnr.setSpecialServiceRequests(Arrays.asList(ssr1, ssr2));

        assertEquals("MA/81111111", TestEJB.pnrBean.getPassengerSpecialServiceRequest(pnr, 1, "FFP").getValue());
        assertEquals("MA/81111112", TestEJB.pnrBean.getPassengerSpecialServiceRequest(pnr, 2, "FFP").getValue());
        assertEquals("MA/81111111", TestEJB.pnrBean.getPassengerSpecialServiceRequest(pnr, 1, 1, "FFP").getValue());
        assertEquals("MA/81111112", TestEJB.pnrBean.getPassengerSpecialServiceRequest(pnr, 2, 1, "FFP").getValue());

        try {
            TestEJB.pnrBean.getPassengerSpecialServiceRequest(pnr, 2, "NONEXISTENT");
            fail();
        } catch (NotFoundException e) {}

        try {
            TestEJB.pnrBean.getPassengerSpecialServiceRequest(pnr, 3, "FFP");
            fail();
        } catch (NotFoundException e) {}

        try {
            TestEJB.pnrBean.getPassengerSpecialServiceRequest(pnr, 2, 2, "FFP");
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void testDeleteSpecialServiceRequest() throws Exception {

    }

    @Test
    public void testSetSpecialServiceRequest() throws Exception {

    }
}