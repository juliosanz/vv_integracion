package com.practica.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.util.Collection;
import java.util.LinkedList;

import javax.naming.OperationNotSupportedException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
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


	@BeforeEach
	 void init() {
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


	@Test
	public void testAddRemoteSystemFallaElUpdate() throws SystemManagerException, OperationNotSupportedException {
		String remote = "Marmota";
		when(auth.getAuthData(usuarioValido.getId())).thenReturn(usuarioValido);
		when(generic.updateSomeData(usuarioValido, remote)).thenReturn(false);


		InOrder ordered = inOrder(auth, generic);
		
		SystemManagerException fallo = assertThrows(SystemManagerException.class, () -> {
			manager.addRemoteSystem(usuarioValido.getId(), remote);});
		assertEquals("cannot add remote", fallo.getMessage());
		
		verify(auth, times(1)).getAuthData(usuarioValido.getId());
		verify(generic, times(1)).updateSomeData(usuarioValido, remote);

		ordered.verify(auth).getAuthData(usuarioValido.getId());
		ordered.verify(generic).updateSomeData(usuarioValido, remote);
		

	}
	
	@Test
	public void testAddRemoteSystem() throws SystemManagerException, OperationNotSupportedException {
		String remote = "Marmota";
		when(auth.getAuthData(usuarioValido.getId())).thenReturn(usuarioValido);
		when(generic.updateSomeData(usuarioValido, remote)).thenReturn(true);

		InOrder ordered = inOrder(auth, generic);
		
		manager.addRemoteSystem(usuarioValido.getId(), remote);
		
		verify(auth, times(1)).getAuthData(usuarioValido.getId());
		verify(generic, times(1)).updateSomeData(usuarioValido, remote);

		ordered.verify(auth).getAuthData(usuarioValido.getId());
		ordered.verify(generic).updateSomeData(usuarioValido, remote);
	}

	@Test
	public void testDeleteRemoteSystem( ) throws OperationNotSupportedException, SystemManagerException {
		String remoteid = "remote123";

		when(generic.deleteSomeData(Mockito.any(User.class), Mockito.anyString())).thenReturn(true);
		manager.deleteRemoteSystem(usuarioValido.getId(), remoteid);
		verify(generic, times(1)).deleteSomeData(Mockito.any(User.class), Mockito.anyString());//se necesitan que ambos esten a any porque si no no se puede usar investigar mas

	}
	
	@Test
	public void testDeleteRemoteSystemFallaElBorrado( ) throws OperationNotSupportedException, SystemManagerException {
		String remoteid = "remote123";

		when(generic.deleteSomeData(Mockito.any(User.class), Mockito.anyString())).thenReturn(false);
		
		SystemManagerException error = assertThrows(SystemManagerException.class, () -> {
			manager.deleteRemoteSystem(usuarioValido.getId(), remoteid);});
		
		assertEquals("cannot delete remote: does remote exists?", error.getMessage());
		
		verify(generic, times(1)).deleteSomeData(Mockito.any(User.class), Mockito.anyString());//se necesitan que ambos esten a any porque si no no se puede usar investigar mas

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
