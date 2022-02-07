package ncp.service.implementations;

import ncp.model.TariffStatus;
import ncp.repository.TariffStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TariffStatusService {
    @Autowired
    TariffStatusRepository tariffStatusRepository;

    public TariffStatus getByName(String name){
        return tariffStatusRepository.getByName(name);
    }
    public TariffStatus getById(Long id){
        return tariffStatusRepository.getById(id);
    }
    public List<TariffStatus> getAll(){
        return tariffStatusRepository.findAll();
    }
}
