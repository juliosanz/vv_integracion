package com.practica.integracion;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.LinkedList;

import javax.naming.OperationNotSupportedException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;



@ExtendWith(MockitoExtension.class)
public class TestValidUser {
	
	
	private static AuthDAO auth;
	
	private static GenericDAO generic;
	
	private static User usuarioValido;
	private static SystemManager manager;
	
	@BeforeAll
	static void setUp() {
		usuarioValido = new User("userid23", "Guts", "Griffith", "Av. Castellana 95 4ºA Madrid, Spain", new LinkedList<Object>());
		auth=mock(AuthDAO.class);
		generic=mock(GenericDAO.class);
		manager = new SystemManager(auth, generic);

	}
	
	
	@Test
	public void startRemoteTest () throws OperationNotSupportedException, SystemManagerException {
		Collection<Object> dataList = new LinkedList<Object>();
		dataList.add("objetos");
		String remoteid="remote123";
		when(auth.getAuthData(usuarioValido.getId())).thenReturn(usuarioValido);
		when(generic.getSomeData(usuarioValido, "where id="+remoteid)).thenReturn(dataList);
		
		
		// Probamos startRemote con un remoteId que existe y otro que no
		Collection<Object> aux;
		aux = manager.startRemoteSystem(usuarioValido.getId(), remoteid);
		
		assertEquals(dataList, aux);
		verify(auth, times(1)).getAuthData(usuarioValido.getId());
		verify(generic, times(1)).getSomeData(usuarioValido, "where id="+remoteid);
	}
}
