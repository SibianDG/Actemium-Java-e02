package tests;

import domain.ActemiumContractType;
import domain.ActemiumCustomer;
import domain.ContractType;
import domain.enums.ContractTypeStatus;
import domain.enums.Timestamp;
import domain.enums.UserStatus;
import domain.manager.Actemium;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.GenericDao;
import repository.GenericDaoJpa;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
public class ContractTypeFacadeTest {

    @Mock
    private GenericDao<ActemiumContractType> contractTypeDaoJpa;

    @InjectMocks
    private Actemium actemium;

    private void trainDummy(ActemiumContractType contractType){
        contractTypeDaoJpa = new GenericDaoJpa<>(ActemiumContractType.class);
        contractTypeDaoJpa = spy(GenericDaoJpa.class);
        doNothing().when(contractTypeDaoJpa).startTransaction();
        doNothing().when(contractTypeDaoJpa).update(contractType);
        doNothing().when(contractTypeDaoJpa).commitTransaction();
        doNothing().when(contractTypeDaoJpa).insert(contractType);
    }


    //todo and now?
    @Test
    public void register_valid_ContractType() {
        //trainDummy();
        //assertDoesNotThrow(() -> userFacade.modifyCustomer((ActemiumCustomer) cust, "usernameAvailable", PASSWORD, "John",
        //        "Smith", theWhiteHouse, UserStatus.ACTIVE));
        //Mockito.verify(userRepoDummy).findByUsername(ADMINUSERNAME);
    }
}
