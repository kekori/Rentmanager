package com.epf.rentmanager.service;

import java.time.LocalDate;
import java.util.List;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.exception.ValideException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import org.springframework.stereotype.Service;

@Service

public class ClientService {

	private ClientDao clientDao;
	private ReservationDao reservationDao;
	private ClientService(ClientDao clientDao, ReservationDao reservationDao){
		this.clientDao = clientDao;
		this.reservationDao = reservationDao;
	}

	public void modify(Client client) throws ServiceException {
		try{
			clientDao.modify(client);
		}
		catch (DaoException e){
			throw new ServiceException(e);
		}
	}
	public void delete(long id) throws ServiceException {
		try{
			for (Reservation reservation:reservationDao.findResaByClientId(clientDao.findById(id))) {
				reservationDao.delete(reservation);
			}
			clientDao.delete(id);
		}
		catch (DaoException e){
			throw new ServiceException(e);
		}
	}














	public boolean valideName(String nom) throws ValideException {
		if(nom.length() < 3){
			throw new ValideException("Nom/Prenom trop court");
		}
		return true;
	}
	public void create(Client client) throws ServiceException {
		try{
			clientDao.create(client);
		}
		catch (DaoException e){
			throw new ServiceException(e);
		}
	}
	public boolean valideAge(Client client) throws ValideException {
		if(client.getBdate().isAfter(LocalDate.now().minusYears(18))) {
			throw new ValideException("Client trop jeune");
		}
		return true;
	}
	public Client findById(long id) throws ServiceException {
		try{
			return clientDao.findById(id);
		}
		catch (DaoException e){
			throw new ServiceException(e);
		}
	}

	public List<Client> findAll() throws ServiceException {
		try{
			return clientDao.findAll();
		}
		catch (DaoException e){
			throw new ServiceException(e);
		}
	}








	public boolean valideEmail(Client client) throws ServiceException, ValideException {
		try{
			for (Client cli:clientDao.findAll()) {
				if(cli.getEmail().equals(client.getEmail()) && cli.getId() != client.getId()){
					throw new ValideException("Email deja utilise");
				}
			}
			return true;
		}
		catch (DaoException e){
			throw new ServiceException(e);
		}
	}






}
