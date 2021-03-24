package tests;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.ActemiumContractType;
import domain.manager.Actemium;
import repository.GenericDao;
import repository.GenericDaoJpa;

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

}
