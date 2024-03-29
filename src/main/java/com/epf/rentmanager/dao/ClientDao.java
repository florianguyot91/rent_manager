package com.epf.rentmanager.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.Exception.DaoException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDao {
	private ClientDao() {}
	
	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	private static final String EDIT_CLIENT_QUERY = "UPDATE Client SET nom=?, prenom=?, email=?, naissance=? WHERE id=?;";


	public long create(Client client) throws DaoException {
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement preparedStatement =
					connection.prepareStatement(CREATE_CLIENT_QUERY, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, client.getNom());
			preparedStatement.setString(2, client.getPrenom());
			preparedStatement.setString(3, client.getMail());
			preparedStatement.setDate(4, client.toSQLNaissance());

			preparedStatement.execute();
			ResultSet resultSet = preparedStatement.getGeneratedKeys();

			if(resultSet.next()){
				int id = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return 0;
	}
	
	public long delete(Client client) throws DaoException {
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement preparedStatement =
					connection.prepareStatement(DELETE_CLIENT_QUERY);
			preparedStatement.setLong(1, client.getId());

			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
	}

	public Client findById(long id) throws DaoException {
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement preparedStatement =
					connection.prepareStatement(FIND_CLIENT_QUERY);
			preparedStatement.setLong(1, id);

			ResultSet resultSet =  preparedStatement.executeQuery();
			resultSet.next();
			String nom = resultSet.getString("nom");
			String prenom = resultSet.getString("prenom");
			String email = resultSet.getString("email");
			Date naissance = resultSet.getDate("naissance");
			return new Client(id, nom, prenom, email, naissance.toLocalDate());
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public List<Client> findAll() throws DaoException {
		List<Client> clients = new ArrayList<Client>();
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement preparedStatement =
					connection.prepareStatement(FIND_CLIENTS_QUERY);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()){
				long client_id = resultSet.getLong("id");
				String nom = resultSet.getString("nom");
				String prenom = resultSet.getString("prenom");
				String email = resultSet.getString("email");
				Date naissance = resultSet.getDate("naissance");

				clients.add(new Client(client_id, nom, prenom, email, naissance.toLocalDate()));
			}
			return clients;
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public int number_of_clients() throws DaoException {
		int clients = 0;
		try {
			Connection connection = ConnectionManager.getConnection();
			Statement statement =
					connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS total FROM Client");
			if(resultSet.next()){
				clients = resultSet.getInt("total");
			}
			return clients;
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public long update(Client client) throws DaoException {
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(EDIT_CLIENT_QUERY);
			preparedStatement.setString(1, client.getNom());
			preparedStatement.setString(2, client.getPrenom());
			preparedStatement.setString(3, client.getMail());
			preparedStatement.setDate(4, Date.valueOf(client.getNaissance()));
			preparedStatement.setLong(5, client.getId());
			long id = preparedStatement.executeUpdate();
			preparedStatement.close();
			connection.close();
			return id;
		} catch (SQLException e) {
			throw new DaoException();
		}
	}
}
