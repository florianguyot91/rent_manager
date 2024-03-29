package com.epf.rentmanager.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.Exception.DaoException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

@Repository
public class VehicleDao {
    private VehicleDao() {
    }


    private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructeur, nb_places) VALUES(?, ?);";
    private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Vehicle WHERE id=?;";
    private static final String FIND_VEHICLE_QUERY = "SELECT id, constructeur, nb_places FROM Vehicle WHERE id=?;";
    private static final String FIND_VEHICLES_QUERY = "SELECT id, constructeur, nb_places FROM Vehicle;";
    private static final String FIND_RESERVATIONS_VEHICLE_BY_CLIENT_QUERY =
            "SELECT DISTINCT Vehicle.id, constructeur, nb_places FROM Vehicle " +
            "INNER JOIN Reservation ON Reservation.vehicle_id = Vehicle.id WHERE Reservation.client_id=?;";
    private static final String EDIT_VEHICLE_QUERY = "UPDATE Vehicle SET constructeur=?, nb_places=? WHERE id=?;";


    public long create(Vehicle vehicle) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement(CREATE_VEHICLE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, vehicle.getConstructeur());
            preparedStatement.setInt(2, vehicle.getNb_places());

            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public long delete(Vehicle vehicle) throws DaoException {    // TODO deleting all rents involving vehicle
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement(DELETE_VEHICLE_QUERY);
            preparedStatement.setLong(1, vehicle.getId());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException();
        }
    }

    public Vehicle findById(long id) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement(FIND_VEHICLE_QUERY);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String constructeur = resultSet.getString("constructeur");
            int nb_places = resultSet.getInt("nb_places");
            return new Vehicle(id, constructeur, nb_places);
        } catch (SQLException e) {
            throw new DaoException();
        }
    }

    public List<Vehicle> findAll() throws DaoException {
        List<Vehicle> vehicules = new ArrayList<Vehicle>();
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement(FIND_VEHICLES_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String constructeur = resultSet.getString("constructeur");
                int nb_places = resultSet.getInt("nb_places");

                vehicules.add(new Vehicle(id, constructeur, nb_places));
            }
            return vehicules;
        } catch (SQLException e) {
            throw new DaoException();
        }
    }

    public int number_of_vehicules() throws DaoException {
        int vehicules = 0;
        try {
            Connection connection = ConnectionManager.getConnection();
            Statement statement =
                    connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS total FROM Vehicle");
            if (resultSet.next()) {
                vehicules = resultSet.getInt("total");
            }
            return vehicules;
        } catch (SQLException e) {
            throw new DaoException();
        }
    }

    public ArrayList<Vehicle> findVehicleByClientId(long clientId) throws DaoException {
        try {
            Connection conn = ConnectionManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(FIND_RESERVATIONS_VEHICLE_BY_CLIENT_QUERY);
            stmt.setLong(1, clientId);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Vehicle> carsList = new ArrayList<Vehicle>();
            while (rs.next()) {
                Vehicle car = new Vehicle(rs.getInt("id"),
                        rs.getString("constructeur"),
                        rs.getInt("nb_places"));
                carsList.add(car);
            }
            conn.close();
            return carsList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException();
        }
    }

    public long update(Vehicle vehicle) throws DaoException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(EDIT_VEHICLE_QUERY);
            preparedStatement.setString(1, vehicle.getConstructeur());
            preparedStatement.setLong(2, vehicle.getNb_places());
            preparedStatement.setLong(3, vehicle.getId());
            long id = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException();
        }
    }
}
