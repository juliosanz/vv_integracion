package com.practica.integracion;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
	
	private User user;
	private List<Object> userRoles;
	
	@BeforeEach
	private void init() {
		user = new User("005", "Guillermo", "Huouin", "Tokyo", userRoles);
	}
	
	@InjectMocks
	private SystemManager sys;
	
	@Mock
	private AuthDAO authDao;
	
	@Mock
	private GenericDAO genericDao;
	
	@Test
	public void testInvalidStartRemoteSystem() throws OperationNotSupportedException {
		when(authDao.getAuthData(user.getId())).thenReturn(user);
		when(genericDao.getSomeData(user, "where id=002")).thenThrow(new OperationNotSupportedException()); //SystemManagerException()); //OperationNotSupportedException
		
		
		sys = new SystemManager(authDao, genericDao);
		assertThrows(SystemManagerException.class, () -> { sys.startRemoteSystem(user.getId(), "002"); });
		
		
	}
	
}
