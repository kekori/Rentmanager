package com.epf.rentmanager.ui.cli;
import com.epf.rentmanager.configuration.AppConfiguration;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class main {


    public static void main(String args[]) throws ServiceException {



        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);


        ClientService clientService = context.getBean(ClientService.class);


        VehicleService vehicleService = context.getBean(VehicleService.class);


        ReservationService reservationService = context.getBean(ReservationService.class);

        Boolean loopA = true;
        while(loopA){

            Scanner sc = new Scanner(System.in);

            System.out.println("Choisissez une action"

            + "\n(1) Partie client"

            + "\n(2) Partie véhicule"

            + "\n(3) Partie réservation"

            + "\n(0) Annuler");

            try{
                switch (sc.nextInt()) {

                    case 1:
                        System.out.println("Choisissez une action"

                                + "\n(1) Ajouter un client"

                                + "\n(2) Supprimer un client"

                                + "\n(3) Afficher les clients"

                                + "\n(4) Rechercher un client"

                                + "\n(0) Annuler");
                        switch (sc.nextInt()) {
                            case 1:
                                clientService.create(new Client(sc.next(), sc.next(), sc.next(), LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
                                break;
                            case 2:
                                clientService.delete(sc.nextLong());
                                break;
                            case 3:
                                for (Client client : clientService.findAll()) {
                                    System.out.println(client);
                                }
                                break;
                            case 4:
                                System.out.println(clientService.findById(sc.nextLong()));
                                break;
                        }
                        break;
                    case 2:
                        System.out.println("Choisissez une action"

                                + "\n(1) Ajouter un véhicule"

                                + "\n(2) Supprimer un véhicule"

                                + "\n(3) Afficher les véhicules"

                                + "\n(4) Rechercher un véhicule"

                                + "\n(0) Annuler");

                        switch (sc.nextInt()) {
                            case 1:
                                vehicleService.create(new Vehicle(sc.next(), sc.next(), sc.nextInt()));
                                break;
                            case 2:
                                vehicleService.delete(sc.nextLong());
                                break;
                            case 3:
                                for (Vehicle vehicle : vehicleService.findAll()) {
                                    System.out.println(vehicle);
                                }
                                break;
                            case 4:
                                System.out.println(vehicleService.findById(sc.nextLong()));
                                break;
                        }
                        break;
                    case 3:
                        System.out.println("Choisissez une action"

                                + "\n(1) Ajouter une réservation"

                                + "\n(2) Supprimer une réservation"

                                + "\n(3) Afficher les réservations"

                                + "\n(4) Rechercher les réservations pour un client"

                                + "\n(5) Rechercher les réservations pour un véhicule"

                                + "\n(0) Annuler");

                        switch (sc.nextInt()) {
                            case 1:
                                reservationService.create(new Reservation(clientService.findById(sc.nextLong()), vehicleService.findById(sc.nextLong()), LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
                                break;
                            case 2:
                                reservationService.delete(reservationService.findById(sc.nextLong()));
                                break;
                            case 3:
                                for (Reservation reservation : reservationService.findAll()) {
                                    System.out.println(reservation);
                                }
                                break;
                            case 4:
                                System.out.println(reservationService.findResaByClientId(clientService.findById(sc.nextLong())));
                                break;
                            case 5:
                                System.out.println(reservationService.findResaByVehicleId(vehicleService.findById(sc.nextLong())));
                                break;
                        }
                        break;
                    case 0:
                        loopA = false;
                        break;
                }
            }
            catch (InputMismatchException e){
                System.out.println("Mauvaise saisie");
            }
            catch (DateTimeParseException e){
                System.out.println("Mauvais format de date");
            }
        }
    }
}