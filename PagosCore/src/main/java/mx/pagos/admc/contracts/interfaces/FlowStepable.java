package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.FlowStep;
import mx.pagos.general.exceptions.DatabaseException;

public interface FlowStepable {
    
    void saveStepImage(FlowStep flowStep) throws DatabaseException;
    
    void deleteStepImage(Integer idFlow, Integer step) throws DatabaseException;
    
    List<String> flowStep(Integer step, Integer idFlow) throws DatabaseException;
}
