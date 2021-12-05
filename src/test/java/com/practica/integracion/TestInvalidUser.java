package com.practica.integracion;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.inOrder;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;

@ExtendWith(MockitoExtension.class)
public class TestInvalidUser {
	
	private User userSinPermisos;
	private User userConPermisos;
	private List<Object> userRoles;
	private SystemManager sys;
	
	@BeforeEach
	private void init() {
		userSinPermisos = new User("001", "Guillermo", "Huouin", "Tokyo", userRoles);
		userConPermisos = new User("032", "Julio", "Mega", "Kyoto", userRoles);
		sys = new SystemManager(authDao, genericDao);
	}
	
	@Mock
	private AuthDAO authDao;
	
	@Mock
	private GenericDAO genericDao;
	
	@Test
	public void invalidStartRemoteSystemTest() throws OperationNotSupportedException {
		String remoteId= "064";
		when(authDao.getAuthData(userSinPermisos.getId())).thenReturn(userSinPermisos);
		when(genericDao.getSomeData(userSinPermisos, "where id=" + remoteId)).thenThrow(new OperationNotSupportedException()); //SystemManagerException()); //OperationNotSupportedException
		
		InOrder ordered = inOrder(authDao, genericDao);
		
		
		assertThrows(SystemManagerException.class, () -> { sys.startRemoteSystem(userSinPermisos.getId(), remoteId); });
		
		verify(authDao, times(1)).getAuthData(userSinPermisos.getId());
		verify(genericDao, times(1)).getSomeData(userSinPermisos, "where id=" + remoteId);
		
		ordered.verify(authDao).getAuthData(userSinPermisos.getId());
		ordered.verify(genericDao).getSomeData(userSinPermisos, "where id=" + remoteId);
	}
	
	@Test
	public void invalidStopRemoteSystemTest() throws OperationNotSupportedException
	{
		String remoteId = "005";
		when(authDao.getAuthData(userSinPermisos.getId())).thenReturn(userSinPermisos);
		when(genericDao.getSomeData(userSinPermisos, "where id=" + remoteId)).thenThrow(new OperationNotSupportedException());
		
		InOrder ordered = inOrder(authDao, genericDao);
		
		assertThrows(SystemManagerException.class, () -> { sys.stopRemoteSystem(userSinPermisos.getId(), remoteId); });
		
		verify(authDao, times(1)).getAuthData(userSinPermisos.getId());
		verify(genericDao, times(1)).getSomeData(userSinPermisos, "where id=" + remoteId);
		
		ordered.verify(authDao).getAuthData(userSinPermisos.getId());
		ordered.verify(genericDao).getSomeData(userSinPermisos, "where id=" + remoteId);
	}
	
	@Test
	public void addRemoteSystemTest() throws OperationNotSupportedException
	{
		String remote="Sevilla";
		when(authDao.getAuthData(userSinPermisos.getId())).thenReturn(userSinPermisos);
		when(genericDao.updateSomeData(userSinPermisos, remote)).thenThrow(new OperationNotSupportedException());
		
		InOrder ordered = inOrder(authDao, genericDao);
		
		assertThrows(SystemManagerException.class, () -> { sys.addRemoteSystem(userSinPermisos.getId(), remote); });
		
		ordered.verify(authDao, times(1)).getAuthData(userSinPermisos.getId());
		ordered.verify(genericDao, times(1)).updateSomeData(userSinPermisos, remote);
		
		//ordered.verify(authDao).getAuthData(user1.getId());
		//ordered.verify(genericDao).updateSomeData(user1, remote);
	}
	
	@Test
	public void deleteRemoteSystemTest() throws OperationNotSupportedException
	{
		String remoteId = "016";
		
		lenient().when(genericDao.deleteSomeData(Mockito.any(User.class), Mockito.anyString())).thenThrow(new OperationNotSupportedException());
		assertThrows(SystemManagerException.class, () -> { sys.deleteRemoteSystem(userSinPermisos.getId(), remoteId);});
		
		lenient().when(genericDao.deleteSomeData(Mockito.any(User.class), Mockito.anyString())).thenReturn(false);
		assertThrows(SystemManagerException.class, () -> { sys.deleteRemoteSystem(userConPermisos.getId(), remoteId);});
	}
}
