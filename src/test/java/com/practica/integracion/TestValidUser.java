package com.practica.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import javax.naming.OperationNotSupportedException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InOrder;
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
		usuarioValido = new User("userid23", "Guts", "Griffith", "Av. Castellana 95 4ºA Madrid, Spain",
				new LinkedList<Object>());
		auth = mock(AuthDAO.class);
		generic = mock(GenericDAO.class);
		manager = new SystemManager(auth, generic);

	}

	/*
	 * Probamos startRemote con un remoteId que existe Comprobamos : - que devuelve
	 * la collection esperada. - que se ejecutan una vez auth data y otra
	 * getsomedata -que se ejecuta primero auth data y luego getsomedata cuando
	 * llamamos a startremote
	 */
	@Test
	public void startRemoteTest() throws OperationNotSupportedException, SystemManagerException {

		String remoteid = "remote123";
		int numElem = 2; // numero de elementos de la sublista de la BBDD que corresponde a los criterios
							// de busqueda

		testStartRemoteSystem(remoteid, numElem);

	}

	/*
	 * Probamos startRemote con un remoteId null. Al no encontrar el remote id
	 * devuelve TODOS los elementos de la BBDD
	 */
	@Test
	public void startRemoteTestFiltroNull() throws OperationNotSupportedException, SystemManagerException {

		String remoteid = "null";
		int numElem = 5;// numero de elementos de la lista que representa la BBDD entera
		testStartRemoteSystem(remoteid, numElem);
	}

	@Test
	public void stopRemoteTest() throws OperationNotSupportedException, SystemManagerException {
		String remoteid = "remote123";
		int numElem = 2; // numero de elementos de la sublista de la BBDD que corresponde a los criterios
							// de busqueda

		testStopRemoteSystem(remoteid, numElem);
	}

	@Test
	public void stopRemoteTestFiltroNull() throws OperationNotSupportedException, SystemManagerException {

		String remoteid = "null";
		int numElem = 5;// numero de elementos de la lista que representa la BBDD entera
		testStopRemoteSystem(remoteid, numElem);
	}


	
	//metodos para evitar la repeticion de codigo. Todavia se pueden parametrizar para que solo haya uno.
	public void testStartRemoteSystem(String remoteid, int numElem)
			throws OperationNotSupportedException, SystemManagerException {

		Collection<Object> dataList = new LinkedList<Object>();
		for (int i = 0; i < numElem; i++) {
			dataList.add("objeto" + i);

		}

		when(auth.getAuthData(usuarioValido.getId())).thenReturn(usuarioValido);
		when(generic.getSomeData(usuarioValido, "where id=" + remoteid)).thenReturn(dataList);

		InOrder ordered = inOrder(auth, generic);

		Collection<Object> resultStartRemote;
		resultStartRemote = manager.startRemoteSystem(usuarioValido.getId(), remoteid);

		assertEquals(dataList, resultStartRemote);
		verify(auth, times(1)).getAuthData(usuarioValido.getId());
		verify(generic, times(1)).getSomeData(usuarioValido, "where id=" + remoteid);

		ordered.verify(auth).getAuthData(usuarioValido.getId());
		ordered.verify(generic).getSomeData(usuarioValido, "where id=" + remoteid);
	}

	public void testStopRemoteSystem(String remoteid, int numElem)
			throws OperationNotSupportedException, SystemManagerException {

		Collection<Object> dataList = new LinkedList<Object>();
		for (int i = 0; i < numElem; i++) {
			dataList.add("objeto" + i);

		}

		when(auth.getAuthData(usuarioValido.getId())).thenReturn(usuarioValido);
		when(generic.getSomeData(usuarioValido, "where id=" + remoteid)).thenReturn(dataList);

		InOrder ordered = inOrder(auth, generic);

		Collection<Object> resultStartRemote;
		resultStartRemote = manager.stopRemoteSystem(usuarioValido.getId(), remoteid);

		assertEquals(dataList, resultStartRemote);
		verify(auth, times(1)).getAuthData(usuarioValido.getId());
		verify(generic, times(1)).getSomeData(usuarioValido, "where id=" + remoteid);

		ordered.verify(auth).getAuthData(usuarioValido.getId());
		ordered.verify(generic).getSomeData(usuarioValido, "where id=" + remoteid);
	}

}
