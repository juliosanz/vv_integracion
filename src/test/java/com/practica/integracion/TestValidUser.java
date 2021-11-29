package com.practica.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.awt.List;
import java.util.Collection;
import java.util.LinkedList;

import javax.naming.OperationNotSupportedException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;

@ExtendWith(MockitoExtension.class)
public class TestValidUser {

	
	
	private User user;
	
	@Mock
	private AuthDAO auth;
	
	@Mock
	private GenericDAO genericdao;
	
	/*importante:
	 * el consturctor de sys toma como parametros un AuthDAO y aun GenericDAO, 
	 * por lo que es obligatorio hacerlo asi para que inyecte las dependencias
	 * 
	 * */
	
	@InjectMocks
	private SystemManager sys;
	
	@Test
	public void testStartremoteSystem() throws OperationNotSupportedException, SystemManagerException {
		
		Collection dataList = mock(Collection.class);
		user=new User("userid", "paloma", "escribano", "pueblo", new LinkedList<>());
		//user.setId(userid);
		
		when(auth.getAuthData("userid")).thenReturn(user);//devuelve un user dado un userid valido
		when(genericdao.getSomeData(user, "where id=remoteid")).thenReturn( dataList);//devuelve datos de la BBDD
		
		assertEquals(dataList, sys.startRemoteSystem("userid", "remoteid"));
		
		
		
	}
}
