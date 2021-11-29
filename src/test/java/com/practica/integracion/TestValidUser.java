package com.practica.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
	private static User usuarioVerified;
	private static SystemManager manager;
	private static User usuarioModificar;
	
	@BeforeAll
	static void setUp() {
		usuarioVerified = new User("23", "Guts", "Griffith", "Av. Castellana 95 4ºA Madrid, Spain", new LinkedList<Object>());
		List<Object> aux = usuarioVerified.getRoles();
		aux.add("Manager");
		auth = mock(AuthDAO.class);
		generic = mock(GenericDAO.class);
		manager = new SystemManager(auth, generic);
		usuarioModificar = new User("12", "Eric", "Cartman", "South Park, Colorado, USA", new LinkedList<Object>());

	}
	
	
	@Test
	public void testear () {
		Collection<Object> listaADevolver = new LinkedList<Object>();
		listaADevolver.add(usuarioModificar);
		when(auth.getAuthData("23")).thenReturn(usuarioVerified);
		try {
			when(generic.getSomeData(usuarioVerified, "where id=12")).thenReturn(listaADevolver);
			when(generic.getSomeData(usuarioVerified, "where id=1")).thenReturn(null);
		} catch (OperationNotSupportedException e) {
			e.printStackTrace();
		}
		
		// Probamos startRemote con un remoteId que existe y otro que no
		Collection<Object> aux;
		try {
			aux = manager.startRemoteSystem(usuarioVerified.getId(), usuarioModificar.getId());
			assertEquals(1, aux.size());
			Iterator<Object> iterador = aux.iterator();
			assertEquals(usuarioModificar, iterador.next());
			verify(auth, times(1)).getAuthData("23");
			verify(generic, times(1)).getSomeData(usuarioVerified, "where id=12");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("pum");
		}
		
		try {
			aux = manager.startRemoteSystem(usuarioVerified.getId(), "1");

			assertEquals(null, aux);
			verify(auth, times(2)).getAuthData("23");
			verify(generic, times(1)).getSomeData(usuarioVerified, "where id=1");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("pum");
		}
		

	 
	 
	}
}
