package com.epf.rentmanager.service;

import com.epf.rentmanager.Exception.DaoException;
import com.epf.rentmanager.Exception.ServiceException;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.model.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReservationService {
    private ReservationDao reservationDao;
    private ReservationService(ReservationDao reservationDao){this.reservationDao = reservationDao;}


    public long create(Reservation reservation) throws ServiceException {
        try {
            reservationDao.create(reservation);
        }catch (DaoException daoException){
            throw new ServiceException("Le client n'a pas été créé");
        }
        return 0;
    }

    public Reservation findById(long id) throws ServiceException {
        Reservation reservation;
        try {
            reservation = reservationDao.findById(id);
        }catch (DaoException daoException){
            throw new ServiceException("La reservation n'a pas été trouvée");
        }
        return reservation;
    }

    public List<Reservation> findResaByVehicleId(long id) throws ServiceException {
        List<Reservation> VehicleId;
        try {
            VehicleId = reservationDao.findResaByVehicleId(id);
        }catch (DaoException daoException){
            throw new ServiceException("La reservation n'a pas été trouvée");
        }
        return VehicleId;
    }

    public List<Reservation> findAll() throws ServiceException {
        List<Reservation> listReservation;
        try {
            listReservation = reservationDao.findAll();
        }catch (DaoException daoException){
            throw new ServiceException("Les reservations n'ont pas été trouvés");
        }
        return listReservation;
    }

    public int count() throws ServiceException {
        int count;
        try {
            count = reservationDao.number_of_reservations();
        }catch (DaoException daoException){
            throw new ServiceException("Problème pour compter les reservations");
        }
        return count;
    }

    public void delete(Reservation reservation) throws ServiceException {
        try {
            reservationDao.delete(reservation);
        }catch (DaoException daoException){
            throw new ServiceException("Le client n'a pas été supprimé");
        }
    }

    public List<Reservation> findResaByClientId(long id) throws ServiceException {
        List<Reservation> ClientId;
        try {
            ClientId = reservationDao.findResaByClientId(id);
        }catch (DaoException daoException){
            throw new ServiceException("La reservation n'a pas été trouvée");
        }
        return ClientId;
    }

    public long update(Reservation reservation) throws ServiceException {
        try {
            return reservationDao.update(reservation);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
