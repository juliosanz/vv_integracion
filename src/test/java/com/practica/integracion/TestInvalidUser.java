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
import org.mockito.junit.jupiter.MockitoExtension;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;

@ExtendWith(MockitoExtension.class)
public class TestInvalidUser {
	
	private User user1;
	private User userConPermisos;
	private List<Object> userRoles;
	
	@BeforeEach
	private void init() {
		user1 = new User("001", "Guillermo", "Huouin", "Tokyo", userRoles);
		userConPermisos = new User("032", "Julio", "Mega", "Kyoto", userRoles);
	}
	
	@InjectMocks
	private SystemManager sys;
	
	@Mock
	private AuthDAO authDao;
	
	@Mock
	private GenericDAO genericDao;
	
	@Test
	public void invalidStartRemoteSystemTest() throws OperationNotSupportedException {
		String remoteId= "064";
		when(authDao.getAuthData(user1.getId())).thenReturn(user1);
		when(genericDao.getSomeData(user1, "where id=" + remoteId)).thenThrow(new OperationNotSupportedException()); //SystemManagerException()); //OperationNotSupportedException
		
		InOrder ordered = inOrder(authDao, genericDao);
		
		sys = new SystemManager(authDao, genericDao);
		assertThrows(SystemManagerException.class, () -> { sys.startRemoteSystem(user1.getId(), remoteId); });
		
		verify(authDao, times(1)).getAuthData(user1.getId());
		verify(genericDao, times(1)).getSomeData(user1, "where id=" + remoteId);
		
		ordered.verify(authDao).getAuthData(user1.getId());
		ordered.verify(genericDao).getSomeData(user1, "where id=" + remoteId);
	}
	
	@Test
	public void invalidStopRemoteSystemTest() throws OperationNotSupportedException
	{
		String remoteId = "005";
		when(authDao.getAuthData(user1.getId())).thenReturn(user1);
		when(genericDao.getSomeData(user1, "where id=" + remoteId)).thenThrow(new OperationNotSupportedException());
		
		InOrder ordered = inOrder(authDao, genericDao);
		
		sys = new SystemManager(authDao, genericDao);
		assertThrows(SystemManagerException.class, () -> { sys.stopRemoteSystem(user1.getId(), remoteId); });
		
		verify(authDao, times(1)).getAuthData(user1.getId());
		verify(genericDao, times(1)).getSomeData(user1, "where id=" + remoteId);
		
		ordered.verify(authDao).getAuthData(user1.getId());
		ordered.verify(genericDao).getSomeData(user1, "where id=" + remoteId);
	}
	
	@Test
	public void addRemoteSystemTest() throws OperationNotSupportedException
	{
		String remote="Sevilla";
		when(authDao.getAuthData(user1.getId())).thenReturn(user1);
		when(genericDao.updateSomeData(user1, remote)).thenThrow(new OperationNotSupportedException());
		
		InOrder ordered = inOrder(authDao, genericDao);
		
		sys = new SystemManager(authDao, genericDao);
		assertThrows(SystemManagerException.class, () -> { sys.addRemoteSystem(user1.getId(), remote); });
		
		ordered.verify(authDao, times(1)).getAuthData(user1.getId());
		ordered.verify(genericDao, times(1)).updateSomeData(user1, remote);
		
		//ordered.verify(authDao).getAuthData(user1.getId());
		//ordered.verify(genericDao).updateSomeData(user1, remote);
		
		when(authDao.getAuthData(userConPermisos.getId())).thenReturn(userConPermisos);
		when(genericDao.updateSomeData(userConPermisos, remote)).thenReturn(false);
		
		sys = new SystemManager(authDao, genericDao);
		assertThrows(SystemManagerException.class, () -> { sys.addRemoteSystem(userConPermisos.getId(), remote); });
		
		ordered.verify(authDao, times(1)).getAuthData(userConPermisos.getId());
		ordered.verify(genericDao, times(1)).updateSomeData(userConPermisos, remote);
		
		//ordered.verify(authDao).getAuthData(userConPermisos.getId());
		//ordered.verify(genericDao).updateSomeData(userConPermisos, remote);
	}
	
	@Test
	public void deleteRemoteSystemTest() throws OperationNotSupportedException
	{
		String remoteId = "016";
		lenient().when(genericDao.deleteSomeData(user1, remoteId)).thenThrow(new OperationNotSupportedException());
		
		sys = new SystemManager(authDao,genericDao);
		assertThrows(SystemManagerException.class, () -> { sys.deleteRemoteSystem(user1.getId(), remoteId);});
		
		lenient().when(genericDao.deleteSomeData(userConPermisos, remoteId)).thenReturn(false);
		
		sys = new SystemManager(authDao,genericDao);
		assertThrows(SystemManagerException.class, () -> { sys.deleteRemoteSystem(userConPermisos.getId(), remoteId);});
	}
}
